; =====================================================================
;  File:   TrabalhoProjeto_p16.s
;  Desc:   Dice‑roller project – uses FED ticks for timing *only with
;          instructions available in the P16 ISA* (no pseudo‑ops).
;          Replace the TIMING CONSTANTS block if the external clock
;          on the FED is changed (e.g. 100 kHz instead of 10 kHz).
; ---------------------------------------------------------------------
;  BUILD:  p16as -s .stack=0x2000 TrabalhoProjeto_p16.s
; =====================================================================

; =========================== CONSTANTES ===============================
    .equ INPORT_ADDRESS   , 0xFF80         ; porta de entrada (DIP‑switch)
    .equ OUTPORT_ADDRESS  , 0xFFC0         ; porta de saída (7‑seg)
    .equ FED_ADDRESS      , 0xFF40         ; 1.º byte da região CS_EXT1

    .equ ENABLE_EXTINT    , 0x10

    .equ ATESTE           , '\0'

; ------------  temporização derivada do FED  -------------------------
        ; ---------- temporização ----------
    .equ  FRAME_TICKS , 1        ; 100 ms
    .equ  FRAMES_ROLL , 30      ; 10 s / 10 ms
    .equ  END_ROLL    , 100

; ------------  dados jogo  -------------------------------------------
    .equ MAX_ANIM      , 8
    .equ TOTAL_SIDES   , 4

    .equ DICE_MSK      , 0x0C           ; SIDES[1:0] no DIP
    .equ ROLL_MSK      , 0x01           ; ROLL no DIP

; ------------  RandMax  ---------------------------------------
    .equ    RAND_MAX_L, 0xFFFF      ; Corresponde ao maior valor inteiro
    .equ    RAND_MAX_H, 0xFFFF      ; sem sinal codificavel com 32 bits

    .equ STACK_SIZE    , 64

; =====================================================================
    .text
    b       reset_vector            ; 0x0000
    b       extint_vector           ; 0x0002

; ========================== VECTORS ==================================
reset_vector:
    ldr     sp, _stack_lit         ; PC-relative (offset = +2 words)
    b       main
_stack_lit:
    .word   stack_top

extint_vector:                ; vector 0x0002
    push    r0
    push    r1
    ; limpa FED
    mov     r0, #FED_ADDRESS & 0xFF
    movt    r0, #(FED_ADDRESS >> 8) & 0xFF
    strb    r0, [r0]
    ; ++tick_cnt
    ldr     r0, _tick_ptr1     ; &tick_cnt (literal logo abaixo)
    ldr     r1, [r0]
    add     r1, r1, #1
    str     r1, [r0]
    pop     r1
    pop     r0
    movs    pc, lr
    
_tick_ptr1: .word tick_cnt


; =========================  WAIT_TICKS ===============================
;  Espera r0 ticks (0…65535) – destrói r1, r2
; ---------------------------------------------------------------------
wait_ticks:
    ldr     r1, _tick_ptr2         ; r1 = &tick_cnt
    ldr     r2, [r1]               ; t0
    add     r0, r0, r2             ; alvo = t0 + delay
WT_loop:
    ldr     r2, [r1]
    cmp     r2, r0
    blo     WT_loop
    mov     pc,  lr
_tick_ptr2:
    .word   tick_cnt

; ===========================  MAIN  ==================================
main:
    push    lr
    push    r4
    push    r5

    mrs     r0, cpsr
    mov     r1, #ENABLE_EXTINT
    orr     r0, r0, r1
    msr     cpsr, r0
    b       main_init             ; inicializa o display

main_init:
    bl      inport_read
    bl      srand
    bl      roll

    ldr     r1, ptr_hex27seg      ; r0 ← endereço da tabela de 7‑seg
    ldrb    r0, [r1, r0]          ; r0 ← valor do dígito
    bl      outport_write         ; escreve no display
    
    mov     r4, #0                ; valor inicial no display
    mov     r5, #0
    mov     r0, #0

main_start_loop:
    mov     r5, r0
    mov     r0, r4
    bl      action
    mov     r4, r0
    ; espera premir ROLL
    bl      inport_read           ; r0 ← entradas
    mov     r1, #ROLL_MSK
    and     r0, r0, r1

    cmp     r0, r5
    bhs     main_start_loop            ; se ROLL não foi premido, continua
    b       Main_loop            ; se ROLL foi premido, continua

