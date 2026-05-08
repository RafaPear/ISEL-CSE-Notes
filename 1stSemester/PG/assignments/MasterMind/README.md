# 🎮 Mastermind Game (Versão com ins)

Este projeto é uma implementação do clássico jogo Mastermind, desenvolvido como parte de um trabalho de programação para o curso de Engenharia Informática e Computadores no Instituto Superior de Engenharia de Lisboa (ISEL).

## 🎯 Objetivo

O objetivo do jogo é adivinhar o código secreto gerado pelo computador. O código é uma combinação de 4 caracteres (posições), escolhidos aleatoriamente de um conjunto de cores (representadas por letras). O jogador tem até 10 tentativas para descobrir o código correto, e após cada tentativa o jogo dá feedback sobre:

- Quantos caracteres estão corretos e na posição correta.
- Quantos caracteres estão corretos, mas na posição errada.

## 📜 Regras

1. O código secreto é composto por **4 posições** e **6 cores** (representadas por letras ou números).
2. O jogador tem **10 tentativas** para descobrir o código.
3. Após cada tentativa, o jogador recebe feedback:
    - Quantos caracteres estão corretos e na posição certa.
    - Quantos caracteres estão corretos, mas em posições diferentes.
4. O código secreto não contém caracteres repetidos.

## ⚙️ Funcionalidades

- **Geração de código secreto**: O código secreto é gerado aleatoriamente e não contém caracteres repetidos.
- **Leitura de tentativas**: O jogador é solicitado a introduzir um palpite, que deve ser validado para garantir que tem o formato correto.
- **Feedback das tentativas**: O jogo informa quantos caracteres estão na posição correta e quantos estão corretos, mas na posição errada.
- **Máximo de tentativas**: O jogo termina após 10 tentativas, ou quando o jogador acerta o código.

## 🕹️ Como jogar

1. Clone o repositório:
   ```bash
   git clone https://github.com/RafaPear/Grupo1_MasterMind_LEIC15D
   cd Grupo1_MasterMind_LEIC15D   

2. Execute o jogo:
    ```bash
    kotlinc main.kt
    kotlin MainKt.class

3. Insira o seu palpite quando solicitado e tente descobrir o código secreto!

## 📂 Estrutura do Projeto

- `main.kt`: Contém a lógica principal do jogo, incluindo a geração do código secreto, a validação de palpites, e o cálculo de feedback.
- Mais por adicionar quando o projeto for reorganizado

## 🚀 Funcionalidades futuras
- De momento todas as funcionalidades estão implementadas
- 
## 👥 Contribuidores
- **Francisco Mendes**
- **Gustavo Costa**
- **Rafael Vermelho Pereira**

## 📄 Licença
Este projeto está licenciado sob a MIT License. Veja mais em [Licença](LICENSE.md)

