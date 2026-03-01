#import "@preview/lilaq:0.5.0" as lq

#set heading(numbering: "1.1.1 -")

// Caixas de destaque (como no original, porque isto faz falta)
#let info-box(content) = pad(y: 5pt, box(
  width: 100%,
  inset: 8pt,
  fill: rgb("#f8fafc"),
  stroke: 0.5pt + rgb("#64748b"),
  radius: 3pt,
  content,
))

#let alert-box(content) = pad(y: 5pt, box(
  width: 100%,
  inset: 8pt,
  fill: rgb("#fffafb"),
  stroke: 0.5pt + rgb("#e11d48"),
  radius: 3pt,
  content,
))

#align(center)[
  #text(
    size: 18pt,
    weight: "bold",
    fill: rgb("#1e293b"),
  )[Cheat Sheet — Probabilidades e Estatística]
  #v(0.2em)
  #text(
    size: 11pt,
    style: "italic",
    fill: rgb("#475569"),
  )[LEIC — Resumo Completo (modo sobreviver mesmo)]
  #v(0.5em)
  #line(length: 100%, stroke: 1pt + rgb("#1e293b"))
]

#v(1em)

#outline(title: "Índice", indent: 1em)

#pagebreak()

= Estatística Descritiva

Antes de começares a inventar regressões todas sexy, tens de saber fazer contas básicas sem falhar sinais.

Média:

$ overline(x) = (sum_(i=1)^n x_i) / n $

Variância amostral:

$ s^2 = (1/(n-1)) sum_(i=1)^n (x_i - overline(x))^2 $

#alert-box[
  Variância com $n$ é da *população*. Com $(n-1)$ é da *amostra* — corrige o facto de a amostra subestimar sempre a variância real. Em exame é sempre $(n-1)$, a não ser que o enunciado diga explicitamente que tens a população toda.
]

Desvio padrão:

$ s = sqrt(s^2) $

Histograma: cada barra mostra quantas observações caem naquele valor. A linha vermelha é a média — percebe-se de imediato se a distribuição é simétrica ou não.

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "Histograma — distribuição amostral (linha vermelha = média)",
    xlim: (-0.5, 7.5),
    ylim: (0, 7),
    let vals = (2, 3, 3, 4, 4, 4, 5, 5, 5, 5, 6, 6, 7),
    let xs = range(1, 8),
    let counts = xs.map(v => vals.filter(x => x == v).len()),
    lq.bar(xs, counts, width: 0.7, fill: rgb("#93c5fd")),
    lq.line((4.69, 0), (4.69, 6.5), stroke: 2pt + rgb("#e11d48")),
  )
]

== Mediana, Quartis e IQR

Mediana: valor que parte a amostra ao meio. Não liga a outliers.

$ "Me" = cases(x_((n+1)/2) & "se" n "ímpar", (x_(n/2) + x_(n/2+1))/2 & "se" n "par") $

Quartis: $Q_1$ (25%), $Q_2$ (mediana, 50%), $Q_3$ (75%).

Amplitude interquartil (robusta, ignora caudas):

$ "IQR" = Q_3 - Q_1 $

#alert-box[
  Outlier suspeito: abaixo de $Q_1 - 1.5 dot "IQR"$ ou acima de $Q_3 + 1.5 dot "IQR"$. Não é lei, é regra de bolso.
]

== Tabelas de Frequência

#table(
  columns: (auto, auto, auto, auto),
  align: center,
  [*Valor*], [*Freq. Abs.*], [*Freq. Rel.*], [*Freq. Acum.*],
  $x_i$, $n_i$, $f_i = n_i \/ n$, $F_i = sum_(j<=i) f_j$,
)

A frequência acumulada chega sempre a 1 no fim. Se não chegar, erraste nas contas.

= Probabilidades

$ P(A) = "casos favoraveis" / "casos possiveis" $

$ P(A "union" B) = P(A) + P(B) - P(A "inter" B) $

Probabilidade condicional (definição base — tudo deriva daqui):

$ P(A|B) = P(A "inter" B) / P(B) $

E o Bayes é só reorganizar isso:

$ P(A|B) = P(B|A) P(A) / P(B) $

#info-box[
  Independência significa que saber que $B$ ocorreu não te diz nada sobre $A$. Se tens dúvida, verifica: $P(A inter B) = P(A) dot P(B)$. Se a igualdade não se verificar, não são independentes.
]

Os dois círculos são A e B. A zona sobreposta (mais escura) é $A inter B$. A zona total coberta é $A union B$.

