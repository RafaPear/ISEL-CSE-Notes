/*
 *	Programação em Sistemas Computacionais
 *	Semestre de inverno de 2025/2026
 *	Primeira série de exercícios
 *
 *	Programa de teste do 2º exercício
 *
 *	Utilização:
 *
 *	$ gcc invert_bits_test.c invert_bits.c -o invert_bits_test -g -Wall
 *
 *	$ ./invert_bits_test
 */

#include <stdio.h>
#include <string.h>

#define ARRAY_SIZE(a)	(sizeof(a) / sizeof(a[0]))

int invert_bits(int value, int position, int nbits);

struct {
	int value;
	int position;
	int nbits;
	int result;
} test_array[] = {
	{.value = 0, .position = 3, .nbits = 2, .result = 0x18},
	{.value = 0, .position = 0, .nbits = 5, .result = 0x1f},
	{.value = 0x80000000, .position = 31, .nbits = 1, .result = 0},
	{.value = 0xffffffff, .position = 16, .nbits = 4, .result = 0xfff0ffff},
	{.value = 0x80008000, .position = 15, .nbits = 17, .result = 0x7fff0000},
	{.value = 0x80008000, .position = 30, .nbits = 2, . result = 0x40008000},
	{.value = 0x80008000, .position = 31, .nbits = 2, . result = 0x8000},
	{.value = 0x80008000, .position = 31, .nbits = 32, . result = 0x8000},
};

int main() {
	int error = 0;
	for (int i = 0; i < ARRAY_SIZE(test_array); ++i) {
		int result = invert_bits(test_array[i].value, test_array[i].position, test_array[i].nbits);
		if (result != test_array[i].result) {
			printf("[%d] - Error, expected = 0x%x, returned = 0x%x\n",
				i, test_array[i].result, result);
			error = 1;
		}
		else
			printf("[%d] - OK\n", i);
	}
	return error;
}

