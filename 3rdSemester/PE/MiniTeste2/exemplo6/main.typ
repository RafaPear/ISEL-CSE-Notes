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
  *1.* A duração de sessões de utilizadores numa plataforma de streaming segue uma distribuição Uniforme entre 15 e 45 minutos.

  #v(0.5em)
  *a)* Calcule a esperança matemática e o desvio-padrão da duração de uma sessão.

  #v(0.5em)
  *b)* Numa análise foram recolhidas 60 sessões independentes. Qual é a probabilidade aproximada de a duração média das sessões ser superior a 32 minutos?
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.* O número de erros de compilação num projeto de software segue uma distribuição Normal com média 8 erros e desvio-padrão 2.5 erros. Foram analisados 40 projetos independentes.

  #v(0.5em)
  *a)* Determine a probabilidade de o número total de erros nos 40 projetos ser no máximo 310 erros.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *3.* Numa rede de sensores IoT, três nós transmitem dados simultaneamente. O consumo energético (em mW) de cada nó segue:
  - Nó 1: $N(50, 8^2)$
  - Nó 2: $N(45, 6^2)$
  - Nó 3: $N(55, 10^2)$

  #v(0.5em)
  *a)* Assumindo independência, determine a probabilidade de o consumo total dos três nós exceder 160 mW.
]

#pagebreak()

== SOLUÇÕES

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *1.a)* Para uma distribuição Uniforme $X tilde U(a, b)$ com $a = 15$ e $b = 45$ minutos:

  *Esperança matemática:*
  $ E[X] = (a + b)/2 = (15 + 45)/2 = 60/2 = 30 "minutos" $

  *Variância:*
  $ "Var"(X) = (b - a)^2/12 = (45 - 15)^2/12 = 30^2/12 = 900/12 = 75 "min"^2 $

  *Desvio-padrão:*
  $ sigma_X = sqrt(75) = sqrt(25 times 3) = 5sqrt(3) approx 8.66 "minutos" $

  *Resposta:* $E[X] = 30$ minutos e $sigma_X approx 8.66$ minutos.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *1.b)* Com $n = 60$ sessões, pelo TLC:

  $ overline(X) approx N(E[X], sigma_X/sqrt(n)) = N(30, 8.66/sqrt(60)) $

  Calculando o desvio-padrão de $overline(X)$:
  $ sigma_(overline(X)) = 8.66/sqrt(60) = 8.66/7.746 approx 1.118 "minutos" $

  Para calcular $P(overline(X) > 32)$, estandardizamos:
  $ z = (32 - 30)/1.118 = 2/1.118 approx 1.789 $

  Consultando a tabela da Normal padrão:
  $ P(overline(X) > 32) = P(Z > 1.789) approx 1 - 0.9633 = 0.0367 $

  *Resposta:* A probabilidade aproximada é 3.67%.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *2.a)* Seja $X_i$ o número de erros no projeto $i$, com $X_i tilde N(8, 2.5^2)$.

  O número total de erros em 40 projetos é: $T = sum_(i=1)^40 X_i$

  *Esperança do total:*
  $ E[T] = 40 times 8 = 320 "erros" $

  *Variância do total:*
  $ "Var"(T) = 40 times 2.5^2 = 40 times 6.25 = 250 $

  *Desvio-padrão do total:*
  $ sigma_T = sqrt(250) = sqrt(25 times 10) = 5sqrt(10) approx 15.81 "erros" $

  Portanto: $T tilde N(320, 15.81^2)$

  Para calcular $P(T <= 310)$, estandardizamos:
  $ z = (310 - 320)/15.81 = (-10)/15.81 approx -0.633 $

  Consultando a tabela da Normal padrão:
  $ P(T <= 310) = P(Z <= -0.633) approx 0.2643 $

  *Resposta:* A probabilidade é aproximadamente 26.43%.
]

#v(1em)

#box(width: 100%, stroke: black, outset: 10pt)[
  *3.a)* O consumo total é $C_t = C_1 + C_2 + C_3$, onde:
  - $C_1 tilde N(50, 8^2)$
  - $C_2 tilde N(45, 6^2)$
  - $C_3 tilde N(55, 10^2)$

  Para a soma de variáveis Normais independentes:

  *Média do consumo total:*
  $ mu_(C_t) = 50 + 45 + 55 = 150 "mW" $

  *Variância do consumo total:*
  $ sigma_(C_t)^2 = 8^2 + 6^2 + 10^2 = 64 + 36 + 100 = 200 $

  *Desvio-padrão do consumo total:*
  $ sigma_(C_t) = sqrt(200) = sqrt(100 times 2) = 10sqrt(2) approx 14.14 "mW" $

  Logo: $C_t tilde N(150, 14.14^2)$

  Para calcular $P(C_t > 160)$, estandardizamos:
  $ z = (160 - 150)/14.14 = 10/14.14 approx 0.707 $

  Consultando a tabela da Normal padrão:
  $ P(C_t > 160) = P(Z > 0.707) approx 1 - 0.7602 = 0.2398 $

  *Resposta:* A probabilidade é aproximadamente 23.98%.
]
