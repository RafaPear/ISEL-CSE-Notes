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
  *1.* O tempo de atraso de voos de uma determinada companhia aérea segue uma distribuição com média $mu = 7$ horas e desvio-padrão $sigma = 2.5$ horas.

  #v(0.5em)
  *a)* Qual é a probabilidade aproximada de o tempo médio do atraso de 100 voos independentes ser inferior a 6.5 horas?
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.* O tempo de reparação de um certo tipo de avaria segue uma distribuição exponencial com valor esperado 30 minutos.

  #v(0.5em)
  *a)* Determine a variância do tempo de reparação de uma avaria.

  #v(0.5em)
  *b)* Calcule a probabilidade aproximada de o tempo total de reparação de 35 avarias exceder 18 horas.
]

#pagebreak()

== SOLUÇÕES

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *1.a)* Seja $X$ o tempo de atraso de um voo, com $E[X] = 7$ horas e $sigma_X = 2.5$ horas.

  Para $n = 100$ voos, pelo Teorema do Limite Central, a média amostral aproxima-se de uma distribuição Normal:

  $ overline(X) approx N(mu, sigma/sqrt(n)) = N(7, 2.5/sqrt(100)) = N(7, 0.25) $

  Para calcular $P(overline(X) < 6.5)$, estandardizamos:
  $ z = (6.5 - 7)/0.25 = (-0.5)/0.25 = -2 $

  Consultando a tabela da Normal padrão:
  $ P(overline(X) < 6.5) = P(Z < -2) approx 0.0228 $

  *Resposta:* A probabilidade aproximada é 2.28%.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.a)* Para uma distribuição exponencial com parâmetro $lambda$:

  $ E[X] = 1/lambda quad "e" quad "Var"(X) = 1/lambda^2 $

  Dado que $E[X] = 30$ minutos:
  $ 1/lambda = 30 arrow.r.double lambda = 1/30 "min"^(-1) $

  Calculando a variância:
  $ "Var"(X) = 1/lambda^2 = 1/(1/30)^2 = 30^2 = 900 "min"^2 $

  Portanto, o desvio-padrão é:
  $ sigma_X = sqrt(900) = 30 "minutos" $

  *Resposta:* A variância é 900 min² e o desvio-padrão é 30 minutos.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.b)* Queremos calcular $P(T > 18 "horas")$, onde $T = sum_(i=1)^35 X_i$ é o tempo total de reparação.

  Primeiro, convertemos 18 horas para minutos:
  $ 18 "horas" = 18 times 60 = 1080 "minutos" $

  Para o tempo total de 35 avarias:

  *Esperança do tempo total:*
  $ E[T] = 35 times E[X] = 35 times 30 = 1050 "minutos" $

  *Variância do tempo total:*
  $ "Var"(T) = 35 times "Var"(X) = 35 times 900 = 31500 "min"^2 $

  *Desvio-padrão do tempo total:*
  $ sigma_T = sqrt(31500) approx 177.48 "minutos" $

  Pelo TLC, para $n = 35$ avarias (amostra grande):
  $ T approx N(1050, 177.48^2) $

  Para calcular $P(T > 1080)$, estandardizamos:
  $ z = (1080 - 1050)/177.48 = 30/177.48 approx 0.169 $

  Consultando a tabela da Normal padrão:
  $ P(T > 1080) = P(Z > 0.169) approx 1 - 0.5671 = 0.4329 $

  *Resposta:* A probabilidade aproximada é 43.29%.
]