#align(center)[
  #box(width: 9cm, height: 4.5cm, {
    place(left + top, dx: 0.3cm, dy: 0.2cm,
      ellipse(width: 5.5cm, height: 4.1cm,
        fill: rgb("#93c5fd88"), stroke: 1.5pt + rgb("#1e40af")))
    place(left + top, dx: 3.2cm, dy: 0.2cm,
      ellipse(width: 5.5cm, height: 4.1cm,
        fill: rgb("#fca5a588"), stroke: 1.5pt + rgb("#be123c")))
    place(left + top, dx: 0.7cm, dy: 1.8cm)[*A*]
    place(left + top, dx: 3.9cm, dy: 1.8cm)[*A∩B*]
    place(left + top, dx: 7.2cm, dy: 1.8cm)[*B*]
    place(left + top, dx: 0cm, dy: 0cm,
      box(width: 9cm, height: 4.5cm,
        stroke: 0.5pt + rgb("#94a3b8"), radius: 4pt))
  })
]

== Probabilidade Total e Bayes com partição

Se $B_1, ..., B_k$ formam uma partição de $Omega$:

$ P(A) = sum_(i=1)^k P(A|B_i) P(B_i) $

Útil quando chegas a $A$ por vários caminhos. Somas as probabilidades de cada caminho.

As faixas $B_1, B_2, B_3$ particionam o espaço amostral. O evento $A$ atravessa algumas delas — a probabilidade de $A$ é a soma das fatias que atravessa.

#align(center)[
  #box(width: 10cm, height: 4cm, {
    place(left+top, dx: 0cm, dy: 0cm,
      box(width: 10cm, height: 4cm, fill: rgb("#f1f5f9"), stroke: 1pt + rgb("#94a3b8"), radius: 3pt))
    place(left+top, dx: 0cm, dy: 0cm,
      box(width: 3.33cm, height: 4cm, fill: rgb("#bfdbfe77"), stroke: 0.5pt + rgb("#64748b")))
    place(left+top, dx: 3.33cm, dy: 0cm,
      box(width: 3.33cm, height: 4cm, fill: rgb("#bbf7d077"), stroke: 0.5pt + rgb("#64748b")))
    place(left+top, dx: 6.66cm, dy: 0cm,
      box(width: 3.34cm, height: 4cm, fill: rgb("#fde68a77"), stroke: 0.5pt + rgb("#64748b")))
    place(left+top, dx: 0.5cm, dy: 0.2cm)[*B₁*]
    place(left+top, dx: 3.83cm, dy: 0.2cm)[*B₂*]
    place(left+top, dx: 7.16cm, dy: 0.2cm)[*B₃*]
    place(left+top, dx: 1.5cm, dy: 1.2cm,
      ellipse(width: 7cm, height: 1.6cm,
        fill: rgb("#e11d4833"), stroke: 1.5pt + rgb("#e11d48")))
    place(left+top, dx: 4.5cm, dy: 1.8cm)[*A*]
  })
]

== Combinatória

Quando o espaço amostral são arranjos de objetos:

Permutações de $n$ elementos distintos:

$ P_n = n! $

Arranjos de $n$ elementos tomados $k$ a $k$ (ordem importa):

$ A_n^k = n! / (n-k)! $

Combinações de $n$ elementos tomados $k$ a $k$ (ordem não importa):

$ C_n^k = binom(n,k) = n! / (k! (n-k)!) $

#info-box[
  Se o enunciado diz "grupos", "comissões", "amostras" → combinações. Se diz "sequências", "ordenados" → arranjos.
]

*Exemplo — arranjos de 2 de \{A, B, C\} (ordem importa):*

#align(center)[
  #grid(columns: 7, gutter: 6pt, align: center + horizon,
    box(fill: rgb("#bfdbfe"), inset: 6pt, radius: 3pt)[*1.º lugar*],
    box(inset: 6pt)[×],
    box(fill: rgb("#bbf7d0"), inset: 6pt, radius: 3pt)[*2.º lugar*],
    box(inset: 6pt)[=],
    box(fill: rgb("#fde68a"), inset: 8pt, radius: 3pt)[*3 × 2 = 6*],
    box(inset: 4pt)[],
    box(inset: 4pt)[],
    box(fill: rgb("#bfdbfe99"), inset: 5pt, radius: 3pt)[3 opções],
    box(inset: 6pt)[],
    box(fill: rgb("#bbf7d099"), inset: 5pt, radius: 3pt)[2 restantes],
    box(inset: 6pt)[],
    box(fill: rgb("#fde68a99"), inset: 5pt, radius: 3pt)[AB AC BA BC CA CB],
    box(inset: 4pt)[],
    box(inset: 4pt)[],
  )
]

*Exemplo — combinações de 2 de \{A, B, C\} (ordem não importa):*

#align(center)[
  #grid(columns: 5, gutter: 6pt, align: center + horizon,
    box(fill: rgb("#fde68a"), inset: 8pt, radius: 3pt)[*6 arranjos*],
    box(inset: 6pt)[÷],
    box(fill: rgb("#fca5a5"), inset: 8pt, radius: 3pt)[*2! = 2*],
    box(inset: 6pt)[=],
    box(fill: rgb("#bbf7d0"), inset: 8pt, radius: 3pt)[*3 combinações*],
  )
  #v(4pt)
  #text(size: 9pt, fill: rgb("#475569"))[AB e BA são a mesma coisa → só conta AB. Idem AC, BC.]
]

