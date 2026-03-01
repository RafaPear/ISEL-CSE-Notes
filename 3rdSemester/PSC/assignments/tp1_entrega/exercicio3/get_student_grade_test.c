#include <stddef.h>
#include <stdio.h>

#define ARRAY_SIZE(a) (sizeof(a) / sizeof(a[0]))

const char *register_name[] = { "", "rbp", "rbx", "r15", "r14", "r13", "r12" };

int invoke_and_test(void *, void (*)(), int, ...);

typedef struct student { int number; char *name; short grades[4]; } Student;

typedef struct class { int class_id; int length; Student *students; } Class;

short get_student_grade(Class *class, int number, int grade_idx);

static short _get_student_grade(Class *class, int number, int grade_idx) {
	short grade = -1;
		if (grade_idx < 4)
		for (int i = 0; i < class->length; i++)
			if (class->students[i].number == number) {
				grade = class->students[i].grades[grade_idx];
				break;
			}
	return grade;
}

static struct student student1[] = {
	{6000, "Ana",   {10, 13, 14,  9}},
	{5000, "Rui",   {11,  9, 17, 19}},
	{4000, "Luis",  {14,  8, 16, 17}},
	{3000, "Luisa", {15, 13,  7, 12}},
	{2000, "Jaime", {18, 12,  6, 11}},
	{1000, "Abel",  {10, 11, 14, 20}}
};

static struct class class1 = { .length = 6, .students = student1 };

static struct {
	struct class *class;
	int number;
	int grade_idx;
	short result;
} tests[] = {
	{ .class = &class1, .number = 1000, .grade_idx = 3 },
	{ .class = &class1, .number = 4000, .grade_idx = 2 },
	{ .class = &class1, .number = 5000, .grade_idx = 1 },
	{ .class = &class1, .number = 6000, .grade_idx = 1 },
	{ .class = &class1, .number = 3000, .grade_idx = 0 },
};

int main() {
	for (int i = 0; i <  ARRAY_SIZE(tests); ++i) {
		tests[i].result = _get_student_grade(tests[i].class,
			tests[i].number, tests[i].grade_idx);
	}
	for (int i = 0; i <  ARRAY_SIZE(tests); ++i) {
		long received_result;	// temm que ser um valor a 64 bits
		int invoke_result = invoke_and_test(&received_result, (void (*)())get_student_grade,
				3, tests[i].class, tests[i].number, tests[i].grade_idx);
		if (invoke_result != 0) {
			printf("Your function currupted %s, that is a calee saved register\n",
							register_name[invoke_result]);
			break;
		}
		printf("Received result: %d\n", (short)received_result);
		if ((short)received_result != (short)tests[i].result) {
			printf("[%d] received result: %d expected result: %d\n",
												i,  (short)received_result, tests[i].result);
		}
	}
}
