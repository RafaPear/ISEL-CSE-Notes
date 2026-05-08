; Ficheiro:  TrabalhoProjeto.s
; Descricao: Programa para a realizacao do Trabalho de Projeto de Arquitetura de Computadores.
; Autores:   Gustavo Costa, Rafael Pereira
; Data:      27-05-2025
; ALL COMMENTS IN ENGLISH

; Initialization of constants
.equ    INPORT_ADDRESS, 0xFF80 ; Input port address
.equ    OUTPORT_ADDRESS, 0xFFC0 ; Output port address
.equ    FED_ADDRESS, 0xFF40 ; Falling edge detection address

.equ    ROLL_TIME, 10 ; Time seconds to mantain the rolling state
.equ    ANIM_fRAME_TIME, 10 ; Time in milliseconds for each animation frame
.equ    MAX_ANIMATIONS, 8 ; Maximum number of animations
.equ    TOTAL_SIDES, 4 ; Total number of sides for the die

.equ    DICE_MASK, 0x0C ; Mask for the die sides
.equ    ROLL_MASK, 0x01 ; Mask for the roll switch

.equ    STACK_SIZE, 64 ; Stack size in bytes

	.text
	b	program
	b	.		; Reservado para a ISR
program:
	ldr	sp, stack_top_addr
	b	main
    b   isr

stack_top_addr:
	.word	stack_top

main:
    bl game ; Start the game

    
