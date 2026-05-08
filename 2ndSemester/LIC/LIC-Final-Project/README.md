# 🎰 Jogo da Roleta

## Sobre o Projeto
Este repositório apresenta a implementação de um **jogo de roleta** desenvolvido para a unidade curricular de **Laboratório de Informática e Computadores**. O jogador pode realizar apostas através de um teclado, utilizando créditos inseridos num moedeiro virtual. O hardware e o software trabalham em conjunto para simular uma máquina de roleta funcional, com os resultados mostrados num ecrã LCD.

## Componentes Principais
- **Keyboard Reader** – lê as teclas pressionadas.
- **Coin Acceptor** – simula a introdução de moedas e converte em créditos.
- **Serial LCD Controller (SLCDC)** – apresenta informações do jogo no LCD.
- **Serial Roulette Controller (SRC)** – indica o número sorteado na roleta.
- **Control (software em Kotlin)** – gere toda a lógica do jogo e a comunicação entre os módulos.

Todos os módulos comunicam entre si através de ligações série.

## Como Instalar
### Pré-requisitos
- Kotlin instalado.
- Ambiente de desenvolvimento compatível (IntelliJ IDEA, VS Code com plugin Kotlin, etc.).
- Hardware de simulação (opcional).

### Compilação e Execução
#### Windows
```sh
git clone https://github.com/seu-repositorio/jogo-roleta.git
cd jogo-roleta
kotlinc src -include-runtime -d jogo-roleta.jar
java -jar jogo-roleta.jar
```

#### Linux
```sh
git clone https://github.com/seu-repositorio/jogo-roleta.git
cd jogo-roleta
kotlinc src -include-runtime -d jogo-roleta.jar
java -jar jogo-roleta.jar
```

## Jogabilidade
1. Iniciar o jogo com `*` quando houver créditos disponíveis.
2. Apostar utilizando as teclas numéricas (0-9) ou alfabéticas (A-D).
3. Cada aposta consome 1 crédito.
4. Finalizar as apostas com `#`, dando início ao sorteio.
5. O número/letra sorteado é exibido e o saldo é atualizado.
6. Modo de manutenção:
   - `A`: consultar contadores de moedas e jogos.
   - `C`: consultar a lista de números sorteados.
   - `D`: desligar o sistema.

## Licença
Projeto de uso educacional, sem licença específica. Se o utilizar ou modificar, cite a fonte original.

---
Desenvolvido para **Laboratório de Informática e Computadores - 2024/2025**
