#set page(
  paper: "a4",
  margin: (x: 2.5cm, y: 2cm),
)
#set text(
  font: "New Computer Modern",
  size: 11pt,
)
#set par(justify: true)

// Cabeçalho com logo e informações
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
    #text(size: 12pt)[Departamento de Matemática]
    #v(0.3em)
    #text(
      size: 11pt,
      weight: "bold",
    )[D.E.E.T.C. — L.E.I.C. — Probabilidades e Estatística]
  ],
)

#v(1em)
#line(length: 100%, stroke: 1pt)
#v(0.5em)

#align(center)[
  #text(size: 16pt, weight: "bold")[2º Teste — 1º Semestre 2022/2023]
  #v(0.3em)
  *Duração:* 1 hora e 30 minutos | *Data:* 06 de Janeiro de 2023
  #v(0.2em)
  *SOLUÇÕES DETALHADAS*
]

#v(0.5em)
#line(length: 100%, stroke: 1pt)
#v(1.5em)

= Grupo I — Regressão Linear Simples

*1.a) Estimativas de Mínimos Quadrados (2,0 valores)*

Consideramos o modelo de regressão linear simples:
$ Y = alpha + beta X + epsilon $

onde $Y$ representa o tempo de CPU e $X$ o número de acessos ao disco.

*Dados fornecidos:* $n = 8$
- $sum_(i=1)^8 x_i = 265$, #h(1em) $sum_(i=1)^8 x_i^2 = 10365$
- $sum_(i=1)^8 y_i = 66.1$, #h(1em) $sum_(i=1)^8 y_i^2 = 658.79$
- $sum_(i=1)^8 x_i y_i = 2603.8$

*Cálculo das médias amostrais:*
$ overline(x) = sum_(i=1)^8 x_i / n = 265/8 = 33.125 $

$ overline(y) = sum_(i=1)^8 y_i / n = 66.1/8 = 8.2625 $

*Cálculo de* $S_(x x)$ *e* $S_(x y)$:
$
  S_(x x) = sum_(i=1)^8 x_i^2 - n overline(x)^2 = 10365 - 8 times (33.125)^2 = 10365 - 8781.125 = 1583.875
$

$
  S_(x y) = sum_(i=1)^8 x_i y_i - n overline(x) overline(y) = 2603.8 - 8 times 33.125 times 8.2625 = 2603.8 - 2189.25 = 414.55
$

*Estimativas de mínimos quadrados:*

Declive:
$ hat(beta) = S_(x y) / S_(x x) = 414.55 / 1583.875 = 0.2617 approx 0.262 $

Ordenada na origem:
$
  hat(alpha) = overline(y) - hat(beta) overline(x) = 8.2625 - 0.262 times 33.125 = 8.2625 - 8.6788 = -0.416 approx -0.42
$

*Equação da reta de regressão ajustada:*
$ hat(Y) = -0.42 + 0.262 X $

*Interpretação dos coeficientes:*
- O coeficiente $hat(beta) = 0.262$ indica que, em média, por cada acesso adicional ao disco, o tempo de CPU aumenta aproximadamente 0.262 unidades.
- O coeficiente $hat(alpha) = -0.42$ representa o valor estimado do tempo de CPU quando não há acessos ao disco. Este valor deve ser interpretado com cautela, pois corresponde a uma extrapolação fora do intervalo observado.


#v(1em)

*1.b) Qualidade do Ajustamento — Coeficiente de Determinação (1,0 valores)*

Para avaliar a qualidade da reta ajustada, calculamos o coeficiente de determinação $R^2$.

*Cálculo de* $S_(y y)$:
$
  S_(y y) = sum_(i=1)^8 y_i^2 - n overline(y)^2 = 658.79 - 8 times (8.2625)^2 = 658.79 - 545.641 = 113.149
$