; Rotina:    isr
; Descricao: *** Para completar ***
; Entradas:  *** Para completar ***
; Saidas:    *** Para completar ***
; Efeitos:   *** Para completar ***
isr:
	push	r1
	push	r0
	mov	r0, #FED_ADDRESS & 0xFF
	movt	r0, #(FED_ADDRESS >> 8) & 0xFF
	strb	r2, [r0, #0]
	ldr	r0, var_addr_isr
	ldrb	r1, [r0, #0]
	add	r1, r1, #1
	strb	r1, [r0, #0]
	pop	r0
	pop	r1
	movs	pc, lr

var_addr_isr:
	.word	var

game:
    push lr ; Save the link register
    push r4 ; Save r4 on the stack

    mov r4, #5 ; Initialize r4 to 0 (die index)
    mov r0, r4 ; Load the initial die index into r0
    bl outport_write ; Output the initial value to the display

game_loop:
    mov r0, r4 ; Load the previous die index into r0
    bl action ; Call the action function to roll the dice
    mov r4, r0 ; Store the new die index in r4
    bl inport_read ; Read the input port to check the roll switch state
    mov r1, #ROLL_MASK ; Load the roll switch mask
    and r0, r0, r1 ; Mask the input to get the roll switch state
    beq game_loop ; If not pressed, continue the game loop

    bl anim ; Start the 7SegmentDisplay animation
    mov r0, r4 ; Load the die index into r0
    bl roll ; Call the roll function to generate a random number
    bl outport_write ; Output the new random number to the display
    mov r0, #ROLL_TIME ; Load the roll time into r0
    ; TODO SACAR O TEMPO DO FED
    ;bl wait ; Wait for a while before the next action
    b game_loop ; Repeat the game loop


; ------------------------------------------------------------
; Roll a dice action
; -------------------------------------------------------------
; Input:
; r0 - Previous die index (0 for d4, 1 for d6, 2 for d8, 3 for d12)
; Returns:
; r0 - New die index (0 for d4, 1 for d6, 2 for d8, 3 for d12)
action:
    push lr ; Save the link register
    push r4 ; Save r4 on the stack
    mov r4, r0 ; Store the previous die index in r4

    bl inport_read ; Read the input port
    mov r5, #DICE_MASK ; Load the die sides mask
    and r0, r0, r5 ; Mask the input to get the roll switch state
    lsr r0, r0, #2
    cmp r0, r4
    beq action_end ; If the roll switch state is the same, return
    mov r4, r0
    bl roll ; Call the roll function to generate a new die index
    bl outport_write ; Output the new die index to the display
    mov r0, r4 ; Update r4 with the new die index

action_end:
    pop r4 ; Restore r4
    pop pc ; Return from action

; ------------------------------------------------------------
; Roll
; ------------------------------------------------------------
; Input:
; r0 - Die index (0 for d4, 1 for d6, 2 for d8, 3 for d12)
; Returns:
; r0 - Random number generated based on the die sides
roll:
    push lr
    bl getDiceMax ; Get the maximum sides for the die
    bl randGen ; Generate a random number
    pop pc ; Return from roll

; ------------------------------------------------------------
; Get the maximum sides for the die based on the index
; Input:
; r0 - Die index (0 for d4, 1 for d6, 2 for d8, 3 for d12)
; Returns:
; r0 - Maximum sides for the die
getDiceMax:
    push r4 ; Save r4 on the stack
    mov r4, #TOTAL_SIDES ; Load the total number of sides for the die
    cmp r0, r4 ; Compare the input with the total sides
    bge getDiceMax_end ; If input is greater or equal, return
    lsl r0, r0, #1 ; Double the input value (to match the die sides)
    mov r4, #max_sides
    ldr r0, [r4, r0] ; Load the maximum sides for the die
getDiceMax_end:
    pop r4 ; Restore r4
    mov pc, lr ; Return from getDiceMax


randGen:
    mov pc, lr ; Return from randGen

; ---------------------------------------
;       7SegmentDisplay Animation
; ---------------------------------------
; r0 - Current animation step
; r4 - Index for the animation sequence
; r5 - Counter for the animation loop
; r6 - Pointer to the animation sequence
; r7 - Temporary register for comparison
; ---------------------------------------

anim:
    push lr        ; salva o endereço de retorno
    push r0        ; mantém valor do chamador em r0
    push r4
    push r5
    push r6
    push r7

    ldr  r6, anim_sequence   ; ponteiro para a sequência
    mov  r4, #0               ; índice da sequência
    mov  r5, #0               ; contador do loop
    b    anim_loop

anim_loop:
    mov r0, #ANIM_fRAME_TIME ; tempo de cada frame
    ; TODO VER TIKS TO FED TODO
    ;bl   wait                ; espera o tempo do frame
    ldr r0, [r6, r4]         ; r0 ← passo actual
    bl   outport_write        ; envia para o display

    ; AJEITAR AQUI PARA O FED
    add  r5, r5, #1           ; ++contador
    mov  r7, #ROLL_TIME
    lsl  r7, r7, #10          ; seg → iterações
    cmp  r5, r7
    beq  anim_break           ; tempo esgotado?

    add  r4, r4, #2        ; próximo passo
    mov  r7, #MAX_ANIMATIONS << 1
    add  r7, r7, #1           ; r7 = número de passos na sequência
    cmp  r4, r7
    blt  anim_loop            ; ainda na sequência
    mov  r4, #0               ; reinicia índice
    b    anim_loop

anim_break:
    pop  r7
    pop  r6
    pop  r5
    pop  r4
    pop  r0                   ; repõe r0 original
    pop  pc                   ; retorna do anim

ptr_anim_sequence:
    .word   anim_sequence


;-------------------------------------------------------
; Input Port Routines
;-------------------------------------------------------
inport_read:
	mov	r1, #INPORT_ADDRESS & 0xFF
	movt	r1, #(INPORT_ADDRESS >> 8) & 0xFF
	ldrb	r0, [r1, #0]
	mov	pc, lr

;-------------------------------------------------------
; Output Port Routines
;-------------------------------------------------------
outport_write:
	mov	r1, #OUTPORT_ADDRESS & 0xFF
	movt	r1, #(OUTPORT_ADDRESS >> 8) & 0xFF
	strb	r0, [r1, #0]
	mov	pc, lr

.data

    var:
	    .space	1
    digitos_tab:
        .byte 0x06, 0x5B, 0x4F, 0x66, 0x6D, 0x7D, 0x07, 0x7F, 0x6F ; 1, 2, 3, 4, 5, 6, 7, 8, 9
        .align 1

    max_sides: ; Define the max number of die sides for each die
        .word 4, 6, 8, 12

    anim_sequence: ; 7Segment Display Animation Sequence (8style)
        .word 0x01
        .word 0x02
        .word 0x40
        .word 0x10
        .word 0x08
        .word 0x04
        .word 0x40
        .word 0x20

;-------------------------------------------------------
; Stack
;-------------------------------------------------------
;
	.stack
	.space	STACK_SIZE
stack_top:
