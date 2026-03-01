#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include <stdbool.h>
#include <getopt.h>

#define MAX_LINE_LENGTH 1024
#define MAX_LINES 1024

typedef struct {
  char *input;
  char *output;
  bool caseSensitive;
  bool reverse;
} SortOptions;

static SortOptions opts = {NULL, NULL, false, false};

static void printHelp(const char *prog) {
  printf("Uso: %s [-i arquivo] [-o arquivo] [-c] [-r] [-h]\n", prog);
  printf("  -i <arquivo>  Especifica o ficheiro de entrada\n");
  printf("  -o <arquivo>  Especifica o ficheiro de saída\n");
  printf("  -c            Ordenação case-sensitive\n");
  printf("  -r            Ordenação reversa\n");
  printf("  -h            Mostra esta ajuda\n");
}

static bool parseArgs(int argc, char *argv[]) {
  int opt;
  while ((opt = getopt(argc, argv, "i:o:crh")) != -1) {
    switch (opt) {
      case 'i': opts.input = optarg; break;
      case 'o': opts.output = optarg; break;
      case 'c': opts.caseSensitive = true; break;
      case 'r': opts.reverse = true; break;
      case 'h': printHelp(argv[0]); return false;
      default:  return false;
    }
  }
  return true;
}

// Função de comparação para qsort
int cmp(const void *a, const void *b) {
  // Desreferencia para obter a string pois o qsort passa ponteiros para os elementos de tipo indefinido (void*)
  const char *s1 = *(const char **)a;
  const char *s2 = *(const char **)b;
  int result;

  // Se caseSensitive está ativo, usa strcmp, senão strcasecmp
  if (opts.caseSensitive)
    result = strcmp(s1, s2);
  else
    result = strcasecmp(s1, s2);

  return opts.reverse ? -result : result;
}

int main(int argc, char *argv[]) {
  if (!parseArgs(argc, argv))
    return 1;

  FILE *in = opts.input ? fopen(opts.input, "r") : stdin;
  if (!in) { perror("Erro ao abrir ficheiro de entrada"); return 1; }

  FILE *out = opts.output ? fopen(opts.output, "w") : stdout;
  if (!out) { perror("Erro ao abrir ficheiro de saída"); return 1; }

  char *lines[MAX_LINES];
  size_t n = 0;
  char buf[MAX_LINE_LENGTH];

  while (fgets(buf, sizeof(buf), in) && n < MAX_LINES){
    lines[n++] = strdup(buf);
  }

  qsort(lines, n, sizeof(char *), cmp);

  for (size_t i = 0; i < n; i++) {
    fputs(lines[i], out);
    free(lines[i]);
  }

  if (in != stdin) fclose(in);
  if (out != stdout) fclose(out);
  return 0;
}