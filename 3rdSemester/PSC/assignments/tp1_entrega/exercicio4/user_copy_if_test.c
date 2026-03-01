#include <stdio.h>
#include <string.h>

// Estrutura user
struct user {
    const char *name;
    int number;
};

// Declaração de copy_if (implementação em assembly)
size_t copy_if(void *dst, void *src, size_t src_size, size_t elem_size,
               int (*predicate)(const void *, const void *), const void *context);

// Predicado: verifica se o nome contém a keyword
int name_contains(const void *elem, const void *context) {
    const struct user *const *ptr = elem;
    const struct user *user = *ptr;
    const char *keyword = context;
    return strstr(user->name, keyword) != NULL;
}

int main(void) {

    static struct user user1 = {"João Silva", 12345};
    static struct user user2 = {"Maria Santos", 23456};
    static struct user user3 = {"Pedro João Alves", 34567};
    static struct user user4 = {"Ana Costa", 45678};
    static struct user user5 = {"Carlos João Mendes", 56789};
    static struct user user6 = {"Beatriz Lima", 67890};
    static struct user user7 = {"João Pedro Sousa", 78901};
    static struct user user8 = {"Sofia Rodrigues", 89012};

    static struct user *src[] = {
        &user1, &user2, &user3, &user4,
        &user5, &user6, &user7, &user8
    };

    static struct user *dst[8];

    const char *keyword = "João";

    static struct user *expected[] = {
        &user1, &user3, &user5, &user7
    };

    size_t src_size = sizeof(src) / sizeof(src[0]);
    size_t expected_count = sizeof(expected) / sizeof(expected[0]);

    size_t count = copy_if(dst, src, src_size,
                           sizeof(struct user *),
                           name_contains, keyword);

    int ok = 1;

    // Verifica quantidade
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
                printf("ERRO: elementos incorretos.\n");
            }
            printf("  Posição %zu:\n", i);
            printf("    Esperado: %s (%d)\n", 
                   expected[i]->name, expected[i]->number);
            printf("    Obtido:   %s (%d)\n", 
                   dst[i]->name, dst[i]->number);
            ok = 0;
        }
    }

    if (ok) {
        return 0;   // Sucesso silencioso
    } else {
        return 1;   // Falha com prints
    }
}