; -------- loop principal ---------------------------------------------
Main_pre_loop:
    mov     r5, r0

Main_loop:
    ; actualiza índice do dado se SIDES mudar

    ; espera premir ROLL
    bl      inport_read           ; r0 ← entradas
    mov     r1, #ROLL_MSK
    and     r0, r0, r1

    cmp     r0, r5
    bhs     Main_pre_loop            ; se ROLL não foi premido, continua

    mov     r5, r0

    bl      inport_read
    mov     r6, #DICE_MSK
    and     r0, r0, r6
    lsr     r0, r0, #2            ; 0‑3
    mov     r6, r0                ; r1 ← índice do dado

    ; anima durante ROLL_TICKS
    bl      anim

    ; gera face aleatória & mostra
    mov     r0, r6                ; r0 ← índice do dado (0‑3)
    bl      roll
    
    ldr     r1, ptr_hex27seg      ; r0 ← endereço da tabela de 7‑seg
    ldrb     r0, [r1, r0]          ; r0 ← valor do dígito
    bl      outport_write         ; escreve no display

    mov     r0, #END_ROLL & 0xFF   ; baixa
    movt    r0, #END_ROLL >> 8     ; alta
    bl      wait_ticks

    b       Main_loop


; ======================  ACTION  =====================================
; in  r0 = idx dado anterior (0‑3)
; out r0 = idx actual
; ---------------------------------------------------------------------
action:
    push    lr
    push    r4
    push    r5

    mov     r4, r0                ; idx anterior
    bl      inport_read
    mov     r5, #DICE_MSK
    and     r0, r0, r5
    lsr     r0, r0, #2            ; 0‑3
    cmp     r0, r4
    beq     Action_ret            ; igual → mantem‑se

    mov     r4, r0                ; novo dado
    bl      roll

    ldr     r1, ptr_hex27seg      ; r0 ← endereço da tabela de 7‑seg
    ldrb     r0, [r1, r0]          ; r0 ← valor do dígito
    bl      outport_write         ; escreve no display

    mov      r0, r4
Action_ret:
    pop     r5
    pop     r4
    pop     pc


ptr_hex27seg:
    .word   hex27seg



; ======================  ROLL ========================================
; in  r0 = idx dado (0‑3)  => 4,6,8,12 ladoslr
; out r0 = número 1…N (placeholder RNG)
; out r1 = idx dado (0‑3)  => 4,6,8,12 lados
; ---------------------------------------------------------------------
roll:
    push    lr
    push    r4
    bl      getDiceMax      ; r0 = N
    mov     r4, r0          ; divisor

    bl      rand            ; r0 = 0…65535
    lsr     r0, r0, #8


; ---------- r0 = r0 mod N  (N ≤ 12) ----------------------------------
; ciclo: enquanto r0 ≥ N  → r0 -= N
mod_loop:
    cmp     r0, r4
    blo     mod_done              ; r0 < N  → já é o resto
    sub     r0, r0, r4
    blo     mod_done
    b       mod_loop
mod_done:
    add     r0, r0, #1            ; passa a 1-based (1…N)

    pop     r4                    ; r4 = N  (máx. número do dado)
    pop     pc

; ------------ getDiceMax ---------------------------------------------
; ------------------------------------------------------------
; Get the maximum sides for the die based on the index
; Input:
; r0 - Die index (0 for d4, 1 for d6, 2 for d8, 3 for d12)
; Returns:
; r0 - Maximum sides for the die
getDiceMax:
    push    r4                     ; Save r4 on the stack
    mov     r4, #TOTAL_SIDES       ; Load the total number of sides for the die
    cmp     r0, r4                 ; Compare the input with the total sides
    bge     getDiceMax_end         ; If input is greater or equal, return
    lsl     r0, r0, #1             ; Double the input value (to match the die sides)
    ldr     r4, ptr_max_sides      ; Load the address of max_sides
    ldr     r0, [r4, r0]           ; Load the maximum sides for the die
getDiceMax_end:
    pop     r4                     ; Restore r4
    mov     pc, lr                 ; Return from getDiceMax

ptr_max_sides:
    .word   max_sides

; ======================  ANIMAÇÃO ====================================
; Faz FRAMES_ROLL frames, cada FRAME_TICKS ticks
; ---------------------------------------------------------------------
anim:
    push    lr
    push    r4
    push    r5
    push    r6

    ldr     r6, ptr_anim_seq
    mov     r4, #0          ; índice 0-7
    mov     r5, #FRAMES_ROLL & 0xFF   ; baixa
    movt    r5, #FRAMES_ROLL >> 8     ; alta

