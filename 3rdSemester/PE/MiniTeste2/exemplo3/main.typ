#set page(
  paper: "a4",
  margin: (x: 2.5cm, y: 2cm),
)
#set text(
  font: "New Computer Modern",
  size: 11pt,
)
#set par(justify: true)

// Cabeçalho com logo e informações do aluno
#grid(
  columns: (auto, 1fr),
  gutter: 1em,
  image("logo.png", width: 3cm),
  align(left)[
    #text(
      size: 14pt,
      weight: "bold",
    )[Instituto Superior de Engenharia de Lisboa]
    #v(0.3em)
    #text(size: 12pt)[Licenciatura em Engenharia Informática e de Computadores]
    #v(0.3em)
    #text(size: 11pt, weight: "bold")[Probabilidades e Estatística]
  ],
)

#v(1em)
#line(length: 100%, stroke: 1pt)
#v(0.5em)

#align(center)[
  #text(size: 16pt, weight: "bold")[Mini-Teste]
  #v(0.3em)
  *Duração:* 20 minutos | Cada alínea vale 0,5 valores
  #v(0.2em)
  Não é permitido o uso de calculadora programável.
]

#v(0.5em)
#line(length: 100%, stroke: 1pt)
#v(1em)

// Campos para preenchimento
#grid(
  columns: (1fr, 1fr),
  gutter: 1em,
  [*Nome:* #box(width: 100%, stroke: (bottom: 0.5pt), height: 1.5em)[]],
  [*Número:* #box(width: 100%, stroke: (bottom: 0.5pt), height: 1.5em)[]],
)

#v(0.5em)

#grid(
  columns: (1fr, 1fr),
  gutter: 1em,
  [*Turma:* #box(width: 100%, stroke: (bottom: 0.5pt), height: 1.5em)[]],
  [*Data:* #box(width: 100%, stroke: (bottom: 0.5pt), height: 1.5em)[]],
)

#v(1.5em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *1.* O peso de peças produzidas numa fábrica segue uma distribuição Normal com média $mu = 200$ gramas e desvio-padrão $sigma = 15$ gramas.

  #v(0.5em)
  *a)* Para efeitos de controlo da qualidade foi recolhida uma amostra com 35 peças. Determine a probabilidade de o peso total das 35 peças ser no máximo 6950 gramas.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.* A velocidade de transferência de ficheiros de um servidor segue uma distribuição Normal com média $mu = 60$ kbits/s e desvio-padrão $sigma = 8$ kbits/s.

  #v(0.5em)
  *a)* Numa determinada noite um aluno vai transferir 10 ficheiros independentes desse servidor. Qual é a probabilidade da velocidade média de transferência dos 10 ficheiros ser no máximo de 58 kbits/s?
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *3.* O tempo de vida útil de televisores de uma determinada marca segue uma distribuição Normal com média $mu = 12$ anos e desvio-padrão $sigma = 2.5$ anos.

  #v(0.5em)
  *a)* Foram selecionados aleatoriamente 20 televisores desta marca. Qual é a probabilidade do tempo médio de vida útil dos 20 televisores ser no máximo de 10 anos?
]

#pagebreak()

== SOLUÇÕES

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *1.a)* Seja $X_i$ o peso da peça $i$, com $X_i tilde N(200, 15^2)$. O peso total de 35 peças é:

  $ T = sum_(i=1)^35 X_i $

  Para a soma de variáveis Normais independentes:

  *Esperança do peso total:*
  $ E[T] = 35 times E[X] = 35 times 200 = 7000 "gramas" $

  *Variância do peso total:*
  $ "Var"(T) = 35 times "Var"(X) = 35 times 15^2 = 35 times 225 = 7875 $

  *Desvio-padrão do peso total:*
  $ sigma_T = sqrt(7875) approx 88.74 "gramas" $

  Portanto: $T tilde N(7000, 88.74^2)$

  Para calcular $P(T <= 6950)$, estandardizamos:
  $ z = (6950 - 7000)/88.74 = (-50)/88.74 approx -0.56 $

  Consultando a tabela da Normal padrão:
  $ P(T <= 6950) = P(Z <= -0.56) approx 0.2877 $

  *Resposta:* A probabilidade é aproximadamente 28.77%.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.a)* Seja $X$ a velocidade de transferência, com $X tilde N(60, 8^2)$. Para $n = 10$ ficheiros, a velocidade média segue:

  $ overline(X) tilde N(mu, sigma/sqrt(n)) = N(60, 8/sqrt(10)) $

  Calculando o desvio-padrão de $overline(X)$:
  $ sigma_(overline(X)) = 8/sqrt(10) approx 8/3.162 approx 2.53 "kbits/s" $

  Para calcular $P(overline(X) <= 58)$, estandardizamos:
  $ z = (58 - 60)/2.53 = (-2)/2.53 approx -0.79 $

  Consultando a tabela da Normal padrão:
  $ P(overline(X) <= 58) = P(Z <= -0.79) approx 0.2148 $

  *Resposta:* A probabilidade é aproximadamente 21.48%.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *3.a)* Seja $X$ o tempo de vida útil, com $X tilde N(12, 2.5^2)$. Para $n = 20$ televisores, o tempo médio de vida útil segue:

  $ overline(X) tilde N(mu, sigma/sqrt(n)) = N(12, 2.5/sqrt(20)) $

  Calculando o desvio-padrão de $overline(X)$:
  $ sigma_(overline(X)) = 2.5/sqrt(20) = 2.5/4.472 approx 0.559 "anos" $

  Para calcular $P(overline(X) <= 10)$, estandardizamos:
  $ z = (10 - 12)/0.559 = (-2)/0.559 approx -3.58 $

  Consultando a tabela da Normal padrão:
  $ P(overline(X) <= 10) = P(Z <= -3.58) approx 0.0002 $

  *Resposta:* A probabilidade é aproximadamente 0.02% (evento muito raro).
]
