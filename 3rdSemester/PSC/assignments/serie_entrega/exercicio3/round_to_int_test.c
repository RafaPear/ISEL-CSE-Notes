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
#include <stdbool.h>
#include <math.h>

#define ARRAY_SIZE(a)	(sizeof(a) / sizeof(a[0]))

bool round_to_int(float fvalue, int *ivalue);

union Float {
	float f;
	unsigned i;
	struct {
		unsigned mantissa: 23;
		unsigned exponent: 8;
		unsigned signal: 1;
	};
};

struct {
	union Float fvalue;
	int ivalue;
	bool result;
} test_array[] = {
	[0] = {.fvalue.f = 3.5, .ivalue = 4, .result = true},
	[1] = {.fvalue.f = 3.4, .ivalue = 3, .result = true},	
	[2] = {.fvalue.f = 0.5, .ivalue = 1, .result = true},
	[3] = {.fvalue.f = -3.5, .ivalue = -4, .result = true},
	[4] = {.fvalue.f = -3.4, .ivalue = -3, .result = true},	
	[5] = {.fvalue.f = -0.5, .ivalue = -1, .result = true},
	[6] = {.fvalue.exponent = 125, .ivalue = 0, .result = true},
	[7] = {.fvalue.f = 0.4999999, .ivalue = 0, .result = true},
	[8] = {.fvalue.exponent = 126 + 32, .ivalue = 0, .result = false},	
	[9] = {.fvalue.exponent = 126 + 31, .ivalue = 1073741824, .result = true},	
};

int main() {
	int error = 0;
	for (int i = 0; i < ARRAY_SIZE(test_array); ++i) {
		if (!test_array[i].result)
			continue;
		long result = lround(test_array[i].fvalue.f);
		if (result != test_array[i].ivalue)
			printf("[%d] - Ill-defined test. lround(%f) = %ld, ivalue expected %d\n",
				i, test_array[i].fvalue.f, result, test_array[i].ivalue);
	}
	for (int i = 0; i < ARRAY_SIZE(test_array); ++i) {
		int ivalue;
		bool result = round_to_int(test_array[i].fvalue.f, &ivalue);
		if (result != test_array[i].result) {
			printf("[%d] - Error, %f - %d %08b %023b expected result = %i, returned result = %i\n",
				i, test_array[i].fvalue.f, test_array[i].fvalue.signal, test_array[i].fvalue.exponent, test_array[i].fvalue.mantissa,
				test_array[i].result, result);
			error = 1;
		}
		else if (ivalue != test_array[i].ivalue)
			printf("[%d] - Error, %f %d %08b %023b expected value = %d, returned value = %d\n",
				i, test_array[i].fvalue.f, test_array[i].fvalue.signal, test_array[i].fvalue.exponent, test_array[i].fvalue.mantissa,
				test_array[i].ivalue, ivalue);
		else
			printf("[%d] OK\n", i);
	}
	return error;
}

