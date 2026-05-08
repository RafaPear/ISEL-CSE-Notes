; 1.º Trabalho de Avaliação - Programação do Assembly do P16 - Arquitetura de Computadores
; Professor: João Patriarca
; Realizado por: Gustavo Costa, n.º 52808 # LEIC24D // Rafael Pereira, n.º 52880 # LEIC24D


.equ STACK_SIZE, 40 ; Tamanho do stack (em bytes)
.equ RAND_MAX, 0xFF ; Valor máximo para rand (8 bits)
.equ N, 5 ; Número de iterações a realizar no loop principal

    .text

    b   program ; Salta para a função principal (main)
    b . ; Reservado (ISR)

    ; -------------------------------------------------------------
    ; Função: umull32
    ; Descrição: Multiplicação de 2 números de 32 bits
    ; Entradas: r0, r1 => M (multiplicando)
    ;           r2, r3 => m (multiplicador)
    ; Internos: r6 => temp_A
    ;           r7 => temp_B
    ;           r8 => p_1
    ;           r9 => i
    ; Saídas: r4, r3, r2, r1 => M * m (resultado da multiplicação)
    ; -------------------------------------------------------------

    umull32:
        push lr ; Salva o link register
        push r4 ; Salva r4 no stack
        push r5 ; Salva r5 no stack
        push r6 ; Salva r6 no stack
        push r7 ; Salva r7 no stack
        push r8 ; Salva r8 no stack
        push r9 ; Salva r9 no stack

        ; Como o R3 e R2 já representam o N,
        ; para fazer int64_t p = N apenas preciso
        ; de colocar os restantes registos de maior peso = 0,
        ; ou seja, r5 e r4 = 0

        mov r4, #0  ; Inicializa r4 com o valor 0
        mov r5, #0  ; Inicializa r5 com o valor 0

        mov r6, #0  ; Inicializa temp_A
        mov r7, #0  ; Inicializa temp_B
        mov r8, #0  ; Inicializa p_1
        mov r9, #0  ; Inicializa i
    
    umull32_for:
        mov r6, #32 ; Define r6 com o valor 32
        cmp r6, r9  ; Compara r6 com r9 (32 com i)
        beq umull32_for_end ; Se forem iguais (i == 32), salta para o fim do loop

    umull32_if:
        mov r6, #1  ; Define r6 com o valor 1
        and r7, r2, r6  ; Faz um and bit a bit entre r2 e r6 (r2 & 1) e armazena em r7
        bzc umull32_else_if ; Se r7 for zero, salta para o else if

        mov r6, #1  ; Define r6 com o valor 1
        cmp r6, r8  ; Compara r6 com r8 (1 com p_1)
        bzc umull32_else_if ; Se forem iguais (p_1 == 1), salta para o else if

        add r4, r4, r0  ; Adiciona r0 a r4
        adc r5, r5, r1  ; Adiciona r1 a r5 com carry
        b umull32_if_end    ; Salta para o fim do if

    umull32_else_if:

        mov r6, #1  ; Define r6 com o valor 1
        and r7, r2, r6  ; Faz um and bit a bit entre r2 e r6 (r2 & 1) e armazena em r7
        bzs umull32_if_end  ; Se r7 for zero, salta para o fim do if

        mov r6, #0  ; Define r6 com o valor 0
        cmp r6, r8  ; Compara r6 com r8 (0 com p_1)
        bzc umull32_if_end  ; Se forem iguais (p_1 == 0), salta para o fim do if

        sub r4, r4, r0  ; Subtrai r0 de r4
        sbc r5, r5, r1  ; Subtrai r1 de r5 com borrow

    umull32_if_end:

        mov r6, #1  ; Define r6 com o valor 1
        and r8, r2, r6  ; Faz um and bit a bit entre r2 e r6 (r2 & 1) e armazena em r8

        asr r5, r5, #1  ; Faz um shift aritmético à direita em r5 (r5 >> 1)
        rrx r4, r4  ; Rotaciona r4 com carry
        rrx r3, r3  ; Rotaciona r3 com carry
        rrx r2, r2  ; Rotaciona r2 com carry

        add r9, r6, r9  ; Adiciona r6 a r9 (i + 1) - incrementa i
        b umull32_for   ; Salta para o início do loop

    umull32_for_end:
        mov r0, r2  ; Armazena o resultado em r0
        mov r1, r3  ; Armazena o resultado em r1

        pop r9  ; Restaura r9 do stack
        pop r8  ; Restaura r8 do stack
        pop r7  ; Restaura r7 do stack
        pop r6  ; Restaura r6 do stack
        pop r5  ; Restaura r5 do stack
        pop r4  ; Restaura r4 do stack
        pop pc  ; Retorna da função

    ; --------------------------------------------------------
    ; Função: srand
    ; Descrição: Inicializa o gerador de números aleatórios
    ; Entradas: r0, r1 => seed
    ; Saídas: r0, r1 => seed (atualizada)
    ; --------------------------------------------------------

    srand:
        push r4 ; Salva r4 no stack
        ldr r4, seed_addr   ; Carrega o endereço da seed
        str r0, [r4]    ; Armazena a seed [parte baixa]
        str r1, [r4, #2]    ; Armazena a seed [parte alta]
        pop r4  ; Restaura r4 do stack
        mov pc, lr  ; Retorna da função

    ; --------------------------------------------------------
    ; Função: mod
    ; Descrição: Calcula o módulo de um número de 64 bits
    ; Entradas: r0, r1 => M (dividendo)
    ;           r2, r3 => m (divisor)
    ; Saídas: r0, r1 => M % m (resultado do módulo)
    ; --------------------------------------------------------

    mod:
        push r4 ; Salva r4 no stack
        push r5 ; Salva r5 no stack
        sub r4, r0, r2  ; r4 = M - m
        sbc r5, r1, r3  ; r5 = M - m (com borrow)
        bcc mod_end ; Se carry, i.e. se o resultado for negativo, salta para o fim do módulo
    mod_loop:
        sub r0, r0, r2  ; r0 = M - m
        sbc r1, r1, r3  ; r1 = M - m (com borrow)
        bcs mod_loop    ; Se houver carry, continua o loop
    mod_end:
        pop r5  ; Restaura r5 do stack
        pop r4  ; Restaura r4 do stack
        mov pc, lr  ; Retorna da função

    ; --------------------------------------------------------
    ; Função: rand
    ; Descrição: Gera um número aleatório, basado na seed
    ; Saída: r0 => rand_number (número aleatório gerado)
    ; --------------------------------------------------------

    rand:
        push lr ; Salva o link register
        push r1 ; Salva r1 no stack
        push r2 ; Salva r2 no stack
        push r3 ; Salva r3 no stack
        push r4 ; Salva r4 no stack
        push r5 ; Salva r5 no stack

        ldr r4, seed_addr   ; Carrega o endereço da seed
        mov r5, #RAND_MAX   ; Carrega RAND_MAX (0xFF) para r5
        movt r5, #RAND_MAX  ; Carrega RAND_MAX (0xFF) para r5 (parte superior)

        ldr r0, [r4]    ; Carrega a seed [parte baixa] para r0
        ldr r1, [r4, #2]    ; Carrega a seed [parte alta] para r1

        ; prepara para umull32: seed * 214013
        ; 214013 = 0x000343FD (32 bits)
        mov r2, #0xFD   ; parte inferior
        movt r2, #0x43  ; parte superior
        mov r3, #0x03   ; parte inferior
        bl umull32  ; seed * 214013

        ; adiciona 2531011 (0x00269EC3) ao resultado
        ; para 64 bits, é preciso carry
        mov r2, #0xC3   ; parte inferior
        movt r2, #0x9E  ; parte superior
        mov r3, #0x26   ; parte inferior
        add r0, r0, r2  ; adiciona a constante ao resultado
        adc r1, r1, r3  ; adiciona a constante ao resultado (com carry)

        mov r2, r5  ; Prepara para o módulo
        mov r3, r5  ; Prepara para o módulo

        bl mod  ; Faz o módulo ((r0, r1) % (r2, r3))
        bl srand    ; Atualiza a seed com o novo valor (r0, r1)

        ; Retorna o resultado (seed >> 16)
        mov r0, r1  ; Retorna os 16 bits superiores
        
        pop r5  ; Restaura r5 do stack
        pop r4  ; Restaura r4 do stack
        pop r3  ; Restaura r3 do stack
        pop r2  ; Restaura r2 do stack
        pop r1  ; Restaura r1 do stack
        pop pc  ; Retorna da função

    seed_addr: .word seed   ; Endereço da seed

    program:
        ldr sp, stack_top_addr  ; Inicializa o stack pointer (topo do stack)
        b main  ; Salta para a função principal (main)
    stack_top_addr:
        .word stack_top ; Endereço do topo do stack

    ; --------------------------------------------------------------------
    ; Função: main
    ; Descrição: Função principal do programa que compara números gerados
    ; --------------------------------------------------------------------

main:
    ; -------- Variáveis --------
    ; r0 => rand_number
    ; r2 => temp_A
    ; r3 => temp_B
    ; r6 => i
    ; r5 => error
    ; ---------------------------
    mov r5, #0  ; Inicializa r5 com 0 (error = 0)

    ; Parametros para o srand
    mov r0, #0x2F  ; r0 = 47
    movt r0, #0x15 ; r0 = 5423
    mov r1, #0     ; r1 = 0

    bl srand    ; Inicializa o gerador de números aleatórios com a seed (r0, r1)


main_for_init:
    mov r4, #0 ; Inicializa i = 0

main_for:
    mov r2, #N  ; r2 = N (5)
    cmp r4, r2  ; Compara i com N
    ; predefine r0 = 0 para error = 0 caso
    ; a condicao se verifique
    bhs main_end   ; Se i >= N, salta para o fim do loop

    bl rand ; Chama a função rand para obter um número aleatório
    
    ldr r2, result_addr ; Carrega o endereço da variável result
    lsl r3, r4, #1  ; i * 2 para indexar corretamente
    ldr r3, [r2, r3]    ; Carrega o valor de result[i] para r3
    add r4, r4, #1  ; Incrementa i (i++)
    cmp r0, r3  ; Compara o número aleatório (r0) com result[i] (r3)
    bzs main_for    ; Se r0 <= result[i], salta para o início do loop
    mov r5, #1  ; Se r0 > result[i], define error = 1
    b main_end ; Salta para o fim do programa

main_end:
    mov r0, #0 ; return 0

    result_addr: .word result   ; Endereço do array result

    .data  ; Variáveis globais

    result:
        .word 17747, 2055, 3664, 15611, 981 ; Valores esperados
    
    seed:
        .word 1, 0  ; Seed do gerador de números aleatórios

    .stack

    .space  STACK_SIZE  ; Espaço reservado para o stack
stack_top:
