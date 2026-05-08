; Ficheiro:  TrabalhoProjeto.s
; Descricao: Programa para a realizacao do Trabalho de Projeto de Arquitetura de Computadores.
; Autores:   Gustavo Costa, Rafael Pereira
; Data:      27-05-2025

.equ    LAST_SIDES, 0
.equ    TIMER_CNT, 1
.equ    STATE, 2
.equ    COUNTER_1S, 3
.equ    COUNTER_10S, 4
.equ    ENABLE_EXTINT, 0x10
.equ    EN_MSK, 0x0001
.equ    INPORT_ADDRESS, 0xFF80
.equ    OUTPORT_ADDRESS, 0xFFC0
.equ    FED_ADDRESS, 0xFF40
.equ    VAR_INIT_VAL, 0
.equ    SIDES_MASK, 0x0C
.equ    READ_SIDES_SHIFT, 2
.equ    MASK_BYTE, 0xFF

.text
    b   program
    b   isr

program:
    ldr sp, stack_top_addr
    b   start

stack_top_addr:
    .word stack_top

start:
    ldr r6, data_start_addr ; Carrega o endereço de data_start
    ldrb r0, [r6, #0] ; Carrega valor inicial
    bl read_sides 
    strb r0, [r6, #LAST_SIDES]
    bl gerador_num
    bl show_num

data_start_addr:
    .word data_start

main:
    mrs r0, cpsr
    mov r1, #ENABLE_EXTINT
    orr r0, r0, r1
    msr cpsr, r0

main_loop:
    ldr r0, [r5]
    bl roll_falling_edge_unblocked
    mov r7, #0
    cmp r0, r7
    beq main_loop

    bl read_sides
    strb r0, [r6, #LAST_SIDES]
    mov r0, #1
    strb r0, [r6, #STATE]
    mov r0, #0
    strb r0, [r6, #TIMER_CNT]

    bl fx_visual
    b main_loop

;-------------------------------------------------------
; Leitura do número de lados
;-------------------------------------------------------
read_sides:
    mov r2, #SIDES_MASK
    and r0, r0, r2
    lsr r0, r0, #READ_SIDES_SHIFT
    ldr r1, sides_die_addr
    ldrb r0, [r1, r0]
    mov pc, lr

sides_die_addr:
    .word sides_die

;-------------------------------------------------------
; Ler o porto de entrada
;-------------------------------------------------------
inport_read:
	ldr	r0, inport_addr
	ldr	r0, [r0, #0]
	mov	pc, lr
;-------------------------------------------------------
; Detecção de transição descendente
;-------------------------------------------------------
roll_falling_edge_unblocked:
    push r1
    push r2
    push r3
    push lr

    ldr r0, inport_addr
    ldr r0, [r0]
    mov r7, #EN_MSK & 0xFF
    movt r7, #EN_MSK >> 8
    and r1, r0, r7
    ldr r2, roll_last_addr
    ldr r3, [r2]
    str r1, [r2]
    mov r0, #0
    and r3, r3, r3
    bzs roll_trg_unblocked_ret
    and r1, r1, r1
    bzc roll_trg_unblocked_ret
    mov r0, #1

roll_trg_unblocked_ret:
    pop r3
    pop r2
    pop r1
    pop pc

roll_last_addr:
    .word roll_last

inport_addr:
    .word INPORT_ADDRESS 
;-------------------------------------------------------
; Gerador pseudoaleatório // O stor vai dar o código do gerador //
;-------------------------------------------------------
gerador_num:
    ldrb r3, [r6, #LAST_SIDES]
    ldr r1, [r4]
    add r1, r1, #3
    str r1, [r4]
    mov r2, r1

mod_loop:
    cmp r2, r0
    blo mod_done
    sub r2, r2, r0
    b mod_loop

mod_done:
    add r2, r2, #1
    mov r0, r2

    mov r7, #12
    cmp r0, r7
    bne gerador_num_end

    ldr r1, [r4]
    mov r2, r1

mod6_loop:
    mov r7, #6
    cmp r2, r7
    blo mod6_done
    sub r2, r2, #6
    b mod6_loop

mod6_done:
    add r2, r2, #1
    mov r0, r2

gerador_num_end:
    mov pc, lr
;-------------------------------------------------------
; Mostrar número no display
;-------------------------------------------------------
show_num:
    push r1
    push r2
    push lr
    sub r0, r0, #1
    ldr r1, digitos_tab_addr
    ldrb r2, [r1, r0]
    ldr r1, outport_addr
    str r2, [r1]
    pop r2
    pop r1
    pop pc

digitos_tab_addr:
    .word digitos_tab
;-------------------------------------------------------
; Animação
;-------------------------------------------------------
fx_visual:
    push r0
    push r1
    push r2
    push r3
    push lr

    mov r3, #0
    ldr r1, seq_tab_addr

fx_loop:
    ldrb r2, [r1, r3]
    ldr r0, outport_addr
    str r2, [r0]

    mov r0, #50
    bl wait_xms

    add r3, r3, #1
    mov r7, #8
    cmp r3, r7
    blo fx_loop

    pop r3
    pop r2
    pop r1
    pop r0
    pop pc

seq_tab_addr:
    .word seq_tab

outport_addr:
    .word OUTPORT_ADDRESS

;-------------------------------------------------------
; Espera de X × 125ms
;-------------------------------------------------------
wait_xms:
    push r1
    push r2
    mov r1, r0

wait_outer:
    mov r2, #0xFF

wait_inner:
    sub r2, r2, #1
    bne wait_inner
    sub r1, r1, #1
    bne wait_outer
    pop r2
    pop r1
    mov pc, lr
;-------------------------------------------------------
; ISR do FED
;-------------------------------------------------------
isr:
    push r0
    push r1
    push r2
    push lr
    
    ldrb r0, [r6, #COUNTER_1S]
    add r0, r0, #1
    strb r0, [r6, #COUNTER_1S]
    
    mov r7, #10
    cmp r0, r7
    blo isr_end
    
    mov r0, #0
    strb r0, [r6, #COUNTER_1S]
    
    ldrb r1, [r6, #COUNTER_10S]
    add r1, r1, #1
    strb r1, [r6, #COUNTER_10S]
    
    mov r7, #10
    cmp r1, r7
    blo isr_end
    
    mov r1, #0
    strb r1, [r6, #COUNTER_10S]
    bl gerador_num
    bl show_num
    
isr_end:
    pop r2
    pop r1
    pop r0
    pop pc
    movs pc, lr

;-------------------------------------------------------
; Secção .data
;-------------------------------------------------------
.data

sides_die:
    .byte 0x04, 0x06, 0x08, 0x0C ; Número de lados do dado: 4, 6, 8, 12

roll_last:
    .byte 0x00 ; Último estado do dado (para detecção de transição descendente)

data_start:
    .byte 0x04   ; LAST_SIDES
    .byte 0x00   ; TIMER_CNT
    .byte 0x00   ; STATE
    .byte 0x00   ; COUNTER_1S
    .byte 0x00   ; COUNTER_10S
    .align 1

;4_die:
;    .byte 0x01, 0x02, 0x03, 0x04 ; Dado de 4 lados: 1, 2, 3, 4

;6_die:
;    .byte 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 ; Dado de 6 lados: 1, 2, 3, 4, 5, 6

;8_die:
;    .byte 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 ; Dado de 8 lados: 1, 2, 3, 4, 5, 6, 7, 8

;12_die:
;    .byte 0x01, 0x01, 0x02, 0x02, 0x03, 0x03 ; Dado de 12 lados: 1, 1, 2, 2, 3, 3
;    .byte 0x04, 0x04, 0x05, 0x05, 0x06, 0x06 ; 4, 4, 5, 5, 6, 6

digitos_tab:
    .byte 0x06, 0x5B, 0x4F, 0x66, 0x6D, 0x7D, 0x07, 0x7F, 0x6F ; 1, 2, 3, 4, 5, 6, 7, 8, 9
    .align 1

seq_tab:
    .byte 0x01, 0x02, 0x40, 0x10, 0x08, 0x04, 0x40, 0x20 ; Efeito luminoso em '8'

;-------------------------------------------------------
; Stack
;-------------------------------------------------------
stack_top:
    .space 64
