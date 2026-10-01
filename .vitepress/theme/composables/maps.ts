const HAS_LOCALE_RE = /^.._.._/;
const VERSION_RE = /^[0-9]+[.][0-9]+([.][0-9]+)?$/;

const versionToPurePathsMap = new Map<string, Set<string>>();
const purePathToVersionsMap = new Map<string, Set<string>>();

export const useMaps = () => {
  const returned = { versionToPurePathsMap, purePathToVersionsMap };

  if (!window.__VP_HASH_MAP__ || versionToPurePathsMap.size > 0) {
    return returned;
  }

  for (const k of Object.keys(window.__VP_HASH_MAP__!)) {
    if (HAS_LOCALE_RE.test(k)) {
      continue;
    }

    const split = k.split("_");
    const version = VERSION_RE.test(split[0]) ? split.shift()! : "";
    const purePath = split.join("/");

    if (!versionToPurePathsMap.has(version)) {
      versionToPurePathsMap.set(version, new Set());
    }
    versionToPurePathsMap.get(version)!.add(purePath);

    if (!purePathToVersionsMap.has(purePath)) {
      purePathToVersionsMap.set(purePath, new Set());
    }
    purePathToVersionsMap.get(purePath)!.add(version);
  }

  return returned;
};
