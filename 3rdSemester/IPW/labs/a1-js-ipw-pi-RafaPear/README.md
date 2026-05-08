![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)

# Trabalho IPW — A1 (JavaScript)

Repositório com a primeira avaliação de Introdução à Programação Web (IPW). Implementa exercícios em JavaScript com testes automáticos (Mocha).

Principais ficheiros
- `src/`
  - `ex1.js` … `ex5.js` — exercícios
  - `part2_promises.js` — parte 2 (API, promises sequential implementation)
  - `part2_promises_all.js` — parte 2 (API, parallel using Promise.all)
  - `part2_async_await.js` — parte 2 (API, async/await implementation)
  - `teams-ids.json` — IDs de equipas de entrada
  - `env.json` — não incluído por segurança; contém credenciais/token para a API
  - `teams.json` — ficheiro gerado (não comitar se contiver dados sensíveis)
  - `test/` — testes (ex1.test.js … ex5.test.js)

Requisitos
- Node.js (recomendado v16+)
- npm

Como executar (Windows - cmd.exe / PowerShell; Linux/macOS terminals similar)
1. Abrir um terminal e entrar na pasta `src`:

   ```cmd
   cd src
   ```

2. Instalar dependências (só na primeira vez):

   ```cmd
   npm install
   ```

3. Executar os testes:

   ```cmd
   npm test
   ```

4. Executar um exercício manualmente (opcional):

   ```cmd
   node ex1.js
   ```

Configurar `env.json` (exemplo mínimo — criar em `src/`)

```json
{
  "football-data.org": {
    "X-Auth-Token": "O_SEU_TOKEN_AQUI"
  }
}
```

Formato esperado para `teams-ids.json`:

```json
{
  "teams-ids": [57, 64, 65]
}
```

Notas
- Substitua `O_SEU_TOKEN_AQUI` pelo token real. Sem token, os scripts que consultam a API falham.
- Não comite `env.json` se contiver credenciais; adicione-o a `.gitignore`.
- `teams.json` é normalmente gerado pelos scripts e pode ser ignorado no repositório.