*Coeficiente de determinação:*
$
  R^2 = S_(x y)^2 / (S_(x x) times S_(y y)) = (414.55)^2 / (1583.875 times 113.149) = 171851.70 / 179247.87 = 0.9587 approx 0.96
$

*Interpretação:*

O valor $R^2 = 0.96$ indica que aproximadamente *96% da variabilidade total* do tempo de CPU é explicada pela variação linear no número de acessos ao disco.

Este valor muito próximo de 1 sugere um *excelente ajustamento* do modelo linear aos dados, confirmando uma forte relação linear positiva entre as duas variáveis. A reta de regressão ajustada é adequada para modelar a relação entre o número de acessos ao disco e o tempo de CPU.


#pagebreak()

*1.c) Estimativas Pontuais e Fiabilidade (1,5 valores)*

Utilizando a equação da reta ajustada $hat(Y) = -0.42 + 0.262 X$, calculamos as estimativas para os três valores solicitados:

*Para* $X = 8$ *acessos:*
$ hat(Y) = -0.42 + 0.262 times 8 = -0.42 + 2.096 = 1.676 "unidades de tempo" $

*Para* $X = 20$ *acessos:*
$ hat(Y) = -0.42 + 0.262 times 20 = -0.42 + 5.24 = 4.82 "unidades de tempo" $

*Para* $X = 40$ *acessos:*
$
  hat(Y) = -0.42 + 0.262 times 40 = -0.42 + 10.48 = 10.06 "unidades de tempo"
$

*Análise da fiabilidade das estimativas:*

Observando os dados originais, o número de acessos ao disco varia entre $x_min = 14$ e $x_max = 53$.

- *$X = 8$:* Este valor está *fora* do intervalo observado ($8 < 14$). Trata-se de *extrapolação*, pelo que a estimativa obtida deve ser interpretada com grande cautela e pode não ser fiável. Não há garantia de que a relação linear se mantém fora do intervalo dos dados observados.

- *$X = 20$:* Este valor encontra-se *dentro* do intervalo observado ($14 <= 20 <= 53$). Trata-se de *interpolação*, logo a estimativa é *fiável* e confiável.

- *$X = 40$:* Este valor encontra-se *dentro* do intervalo observado ($14 <= 40 <= 53$). Trata-se de *interpolação*, logo a estimativa é *fiável* e confiável.

*Estimativa dos erros:*

Para determinar as estimativas dos erros cometidos, precisamos calcular o erro padrão residual $s_e$:

$
  s_e = sqrt((S_(y y) - hat(beta) S_(x y))/(n-2)) = sqrt((113.149 - 0.262 times 414.55)/6)
$

$ s_e = sqrt((113.149 - 108.612)/6) = sqrt(4.537/6) = sqrt(0.756) = 0.870 $

O erro padrão da previsão para um valor específico $X_0$ é dado por:

$ s_(hat(Y)_0) = s_e sqrt(1/n + (X_0 - overline(x))^2/S_(x x)) $

Calculando para cada valor:

- Para $X = 8$: $s_(hat(Y)_8) = 0.870 sqrt(1/8 + (8-33.125)^2/1583.875) = 0.870 sqrt(0.125 + 0.398) = 0.629$

- Para $X = 20$: $s_(hat(Y)_(20)) = 0.870 sqrt(1/8 + (20-33.125)^2/1583.875) = 0.870 sqrt(0.125 + 0.109) = 0.420$

- Para $X = 40$: $s_(hat(Y)_(40)) = 0.870 sqrt(1/8 + (40-33.125)^2/1583.875) = 0.870 sqrt(0.125 + 0.030) = 0.342$


#v(1em)

*1.d) Teste de Normalidade dos Resíduos (1,5 valores)*

*Objetivo:* Verificar se os resíduos seguem uma distribuição normal, um dos pressupostos fundamentais da regressão linear.

