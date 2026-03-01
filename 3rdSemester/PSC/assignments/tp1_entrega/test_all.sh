echo ".............Teste do exercício 1"
cd exercicio1
./build.sh
./reverse_test
rm -f *.o *.out *.exe a.out *_test *.s~ *.c~ *.obj *.asm.out

echo ".............Teste do exercício 2"
cd ../exercicio2
./build.sh
./my_memmove_test
rm -f *.o *.out *.exe a.out *_test *.s~ *.c~ *.obj *.asm.out

echo ".............Teste do exercício 3"
cd ../exercicio3
./build.sh
./get_student_grade_test

echo ".............Teste do exercício 3.b"
./rafa_get_student_grade_test
rm -f *.o *.out *.exe a.out *_test *.s~ *.c~ *.obj *.asm.out

echo ".............Teste do exercício 4"
cd ../exercicio4
./build.sh
./copy_if_test

echo ".............Teste do exercício 4.b"
./rafa_copy_if_test

echo ".............Teste do exercício 4.c"
./user_copy_if_test
rm -f *.o *.out *.exe a.out *_test *.s~ *.c~ *.obj *.asm.out