= Variáveis Aleatórias

Esperança:

$ E(X) = sum x_i p_i $

Variância (forma inteligente):

$ "Var"(X) = E(X^2) - (E(X))^2 $

#info-box[
  $"Var"(X) = E(X^2) - (E(X))^2$ é a forma rápida. A outra ($sum(x_i - mu)^2 p_i$) dá o mesmo mas obriga a calcular a média primeiro e depois fazer a subtracção para cada termo — lento e com mais margem para erro.
]

== Binomial

$ X ~ "Bin"(n,p) $

$ P(X = k) = binom(n,k) p^k (1-p)^(n-k) $

$ E(X) = n p $

$ "Var"(X) = n p(1-p) $

O gráfico mostra $P(X=k)$ para $n=10$, $p=0.3$. A barra mais alta está em $k = floor(n p) = 3$, que é a moda da distribuição.

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "Binomial Bin(10, 0.3) — P(X=k) para cada k",
    xlim: (-0.5, 10.5),
    ylim: (0, 0.35),
    let n = 10,
    let p = 0.3,
    let xs = range(n + 1),
    let fact = n => if n <= 1 { 1 } else {
      range(1, n + 1).fold(1, (a, b) => a * b)
    },
    let comb = (n, k) => fact(n) / (fact(k) * fact(n - k)),
    let ys = xs.map(k => comb(n, k) * calc.pow(p, k) * calc.pow(1 - p, n - k)),
    lq.bar(xs, ys, width: 0.6),
  )
]

== Geométrica

Número de tentativas até ao primeiro sucesso:

$ X ~ "Geom"(p) $

$ P(X = k) = (1-p)^(k-1) p, quad k = 1, 2, 3, ... $

$ E(X) = 1/p $

$ "Var"(X) = (1-p)/p^2 $

#info-box[
  Propriedade da falta de memória: $P(X > n+m | X > n) = P(X > m)$. O passado não conta. Útil para filas de espera e avarias.
]

Cada barra é $P(X=k) = (1-p)^(k-1) p$. A probabilidade decresce exponencialmente — a maioria dos sucessos chega cedo.

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "Geométrica Geom(0.4) — P(X=k), decaimento exponencial",
    xlim: (0.5, 10.5),
    ylim: (0, 0.45),
    let p = 0.4,
    let xs = range(1, 11),
    let ys = xs.map(k => calc.pow(1 - p, k - 1) * p),
    lq.bar(xs, ys, width: 0.6, fill: rgb("#86efac")),
  )
]

== Hipergeométrica

Amostragem sem reposição de uma população finita com $N$ elementos, $M$ "sucessos" e $N-M$ "falhas":

$ X ~ "HGeom"(N, M, n) $

$ P(X = k) = binom(M, k) binom(N-M, n-k) / binom(N, n) $

$ E(X) = n M/N $

$ "Var"(X) = n M/N (1 - M/N) (N-n)/(N-1) $

#alert-box[
  Usa Hipergeométrica quando a amostra é grande relativamente à população (sem reposição). Se $n\/N < 0.05$, podes aproximar pela Binomial.
]

== Poisson

$ X ~ "Pois"(lambda) $

$ P(X = k) = (e^(-lambda) lambda^k) / k!, quad k = 0, 1, 2, ... $

$ E(X) = lambda $

$ "Var"(X) = lambda $

Cada cor representa um $lambda$ diferente. Quanto maior o $lambda$, mais a distribuição se desloca para a direita e achata. Para $lambda$ grande, a Poisson aproxima-se de uma Normal.

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "Poisson — comparação λ=1, 3, 6 (mais largo = maior λ)",
    xlim: (-0.5, 14.5),
    ylim: (0, 0.4),
    let xs = range(15),
    let fact = n => if n <= 1 { 1 } else {
      range(1, n + 1).fold(1, (a, b) => a * b)
    },
    let ys1 = xs.map(k => calc.exp(-1) * calc.pow(1, k) / fact(k)),
    let ys3 = xs.map(k => calc.exp(-3) * calc.pow(3, k) / fact(k)),
    let ys6 = xs.map(k => calc.exp(-6) * calc.pow(6, k) / fact(k)),
    lq.bar(xs.map(x => x - 0.25), ys1, width: 0.22, fill: rgb("#93c5fd"), label: "λ=1"),
    lq.bar(xs, ys3, width: 0.22, fill: rgb("#86efac"), label: "λ=3"),
    lq.bar(xs.map(x => x + 0.25), ys6, width: 0.22, fill: rgb("#fca5a5"), label: "λ=6"),
  )
]

== Normal

$ X ~ "N"(mu, sigma^2) $

Padronização:

$ Z = (X - mu)/sigma $

A curva é simétrica em torno de $mu$. A área total vale 1. Quando padronizas, $Z ~ "N"(0,1)$ e usas a tabela.

Para calcular probabilidades, padronizas e vais à tabela da Normal:

$ P(a < X < b) = Phi((b - mu)/sigma) - Phi((a - mu)/sigma) $

$Phi(z)$ é o valor da tabela da Normal para $z$. Lê bem a tabela — se for negativo, usa $Phi(-z) = 1 - Phi(z)$.

#alert-box[
  O valor que a tabela dá é sempre $P(Z <= z)$, i.e., a área à *esquerda*. Desenha sempre um esboço antes de calcular para não te enganares no sinal.
]

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "Normal N(0,1) — simétrica, área total = 1",
    xlim: (-4, 4),
    ylim: (0, 0.5),
    let x = lq.linspace(-4, 4),
    lq.plot(x, x => 1 / calc.sqrt(2 * 3.1416) * calc.exp(-(x * x) / 2)),
  )
]

== Uniforme Contínua

$ X ~ "U"(a, b) $

$ f(x) = 1/(b-a), quad x in [a,b] $

$ E(X) = (a+b)/2, quad "Var"(X) = (b-a)^2/12 $

$ P(X <= x) = (x-a)/(b-a) $

#info-box[
  Todos os valores no intervalo são igualmente prováveis. O gerador de números aleatórios do computador é $U(0,1)$.
]

A densidade é constante entre $a$ e $b$ — um rectângulo. Fora desse intervalo é zero.

#align(center)[
  #lq.diagram(
    width: 70%,
    title: "Uniforme U(2,5) — densidade constante 1/(b-a)",
    xlim: (0, 7),
    ylim: (0, 0.6),
    lq.rect(2, 0, width: 3, height: 0.333, fill: rgb("#bfdbfe88"), stroke: none),
    lq.line((0, 0), (2, 0), stroke: 2pt + rgb("#1e40af")),
    lq.line((2, 0), (2, 0.333), stroke: 2pt + rgb("#1e40af")),
    lq.line((2, 0.333), (5, 0.333), stroke: 2pt + rgb("#1e40af")),
    lq.line((5, 0.333), (5, 0), stroke: 2pt + rgb("#1e40af")),
    lq.line((5, 0), (7, 0), stroke: 2pt + rgb("#1e40af")),
    lq.place(3.5, 0.18, align: center + horizon)[1/(b−a)],
  )
]

== Exponencial

Modela o tempo até ao próximo evento (complementar da Poisson):

$ X ~ "Exp"(lambda) $

$ f(x) = lambda e^(-lambda x), quad x >= 0 $

$ E(X) = 1/lambda, quad "Var"(X) = 1/lambda^2 $

$ P(X > x) = e^(-lambda x) $

$ P(X <= x) = 1 - e^(-lambda x) $

#info-box[
  Também tem falta de memória: $P(X > s+t | X > s) = P(X > t)$. Mesma propriedade da Geométrica, mas contínua.
]

A curva decresce rapidamente. A área sombreada até $x=1$ é $P(X <= 1) = 1 - e^{-lambda}$.

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "Exponencial Exp(1) — PDF (curva) e P(X≤1) sombreado",
    xlim: (0, 5),
    ylim: (0, 1.1),
    let x = lq.linspace(0, 5),
    let xs1 = lq.linspace(0, 1),
    lq.fill-between(xs1, xs1.map(v => calc.exp(-v)), fill: rgb("#bfdbfe88"), stroke: none),
    lq.plot(x, x => calc.exp(-x)),
    lq.line((1, 0), (1, calc.exp(-1)), stroke: 1pt + rgb("#e11d48")),
  )
]

== t-Student, Chi-quadrado e F

Estas distribuições saem naturalmente quando trabalhas com amostras pequenas e variâncias desconhecidas.

$T = (overline(X) - mu)/(S\/sqrt(n)) ~ t_(n-1)$ — usas nos IC e testes com $sigma$ desconhecido.

$chi^2 = (n-1)S^2/sigma^2 ~ chi^2_(n-1)$ — usas para fazer inferência sobre variâncias.

$F = chi^2_(nu_1)\/nu_1 \/ (chi^2_(nu_2)\/nu_2) ~ F_(nu_1, nu_2)$ — usas para comparar duas variâncias.

#alert-box[
  Não confundas os graus de liberdade. t tem $n-1$. Chi-quadrado tem $n-1$. F tem dois: $(n_1-1)$ e $(n_2-1)$.
]

A t-Student tem caudas mais pesadas que a Normal — quanto menor o $n$, mais gorda a cauda. Com $n > 30$ já é quase Normal.

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "t-Student vs Normal — caudas mais pesadas para df pequeno",
    xlim: (-4, 4),
    ylim: (0, 0.5),
    let x = lq.linspace(-4, 4),
    let norm-pdf = x => 1 / calc.sqrt(2 * 3.1416) * calc.exp(-(x * x) / 2),
    let t1-pdf = x => 1 / (3.1416 * (1 + x * x)),
    let t5-pdf = x => {
      let c = 0.7797
      c * calc.pow(1 + x * x / 5, -3)
    },
    lq.plot(x, t1-pdf, stroke: 2pt + rgb("#e11d48"), label: "t(1)"),
    lq.plot(x, t5-pdf, stroke: 2pt + rgb("#f59e0b"), label: "t(5)"),
    lq.plot(x, norm-pdf, stroke: 2pt + rgb("#1e40af"), label: "N(0,1)"),
  )
]

A chi-quadrado é sempre não-negativa e assimétrica. Com df maior fica mais parecida com uma Normal.

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "Chi-quadrado — assimétrica, desloca-se com df",
    xlim: (0, 16),
    ylim: (0, 0.5),
    let x = lq.linspace(0.05, 16),
    lq.plot(x, x => 0.5 * calc.exp(-x / 2), stroke: 2pt + rgb("#e11d48"), label: "df=2"),
    lq.plot(x, x => x * calc.exp(-x / 2) / 4, stroke: 2pt + rgb("#f59e0b"), label: "df=4"),
    lq.plot(x, x => x * x * calc.exp(-x / 2) / 16, stroke: 2pt + rgb("#1e40af"), label: "df=6"),
  )
]

= Intervalos de Confiança

Estrutura mental: estimativa ± margem de erro.

Usa $z$ quando $sigma$ é *conhecida*. Usa $t$ quando $sigma$ é *desconhecida* (quase sempre):

$ overline(x) plus.minus z_(1-alpha/2) sigma/sqrt(n) quad "(σ conhecida)" $

$ overline(x) plus.minus t_(n-1,1-alpha/2) s/sqrt(n) quad "(σ desconhecida, tabela t com n-1 gl)" $

$ hat(p) plus.minus z_(1-alpha/2) sqrt(hat(p)(1-hat(p))/n) $

IC para variância ($sigma^2$ desconhecida, usa chi-quadrado):

$ ((n-1)s^2 / chi^2_(n-1, 1-alpha/2) , (n-1)s^2 / chi^2_(n-1, alpha/2)) $

#alert-box[
  O IC da variância não é simétrico — a chi-quadrado é assimétrica. Os dois limites usam quantis diferentes: $chi^2_(alpha/2)$ (esquerda) e $chi^2_(1-alpha/2)$ (direita). E os limites ficam *invertidos* porque divides por eles.
]

A região sombreada é o IC da variância. Os dois cortes verticais são $chi^2_(alpha/2)$ e $chi^2_(1-alpha/2)$. Os limites do IC ficam invertidos (divide-se por eles).

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "IC 95% para variância — chi-quadrado(5), quantis 0.025 e 0.975",
    xlim: (0, 16),
    ylim: (0, 0.2),
    let x = lq.linspace(0.05, 16),
    let pdf = x => x * calc.exp(-x / 2) / 4,
    let xm = lq.linspace(0.83, 12.83),
    lq.fill-between(xm, xm.map(pdf), fill: rgb("#bfdbfe88"), stroke: none),
    lq.plot(x, pdf),
    lq.line((0.83, 0), (0.83, pdf(0.83)), stroke: 1pt + rgb("#e11d48")),
    lq.line((12.83, 0), (12.83, pdf(12.83)), stroke: 1pt + rgb("#e11d48")),
  )
]

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "IC 95% — zona central (azul) entre z = ±1.96",
    xlim: (-4, 4),
    ylim: (0, 0.5),
    let x = lq.linspace(-4, 4),
    lq.plot(x, x => 1 / calc.sqrt(2 * 3.1416) * calc.exp(-(x * x) / 2)),
    lq.rect(-1.96, 0.45, width: 3.92, height: 100%, fill: rgb("#bfdbfe66")),
    lq.line((-1.96, 0), (-1.96, 0.42)),
    lq.line((1.96, 0), (1.96, 0.42)),
  )
]

#alert-box[
  "95% de confiança" não quer dizer que há 95% de probabilidade de o parâmetro estar no intervalo. O parâmetro ou está ou não está — não tem probabilidade. O que tem 95% é o *método*: se repetisses o estudo muitas vezes, 95% dos intervalos calculados conteriam o parâmetro real.
]

= Testes de Hipóteses

Primeiro defines as hipóteses. Sempre. Sem isto não há teste.

*Teste à média* ($sigma$ desconhecida):

$ H_0: mu = mu_0 quad "vs" quad H_1: cases(mu != mu_0 & "(bilateral)", mu > mu_0 & "(unilateral direita)", mu < mu_0 & "(unilateral esquerda)") $

$ t_0 = (overline(x)-mu_0)/(s/sqrt(n)) quad ~ quad t_(n-1) $

Regra de decisão: rejeitas $H_0$ se:
- bilateral: $|t_0| > t_(n-1, 1-alpha/2)$
- unilateral direita: $t_0 > t_(n-1, 1-alpha)$
- unilateral esquerda: $t_0 < -t_(n-1, 1-alpha)$

*Teste à proporção:*

$ H_0: p = p_0 $

$ z_0 = (hat(p)-p_0)/sqrt(p_0(1-p_0)/n) quad ~ quad "N"(0,1) $