*Hipóteses do teste:*
$ H_0: "Os resíduos seguem uma distribuição normal" $
$ H_1: "Os resíduos não seguem uma distribuição normal" $

*Teste estatístico aplicado:* Teste de Shapiro-Wilk

Este teste é adequado para amostras pequenas ($n <= 50$) e é um dos testes mais potentes para verificar a normalidade.

*Nível de significância:* $alpha = 0.05$ (5%)

*Resultado obtido:* $p"-value" = 0.8129$

*Decisão:*

Como $p"-value" = 0.8129 > 0.05 = alpha$, *não se rejeita* a hipótese nula $H_0$ ao nível de significância de 5%.

*Conclusão:*

Não existe evidência estatística suficiente para rejeitar a hipótese de normalidade dos resíduos. O valor elevado do p-value (81.29%) sugere uma boa adequação à distribuição normal. Podemos considerar que os resíduos seguem aproximadamente uma distribuição normal, o que valida um dos pressupostos fundamentais do modelo de regressão linear e confere maior credibilidade às inferências realizadas.


#pagebreak()

*1.e) Teste de Significância do Modelo de Regressão (2,0 valores)*

*Objetivo:* Testar se existe uma relação linear significativa entre o número de acessos ao disco e o tempo de CPU.

*Hipóteses do teste:*
$ H_0: beta = 0 quad "(não existe relação linear significativa)" $
$ H_1: beta != 0 quad "(existe relação linear significativa)" $

A hipótese nula afirma que o coeficiente de regressão é zero, ou seja, que o número de acessos ao disco não tem efeito sobre o tempo de CPU. A hipótese alternativa afirma que existe um efeito significativo.

*Nível de significância:* $alpha = 0.05$ (5%)

*Resultado fornecido:* $p"-value" = 0.00002$

*Decisão:*

Como $p"-value" = 0.00002 < 0.05 = alpha$, *rejeita-se fortemente* a hipótese nula $H_0$ ao nível de significância de 5%.

O p-value extremamente baixo (0.002%) indica que a probabilidade de observar os dados obtidos, assumindo que não existe relação linear entre as variáveis, é praticamente nula.

*Conclusão:*

Existe evidência estatística *muito forte* de que o coeficiente de regressão $beta$ é significativamente diferente de zero. Isto significa que o número de acessos ao disco tem um efeito estatisticamente significativo sobre o tempo de CPU.

O modelo de regressão linear é *altamente significativo* e adequado para descrever a relação entre estas variáveis. A relação linear identificada não pode ser atribuída ao acaso, mas representa uma associação real e significativa entre o número de acessos ao disco e o tempo de processamento de CPU.


#v(2em)
#line(length: 100%, stroke: 1pt)
#v(1em)

= Grupo II — Comparação de Médias

*2. Teste de Comparação de Duas Médias Populacionais (3,0 valores)*

*Contexto:* Pretende-se comparar o número médio de acessos semanais entre dois sites da internet para determinar se existe uma diferença significativa na sua popularidade.

*Variáveis em estudo:*
- $X_1$: número de acessos semanais ao site 1
- $X_2$: número de acessos semanais ao site 2

*Dados fornecidos:*
#table(
  columns: (auto, auto, auto),
  align: center,
  stroke: 0.5pt,
  [*Estatística*], [*Site 1*], [*Site 2*],
  [$n$], [$41$], [$41$],
  [$overline(x)$], [$2952.8$], [$3002.4$],
  [$s^2$], [$3307.53$], [$3100.20$],
)

*Formulação das hipóteses:*
$ H_0: mu_1 = mu_2 quad "(o número médio de acessos é igual nos dois sites)" $
$
  H_1: mu_1 != mu_2 quad "(o número médio de acessos é diferente nos dois sites)"
$

Trata-se de um teste bilateral.

*Nível de significância:* $alpha = 0.05$ (5%)

*Escolha do teste estatístico:*

