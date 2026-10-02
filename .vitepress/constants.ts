import * as fs from "node:fs";
import * as path from "node:path";
import * as process from "node:process";
import * as tinyglobby from "tinyglobby";

export const AT = path.join(import.meta.dirname, "..");

// https://docs.github.com/en/actions/reference/workflows-and-actions/variables#default-environment-variables
// https://docs.netlify.com/build/configure-builds/environment-variables/#read-only-variables
export const ENV =
  process.env.NODE_ENV !== "production"
    ? "dev"
    : process.env.GITHUB_ACTIONS
      ? "github"
      : process.env.NETLIFY
        ? Number(process.env.REVIEW_ID) || "netlify"
        : "build";

export const VERSION_RE = /^[0-9]+[.][0-9]+([.][0-9]+)?$/;

export const LATEST_VERSION =
  fs
    .readFileSync(path.join(AT, "reference", "latest", "build.gradle"), "utf-8")
    .match(/def minecraftVersion = "([^"]+)"/)?.[1] || "";

export const EXCLUDED_OLD_VERSIONS = (process.env.EXCLUDED_VERSIONS || "")
  .split(",")
  .filter(Boolean);

export const OLD_VERSIONS = tinyglobby
  .globSync("*", { cwd: path.join(AT, "versions"), onlyDirectories: true })
  .map((v) => path.basename(v))
  .filter((v) => !EXCLUDED_OLD_VERSIONS.includes(v));

export const FUTURE_VERSIONS = tinyglobby
  .globSync("*.*", { cwd: AT, onlyDirectories: true })
  .map((v) => path.basename(v))
  .filter((v) => VERSION_RE.test(v));