Mesma regra mas com $z$ em vez de $t$.

Teste para a variância (usa chi-quadrado):

$ chi^2_0 = (n-1)s^2/sigma_0^2 $

Rejeitas $H_0 : sigma^2 = sigma_0^2$ se $chi^2_0$ cair fora da zona de aceitação da tabela chi-quadrado.

Se p-value $< alpha$ → rejeitas $H_0$.

#info-box[
  "Não rejeitar $H_0$" não é o mesmo que "provar $H_0$". Significa apenas que não há evidência suficiente nos dados para a rejeitar. É como um julgamento: absolvido não é inocente, é "não provado culpado".
]

As áreas a vermelho são a zona de rejeição. Se o teu $z_0$ cair ali, rejeitas. Se cair no meio, não rejeitas — mas não provaste nada.

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "Teste bilateral — as caudas (vermelho) são a zona de rejeição",
    xlim: (-4, 4),
    ylim: (0, 0.5),
    let x = lq.linspace(-4, 4),
    let xl = lq.linspace(-4, -1.96),
    let xr = lq.linspace(1.96, 4),
    lq.fill-between(
      xl,
      xl.map(v => 1 / calc.sqrt(2 * 3.1416) * calc.exp(-(v * v) / 2)),
      fill: rgb("#fca5a566"),
      stroke: none,
    ),
    lq.fill-between(
      xr,
      xr.map(v => 1 / calc.sqrt(2 * 3.1416) * calc.exp(-(v * v) / 2)),
      fill: rgb("#fca5a566"),
      stroke: none,
    ),
    lq.plot(x, x => 1 / calc.sqrt(2 * 3.1416) * calc.exp(-(x * x) / 2)),
    lq.line((-1.96, 0), (-1.96, 0.42), stroke: 1pt + rgb("#e11d48")),
    lq.line((1.96, 0), (1.96, 0.42), stroke: 1pt + rgb("#e11d48")),
  )
]

== Teste Chi-quadrado de Aderência

Verifica se os dados seguem uma distribuição teórica (ex.: será que isto é mesmo Poisson?):

$ chi^2_0 = sum_(i=1)^k (O_i - E_i)^2 / E_i $

Onde $O_i$ são as frequências observadas e $E_i = n p_i$ as esperadas. Graus de liberdade: $k - 1 - "parâmetros estimados"$.

Regra de decisão: rejeitas $H_0$ se $chi^2_0 > chi^2_(k-1, 1-alpha)$ (unilateral — cauda direita sempre).

#alert-box[
  Para usar este teste, cada $E_i >= 5$. Se não, agrupa classes. Se te esqueceres disto, o examinador não se esquece.
]

== Teste Chi-quadrado de Independência

Dois critérios (ex.: género vs. preferência) são independentes?

$ chi^2_0 = sum_i sum_j (O_(i j) - E_(i j))^2 / E_(i j), quad E_(i j) = "total linha" times "total coluna" / n $

Graus de liberdade: $(r-1)(c-1)$, onde $r$ = linhas e $c$ = colunas.

Regra de decisão: rejeitas $H_0$ se $chi^2_0 > chi^2_((r-1)(c-1), 1-alpha)$.

== Teste F para Igualdade de Variâncias

$ H_0 : sigma_1^2 = sigma_2^2 $

$ F_0 = s_1^2 / s_2^2 $

Põe sempre a maior variância no numerador (garante $F_0 > 1$). Rejeitas $H_0$ se $F_0 > F_(alpha/2, n_1-1, n_2-1)$ — como puseste a maior em cima, só precisas da cauda direita.

#info-box[
  Deves fazer este teste antes do teste t de duas amostras para decidir se usas variâncias iguais ou diferentes.
]

= Duas Amostras

Variâncias iguais (graus de liberdade: $n_1 + n_2 - 2$):

$ s_p^2 = ((n_1-1)s_1^2 + (n_2-1)s_2^2)/(n_1+n_2-2) $

$ t_0 = (overline(x)_1 - overline(x)_2)/(s_p sqrt(1/n_1+1/n_2)) $

IC para a diferença de médias (variâncias iguais):

$ (overline(x)_1 - overline(x)_2) plus.minus t_(n_1+n_2-2, 1-alpha/2) s_p sqrt(1/n_1+1/n_2) $

Variâncias diferentes — graus de liberdade de Welch-Satterthwaite (não é $n_1+n_2-2$):

$ t_0 = (overline(x)_1 - overline(x)_2)/sqrt(s_1^2/n_1 + s_2^2/n_2) $

$ nu = (s_1^2/n_1 + s_2^2/n_2)^2 / ((s_1^2/n_1)^2/(n_1-1) + (s_2^2/n_2)^2/(n_2-1)) $

IC para a diferença de médias (variâncias diferentes):

$ (overline(x)_1 - overline(x)_2) plus.minus t_(nu, 1-alpha/2) sqrt(s_1^2/n_1 + s_2^2/n_2) $