Anim_loop:
    mov     r0, #FRAME_TICKS
    bl      wait_ticks

    ldrb    r0, [r6, r4]
    bl      outport_write

    add     r4, r4, #2            ; próximo byte
    mov     r1, #(MAX_ANIM * 2)
    cmp     r4, r1
    blt     Anim_idx_ok
    mov     r4, #0
Anim_idx_ok:

    sub     r5, r5, #1
    bne     Anim_loop

    pop     r6
    pop     r5
    pop     r4
    pop     pc

ptr_anim_seq:
    .word   anim_sequence

; ======================  I/O ROUTINES ================================
;-------------------------------------------------------
; Input Port Routines
;-------------------------------------------------------
inport_read:
    mov     r1, #INPORT_ADDRESS & 0xFF
    movt    r1, #(INPORT_ADDRESS >> 8) & 0xFF
    ldrb    r0, [r1, #0]
    mov     pc, lr

;-------------------------------------------------------
; Output Port Routines
;-------------------------------------------------------
outport_write:
    mov     r1, #OUTPORT_ADDRESS & 0xFF
    movt    r1, #(OUTPORT_ADDRESS >> 8) & 0xFF
    strb    r0, [r1, #0]
    mov     pc, lr

; Rotina:    umull32
; Descricao: Realiza a multiplicacao de dois numeros naturais codificados com
;            32 bits.
;            Interface exemplo: uint32_t umull( uint32_t M, uint32_t m );
; Entradas:  R1:R0 - Valor do multiplicando (M)
;            R3:R2 - Valor do multiplicador (m)
; Saidas:    R1:R0 - Valor do produto
; Efeitos:   R5:R4 - Parte alta (bits 63..32) do produto (p), pois R3:R2 contem
;                    a parte baixa (bits 31..0)
;            R6    - Mapeia a variavel p_1
;            R7    - guarda o valor da iteracao do ciclo for (i)
;            R8    - guarda valores temporariamente
;
umull32:
    ; Prologo
    push    r8
    push    r7
    push    r6
    push    r5
    push    r4

    ; Iniciar p fazendo a extensao de sinal aos 16 MSb
    asr     r4, r3, #15
    mov     r5, r4
    ; Inicia p_1
    mov     r6, #0
    ; Implementacao do ciclo for
    mov     r7, #0      ; Inicia i
umull32_loop:
    mov     r8, #32     ; Avaliar o limite maximo de i
    cmp     r7, r8
    bhs     umull32_ret
    ; Implementacao do if
    mov     r8, #1
    and     r8, r2, r8
    bzc     umull32_else
    mov     r8, #1
    cmp     r6, r8
    bne     umull32_loop_end
    add     r4, r4, r0  ; Atualizar o valor de p
    adc     r5, r5, r1
    b       umull32_loop_end
umull32_else:
    ; Implementacao otimizada do else
    mov     r8, #0
    cmp     r6, r8
    bne     umull32_loop_end
    sub     r4, r4, r0  ; Atualizar o valor de p
    sbc     r5, r5, r1
umull32_loop_end:
    mov     r8, #1      ; Definir o novo valor de p_1
    and     r6, r2, r8
    asr     r5, r5, #1
    rrx     r4, r4
    rrx     r3, r3
    rrx     r2, r2
    add     r7, r7, #1  ; Incrementar i
    b       umull32_loop

umull32_ret:
    ; Epilogo
    mov     r0, r2      ; Preparar o valor a devolver
    mov     r1, r3

    pop     r4
    pop     r5
    pop     r6
    pop     r7
    pop     r8
    mov     pc, lr

; Rotina:    srand
; Descricao: Afeta a variavel global seed com um novo valor (semente),
;            recebido por parametro.
;            Interface exemplo: void srand( uint32_t nseed );
; Entradas:  R0 - Parte baixa (bits 0..15) do novo valor de seed
;            R1 - Parte alta (bits 16..31) do novo valor de seed
; Saidas:    -
; Efeitos:   Altera o valor da variavel global seed
;            R2 - guarda valores temporariamente
;
srand:
    ldr     r2, seed_addr_srand
    str     r0, [r2, #0]
    str     r1, [r2, #2]
    mov     pc, lr

seed_addr_srand:
    .word   seed

; Rotina:    rand
; Descricao: Implementa um gerador congruencial linear (LCG) para gerar numeros
;            pseudo-aleatorios entre zero e RAND_MAX.
;            Interface exemplo: uint16_t rand( void );
; Entradas:  R0 - Valor maximo para gerar
; Saidas:    R0 - O valor pseudo-aleatorio gerado
; Efeitos:   Altera o valor da variavel global seed
;            R0 - guarda valores temporariamente
;            R1 - guarda valores temporariamente
;            R2 - guarda valores temporariamente
;            R3 - guarda valores temporariamente
;
rand:
    ; Prologo
    push    lr
    ; Obter o valor atual de seed
    ldr     r2, seed_addr_rand
    ldr     r0, [r2, #0]
    ldr     r1, [r2, #2]
    ; Calcular a multiplicacao a 32 bits
    mov     r2, #( 0x43FD >> 0 ) & 0xFF    ; Carregar o valor 214013
    movt    r2, #( 0x43FD >> 8 ) & 0xFF
    mov     r3, #( 0x0003 >> 0 ) & 0xFF
    movt    r3, #( 0x0003 >> 8 ) & 0xFF
    bl      umull32
    ; Calcular a adicao a 32 bits
    mov     r2, #( 0x9EC3 >> 0 ) & 0xFF    ; Carregar o valor 2531011
    movt    r2, #( 0x9EC3 >> 8 ) & 0xFF
    mov     r3, #( 0x0026 >> 0 ) & 0xFF
    movt    r3, #( 0x0026 >> 8 ) & 0xFF
    add     r0, r0, r2
    adc     r1, r1, r3

    ; Nao e necessario implementar a divisao modulo, pois a operacao realizada
    ; com valores de 32 bits nunca ultrapassa o valor RAND_MAX (0xFFFFFFFF).
    ; No entanto, a operacao % RAND_MAX deve devolver 0 quando o novo valor de
    ; seed e exatamente igual a RAND_MAX. Assim, e suficiente verificar este
    ; caso e forcar seed a zero.
    mov     r2, #( RAND_MAX_L >> 0 ) & 0xFF
    movt    r2, #( RAND_MAX_L >> 8 ) & 0xFF
    cmp     r0, r2
    bne     rand_save_seed
    mov     r3, #( RAND_MAX_H >> 0 ) & 0xFF
    movt    r3, #( RAND_MAX_H >> 8 ) & 0xFF
    cmp     r1, r3
    bne     rand_save_seed
    mov     r0, #0      ; Atribuir a seed o valor 0
    mov     r1, #0

rand_save_seed:
    ; Atualizar o valor de seed
    ldr     r2, seed_addr_rand
    str     r0, [r2, #0]
    str     r1, [r2, #2]
    ; Epilogo
    mov     r0, r1      ; Preparar o valor a devolver (16 MSb do valor)
    pop     pc

seed_addr_rand:
    .word   seed
    ; Definicao da variavel global do tipo uint32_t com valor inicial 1
    ; Como o processador P16 utiliza ordenacao little endian, o byte menos
    ; significativo (LSB) é armazenado no endereço mais baixo, logo, o valor
    ; um (0x00000001) é escrito primeiro
seed:
    .word   1, 0

; ============================  DATA ==================================
    .data
    .align 2

; ponteiros constantes para contornar limitação PC‑relative
inport_addr:       .word INPORT_ADDRESS
outport_addr:      .word OUTPORT_ADDRESS

tick_cnt_ptr:      .word tick_cnt
rng_seed:          .word 12345

tick_cnt:          .word 0          ; incrementado pela ISR
max_sides:         .word 4, 6, 8, 6
.align 1

anim_sequence:     ; 7Segment Display Animation Sequence (8style)
    .word 0x21
    .word 0x03
    .word 0x42
    .word 0x50
    .word 0x18
    .word 0x0C
    .word 0x44
    .word 0x60

hex27seg:
    .byte   0x3F, 0x06, 0x5B, 0x4F, 0x66, 0x6D, 0x7D, 0x07
    .byte   0x7F, 0x6F, 0x77, 0x7C, 0x39, 0x5E, 0x79, 0x71

; ===========================  STACK ==================================
    .stack
    .space  STACK_SIZE
stack_top:
