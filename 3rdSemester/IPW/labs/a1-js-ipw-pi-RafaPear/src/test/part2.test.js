import { strict as assert } from 'assert';
import fs from 'fs/promises';
import path from 'path';
import { execFile } from 'child_process';
import { promisify } from 'util';

const execFileP = promisify(execFile);

function makeRunnerScript(scriptName) {
  return `// runner for ${scriptName}
globalThis.fetch = async (url, opts) => {
  const id = parseInt(url.split('/').pop());
  return { ok: true, json: async () => ({ id, name: 'Team ' + id, squad: [{name: 'P1'}, {name: 'P2'}] }) };
};
import('./${scriptName}');`;
}

describe('part2 scripts', function () {
  this.timeout(10000);
  const scripts = ['part2_promises.js', 'part2_promises_all.js', 'part2_async_await.js'];

  for (const script of scripts) {
    it(`runs ${script} and writes teams.json`, async () => {
      const base = process.cwd();
      const tmp = await fs.mkdtemp(path.join(base, 'tmp-test-'));

      // copy script
      const srcPath = path.join(base, script);
      const destPath = path.join(tmp, script);
      const content = await fs.readFile(srcPath, 'utf8');
      await fs.writeFile(destPath, content);

      // write env.json and teams-ids.json in temp dir
      await fs.writeFile(path.join(tmp, 'env.json'), JSON.stringify({ 'football-data.org': { 'X-Auth-Token': 'T' } }));
      await fs.writeFile(path.join(tmp, 'teams-ids.json'), JSON.stringify({ 'teams-ids': [7, 8] }));

      // create runner
      const runner = path.join(tmp, 'runner.mjs');
      await fs.writeFile(runner, makeRunnerScript(script));

      // run node on the runner (ESM)
      await execFileP(process.execPath, [runner], { cwd: tmp });

      // read and assert teams.json
      const teamsFile = path.join(tmp, 'teams.json');
      const teamsRaw = await fs.readFile(teamsFile, 'utf8');
      const teams = JSON.parse(teamsRaw);
      assert.equal(teams.length, 2);
      assert.equal(teams[0].id, 7);
      assert.equal(teams[0].name, 'Team 7');
      assert.deepEqual(teams[0].players, ['P1', 'P2']);

      // cleanup
      await fs.rm(tmp, { recursive: true, force: true });
    });
  }
});
