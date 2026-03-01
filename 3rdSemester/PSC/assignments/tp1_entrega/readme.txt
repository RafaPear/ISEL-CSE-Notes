Descompactar o pacote com os programas de teste
numa diretoria relativa à realização da série de exercicios.
Exemplo de estrutura de diretorias:

home
└── aluno
    └── psc
        └── 2526inv
              ├── serie
              ├── tp1
              └── tp2

$ cd ~/psc/2526inv/tp1
$ unzip ~/Downloads/tp1_entrega.zip

Depois da descompactação deve verificar o seguinte conteúdo:

$ tree serie2_entrega

serie2_entrega
├── exercicio1
│   ├── build.sh
|   ├── reverse.c
│   ├── reverse_asm.s     <-- a realizar pelo aluno
│   └── reverse_test.c
├── exercicio2
│   ├── build.sh
│   ├── my_memmove_asm.s     <-- a realizar pelo aluno
│   └── my_memmove_test.c
├── exercicio3
│   ├── build.sh
│   ├── get_student_grade_asm.s     <-- a realizar pelo aluno
│   ├── get_student_grade.c
│   └── get_student_grade_test.c
├── exercicio4
│   ├── build.sh
│   ├── copy_if_asm.s     <-- a realizar pelo aluno
│   ├── copy_if.c
│   ├── copy_if_test.c
│   └── less_than.s
├── invoke
│   └── invoke.s
├── readme.txt
└── test_all.sh


Os nomes dos ficheiros com o programa em assembly têm a forma xxx_asm.s,
em que xxx corresponde ao nome da função que contêm.
Os nomes dos ficheiros com o programa de teste têm a forma xxx_test.c,
em que xxx corresponde ao nome da função em teste.

A entrega deve ser realizada no Moodle através da submissão de um pacote zip
gerado da seguinte forma:

$ cd ~/psc/2526inv/tp1
$ zip -r tp1_entrega.zip tp1_entrega