Como ambas as amostras são grandes ($n_1 = n_2 = 41 > 30$), podemos aplicar o Teorema do Limite Central. As variâncias populacionais são desconhecidas, mas dispomos das variâncias amostrais. Utilizamos o teste Z para comparação de duas médias com amostras independentes.

*Estatística de teste:*
$ Z = (overline(x)_1 - overline(x)_2) / sqrt(s_1^2/n_1 + s_2^2/n_2) $

*Cálculos:*

Variância do estimador da diferença:
$ s_1^2/n_1 = 3307.53/41 = 80.672 $
$ s_2^2/n_2 = 3100.20/41 = 75.615 $
$ s_1^2/n_1 + s_2^2/n_2 = 80.672 + 75.615 = 156.287 $

Desvio-padrão da diferença:
$ sqrt(156.287) = 12.501 $

Valor da estatística de teste:
$ Z = (2952.8 - 3002.4) / 12.501 = (-49.6) / 12.501 = -3.968 $

*Valor crítico:*

Para um teste bilateral com $alpha = 0.05$:
$ z_(alpha/2) = z_(0.025) = 1.96 $

Região crítica: $|Z| > 1.96$

*Decisão:*

Como $|Z| = |-3.968| = 3.968 > 1.96$, *rejeita-se* a hipótese nula $H_0$ ao nível de significância de 5%.

*Conclusão:*

Existe evidência estatística significativa de que o número médio de acessos semanais é *diferente* entre os dois sites. Os dados sugerem que o site 2 recebe, em média, significativamente mais acessos semanais do que o site 1 (diferença observada de aproximadamente 49.6 acessos, com o site 2 a apresentar mais acessos).

Esta diferença não pode ser atribuída à variabilidade amostral, mas representa uma diferença real na popularidade dos dois sites.


#pagebreak()

= Grupo III — Inferência sobre Parâmetros de uma População Normal

*3. Análise do Tempo de Processamento de uma Tarefa*

*Contexto:* A variável aleatória $X$ representa o tempo de processamento (em minutos) de uma tarefa por certo tipo de máquina. Admite-se que $X$ segue uma distribuição normal: $X tilde N(mu, sigma^2)$.

*Dados da amostra:* $n = 10$ observações
$ sum_(i=1)^10 x_i = 29.8, quad sum_(i=1)^10 x_i^2 = 89.94 $


#v(1em)

*3.a) Estimativas Pontuais para a Média e Desvio Padrão (1,0 valores)*

*Estimativa da média populacional:*

A estimativa não enviesada da média é a média amostral:
$ hat(mu) = overline(x) = (sum_(i=1)^10 x_i) / n = 29.8/10 = 2.98 "minutos" $

*Estimativa da variância e desvio padrão:*

A estimativa não enviesada da variância populacional é:
$
  s^2 = (sum_(i=1)^10 x_i^2 - n overline(x)^2) / (n-1) = (89.94 - 10 times (2.98)^2) / 9
$

Calculando:
$
  s^2 = (89.94 - 10 times 8.8804) / 9 = (89.94 - 88.804) / 9 = 1.136/9 = 0.1262
$

O desvio padrão amostral é:
$ s = sqrt(s^2) = sqrt(0.1262) = 0.355 "minutos" $

*Resposta:*
- Estimativa do tempo médio de processamento: $hat(mu) = 2.98$ minutos
- Estimativa do desvio padrão: $s = 0.355$ minutos


#v(1em)

*3.b) Intervalo de Confiança a 99% para o Desvio Padrão (3,0 valores)*

*Objetivo:* Construir um intervalo de confiança a 99% para o desvio padrão populacional $sigma$.

*Distribuição de referência:*

Como $X tilde N(mu, sigma^2)$, a estatística:
$ (n-1) s^2 / sigma^2 tilde chi^2_(n-1) = chi^2_9 $

segue uma distribuição qui-quadrado com $n-1 = 9$ graus de liberdade.

