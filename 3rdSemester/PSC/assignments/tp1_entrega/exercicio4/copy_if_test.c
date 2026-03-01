#include <stdio.h>
#include <stddef.h>
#include <string.h>

#define ARRAY_SIZE(a) (sizeof(a) / sizeof(a[0]))

const char *register_name[] = { "", "rbp", "rbx", "r15", "r14", "r13", "r12" };

int invoke_and_test(void *, void (*)(), int, ...);

static size_t _copy_if(void *dst, void *src, size_t src_size, size_t elem_size,
				int (*predicate)(const void *, const void *), const void *context) {
	char *iter, *dst_iter = dst;
	char *last = (char *)src + src_size * elem_size;
	for (iter = src; iter < last ; iter += elem_size)
		if (predicate(iter, context)) {
			memcpy(dst_iter, iter, elem_size);
			dst_iter += elem_size;
		}
	return (dst_iter - (char*)dst) / elem_size;
}

size_t copy_if(void *dst, void *src, size_t src_size, size_t elem_size,
				int (*predicate)(const void *, const void *), const void *context);
                    
static int numbers[] = {10, 100, 20, 200, 10, 300};
static int *pnumbers[] = {&numbers[0], &numbers[1], &numbers[2], &numbers[3], &numbers[4], &numbers[5]};
static int *pnumbers1[ARRAY_SIZE(pnumbers)];
static int *pnumbers2[ARRAY_SIZE(pnumbers)];

int less_than(const void *a, const void *b);

int greater_than(const void *a, const void *b) {
	return **(int **)a > (long)b;
}

int equal_to(const void *a, const void *b) {
	return **(int **)a == (long)b;
}

static struct {
	int **src;
	size_t size;
	int (*compare)(const void *, const void *);
	void *context;
} tests[] = {
	{.src = pnumbers, .size = ARRAY_SIZE(pnumbers), .compare = less_than,    .context = (void *) 100 },
	{.src = pnumbers, .size = ARRAY_SIZE(pnumbers), .compare = greater_than, .context = (void *) 100 },
	{.src = pnumbers, .size = ARRAY_SIZE(pnumbers), .compare = equal_to,     .context = (void *)   1 },
	{.src = pnumbers, .size = ARRAY_SIZE(pnumbers), .compare = equal_to,     .context = (void *)  10 }
};

int main() {
	for (int i = 0; i < ARRAY_SIZE(tests); ++i) {
		size_t size2 = _copy_if(pnumbers1, tests[i].src, tests[i].size, sizeof(tests[i].src[0]), tests[i].compare, tests[i].context);

		size_t size1;
		int invoke_result = invoke_and_test(&size1, (void (*)())copy_if,
				6, pnumbers2, tests[i].src, tests[i].size, sizeof(tests[i].src[0]), tests[i].compare, tests[i].context);
		if (invoke_result != 0) {
			printf("Your function currupted %s, that is a calee saved register\n",
							register_name[invoke_result]);
			break;
		}
//		printf("test[%d] returned size: %zd, expected: %zd\n", i, size1, size2);
		if (size1 == size2) {
			for (size_t j = 0; j < size1; ++j)
				if (pnumbers1[j] != pnumbers2[j])
					printf("array[%d]: %p, expected: %p\n", i, pnumbers1[j], pnumbers2[j]);
		}
		else {
			printf("test[%d] returned size: %zd, expected: %zd\n", i, size1, size2);
		}
	}
}