Regra de decisão para qualquer dos dois: rejeitas $H_0: mu_1 = mu_2$ se $|t_0| > t_("gl", 1-alpha/2)$.

As duas curvas representam as distribuições das duas populações. Se estiverem muito sobrepostas, não há evidência de diferença entre as médias.

#align(center)[
  #lq.diagram(
    width: 75%,
    title: "Duas médias — sobreposição indica diferença pouco significativa",
    xlim: (-6, 8),
    ylim: (0, 0.5),
    let x = lq.linspace(-6, 8),
    lq.plot(x, x => (
      1 / calc.sqrt(2 * 3.1416) * calc.exp(-((x + 1) * (x + 1)) / 2)
    )),
    lq.plot(x, x => 1 / calc.sqrt(2 * 3.1416) * calc.exp(-((x - 3) * (x - 3)) / 2)),
  )
]

== Amostras Emparelhadas

Quando cada observação da amostra 1 tem um par natural na amostra 2 (ex.: antes/depois, esquerda/direita):

$ H_0: mu_d = 0 quad "vs" quad H_1: mu_d != 0 $

$ d_i = x_(1i) - x_(2i) $

$ t_0 = overline(d) / (s_d / sqrt(n)) quad ~ quad t_(n-1) $

Rejeitas $H_0$ se $|t_0| > t_(n-1, 1-alpha/2)$.

#alert-box[
  Não confundas com duas amostras independentes. Se os dados têm pares naturais, és obrigado a usar o teste emparelhado. Se usares o outro, estás a desperdiçar informação e a fazer o examinador chorar.
]

= Correlação e Regressão Linear

A regressão serve para modelar a relação entre duas variáveis e fazer previsões. A correlação mede se essa relação existe e quão forte é.

== Coeficiente de Correlação de Pearson

Mede a força e direcção da relação linear entre $x$ e $y$:

$ r = S_(x y) / sqrt(S_(x x) dot S_(y y)) $

$r in [-1, 1]$. Perto de $±1$ → correlação forte. Perto de 0 → relação linear fraca (pode existir relação não linear).

#info-box[
  Em exame, quando te pedem para *interpretar* $r$: "Existe uma correlação linear forte/fraca, positiva/negativa entre $x$ e $y$." Para $|r| > 0.8$ diz forte, para $|r| < 0.5$ diz fraca. Entre os dois, moderada.
]

Os três exemplos mostram o que $r$ significa visualmente. Quanto mais alinhados, mais perto de $±1$.

#grid(columns: (1fr, 1fr, 1fr), gutter: 4pt,
  [#lq.diagram(
    width: 100%,
    height: 4cm,
    title: "r ≈ +1",
    xlim: (0, 6), ylim: (0, 6),
    lq.scatter((1,2,3,4,5), (1.1, 2.0, 3.1, 3.9, 5.0)),
    lq.plot(lq.linspace(0,6), x => x),
  )],
  [#lq.diagram(
    width: 100%,
    height: 4cm,
    title: "r ≈ 0",
    xlim: (0, 6), ylim: (0, 6),
    lq.scatter((1,2,3,4,5), (3.5, 1.8, 4.2, 2.1, 3.9)),
    lq.line((0, 3), (6, 3), stroke: 1pt + gray),
  )],
  [#lq.diagram(
    width: 100%,
    height: 4cm,
    title: "r ≈ −1",
    xlim: (0, 6), ylim: (0, 6),
    lq.scatter((1,2,3,4,5), (4.9, 4.0, 3.1, 2.0, 0.9)),
    lq.plot(lq.linspace(0,6), x => 6 - x),
  )],
)

== Regressão Linear ($hat(y) = a + b x$)

Calculas $hat(y) = a + b x$ — desmontas a coisa nesta ordem: $overline(x)$, $overline(y)$, $S_(x x)$, $S_(x y)$, $b$, $a$. Sempre por esta sequência.

Médias (precisas delas para tudo o resto):

$ overline(x) = (sum x_i)/n, quad overline(y) = (sum y_i)/n $

$ a = overline(y) - b overline(x) $

$ b = S_(x y)/S_(x x) $

$ S_(x x) = sum x_i^2 - n overline(x)^2 $

$ S_(x y) = sum x_i y_i - n overline(x) overline(y) $

$ S_(y y) = sum y_i^2 - n overline(y)^2 $

#info-box[
  $S_(y y)$ parece inútil na regressão, mas precisas dela para calcular $r = S_(x y)/sqrt(S_(x x) dot S_(y y))$. Não te esqueças.
]

Declive $b$ é quanto $y$ sobe quando $x$ aumenta 1 unidade.

Ordenada $a$ é o valor quando $x=0$. Se $x=0$ não fizer sentido, não inventes física alternativa.

#info-box[
  Em exame, quando te pedem para *interpretar* $b$: "Por cada aumento de 1 unidade em $x$, $y$ aumenta/diminui em média $b$ unidades." Não escrevas só o número — o examinador quer a frase completa com as unidades certas.
]

