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
  *1.* Em um determinado sistema, dois processadores estão emparelhados de forma que a capacidade total de processamento pode ser assumida como a soma das capacidades individuais dos dois processadores. Seja $C_1$ a v.a. correspondente à carga no Processador 1 e $C_2$ a v.a. correspondente à carga no Processador 2. Considere a carga total $C_t = C_1 + C_2$, em que:

  - $C_1 tilde N(140, 15^2)$
  - $C_2 tilde N(150, 10^2)$

  #v(0.5em)
  *a)* Determine a probabilidade de a carga total ultrapassar 270 MIPS.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.* Um servidor processa requisições cujo tempo de resposta segue uma distribuição Normal com média $mu = 18$ ms e desvio-padrão $sigma = 5$ ms.

  #v(0.5em)
  *a)* Se o servidor tiver de enviar 100 respostas seguidas, qual é a probabilidade de o tempo médio dessas respostas ser superior a 20 ms?
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *3.* O tempo de preenchimento de um impresso do tipo A segue uma distribuição Uniforme no intervalo [2.5, 5] minutos.

  #v(0.5em)
  *a)* Determine a esperança matemática e a variância do tempo de preenchimento de um impresso.

  #v(0.5em)
  *b)* Considere que num determinado dia são preenchidos 70 impressos do tipo A. Determine a probabilidade de o tempo total de preenchimento ser pelo menos 4.5 horas.
]

#pagebreak()

== SOLUÇÕES

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *1.a)* Como a soma de variáveis aleatórias Normais independentes é também Normal, temos:

  $ C_t = C_1 + C_2 tilde N(mu_(C_t), sigma_(C_t)^2) $

  Calculando a média de $C_t$:
  $ mu_(C_t) = mu_(C_1) + mu_(C_2) = 140 + 150 = 290 "MIPS" $

  Calculando a variância de $C_t$ (assumindo independência):
  $
    sigma_(C_t)^2 = sigma_(C_1)^2 + sigma_(C_2)^2 = 15^2 + 10^2 = 225 + 100 = 325
  $

  Portanto, o desvio-padrão é:
  $ sigma_(C_t) = sqrt(325) approx 18.03 "MIPS" $

  Logo: $C_t tilde N(290, 18.03^2)$

  Para calcular $P(C_t > 270)$, estandardizamos:
  $ z = (270 - 290)/18.03 = (-20)/18.03 approx -1.11 $

  Consultando a tabela da Normal padrão:
  $ P(C_t > 270) = P(Z > -1.11) = P(Z < 1.11) approx 0.8665 $

  *Resposta:* A probabilidade é aproximadamente 86.65%.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.a)* Dado que $X tilde N(18, 5^2)$ e temos $n = 100$ respostas, a média amostral segue:

  $ overline(X) tilde N(mu, sigma/sqrt(n)) = N(18, 5/sqrt(100)) = N(18, 0.5) $

  Para calcular $P(overline(X) > 20)$, estandardizamos:
  $ z = (20 - 18)/0.5 = 2/0.5 = 4 $

  Consultando a tabela da Normal padrão:
  $ P(overline(X) > 20) = P(Z > 4) approx 0.00003 $

  *Resposta:* A probabilidade é praticamente nula (aproximadamente 0.003%).
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *3.a)* Para uma distribuição Uniforme $X tilde U(a, b)$ com $a = 2.5$ e $b = 5$ minutos:

  *Esperança matemática:*
  $ E[X] = (a + b)/2 = (2.5 + 5)/2 = 7.5/2 = 3.75 "minutos" $

  *Variância:*
  $
    "Var"(X) = (b - a)^2/12 = (5 - 2.5)^2/12 = 2.5^2/12 = 6.25/12 approx 0.5208 "min"^2
  $

  *Resposta:* $E[X] = 3.75$ minutos e $"Var"(X) approx 0.5208$ $text("min")^2$.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *3.b)* Queremos $P(T >= 4.5 "horas")$ onde $T$ é o tempo total de 70 impressos.

  *Passo 1: Converter 4.5 horas para minutos*
  $ 4.5 "horas" = 4.5 times 60 = 270 "minutos" $

  *Passo 2: Calcular as propriedades de T (em minutos)*

  O tempo total é: $T = sum_(i=1)^70 X_i$

  $ E[T] = 70 times E[X] = 70 times 3.75 = 262.5 "minutos" $
  $ "Var"(T) = 70 times "Var"(X) = 70 times 0.5208 approx 36.46 "min"^2 $
  $ sigma_T = sqrt(36.46) approx 6.04 "minutos" $

  *Passo 3: Aplicar o TLC*

  Pelo TLC, $T$ aproxima-se de uma Normal:
  $ T approx N(262.5, 6.04^2) $

  *Passo 4: Calcular a probabilidade*

  Para calcular $P(T >= 270)$, estandardizamos:
  $ z = (270 - 262.5)/6.04 = 7.5/6.04 approx 1.24 $

  Consultando a tabela da Normal padrão:
  $ P(T >= 270) = P(Z >= 1.24) approx 1 - 0.8925 = 0.1075 $

  *Resposta:* A probabilidade é aproximadamente 10.75%.

  *Interpretação:* O tempo esperado é 262.5 minutos (≈ 4.38 horas), portanto há cerca de 11% de chance de exceder 270 minutos (4.5 horas).
]
