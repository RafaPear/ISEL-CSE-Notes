; Ficheiro:  lab04_ex2.S
; Descricao: Programa para a realizacao da 4a atividade laboratorial de
;            Arquitetura de Computadores.
; Autor:     Tiago M Dias (tiago.dias@isel.pt)
; Data:      09-05-2025

; Definicao dos valores dos simbolos utilizados no programa
;
	.equ	STACK_SIZE, 64                ; Dimensao do stack, em bytes

; *** Inicio de troco para completar ***
	.equ	ENABLE_EXTINT, 0x10         ;
; *** Fim de troco para completar ***

	.equ	OUTPORT_ADDRESS, 0xFFC0       ; Endereco do porto de saida

; *** Inicio de troco para completar ***
	.equ	FED_ADDRESS, 0xFF40           ; ??
; *** Fim de troco para completar ***

	.equ	VAR_INIT_VAL, 0               ; Valor inicial de var

; Seccao:    text
; Descricao: Guarda o codigo do programa
;
	.text
	b	program
	b	isr
program:
	ldr	sp, stack_top_addr
    b   main

stack_top_addr:
	.word	stack_top

; Rotina:    main
; Descricao: Inicializa a variável global `var` com zero, configura o periférico 
;            FED para permitir interrupções externas e entra num ciclo infinito 
;            onde lê o valor de `var` e o envia para o porto de saída.
; Entradas:  Nenhuma
; Saidas:    Nenhuma
; Efeitos:   Escreve no porto de saída; ativa interrupções externas; altera o conteúdo de `var`

main:
	mov	r0, #VAR_INIT_VAL
	ldr	r1, var_addr_main
	strb	r0, [r1, #0]
	bl	outport_write
	mov	r0, #FED_ADDRESS & 0xFF
	movt	r0, #(FED_ADDRESS >> 8) & 0xFF
	strb	r0, [r0, #0]
	mrs	r0, cpsr
	mov	r1, #ENABLE_EXTINT
	orr	r0, r0, r1
	msr	cpsr, r0
main_loop:
	ldr	r0, var_addr_main
	ldrb	r0, [r0, #0]
	bl	outport_write
	b	main_loop

var_addr_main:
	.word	var

; Rotina:    isr
; Descricao: Rotina de serviço de interrupção que incrementa o valor da variável global `var`.
; Entradas:  Nenhuma explícita (invocada por interrupção externa)
; Saidas:    Nenhuma
; Efeitos:   Atualiza `var`; limpa o pedido de interrupção no FED; usa e restaura r0 e r1
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

; Rotina:    outport_write
; Descricao: Escreve num porto de saida a 8 bits o valor passado como argumento.
;            Interface exemplo: void outport_write( uint8_t value );
; Entradas:  r0 - valor a escrever no porto de saida
; Saidas:    -
; Efeitos:   r1 - guarda o endereco do porto alvo da escrita
outport_write:
	mov	r1, #OUTPORT_ADDRESS & 0xFF
	movt	r1, #(OUTPORT_ADDRESS >> 8) & 0xFF
	strb	r0, [r1, #0]
	mov	pc, lr

; Seccao:    data
; Descricao: Guarda as variaveis globais
;
	.data
var:
	.space	1

; Seccao:    stack
; Descricao: Implementa a pilha com a dimensao definida pelo simbolo STACK_SIZE
;
	.stack
	.space	STACK_SIZE
stack_top:
