#include <stdio.h>
#include <string.h>

void string_detab(char *string, int tab_size){
    char buffer[1024];
    int buffer_index = 0;
    int column = 0; // Serve para alinhar os tabs

    // EX: AB\tC com tab_size = 4
    // column = 2
    // spaces_to_add = 4 - (2 % 4) = 2
    // Resultado: AB..C
    // Se houver \n, reiniciar column para 0
    // Se nao houvesse noção de column o resultado estaria desalinhado

    for (int i = 0; string[i] != '\0'; i++) {
        if (string[i] == '\t') {
            int spaces_to_add = tab_size - (column % tab_size);
            for (int j = 0; j < spaces_to_add; j++) {
                buffer[buffer_index++] = ' ';
                column++;
            }
        } else {
            buffer[buffer_index++] = string[i];
            column++;
            if (string[i] == '\n') {
                column = 0;
            }
        }
    }
    buffer[buffer_index] = '\0';
    strcpy(string, buffer);
}