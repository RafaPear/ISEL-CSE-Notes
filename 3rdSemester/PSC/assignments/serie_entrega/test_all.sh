#!/bin/sh

echo
echo "Testes do exercício 1"

cd exercicio1
./build.sh
./count_ones_test

echo
echo "Testes do exercício 2"

cd ../exercicio2
./build.sh
./invert_bits_test

echo
echo "Testes do exercício 3"

cd ../exercicio3
./build.sh
./round_to_int_test

echo
echo "Testes do exercício 4"

cd ../exercicio4
./build.sh
./string_detab_test

echo
echo "Testes do exercício 5"

cd ../exercicio5
./build.sh
./sumtimes_test

echo
echo "Testes do exercício 6"

cd ../exercicio6
./build.sh
./test.sh

cd ..

read -p "Press [Enter] key to continue..."