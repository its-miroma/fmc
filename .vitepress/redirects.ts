import { Fabric } from "./types";

export default [
  {
    from: /^(.*)((?<=^|\/)index)?\.(md|html)$/,
    dest: "$1",
  },
] satisfies Fabric.RedirectRules;