== Decomposição

$ "SQT" = "SQR" + "SQE" $

$ R^2 = "SQR"/"SQT" $

Como calcular cada um:

$ "SQT" = S_(y y) = sum y_i^2 - n overline(y)^2 $

$ "SQR" = b^2 S_(x x) = b dot S_(x y) $

$ "SQE" = "SQT" - "SQR" $

#info-box[
  Na prática: calculas $S_(y y)$, calculas $b dot S_(x y)$, subtrais. Nunca calculas o $"SQE"$ directamente — é sempre resíduo.
]

A barra azul é o SQT total. A parte azul-clara é o SQR (variação explicada pelo modelo) e a parte vermelha é o SQE (variação residual). $R^2$ = proporção azul-clara sobre o total.

#align(center)[
  #lq.diagram(
    width: 70%,
    height: 3cm,
    title: "SQT = SQR (explicado) + SQE (resíduo)",
    xlim: (0, 10),
    ylim: (0, 4),
    lq.rect(1, 1, width: 8, height: 1.5, fill: rgb("#cbd5e1"), stroke: 0.5pt + gray),
    lq.rect(1, 1, width: 5, height: 1.5, fill: rgb("#93c5fd"), stroke: none),
    lq.rect(6, 1, width: 3, height: 1.5, fill: rgb("#fca5a5"), stroke: none),
    lq.place(3.5, 1.75, align: center + horizon)[SQR],
    lq.place(7.5, 1.75, align: center + horizon)[SQE],
    lq.place(5, 3.2, align: center + horizon)[SQT],
    lq.line((1, 2.7), (9, 2.7), stroke: 0.5pt + rgb("#64748b")),
    lq.line((1, 2.5), (1, 2.9), stroke: 0.5pt + rgb("#64748b")),
    lq.line((9, 2.5), (9, 2.9), stroke: 0.5pt + rgb("#64748b")),
  )
]

== Erro Padrão

$ s_(y.x) = sqrt("SQE"/(n-2)) $

$ s_b = s_(y.x)/sqrt(S_(x x)) $

== Intervalo Média vs Previsão

$
  hat(y)_0 plus.minus t_(n-2, 1-alpha/2) s_(y.x) sqrt(1/n + (x_0-overline(x))^2/S_(x x))
$

$
  hat(y)_0 plus.minus t_(n-2, 1-alpha/2) s_(y.x) sqrt(1 + 1/n + (x_0-overline(x))^2/S_(x x))
$

#alert-box[
  O segundo IC (previsão individual) é sempre maior do que o IC da média porque inclui a variabilidade do ponto individual, não só a incerteza sobre a média. Se o enunciado pede "previsão para um novo valor", usa o segundo. Se pede "valor médio esperado para $x_0$", usa o primeiro.
]

== Teste ao Declive

$ H_0 : beta_1 = 0 $

$ t_0 = b/s_b $

Se rejeitares $H_0$, há evidência de relação linear.

Se não rejeitares, não provaste nada.

Regra de decisão: rejeitas $H_0$ se $|t_0| > t_(n-2, 1-alpha/2)$.

Os pontos são dados reais; a linha é a reta ajustada $hat(y) = -0.53 + 0.26x$. Quanto mais próximos os pontos da reta, maior o $R^2$.

#align(center)[
  #lq.diagram(
    width: 80%,
    title: "Regressão Linear — pontos (dados) e reta ajustada",
    xlim: (0, 60),
    ylim: (0, 14),
    lq.scatter(
      (14, 15, 23, 31, 38, 40, 51, 53),
      (2.0, 4.6, 5.7, 7.3, 9.8, 10.9, 13.2, 12.6),
    ),
    let x = lq.linspace(0, 60),
    lq.plot(x, x => -0.53 + 0.26 * x),
  )
]

Cada ponto é $hat(y)_i$ no eixo $x$ contra o resíduo $e_i = y_i - hat(y)_i$ no eixo $y$. Ideal: pontos dispersos aleatoriamente em torno da linha vermelha (zero). Padrão em arco = modelo errado.

#align(center)[
  #lq.diagram(
    width: 80%,
    title: "Resíduos — sem padrão é bom sinal (linha vermelha = zero)",
    xlim: (0, 14),
    ylim: (-2.5, 2.5),
    let xhats = (14, 15, 23, 31, 38, 40, 51, 53).map(xi => -0.53 + 0.26 * xi),
    let yobs  = (2.0, 4.6, 5.7, 7.3, 9.8, 10.9, 13.2, 12.6),
    let resid = range(8).map(i => yobs.at(i) - xhats.at(i)),
    lq.scatter(xhats, resid),
    lq.line((0, 0), (14, 0), stroke: 1pt + rgb("#e11d48")),
  )
]

#info-box[
  Resíduos devem ter padrão aleatório em torno de zero. Se tiverem curva, o modelo linear não chega.
]

Agora tens tudo. Usa o índice para navegar.
