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
  *1.* O consumo de memória RAM de uma aplicação pode ser modelado por uma variável aleatória contínua $X$ com função densidade de probabilidade:

  $ f(x) = (2x)/25, quad 0 <= x <= 5 "GB" $

  #v(0.5em)
  *a)* Verifique que $f(x)$ é uma função densidade válida e determine a esperança matemática e a variância de $X$.

  #v(0.5em)
  *b)* Foram monitorizadas 50 execuções independentes desta aplicação. Usando o TLC, estime a probabilidade de o consumo médio de RAM exceder 3.5 GB.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.* Num data center, dois sistemas de armazenamento funcionam em paralelo. A capacidade disponível do Sistema A segue $N(500, 40^2)$ GB e a do Sistema B segue $N(600, 50^2)$ GB. Assumindo independência entre os sistemas:

  #v(0.5em)
  *a)* Determine a probabilidade de a capacidade total disponível ser inferior a 1050 GB.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *3.* O tempo entre chegadas de pedidos HTTP a um servidor web segue uma distribuição exponencial com média de 2 segundos.

  #v(0.5em)
  *a)* Calcule a probabilidade aproximada de que o tempo total entre 80 pedidos consecutivos seja superior a 170 segundos.
]

#pagebreak()

== SOLUÇÕES

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *1.a)* Verificando que $f(x)$ é uma fdp válida:

  $
    integral_0^5 (2x)/25 dif x = 2/25 integral_0^5 x dif x = 2/25 [x^2/2]_0^5 = 2/25 dot 25/2 = 1 checkmark
  $

  *Esperança matemática:*
  $
    E[X] = integral_0^5 x dot (2x)/25 dif x = 2/25 integral_0^5 x^2 dif x = 2/25 [x^3/3]_0^5
  $
  $ = 2/25 dot 125/3 = 250/75 = 10/3 approx 3.333 "GB" $

  *Cálculo de* $E[X^2]$:
  $
    E[X^2] = integral_0^5 x^2 dot (2x)/25 dif x = 2/25 integral_0^5 x^3 dif x = 2/25 [x^4/4]_0^5
  $
  $ = 2/25 dot 625/4 = 1250/100 = 12.5 "GB"^2 $

  *Variância:*
  $ "Var"(X) = E[X^2] - (E[X])^2 = 12.5 - (10/3)^2 = 12.5 - 100/9 $
  $ = 112.5/9 - 100/9 = 12.5/9 approx 1.389 "GB"^2 $

  *Desvio-padrão:*
  $ sigma_X = sqrt(1.389) approx 1.179 "GB" $

  *Resposta:* $E[X] approx 3.333$ GB, $"Var"(X) approx 1.389$ GB².
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *1.b)* Com $n = 50$ execuções, pelo TLC:

  $ overline(X) approx N(E[X], sigma_X/sqrt(n)) = N(3.333, 1.179/sqrt(50)) $

  Calculando o desvio-padrão de $overline(X)$:
  $ sigma_(overline(X)) = 1.179/sqrt(50) = 1.179/7.071 approx 0.167 "GB" $

  Para calcular $P(overline(X) > 3.5)$, estandardizamos:
  $ z = (3.5 - 3.333)/0.167 = 0.167/0.167 = 1 $

  Consultando a tabela da Normal padrão:
  $ P(overline(X) > 3.5) = P(Z > 1) approx 1 - 0.8413 = 0.1587 $

  *Resposta:* A probabilidade aproximada é 15.87%.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.a)* A capacidade total é $C_t = C_A + C_B$, onde:
  - $C_A tilde N(500, 40^2)$
  - $C_B tilde N(600, 50^2)$

  Para a soma de variáveis Normais independentes:

  $ mu_(C_t) = 500 + 600 = 1100 "GB" $
  $ sigma_(C_t)^2 = 40^2 + 50^2 = 1600 + 2500 = 4100 $
  $ sigma_(C_t) = sqrt(4100) approx 64.03 "GB" $

  Logo: $C_t tilde N(1100, 64.03^2)$

  Para calcular $P(C_t < 1050)$, estandardizamos:
  $ z = (1050 - 1100)/64.03 = (-50)/64.03 approx -0.78 $

  Consultando a tabela da Normal padrão:
  $ P(C_t < 1050) = P(Z < -0.78) approx 0.2177 $

  *Resposta:* A probabilidade é aproximadamente 21.77%.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *3.a)* Para uma distribuição exponencial com $E[X] = 2$ segundos:

  $ lambda = 1/2 = 0.5 "s"^(-1) $
  $ "Var"(X) = 1/lambda^2 = 4 "s"^2 $
  $ sigma_X = 2 "s" $

  Para o tempo total de 80 pedidos: $T = sum_(i=1)^80 X_i$

  $ E[T] = 80 times 2 = 160 "segundos" $
  $ "Var"(T) = 80 times 4 = 320 "s"^2 $
  $ sigma_T = sqrt(320) approx 17.89 "segundos" $

  Pelo TLC: $T approx N(160, 17.89^2)$

  Para calcular $P(T > 170)$, estandardizamos:
  $ z = (170 - 160)/17.89 = 10/17.89 approx 0.559 $

  Consultando a tabela da Normal padrão:
  $ P(T > 170) = P(Z > 0.559) approx 1 - 0.7119 = 0.2881 $

  *Resposta:* A probabilidade aproximada é 28.81%.
]
