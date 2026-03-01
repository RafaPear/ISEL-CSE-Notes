
gcc -g -Wall copy_if_test.c copy_if_asm.s less_than.s ../invoke/invoke.s -o copy_if_test
gcc -g -Wall rafa_copy_if_test.c copy_if_asm.s less_than.s ../invoke/invoke.s -o rafa_copy_if_test
gcc -g -Wall user_copy_if_test.c copy_if_asm.s less_than.s ../invoke/invoke.s -o user_copy_if_test