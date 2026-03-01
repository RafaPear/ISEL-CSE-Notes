/*
typedef struct student { int number; char *name; short grades[4]; } Student;
Offset of fields:
 - number: 0  -> Integer (4 bytes, 32 bits)
 - [PADDING: 4 bytes for alignment]
 - name:   8  -> Pointer (8 bytes, 64 bits)
 - grades: 16 -> Array of 4 shorts (8 bytes, 64 bits)
Sizeof(Student) = 24 bytes

typedef struct { int class_id; int length; Student *students; } Class;
Offset of fields:
 - class_id: 0 -> Integer (4 bytes, 32 bits)
 - length:   4 -> Integer (4 bytes, 32 bits)
 - students: 8 -> Pointer to array of Students (8 bytes, 64 bits)
Sizeof(Class) = 16 bytes

short get_student_grade(Class *class, int number, int grade_idx) {
  short grade = -1;
  if (grade_idx < 4)
    for (int i = 0; i < class->length; i++)
      if (class->students[i].number == number) {
        grade = class->students[i].grades[grade_idx];
        break;
      }
  return grade;
}
*/

# ------- Student -------
.equ STUDENT_SIZEOF, 24
.equ STUDENT_NUMBER, 0
.equ STUDENT_NAME,   8
.equ STUDENT_GRADES, 16

# ------- Class --------
.equ CLASS_CLASS_ID, 0
.equ CLASS_LENGTH,   4
.equ CLASS_STUDENTS, 8

	.text
        .global get_student_grade

# Registers:
# %rdi - pointer to Class struct (class) - arg 1
# %esi - student number to search (number) - arg 2 (32-bit)
# %edx - grade index to retrieve (grade_idx) - arg 3 (32-bit)
# %rcx - loop counter i
# %r8  - class->length
# %r9  - pointer to class->students
# %r10 - pointer to current student (students[i])
# %eax - return value (grade)

get_student_grade:
        # Initialize return value to -1 (default grade)
        movw $-1, %ax                    # grade = -1 (16-bit)
        
        # Check if grade_idx < 4
        cmp $4, %edx
        jge get_student_grade_ret        # if grade_idx >= 4, return -1
        
        # Load class->length
        mov CLASS_LENGTH(%rdi), %r8d     # r8d = class->length (32-bit)
        
        # Check if length > 0
        test %r8d, %r8d
        jz get_student_grade_ret         # if length == 0, return -1
        
        # Load class->students pointer
        mov CLASS_STUDENTS(%rdi), %r9    # r9 = class->students
        
        # Initialize loop counter
        xor %rcx, %rcx                   # i = 0

get_student_grade_loop:
        # Check if i < class->length
        cmp %r8d, %ecx
        jge get_student_grade_ret        # if i >= length, exit loop
        
        # Calculate address of students[i]: students + i * sizeof(Student)
        # r10 = r9 + rcx * 24
        lea (%r9,%rcx,8), %r10           # r10 = r9 + rcx * 8
        lea (%r10,%rcx,8), %r10          # r10 = r10 + rcx * 8 = r9 + rcx * 16
        lea (%r10,%rcx,8), %r10          # r10 = r10 + rcx * 8 = r9 + rcx * 24
        
        # Load students[i].number
        mov STUDENT_NUMBER(%r10), %r11d  # r11d = students[i].number
        
        # Compare with target number
        cmp %esi, %r11d
        jne get_student_grade_continue   # if not equal, continue loop
        
        # Found the student! Load grades[grade_idx]
        # grades is at offset 16, each short is 2 bytes
        # Address: r10 + STUDENT_GRADES + grade_idx * 2
        movw STUDENT_GRADES(%r10,%rdx,2), %ax
        jmp get_student_grade_ret        # break

get_student_grade_continue:
        inc %rcx                         # i++
        jmp get_student_grade_loop

get_student_grade_ret:
        ret

        .section        .note.GNU-stack
	