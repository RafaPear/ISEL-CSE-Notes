; Ficheiro:  DadoEletronico.s
; Descricao: Programa para a realizacao do Trabalho de Projeto de Arquitetura de Computadores.
; Autores:   Gustavo Costa, Rafael Pereira
; Data:      19-05-2025

.equ    LAST_SIDES, 0
.equ    TIMER_CNT, 1
.equ    STATE, 2
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
    b   _start

stack_top_addr:
    .word stack_top

_start:
    mov r6, #data_ptr_base & 0xFF
    movt r6, #data_ptr_base >> 8
    ldr r6, [r6]

    bl read_sides
    strb r0, [r6, #LAST_SIDES]
    bl gerador_num
    bl show_num

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
    ldr r0, [r5]
    mov r7, #SIDES_MASK
    and r0, r0, r7
    lsr r0, r0, #READ_SIDES_SHIFT

    mov r7, #0
    cmp r0, r7
    beq lado_4

    mov r7, #1
    cmp r0, r7
    beq lado_6

    mov r7, #2
    cmp r0, r7
    beq lado_8

    b lado_12

lado_4:  mov r0, #4
         b read_sides_end

lado_6:  mov r0, #6
         b read_sides_end

lado_8:  mov r0, #8
         b read_sides_end

lado_12: mov r0, #12

read_sides_end:
    mov pc, lr

;-------------------------------------------------------
; Ler o porto de entrada
;-------------------------------------------------------
inport_read:
	mov	r0, #INPORT_ADDRESS
	ldr	r0, [r0, #0]
	mov	pc, lr

;-------------------------------------------------------
; Gerador pseudoaleatório
;-------------------------------------------------------
gerador_num:
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
    sub r0, r0, #1
    mov r1, #0x00
    movt r1, #0x02
    add r2, r1, r0
    ldrb r3, [r2]
    str r3, [r4]
    mov pc, lr

;-------------------------------------------------------
; Animação de rolagem
;-------------------------------------------------------
fx_visual:
    mov r0, #0

fx_loop:
    mov r7, #8
    cmp r0, r7
    bhs fx_end

    mov r1, #0x10
    movt r1, #0x02
    add r2, r1, r0
    ldrb r3, [r2]
    str r3, [r4]

    mov r1, #1
    bl wait_xms

    add r0, r0, #1
    b fx_loop

fx_end:
    mov pc, lr

;-------------------------------------------------------
; Deteção de transição descendente
;-------------------------------------------------------
roll_falling_edge_unblocked:
    ldr r0, [r5]
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
    mov pc, lr

roll_last_addr:
    .word roll_last

;-------------------------------------------------------
; Espera de X × 125ms
;-------------------------------------------------------
wait_xms:
    mov r1, #0
wait_outer:
    cmp r1, r0
    bhs wait_end

    mov r2, #0xFF
wait_inner:
    sub r2, r2, #1
    mov r7, #0
    cmp r2, r7
    bne wait_inner

    add r1, r1, #1
    b wait_outer

wait_end:
    mov pc, lr

;-------------------------------------------------------
; ISR do FED
;-------------------------------------------------------
isr:
    push r0
    push r1

    mov r6, #data_ptr_base
    ; falta alterar
    and r6, r6, #MASK_BYTE
    movt r6, #data_ptr_base >> 8
    ldr r6, [r6]

    ldrb r1, [r6, #TIMER_CNT]
    add r1, r1, #1
    strb r1, [r6, #TIMER_CNT]

    ldrb r1, [r6, #STATE]
    mov r7, #1
    cmp r1, r7
    bne check_delay

    ldrb r1, [r6, #TIMER_CNT]
    mov r7, #8
    cmp r1, r7
    blo end_isr

    mov r0, #0
    strb r0, [r6, #TIMER_CNT]
    mov r0, #2
    strb r0, [r6, #STATE]

    ldrb r0, [r6, #LAST_SIDES]
    bl gerador_num
    bl show_num
    b end_isr

check_delay:
    mov r7, #2
    cmp r1, r7
    bne end_isr

    ldrb r1, [r6, #TIMER_CNT]
    mov r7, #80
    cmp r1, r7
    blo end_isr

    mov r0, #0
    strb r0, [r6, #TIMER_CNT]
    strb r0, [r6, #STATE]

end_isr:
    pop r1
    pop r0
    movs pc, lr

;-------------------------------------------------------
; Dados e Ponteiros
;-------------------------------------------------------
data_ptr_base:
    .word data_start

.data
roll_last:
    .byte 0x00

data_start:
    .byte 0xFF   ; LAST_SIDES
    .byte 0x00   ; TIMER_CNT
    .byte 0x00   ; STATE

4_die:
    .byte 0x01, 0x02, 0x03, 0x04

6_die:
    .byte 0x01, 0x02, 0x03, 0x04, 0x05, 0x06

8_die:
    .byte 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08

12_die:
    .byte 0x01, 0x01, 0x02, 0x02, 0x03, 0x03
    .byte 0x04, 0x04, 0x05, 0x05, 0x06, 0x06

digitos_tab:
    .byte 0x06, 0x5B, 0x4F, 0x66, 0x6D, 0x7D, 0x07, 0x7F, 0x6F
    .align 1

seq_tab:
    .byte 0x01, 0x02, 0x40, 0x10, 0x08, 0x04, 0x40, 0x20 ; Efeito em '8'

;-------------------------------------------------------
; Stack
;-------------------------------------------------------
stack_top:
    .space 64
