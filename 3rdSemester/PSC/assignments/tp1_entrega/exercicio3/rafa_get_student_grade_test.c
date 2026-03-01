#include <stdio.h>
#include <string.h>

// Definição das estruturas
typedef struct student {
    int number;
    char *name;
    short grades[4];
} Student;

typedef struct {
    int class_id;
    int length;
    Student *students;
} Class;

// Declaração da função em assembly
short get_student_grade(Class *class, int number, int grade_idx);

int main(void) {
    // Definição estática dos estudantes
    static Student students[] = {
        {
            .number = 12345,
            .name = "João Silva",
            .grades = {15, 18, 16, 17}
        },
        {
            .number = 23456,
            .name = "Maria Santos",
            .grades = {19, 20, 18, 19}
        },
        {
            .number = 34567,
            .name = "Pedro Costa",
            .grades = {14, 13, 15, 16}
        },
        {
            .number = 45678,
            .name = "Ana Oliveira",
            .grades = {17, 16, 18, 20}
        }
    };
    
    // Definição estática da turma
    static Class my_class = {
        .class_id = 101,
        .length = 4,
        .students = students
    };
    
    int tests_failed = 0;
    
    // Teste 1: Nota existente
    short grade = get_student_grade(&my_class, 12345, 0);
    if (grade != 15) {
        printf("Teste 1 FALHOU: aluno 12345, índice 0\n");
        printf("  Esperado: 15, Obtido: %d\n", grade);
        tests_failed++;
    }
    
    // Teste 2: Outra nota do mesmo aluno
    grade = get_student_grade(&my_class, 12345, 2);
    if (grade != 16) {
        printf("Teste 2 FALHOU: aluno 12345, índice 2\n");
        printf("  Esperado: 16, Obtido: %d\n", grade);
        tests_failed++;
    }
    
    // Teste 3: Nota de outro aluno
    grade = get_student_grade(&my_class, 23456, 3);
    if (grade != 19) {
        printf("Teste 3 FALHOU: aluno 23456, índice 3\n");
        printf("  Esperado: 19, Obtido: %d\n", grade);
        tests_failed++;
    }
    
    // Teste 4: Último aluno
    grade = get_student_grade(&my_class, 45678, 1);
    if (grade != 16) {
        printf("Teste 4 FALHOU: aluno 45678, índice 1\n");
        printf("  Esperado: 16, Obtido: %d\n", grade);
        tests_failed++;
    }
    
    // Teste 5: Aluno não existe
    grade = get_student_grade(&my_class, 99999, 0);
    if (grade != -1) {
        printf("Teste 5 FALHOU: aluno inexistente 99999\n");
        printf("  Esperado: -1, Obtido: %d\n", grade);
        tests_failed++;
    }
    
    // Teste 6: Índice inválido
    grade = get_student_grade(&my_class, 12345, 5);
    if (grade != -1) {
        printf("Teste 6 FALHOU: índice inválido 5\n");
        printf("  Esperado: -1, Obtido: %d\n", grade);
        tests_failed++;
    }
    
    // Teste 7: Índice limite
    grade = get_student_grade(&my_class, 34567, 3);
    if (grade != 16) {
        printf("Teste 7 FALHOU: aluno 34567, índice 3\n");
        printf("  Esperado: 16, Obtido: %d\n", grade);
        tests_failed++;
    }
    
    // Teste 8: Primeiro aluno, primeira nota
    grade = get_student_grade(&my_class, 12345, 0);
    if (grade != 15) {
        printf("Teste 8 FALHOU: aluno 12345, índice 0 (repetição)\n");
        printf("  Esperado: 15, Obtido: %d\n", grade);
        tests_failed++;
      }
      
    return tests_failed > 0 ? 1 : 0;
}