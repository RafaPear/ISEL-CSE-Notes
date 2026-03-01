/*
 *	Programação em Sistemas Computacionais
 *	Semestre de inverno de 2025/2026
 *	Série de exercícios
 *
 *	Programa de teste do 1º exercício
 *
 *	Utilização:
 *
 *	$ gcc count_ones_test.c count_ones.c -o count_ones_test -g -Wall
 *
 *	$ ./count_ones_test
 */

#include <stdio.h>
#include <string.h>

#define ARRAY_SIZE(a)	(sizeof(a) / sizeof(a[0]))

int count_ones(int value);

struct {
	int value;
	int result;
} test_array[] = {
	{.value = 0,  .result = 0},
	{.value = 1,  .result = 1},
	{.value = 3,  .result = 2},
	{.value = 5,  .result = 2},
	{.value = 0x11111111,  .result = 8},
	{.value = 0x35924628,  .result = 12},
	{.value = 0xffffffff,  .result = 32},
	{.value = 0xf0000000,  .result = 4},
	{.value = 0x0000000f,  .result = 4},
};

int main() {
	int error = 0;
	for (int i = 0; i < ARRAY_SIZE(test_array); ++i) {
		int result = count_ones(test_array[i].value);
		if (result != test_array[i].result) {
			printf("[%d] - Error, expected = %d, returned = %d\n",
				i, test_array[i].result, result);
			error = 1;
		}
		else
			printf("[%d] - OK\n", i);
	}
	return error;
}

