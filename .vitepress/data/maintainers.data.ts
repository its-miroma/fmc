import { Octokit } from "octokit";
import { DefaultTheme } from "vitepress";

const GITHUB_TOKEN = process.env.GITHUB_TOKEN || "";
const octokit = new Octokit({ auth: GITHUB_TOKEN });

export default {
  async load() {
    const members = await (async () => {
      try {
        return await octokit.paginate(octokit.rest.teams.listMembersInOrg, {
          org: "FabricMC",
          team_slug: "documentation",
        });
      } catch (e) {
        // Allows build without a GITHUB_TOKEN or without internet
        return [];
      }
    })();

    return (
      await Promise.all(
        members.map(async (member) => ({
          val: await (async () => {
            try {
              return await octokit.paginate(octokit.rest.orgs.listForUser, {
                username: member.login,
              });
            } catch (e) {
              // Allows build without internet
              return [{ login: "FabricMC" }];
            }
          })(),
          member,
        }))
      )
    )
      .filter(
        (result) =>
          result.val.map((org) => org.login).includes("FabricMC") &&
          result.member.login !== "FabricMCBot"
      )
      .map(
        (result) =>
          ({
            avatar: result.member.avatar_url,
            links: [{ icon: "github", link: result.member.html_url }],
            name: result.member.login,
          } as DefaultTheme.TeamMember)
      )
      .sort((a, b) => a.name.localeCompare(b.name));
  },
};
