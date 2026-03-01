#include <stdio.h>
#include <stdint.h>

#define ARRAY_SIZE(a) (sizeof(a) / sizeof(a[0]))

const char *register_name[] = { "", "rbp", "rbx", "r15", "r14", "r13", "r12" };

int invoke_and_test(void *, void (*)(), int, ...);

uint64_t reverse(uint64_t value);

struct {
	uint64_t value;
	uint64_t result;
} tests[] = {
	{0x0000000000000004, 0x2000000000000000},
	{0xaaaaaaaaaaaaaaaa, 0x5555555555555555},
	{0xfafafafafafafafa, 0x5f5f5f5f5f5f5f5f},
	{0x0000000000000000, 0x0000000000000000},
	{0xffffffffffffffff, 0xffffffffffffffff},
};

int main() {
	for (size_t i = 0; i < ARRAY_SIZE(tests); i++) {
		size_t received_result;
		int result = invoke_and_test(&received_result, (void (*)())reverse, 1, tests[i].value);
		if (result != 0) {
			printf("Your function currupted %s, that is a calee saved register\n",
							register_name[result]);
			break;
		}
		if (tests[i].result != received_result) {
			printf("Expected: 0x%016lx received: 0x%016lx\n", tests[i].result, received_result);
		}
		else
			printf("OK\n");
	}
}

