#include <stdio.h>
#include <string.h>

#define ARRAY_SIZE(a) (sizeof(a) / sizeof(a[0]))

const char *register_name[] = { "", "rbp", "rbx", "r15", "r14", "r13", "r12" };

int invoke_and_test(void *, void (*)(), int, ...);

int my_memmove(const void *ptr1, const void *ptr2, size_t num);

char array1[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
char array2[10];
char array3[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

char array4[20] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
char array5[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

char array6[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
char array7[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};

struct {
	void *dst, *src, *result;
	size_t size;
} tests[] = {
	{.dst = array2, .src = array1, .size = 10, .result = array3 },
	{.dst = array4 + 1, .src = array4, .size = 10, .result = array5 },
	{.dst = array6, .src = array6 + 1, .size = 9, .result = array7 },
};

int main() {
	for (int i = 0; i < ARRAY_SIZE(tests); ++i) {
		void *received_result;	// temm que ser um valor a 64 bits
		int invoke_result = invoke_and_test(&received_result, (void (*)())my_memmove,
									3, tests[i].dst, tests[i].src, tests[i].size);
		if (invoke_result != 0) {
			printf("Your function currupted %s, that is a calee saved register\n",
							register_name[invoke_result]);
			break;
		}
		if (received_result != tests[i].dst)
			printf("Received pointer: %p, expected pointer: %p\n", received_result, tests[i].dst);
		if (memcmp(tests[i].result, tests[i].dst, tests[i].size) != 0 )
			printf("[%d] error\n", i);
	}
}
