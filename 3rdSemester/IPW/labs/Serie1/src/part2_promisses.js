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
  })
    .then((r) => (r.ok ? r.json() : {}))
    .catch(() => ({}));
}

function parseTeam(d, id) {
  return {
    id: d.id ?? id,
    name: d.name ?? "NOT FOUND",
    players: d.squad ? d.squad.map((p) => p.name) : [],
  };
}

function getTeamsFromFile(f) {
  return fs.readFile(f, "utf-8").then((d) => JSON.parse(d)["teams-ids"]);
}

function saveTeamsToFile(t, f) {
  return fs
    .writeFile(f, JSON.stringify(t, null, 2))
    .then(() => console.log("WRITE TO", f, "DONE"));
}

function checkLastRun(s) {
  return fs
    .stat(outputFile)
    .then((st) => ({ recent: (Date.now() - st.mtimeMs) / 1000 < s }))
    .catch(() => ({ recent: false }));
}

function fetchTeamsSequentially(ids, token, results = []) {
  if (ids.length === 0) return Promise.resolve(results);
  const id = ids[0];
  return getTeam(id, token)
    .then((t) => parseTeam(t, id))
    .then((team) => {
      results.push(team);
      return fetchTeamsSequentially(ids.slice(1), token, results);
    });
}

function main() {
  checkLastRun(60)
    .then(({ recent }) => {
      if (recent) return Promise.reject("Recent file");
      console.log("GETTING TEAMS...");
      return getTeamsFromFile(inputFile);
    })
    .then((ids) => getToken("football-data.org").then((token) => fetchTeamsSequentially(ids, token)))
    .then((teams) => saveTeamsToFile(teams, outputFile))
    .catch(() => console.log("SKIP - RECENT FILE"))
    .finally(() => console.log("END"));
}

main();
