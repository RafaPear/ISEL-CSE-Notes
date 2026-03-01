Descompactar o pacote com os programas de teste
numa diretoria relativa à realização da série de exercicios.
Recomenda-se a seguinte estrutura de diretórios:

home
└── aluno
    └── psc
        └── 2526inv
              ├── serie
              ├── tp1
              └── tp2

$ cd ~/psc/2526inv/serie
$ unzip ~/Downloads/serie_entrega.zip

Depois da descompactação deve verificar o seguinte conteúdo:

$ tree serie_entrega

serie_entrega
├── exercicio1
│   ├── build.sh
│   ├── count_ones.c	*
│   └── count_ones_test.c
├── exercicio2
│   ├── build.sh
│   ├── invert_bits.c	*
│   └── invert_bits_test.c
├── exercicio3
│   ├── build.sh
│   ├── round_to_int.c *
│   └── round_to_int_test.c
├── exercicio4
│   ├── build.sh
│   ├── string_detab.c *
│   └── string_detab_test.c
├── exercicio5
│   ├── build.sh
│   ├── sumtimes.c *
│   └── sumtimes_test.c
├── exercicio6
│   ├── build.sh
│   ├── test.sh
│   ├── smallfile.txt
│   ├── mediumfile.txt
│   ├── bigfile.txt
│   ├── mysort_ref
│   └── mysort.c *
├── readme.txt
└── test_all.sh

Os ficheiros assinalados com * estão originalmente vazios.
O aluno deve substitui-los ou preenche-los com a sua proposta de resolução.

O nome do ficheiro é igual ao nome da função que contém.
Por exemplo count_ones.c para o caso do exercício1.

Os nomes dos ficheiros com o programa de teste têm a forma `xxx_test.c`,
em que ``xxx`` corresponde ao nome da função a testar.

Após a descompactação, os ficheiros *script* (extensão `.sh`)
e o executável de referência no exercício6 (`mysort_ref`)
podem não possuir o atributo de executável.
Para tornar um ficheiro executável deve fazer-se:

$ cd serie_entrega
$ chmod +x exercicio1/build.sh

$ chmod +x exercicio6/mysort_ref

Pode testar individualmente cada programa,
invocando o executável produzido pelos *script* `build.sh`.

$ cd serie_entrega/exercicio1
$ ./build.sh
$ ./count_ones_test

No final pode confirmar todos os testes invocando o scrip de teste global:

$ cd ~/psc/2526inv/serie/serie_entrega
$ ./test_all.sh

A entrega deve ser realizada no Moodle através da submissão do pacote **zip**
gerado da seguinte forma:

$ cd ~/psc/2526inv/serie
$ zip -r serie_entrega.zip serie_entrega

