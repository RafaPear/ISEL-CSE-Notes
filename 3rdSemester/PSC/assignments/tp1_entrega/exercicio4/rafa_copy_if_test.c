#include <stdio.h>
#include <string.h>

// Declaração da função copy_if
size_t copy_if(void *dst, void *src, size_t src_size, size_t elem_size,
               int (*predicate)(const void *, const void *), const void *context);

// Predicado: inteiro > threshold
int greater_than(const void *elem, const void *context) {
    int value = *(const int *)elem;
    int threshold = *(const int *)context;
    return value > threshold;
}

int main(void) {

    static int src[] = {10, 5, 23, 8, 42, 15, 7, 30, 12, 50};
    size_t src_size = sizeof(src) / sizeof(src[0]);

    static int dst[10];

    int threshold = 20;

    static int expected[] = {23, 42, 30, 50};
    size_t expected_count = sizeof(expected) / sizeof(expected[0]);

    size_t count = copy_if(dst, src, src_size,
                           sizeof(int),
                           greater_than, &threshold);

    int ok = 1;

    // Verifica número de elementos
    if (count != expected_count) {
        printf("ERRO: número de elementos incorreto.\n");
        printf("  Esperado: %zu\n", expected_count);
        printf("  Obtido:   %zu\n", count);
        ok = 0;
    }

    // Verifica conteúdo
    size_t min = (count < expected_count) ? count : expected_count;
    for (size_t i = 0; i < min; i++) {
        if (dst[i] != expected[i]) {
            if (ok) {
                printf("ERRO: valores incorretos.\n");
            }
            printf("  Posição %zu: esperado %d, obtido %d\n",
                   i, expected[i], dst[i]);
            ok = 0;
        }
    }

    return ok ? 0 : 1;
}
