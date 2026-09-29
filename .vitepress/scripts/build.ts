import * as crypto from "node:crypto";
import * as events from "node:events";
import * as fs from "node:fs";
import * as path from "node:path";
import * as perfHooks from "node:perf_hooks";
import * as process from "node:process";
import * as util from "node:util";
import * as workerThreads from "node:worker_threads";
import * as tinyglobby from "tinyglobby";
import { AT, LATEST_VERSION, OLD_VERSIONS } from "../constants.ts";

if (!workerThreads.isMainThread) {
  const vitepress = await import("vitepress");
  await vitepress.build(AT, workerThreads.workerData);
  process.exit(0);
}

const start = perfHooks.performance.now();
process.chdir(AT);

const tempDir = path.join(AT, ".vitepress", ".versions");

const args = util.parseArgs({
  options: { "list": { type: "boolean" }, "skip-build": { type: "boolean" } },
  allowPositionals: true,
});

const versions = new Set(
  args.positionals.map((a) => path.basename(a)).map((a) => (a === "latest" ? LATEST_VERSION : a))
);

const isList = Boolean(args.values["list"]);
const isMerge = Boolean(args.values["skip-build"]);

if (isMerge) {
  if (isList || versions.size) {
    throw new Error("--merge is not compatible with other options");
  }

  if (!fs.statSync(tempDir, { throwIfNoEntry: false })?.isDirectory()) {
    throw new Error("couldn't find .versions directory");
  }
}

for (const v of versions) {
  if (v !== LATEST_VERSION && !OLD_VERSIONS.includes(v)) {
    throw new Error(`unrecognized version: '${v}'`);
  }
}

if (versions.size === 0) {
  const detectedVersions = isMerge
    ? tinyglobby.globSync("*", { cwd: tempDir, onlyDirectories: true }).map((v) => path.basename(v))
    : OLD_VERSIONS;

  for (const v of detectedVersions) {
    versions.add(v);
  }
}

versions.add(LATEST_VERSION);

const collator = new Intl.Collator(undefined, { numeric: true });
const sortedVersions = [...versions].toSorted(collator.compare).toReversed();

if (isList) {
  console.log(JSON.stringify(sortedVersions));

  process.exit(0);
}

console.warn("PLEASE DO NOT TOUCH ANY FILE DURING BUILD\n");

if (!isMerge) {
  fs.rmSync(tempDir, { recursive: true, force: true });
}

const getOutDir = (version: string) => path.join(tempDir, version);

for (const [i, version] of sortedVersions.entries()) {
  if (isMerge) {
    break;
  }

  console.log(`building ${version} (${i + 1}/${sortedVersions.length})...`);

  const outDir = getOutDir(version);
  fs.mkdirSync(outDir, { recursive: true });

  const worker = new workerThreads.Worker(import.meta.filename, {
    workerData: { outDir },
    env: {
      ...process.env,
      CI: "1",
      SHOW_ALL_VERSIONS: "1",
      EXCLUDED_VERSIONS: OLD_VERSIONS.filter((v) => v !== version).join(","),
    },
  });

  const [code] = await events.once(worker, "exit");

  if (code !== 0) throw new Error(`building ${version} failed with exit code ${code}!`);
}

console.log(`merging metadata...`);
const getNewHash = (content: string) => crypto.hash("sha256", content, "hex").slice(0, 8);

const hashes: string[] = [];
const hashMap: Record<string, string> = {};
let siteData: unknown;

for (const version of sortedVersions) {
  const window = ((globalThis as any).window = {} as any);
  const outDir = getOutDir(version);

  const metadataFile = tinyglobby.globSync("metadata.*.js", {
    cwd: path.join(outDir, "assets", "chunks"),
    absolute: true,
  })[0];

  hashes.push(path.basename(metadataFile).match(/^metadata[.](.+)[.]js$/)![1]);

  const fileContent = fs.readFileSync(metadataFile, { encoding: "utf-8" });
  const split = fileContent
    .split(";")
    .map((s) => s.trim())
    .filter(Boolean);

  if (split.length !== 2) {
    throw new Error(`too many assignments in ${metadataFile}`);
  }

  await import(metadataFile);

  if (!split[0].startsWith(`window.__VP_HASH_MAP__=JSON.parse`)) {
    throw new Error(`failed to parse hash map in ${metadataFile}`);
  }
  const versionHashMap = window.__VP_HASH_MAP__;

  if (!split[1].startsWith(`window.__VP_SITE_DATA__=JSON.parse`)) {
    throw new Error(`failed to parse site data in ${metadataFile}`);
  }

  if (version === LATEST_VERSION) {
    siteData = window.__VP_SITE_DATA__;
  }

  (globalThis as any).window = undefined;

  Object.assign(hashMap, versionHashMap);
}

const targetDir = path.join(AT, ".vitepress", "dist");
fs.rmSync(targetDir, { recursive: true, force: true });

const newMetadataContent = `window.__VP_HASH_MAP__=JSON.parse(${JSON.stringify(JSON.stringify(hashMap))});window.__VP_SITE_DATA__=JSON.parse(${JSON.stringify(JSON.stringify(siteData))});`;
const newHash = getNewHash(newMetadataContent);

const newMetadataPath = path.join(targetDir, "assets", "chunks", `metadata.${newHash}.js`);
const newHashmapJsonPath = path.join(targetDir, "hashmap.json");

fs.mkdirSync(path.dirname(newMetadataPath), { recursive: true });
fs.writeFileSync(newMetadataPath, newMetadataContent, "utf-8");
fs.writeFileSync(newHashmapJsonPath, JSON.stringify(hashMap), "utf-8");

console.log(`merging pages...`);
for (const version of sortedVersions) {
  const outDir = getOutDir(version);
  const files = tinyglobby.globSync("**/*", {
    cwd: outDir,
    ignore: ["hashmap.json", "assets/chunks/metadata.*.js"],
    absolute: true,
  });

  for (const f of files) {
    if (fs.statSync(f).isDirectory()) {
      continue;
    }

    const destPath = f.replace(outDir, targetDir);
    fs.mkdirSync(path.dirname(destPath), { recursive: true });

    if (f.endsWith(".html")) {
      let content = fs.readFileSync(f, "utf-8");
      for (const h of hashes) {
        content = content.replaceAll(`metadata.${h}.js`, `metadata.${newHash}.js`);
      }

      fs.writeFileSync(destPath, content, "utf-8");
    } else {
      fs.copyFileSync(f, destPath);
    }
  }
}

const finish = perfHooks.performance.now();
console.log(`built ${sortedVersions.length} versions in ${((finish - start) / 1000).toFixed(2)}s`);
