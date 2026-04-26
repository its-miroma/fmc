import { HeadConfig } from "vitepress";
import { Fabric } from "../types";

const getRedirects = (latestVersion: string): { from: RegExp; dest: string }[] => [
  {
    from: /[/]{2,}/g,
    dest: "/",
  },
  {
    from: /((?<=^|[/])index)?[.](html|md)$/,
    dest: "",
  },
  {
    from: new RegExp(`^${latestVersion.replaceAll(".", "[.]")}([/]|$)`),
    dest: "",
  },
  {
    from: /^1[.]21[.]10([/]|$)/,
    dest: "1.21.11/",
  },
  {
    from: /develop[/]items[/]custom-item-groups$/,
    dest: "develop/items/custom-creative-tabs",
  },
  {
    from: /develop[/]rendering[/]draw-context$/,
    dest: "develop/rendering/gui-graphics",
  },
  {
    from: /develop[/]migrating-mappings([/]|$)/,
    dest: "develop/porting/mappings/",
  },
  {
    from: /develop[/]porting[/]current$/,
    dest: "develop/porting/",
  },
  {
    from: /develop[/]porting[/](next|26[.]1)([/]|$)/,
    dest: "26.1/develop/porting/",
  },
  {
    from: /develop[/]blocks[/]transparency-and-tinting$/,
    dest: "develop/blocks/block-tinting",
  },
  // TODO: review if this works, at 404 and 200
  {
    from: /develop[/]blocks[/]block-tinting$/,
    dest: "develop/blocks/transparency-and-tinting/",
  },
  {
    from: /^(?:[0-9.]+[/])?develop[/]porting[/]mappings([/].*)?$/,
    dest: "1.21.11/develop/porting/mappings$1",
  },
];

export const getTransformHead =
  (latestVersion: string): NonNullable<Fabric.Config["transformHead"]> =>
  (context) => {
    const returned: HeadConfig[] = [];
    const localeConfig = context.siteConfig.userConfig.locales![context.siteData.localeIndex!];
    const origin = context.siteConfig.sitemap!.hostname;

    if (context.pageData.isNotFound) {
      const serializedRedirects = JSON.stringify(getRedirects(latestVersion), (_, v) =>
        v instanceof RegExp ? { r: v.source, f: v.flags } : v
      );

      const script = (serializedRedirects: { from: { r: string; f: string }; dest: string }[]) => {
        const l = window.location;
        const split = decodeURIComponent(l.pathname).toLowerCase().replace(/^\/+/, "").split("/");
        const locale = /^..[_-]..$/.test(split[0]) ? `/${split.shift()!.replace("-", "_")}/` : "/";
        const newPath = serializedRedirects
          .map(({ from: { r, f }, dest }) => ({ from: new RegExp(r, f), dest }))
          .reduce((p, r) => p.replace(r.from, r.dest), split.join("/"));

        if (newPath !== split.join("/")) {
          l.replace(`${locale}${newPath}${l.search}${l.hash}`);
        } else {
          const href = `${l.origin}${locale}${newPath}`;
          const head: HeadConfig[] = [
            ["link", { rel: "canonical", href }],
            ["meta", { property: "og:url", content: href }],
            ["meta", { name: "robots", content: "none" }],
          ];

          for (const h of head) {
            const el = document.createElement(h[0]);
            for (const [k, v] of Object.entries(h[1])) el.setAttribute(k, v);
            if (h[2]) el.innerHTML = h[2];
            document.head.appendChild(el);
          }
        }
      };

      returned.push(["script", {}, `(${script.toString()})(${serializedRedirects})`]);
    } else {
      const path = getRedirects(latestVersion).reduce(
        (p, r) => p.replace(r.from, r.dest),
        context.pageData.relativePath.toLowerCase().replace(localeConfig.link!.slice(1), "")
      );
      const href = `${origin}${localeConfig.link!.slice(1)}${path}`;

      returned.push(["link", { rel: "canonical", href }]);
      returned.push(["meta", { property: "og:url", content: href }]);

      if (context.pageData.filePath.startsWith("versions/")) {
        returned.push(["meta", { name: "robots", content: "none" }]);
      }
    }

    const ogLocale = context.siteData
      .localeIndex!.replace("root", "en_us")
      .replace(/..$/, (m) => m.toUpperCase());
    returned.push(
      ["meta", { property: "og:site_name", content: localeConfig.title! }],
      ["meta", { property: "og:title", content: context.title }],
      ["meta", { property: "og:description", content: context.description }],
      ["meta", { property: "og:image", content: `${origin}logo.png` }],
      ["meta", { property: "og:type", content: "article" }],
      ["meta", { property: "og:locale", content: ogLocale }]
    );

    const lastUpdated = context.pageData.lastUpdated ?? 0;
    if (lastUpdated > 0) {
      const ogLastUpdated = new Date(lastUpdated).toISOString();
      returned.push(["meta", { property: "article:modified_time", content: ogLastUpdated }]);
    }

    returned.push(
      ["link", { rel: "icon", sizes: "32x32", href: "/favicon.png" }],
      ["link", { rel: "license", href: "https://github.com/FabricMC/fabric-docs/blob/-/LICENSE" }],
      ["meta", { name: "theme-color", content: "#2275da" }],
      ["meta", { name: "twitter:card", content: "summary" }] // haha still twitter
    );

    return returned;
  };
