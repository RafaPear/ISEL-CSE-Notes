import fs from "fs/promises";

const outputFile = "teams.json";
const inputFile = "teams-ids.json";
const envFile = "env.json";

function getToken(service, env = envFile, header = "X-Auth-Token") {
  return fs.readFile(env, "utf-8").then((d) => JSON.parse(d)[service][header]);
}

function getTeam(id, token) {
  return fetch(`https://api.football-data.org/v4/teams/${id}`, {
    headers: { "X-Auth-Token": token },
  }).then((r) => (r.ok ? r.json() : {}));
}

function parseTeam(data, id) {
  return {
    id: data.id ?? id,
    name: data.name ?? "NOT FOUND",
    players: data.squad ? data.squad.map((p) => p.name) : [],
  };
}

function getTeamsFromFile(file) {
  return fs.readFile(file, "utf-8").then((d) => JSON.parse(d)["teams-ids"]);
}

function saveTeamsToFile(teams, file) {
  return fs
    .writeFile(file, JSON.stringify(teams, null, 2))
    .then(() => console.log("WRITE TO", file, "DONE"));
}

function checkLastRun(seconds) {
  return fs
    .stat(outputFile)
    .then((s) => ({ recent: (Date.now() - s.mtimeMs) / 1000 < seconds }))
    .catch(() => ({ recent: false }));
}

function main() {
  checkLastRun(60)
    .then(({ recent }) => {
      if (recent) return Promise.reject("Recent file");
      console.log("GETTING TEAMS...");
      return Promise.all([getTeamsFromFile(inputFile), getToken("football-data.org")]);
    })
    .then(([ids, token]) =>
      Promise.all(ids.map((id) => getTeam(id, token).then((t) => parseTeam(t, id))))
    )
    .then((teams) => saveTeamsToFile(teams, outputFile))
    .catch(() => console.log("SKIP - RECENT FILE"))
    .finally(() => console.log("END"));
}

main();
