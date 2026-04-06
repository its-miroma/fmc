import * as crossSpawn from "cross-spawn";
import * as fs from "node:fs";
import * as process from "node:process";
import * as tinyglobby from "tinyglobby";

const v = process.argv[2];
const DO_IT = Boolean(process.argv[3]);

if (!(/^[0-9.]+$/.test(v) && fs.existsSync(`versions/${v}/`) && fs.existsSync(`reference/${v}/`))) {
  console.error(`WRONG VERSION: ${v}`);
  console.log("usage: npx tsx .vitepress/migrate.ts VERSION [DO_IT]");
  process.exit(1);
}

const warn = (...args: [string, ...string[]]) => console.warn(args.join("\n  "));

const git = (...args: string[]) => {
  const res = crossSpawn.sync("git", args, { encoding: "utf8" });
  if (res.error) {
    console.error(`Failed to run 'git ${args.join(" ")}'!\n  ${res.error}`);
    process.exit(1);
  }
  return res;
};

if (git("status", "--porcelain").stdout.toString().trim().length > 0) {
  console.error("Working directory must be clean!");
  process.exit(1);
}

const PRIORITY = [
  "index.md",
  "basics.md",
  "setup.md",
  "first-block.md",
  "first-entity.md",
  "first-item.md",
].reverse();

const { compare } = Intl.Collator();

const sorter = (a: string, b: string) => {
  const as = a.split("/").map((f) => (f.endsWith(".md") ? f : f + "/"));
  const bs = b.split("/").map((f) => (f.endsWith(".md") ? f : f + "/"));

  while (as.length * bs.length !== 0) {
    const aFragment = as.shift()!;
    const bFragment = bs.shift()!;

    if ([...aFragment].reverse()[0] !== [...bFragment].reverse()[0]) {
      if (aFragment.endsWith(".md")) return -10.1;
      return +10.1;
    }

    const comparison =
      PRIORITY.indexOf(bFragment) - PRIORITY.indexOf(aFragment) || compare(aFragment, bFragment);
    if (comparison !== 0) return comparison;
  }

  return as.length - bs.length;
};

const pathToContentMap = Object.fromEntries(
  tinyglobby
    .globSync([`versions/${v}/develop/**/*.md`, `versions/${v}/players/**/*.md`])
    .sort(sorter)
    .map((f) => [f, fs.readFileSync(f, "utf-8")] as const)
    .filter(([_, c]) => c.split("@[").length !== 1)
);

const javaToNewMap: Record<string, string> = {};

for (const f of Object.keys(pathToContentMap)) {
  const newContent = pathToContentMap[f].replaceAll(
    /@\[code([^\]]*)\]\(([^\)]+)\)/g,
    (old, ...args) => {
      const config: Record<"highlight" | "transcludeWith", string> = Object.fromEntries(
        (args[0]! as string)
          .split(" ")
          .filter(Boolean)
          .filter((x) => !["java", "lang=java", "transclude"].includes(x))
          .map((kv) => kv.split("=", 2))
      );

      const includedPath = (args[1] as string).replace(/^@\//, "");

      if ("transclude" in config) {
        warn("LINE TRANSCLUSION DETECTED", f, includedPath);

        return old;
      }

      let replacement = `<<< @/${includedPath}`;
      if ("transcludeWith" in config) {
        const region = config.transcludeWith
          .replace(/^[:_#]+/, "")
          .replace(/:+$/, "")
          .replaceAll("_", "-")
          .replaceAll(":", "--")
          .replace(/([a-z])([A-Z])/g, "$1-$2")
          .toLowerCase()
          .replace(/^$/, "TODO-give-me-a-name");

        if (!/^[a-z0-9-]+$/.test(region) && region !== "TODO-give-me-a-name") {
          warn("WEIRD REGION", region, f);
        }

        replacement += "#";
        replacement += region;

        const splits = (
          javaToNewMap[includedPath as keyof typeof javaToNewMap]
          || fs.readFileSync(includedPath, "utf-8")
        ).split(config.transcludeWith);

        if (splits.length % 2 === 0) {
          warn("ODD NUMBER OF COMMENTS", region, includedPath, f);
        }

        for (let i = 0; i < splits.length - 1; i++) {
          if (!/(\n[\t ]*(?:\/\/|#)) ?$/.test(splits[i])) {
            warn("WEIRD PRE-TRANSCLUSION", region, includedPath, f);
          }

          splits[i] = splits[i].replace(
            /(\n[\t ]*(?:\/\/|#)) ?$/,
            (_, group) => group + ` #${i % 2 !== 0 ? "end" : ""}region ${region}`
          );
        }

        for (const post of splits.slice(1)) {
          if (!/^\n/.test(post)) {
            warn("WEIRD POST-TRANSCLUSION", region, includedPath, f);
          }
        }

        javaToNewMap[includedPath] = splits.join("");
      }

      if ("highlight" in config) {
        replacement += config.highlight;
      }

      return replacement;
    }
  );

  if (DO_IT) {
    fs.writeFileSync(f, newContent);
  }
}

if (DO_IT) {
  for (const f of Object.keys(javaToNewMap)) {
    fs.writeFileSync(f, javaToNewMap[f]);
  }
}

git("add", ".");
git("commit", "--message=npx tsx .vitepress/migrate.ts");

const startCommit = git("stem", "--reverse", "--pretty=%H", "main...migration")
  .stdout.split("\n")[2]
  .trim();

const patch = git("diff", "--unified=1", `${startCommit}...migration`)
  .stdout.replace(/( [ab])[/](?!reference[/])/gm, `\$1/versions/${v}/`)
  .replace(/([ab@][/]reference)[/]latest[/]/gm, `\$1/${v}/`)
  .replace(/^(rename [^ ]+ reference)[/]latest[/]/gm, `\$1/${v}/`);

if (DO_IT) {
  fs.writeFileSync(`${v}.patch`, patch);
  git("apply", "--reject", `${v}.patch`);
  fs.rmSync(`${v}.patch`);
  git("add", ".", ":!**/*.rej");
}
