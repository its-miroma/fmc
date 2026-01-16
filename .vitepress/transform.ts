import * as semver from "semver";
import { HeadConfig, SiteConfig, UserConfig } from "vitepress";

import redirects from "./redirects";

export const transformUrl = (href: string, latestVersion: string): string => {
  const url = new URL(href, "https://a.com/");
  const split = url.pathname.toLowerCase().split("/");

  const initial = split[0] === "" ? split.shift() : undefined;
  const locale = /^..[-_]..$/.test(split[0]) ? split.shift()!.replace("-", "_") : undefined;
  const version = /^[0-9]+\.[0-9]+/.test(split[0]) ? split.shift()! : undefined;
  let theRest = split.join("/");

  for (const { from, dest, appliesTo } of redirects) {
    if (!semver.satisfies(version ?? latestVersion, appliesTo ?? "*")) continue;
    if (from === theRest) theRest = dest;
    if (from instanceof RegExp) theRest = theRest.replace(from, dest);
  }

  url.pathname = [initial, locale, version, theRest].filter((s) => s !== undefined).join("/");
  return `${url.pathname}${url.search}${url.hash}`;
};

export const transformHead: SiteConfig["transformHead"] = (context) => {
  const returned: HeadConfig[] = [];

  const split = context.pageData.filePath.split("/");
  if (split[0] === "versions") split.splice(0, 2);
  const locale = split[0] === "translated" ? split[1] : "en_us";

  const hostname = context.siteConfig.sitemap!.hostname;
  const userConfig =
    context.siteConfig.userConfig.locales![locale] || context.siteConfig.userConfig.locales!.root!;
  const canonicalUrl = `${hostname}${transformUrl(
    context.pageData.relativePath,
    userConfig.themeConfig.nav.at(-1).props.versioningPlugin.latestVersion
  )}`;

  // https://ogp.me/
  const og: [string, string][] = [
    ["og:site_name", userConfig.title!],
    ["og:title", context.pageData.title],
    ["og:description", context.pageData.description],
    ["og:url", canonicalUrl],
    ["og:image", `${hostname}logo.png`],
    ["og:type", "article"],
    ["og:locale", locale.replace(/_..$/, (m) => m.toUpperCase().replace("_", "-"))],
  ];
  if ((context.pageData.lastUpdated ?? 0) > 0) {
    og.push(["article:modified_time", String(context.pageData.lastUpdated)]);
  }

  returned.push(...og.map(([property, content]) => ["meta", { property, content }] as HeadConfig));
  returned.push(["link", { rel: "canonical", href: canonicalUrl }]);

  // Don't index the page if it's a versioned page.
  if (context.pageData.filePath.startsWith("versions/")) {
    returned.push(["meta", { name: "robots", content: "none" }]);
  }

  return returned;
};

export const transformItems: NonNullable<UserConfig["sitemap"]>["transformItems"] = (items) => {
  const config = (globalThis as any).VITEPRESS_CONFIG as SiteConfig;
  return items.filter((i) => {
    const relativePath = i.url.replace(config.sitemap!.hostname, "");
    return !config.rewrites.inv[relativePath]?.startsWith("versions/");
  });
};
