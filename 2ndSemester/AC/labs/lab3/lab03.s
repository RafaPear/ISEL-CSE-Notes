; Ficheiro:  lab03.s
; Descricao: Programa para a realização da 3a atividade laboratorial de
;            Arquitetura de Computadores.
; Autor:     Tiago M Dias (tiago.dias@isel.pt)
; Data:      29-04-2025

; Definicao dos valores dos simbolos utilizados no programa
;
	.equ	STACK_SIZE, 64                ; Dimensao do stack, em bytes

	.equ	ALL_ONES, 0xFFFF              ; Palavra com todos os bits a 1
	.equ	ALL_ZEROS, 0x0000             ; Palavra com todos os bits a 0

	.equ	INPORT_ADDRESS, 0xFF80        ; Endereço do inport
	.equ	OUTPORT_ADDRESS, 0xFFC0       ; Endereço do outport

; Seccao:    text
; Descricao: Guarda o código do programa
;
	.text
	b	program
	b	.		; Reservado para a ISR
program:
	ldr	sp, stack_top_addr
	b	main

stack_top_addr:
	.word	stack_top

; Rotina:    main
; Descricao: Escreve no Outport o valor do inport
; Entradas:  -
; Saidas:    -
; Efeitos:   r0
main:
	mov	r0, #ALL_ONES & 0xFF
	bl	outport_write
	mov	r0, #ALL_ZEROS & 0xFF
	bl	outport_write
loop:
	bl	inport_read
	bl	outport_write
	b	loop

; Rotina:    inport_read
; Descricao: Lê para o r0 o valor do inport
; Entradas:  -
; Saidas:    r0 - Contém o valor do inport
; Efeitos:   r1 - Contém o endereço da parte baixa do inport
inport_read:
	mov	r1, #INPORT_ADDRESS & 0xFF
	movt	r1, #(INPORT_ADDRESS >> 8) & 0xFF
	ldrb	r0, [r1, #0]
	mov	pc, lr

; Rotina:    outport_write
; Descricao: Escreve o valor de r0 no porto de saída
; Entradas:  r0 - Parâmetro para escrita
; Saidas:    -
; Efeitos:   r1 - Contém a parte baixa do endereço do porto de saída
outport_write:
	mov	r1, #OUTPORT_ADDRESS & 0xFF
	movt	r1, #(OUTPORT_ADDRESS >> 8) & 0xFF
	strb	r0, [r1, #0]
	mov	pc, lr

; Rotina:    sleep
; Descricao: *** Para completar ***
; Entradas:  *** Para completar ***
; Saidas:    *** Para completar ***
; Efeitos:   *** Para completar ***
sleep:
	and	r0, r0, r0
	beq	sleep_end
sleep_outer_loop:
	mov	r1, #0x3E
	movt	r1, #0x03
sleep_inner_loop:
	sub	r1, r1, #1
	bne	sleep_inner_loop
	sub	r0, r0, #1
	bne	sleep_outer_loop
sleep_end:
	mov	pc, lr

; Seccao:    data
; Descricao: Guarda as variáveis globais
;
;	.data

; Seccao:    stack
; Descricao: Implementa a pilha com a dimensao definida pelo simbolo STACK_SIZE
;
	.stack
	.space	STACK_SIZE
stack_top:
