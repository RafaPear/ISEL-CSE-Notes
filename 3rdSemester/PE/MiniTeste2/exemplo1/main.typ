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
  *1.* O tamanho dos ficheiros processados por um determinado servidor pode ser modelado por uma variável aleatória Normal com média $mu = 200 "MB"$ e desvio-padrão $sigma = 20 "MB"$. Retira-se uma amostra aleatória simples de $n = 8$ ficheiros, assumindo independência entre observações.
  #v(0.5em)
  *a)* Determine a probabilidade de a média amostral $overline(X)$ exceder 900 MB. Apresente os passos de resolução.
]
#v(1em)
#box(width: 100%, stroke: black, outset: 10pt)[
  *2.* Um segundo servidor processa ficheiros provenientes da mesma população, mas desta vez com uma amostra grande. Seja $n = 100$.
  #v(0.5em)
  *a)* Usando o Teorema do Limite Central, estime $P(overline(X) > 220)$.
]
#v(1em)
#box(width: 100%, stroke: black, outset: 10pt)[
  *3.* Considere a variável aleatória contínua $X$ que representa o tempo (em minutos) necessário para concluir uma operação de I/O num sistema, com função densidade de probabilidade
  $ f(x) = (3x^2)/1000, quad 0 <= x <= 10. $
  #v(0.5em)
  *a)* Verifique que $f(x)$ é uma função densidade válida e determine:
  - a esperança matemática $E[X]$,
  - a variância e o desvio-padrão de $X$.
  #v(0.5em)
  *b)* Suponha que são recolhidas $n = 36$ observações independentes da mesma variável $X$. Aplicando o TLC, estime a probabilidade de a média amostral satisfazer:
  $ overline(X) > 8. $
]
#pagebreak()
== SOLUÇÕES
#v(1em)
#box(width: 100%, stroke: black, outset: 10pt)[
  *1.a)* Como $X tilde N(200, 20)$ e temos uma amostra de $n = 8$ observações, a média amostral segue:

  $ overline(X) tilde N(mu, sigma/sqrt(n)) = N(200; 20/sqrt(8)) $

  Calculando o desvio-padrão de $overline(X)$:
  $
    sigma_(overline(X)) = 20/sqrt(8) = 20/(2sqrt(2)) = (10sqrt(2))/1 approx 7.071 "MB"
  $

  Para encontrar $P(overline(X) > 900)$, estandardizamos:
  $ z = (900 - 200)/7.071 = 700/7.071 approx 99.00 $

  Como $z approx 99$ está extremamente afastado da média (99 desvios-padrão), consultando a tabela da Normal:
  $ P(overline(X) > 900) = P(Z > 99) approx 0 $

  *Resposta:* A probabilidade é praticamente nula (evento impossível na prática).
]
#v(1em)
#box(width: 100%, stroke: black, outset: 10pt)[
  *2.a)* Pelo Teorema do Limite Central, para $n = 100$ (amostra grande), a média amostral aproxima-se de uma distribuição Normal:

  $ overline(X) approx N(mu, sigma/sqrt(n)) = N(200; 20/sqrt(100)) = N(200; 2) $

  Para calcular $P(overline(X) > 220)$, estandardizamos:
  $ z = (220 - 200)/2 = 20/2 = 10 $

  Consultando a tabela da Normal padrão:
  $ P(overline(X) > 220) = P(Z > 10) approx 0 $

  *Resposta:* A probabilidade é praticamente zero, pois 220 MB está 10 desvios-padrão acima da média.
]
#v(1em)
#box(width: 100%, stroke: black, outset: 10pt)[
  *3.a)* Para verificar que $f(x)$ é uma fdp válida, calculamos:

  $
    integral_0^10 (3x^2)/1000 dif x = 3/1000 integral_0^10 x^2 dif x = 3/1000 [x^3/3]_0^10 = 3/1000 dot 1000/3 = 1 checkmark
  $

  *Esperança matemática:*
  $
    E[X] = integral_0^10 x dot (3x^2)/1000 dif x = 3/1000 integral_0^10 x^3 dif x = 3/1000 [x^4/4]_0^10
  $
  $ = 3/1000 dot 10000/4 = 30000/4000 = 7.5 "minutos" $

  *Cálculo de* $E[X^2]$:
  $
    E[X^2] = integral_0^10 x^2 dot (3x^2)/1000 dif x = 3/1000 integral_0^10 x^4 dif x = 3/1000 [x^5/5]_0^10
  $
  $ = 3/1000 dot 100000/5 = 300000/5000 = 60 $

  *Variância:*
  $ "Var"(X) = E[X^2] - (E[X])^2 = 60 - (7.5)^2 = 60 - 56.25 = 3.75 $

  *Desvio-padrão:*
  $ sigma_X = sqrt(3.75) approx 1.9365 "minutos" $
]
#v(1em)
#box(width: 100%, stroke: black, outset: 10pt)[
  *3.b)* Com $n = 36$ observações independentes, pelo TLC:

  $
    overline(X) approx N(E[X]; sigma_X/sqrt(n)) = N(7.5; 1.9365/sqrt(36)) = N(7.5; 1.9365/6)
  $

  Calculando o desvio-padrão de $overline(X)$:
  $ sigma_(overline(X)) = 1.9365/6 approx 0.3227 "minutos" $

  Para encontrar $P(overline(X) > 8)$, estandardizamos:
  $ z = (8 - 7.5)/0.3227 = 0.5/0.3227 approx 1.549 $

  Consultando a tabela da Normal padrão, $P(Z > 1.549) approx P(Z > 1.55)$:
  $ P(overline(X) > 8) = P(Z > 1.549) approx 1 - 0.9394 = 0.0606 $

  *Resposta:* A probabilidade é aproximadamente 6.06%.
]