*Nível de confiança:* $1 - alpha = 0.99 arrow.r.double alpha = 0.01$

*Quantis da distribuição qui-quadrado:*

Consultando a tabela da distribuição $chi^2_9$:
$ chi^2_(9; 1-alpha/2) = chi^2_(9; 0.995) = 1.735 $
$ chi^2_(9; alpha/2) = chi^2_(9; 0.005) = 23.589 $

*Intervalo de confiança para* $sigma^2$:

O intervalo de confiança a 99% para a variância é:
$ [(n-1)s^2 / chi^2_(9; 0.005), (n-1)s^2 / chi^2_(9; 0.995)] $

Substituindo os valores:
$
  [(9 times 0.1262) / 23.589, (9 times 0.1262) / 1.735] = [1.1358 / 23.589, 1.1358 / 1.735]
$

$ [0.0481, 0.6547] $

*Intervalo de confiança para* $sigma$ *(a 99%):*

Extraindo a raiz quadrada dos extremos:
$ [sqrt(0.0481), sqrt(0.6547)] = [0.219, 0.809] "minutos" $

*Resposta e interpretação:*

Com 99% de confiança, o desvio padrão do tempo de processamento da tarefa encontra-se entre *0.219 e 0.809 minutos*.

Isto significa que, se repetíssemos o processo de amostragem e construção do intervalo muitas vezes, em aproximadamente 99% dos casos o verdadeiro valor de $sigma$ estaria contido no intervalo calculado.


#pagebreak()

*3.c) Teste de Hipóteses para a Média Populacional (3,0 valores)*

*Objetivo:* Testar se o tempo médio de processamento é no máximo 2.7 minutos, ao nível de significância de 10%.

*Formulação das hipóteses:*
$ H_0: mu <= 2.7 "minutos" quad "(tempo médio é no máximo 2.7 minutos)" $
$ H_1: mu > 2.7 "minutos" quad "(tempo médio é superior a 2.7 minutos)" $

Trata-se de um teste unilateral à direita.

*Nível de significância:* $alpha = 0.10$ (10%)

*Escolha da distribuição:*

Como a variância populacional $sigma^2$ é desconhecida e a amostra é pequena ($n = 10$), utilizamos a distribuição $t$ de Student. A estatística de teste é:

$ T = (overline(X) - mu_0) / (s / sqrt(n)) tilde t_(n-1) = t_9 $

onde $mu_0 = 2.7$ é o valor especificado na hipótese nula.

*Cálculo da estatística de teste:*

Erro padrão da média:
$ s / sqrt(n) = 0.355 / sqrt(10) = 0.355 / 3.162 = 0.1123 $

Valor observado da estatística:
$
  T = (overline(x) - mu_0) / (s / sqrt(n)) = (2.98 - 2.7) / 0.1123 = 0.28 / 0.1123 = 2.493
$

*Valor crítico:*

Para um teste unilateral à direita com $alpha = 0.10$ e $9$ graus de liberdade:
$ t_(9; 0.90) = 1.383 $

Região crítica: $T > 1.383$

*Decisão:*

Como $T = 2.493 > 1.383$, o valor observado da estatística de teste cai na região crítica. Portanto, *rejeita-se* a hipótese nula $H_0$ ao nível de significância de 10%.

*Conclusão:*

Existe evidência estatística significativa, ao nível de 10%, de que o tempo médio de processamento é *superior a 2.7 minutos*.

Os dados sugerem que o tempo médio de processamento da tarefa excede o valor máximo especificado de 2.7 minutos. A máquina parece necessitar de mais tempo do que o limite estabelecido para concluir o processamento da tarefa.


#v(2em)
#align(center)[
  #line(length: 60%, stroke: 1pt)
  #v(0.5em)
  *Fim das Soluções*
  #v(0.5em)
  #line(length: 60%, stroke: 1pt)
]
