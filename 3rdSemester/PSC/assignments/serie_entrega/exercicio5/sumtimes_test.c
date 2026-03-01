/*
 *	Programação em Sistemas Computacionais
 *	Semestre de inverno de 2025/2026
 *	Primeira série de exercícios
 *
 *	Programa de teste do 2º exercício
 *
 *	Utilização:
 *
 *	$ gcc sum_times_test.c sum_times.c -o sum_times_test -g -Wall
 *
 *	$ ./sum_times_test
 */

#include <stdio.h>
#include <string.h>
#include <stdbool.h>


#define ARRAY_SIZE(a)	(sizeof(a) / sizeof(a[0]))

struct hmstime { unsigned short hours; unsigned char minutes; unsigned char seconds; };

void sumtimes(struct hmstime *res, const struct hmstime times[], size_t ntimes);

struct {
	struct hmstime hms[10];
	size_t hms_size;
	struct hmstime result;
} test_array[] = {
	{.hms = {{23, 28, 03}, {12, 3, 13}}, .hms_size = 2, .result = {35, 31, 16}},
	{.hms = {{10, 59, 59}, {1, 0, 1}}, .hms_size = 2, .result = {12, 0, 0}},
	{.hms = {{10, 59, 59}, {1, 1, 1}, {12, 59, 1}}, .hms_size = 3, .result = {25, 0, 1}},
};

int main() {
	int error = 0;
	for (int i = 0; i < ARRAY_SIZE(test_array); ++i) {
		struct hmstime result;
		sumtimes(&result, test_array[i].hms, test_array[i].hms_size);
		if (result.hours != test_array[i].result.hours ||
		    result.minutes != test_array[i].result.minutes ||
		    result.seconds != test_array[i].result.seconds) {
		printf("[%d] - Error, expected result = %d,%d,%d, returned result = %d,%d,%d\n",
			i, test_array[i].result.hours, test_array[i].result.minutes, test_array[i].result.seconds,
			result.hours, result.minutes, result.seconds);
			error = 1;
		}
		else
			printf("[%d] OK\n", i);
	}
	return error;
}

