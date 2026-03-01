/*
 *	Programação em Sistemas Computacionais
 *	Semestre de inverno de 2025/2026
 *	Primeira série de exercícios
 *
 *	Programa de teste do 4º exercício
 *
 *	Utilização:
 *
 *	$ gcc string_detab_test.c string_detab.c -o string_detab_test -g -Wall
 *
 *	$ ./string_detab_test
 */

#include <stdio.h>
#include <string.h>

#define ARRAY_SIZE(a)	(sizeof(a) / sizeof(a[0]))

void string_detab(char *string, int tab_size);

struct {
	int tab_size;
	char origin[100];
	char *expected;
} test_array[] = {
	{.tab_size = 8, .origin = "aaa\tbbbbbbbb\0""12345678", .expected = "aaa     bbbbbbbb"},
	{.tab_size = 3, .origin = "aaa\tbbbbbbbb\0""12345678", .expected = "aaa   bbbbbbbb"},
	{.tab_size = 2, .origin = "aaa\tbbbbbbbb\0""12345678", .expected = "aaa bbbbbbbb"},
	{.tab_size = 1, .origin = "aaa\tbbbbbbbb\0""12345678", .expected = "aaa bbbbbbbb"},
	{.tab_size = 8, .origin = "aaa\tbbbb\tccc\0""1234567", .expected = "aaa     bbbb    ccc"},
	{.tab_size = 8, .origin = "aaa\t\tbbbb\tccc\0""1234567", .expected = "aaa             bbbb    ccc"},
};

int main() {
	int error = 0;
	for (int i = 0; i < ARRAY_SIZE(test_array); ++i) {
		string_detab(test_array[i].origin, test_array[i].tab_size);
		if (strcmp(test_array[i].origin, test_array[i].expected) != 0) {
			printf("[%d] - Error, expected = %s, returned = %s\n",
				i, test_array[i].expected, test_array[i].origin);
			error = 1;
		}
		else
			printf("[%d] - OK\n", i);
	}
	return error;
}

