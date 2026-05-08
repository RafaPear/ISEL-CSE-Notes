; Ficheiro:  ac_2425v_tp1.s
; Descricao: Exemplo de solucao para o Trabalho Pratico 1 de Arquitetura de
;            Computadores do semestre de verao do ano letivo 2024/2025.
; Autor:     Tiago M Dias (tiago.dias@isel.pt)
; Data:      31-05-2025

; Definicao dos valores dos simbolos utilizados no programa
;
	.equ	STACK_SIZE, 64			; Dimensao do stack, em bytes

		.equ	RAND_MAX_L, 0xFFFF		; Corresponde ao maior valor inteiro
		.equ	RAND_MAX_H, 0xFFFF		; sem sinal codificavel com 32 bits

	.equ	N, 5


; Seccao:    text
; Descricao: Guarda o codigo do programa
;
	.text
	b	program
	b	.		; Reservado para a ISR
program:
	ldr	sp, stack_top_addr
	bl	main
	b	.

stack_top_addr:
	.word	stack_top

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
	push	r8
	push	r7
	push	r6
	push	r5
	push	r4

	; Iniciar p fazendo a extensao de sinal aos 16 MSb
	asr	r4, r3, #15
	mov	r5, r4
	; Inicia p_1
	mov	r6, #0
	; Implementacao do ciclo for
	mov	r7, #0	; Inicia i
umull32_loop:
	mov	r8, #32	; Avaliar o limite maximo de i
	cmp	r7, r8
	bhs	umull32_ret
	; Implementacao do if
	mov	r8, #1
	and	r8, r2, r8
	bzc	umull32_else
	mov	r8, #1
	cmp	r6, r8
	bne	umull32_loop_end
	add	r4, r4, r0	; Atualizar o valor de p
	adc	r5, r5, r1
	b	umull32_loop_end
umull32_else:
	; Implementacao otimizada do else
	mov	r8, #0
	cmp	r6, r8
	bne	umull32_loop_end
	sub	r4, r4, r0	; Atualizar o valor de p
	sbc	r5, r5, r1
umull32_loop_end:
	mov	r8, #1	; Definir o novo valor de p_1
	and	r6, r2, r8
	asr	r5, r5, #1
	rrx	r4, r4
	rrx	r3, r3
	rrx	r2, r2
	add	r7, r7, #1	; Incrementar i
	b	umull32_loop

umull32_ret:
	; Epilogo
	mov	r0, r2	; Preparar o valor a devolver
	mov	r1, r3

	pop	r4
	pop	r5
	pop	r6
	pop	r7
	pop	r8
	mov	pc, lr

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
	ldr	r2, seed_addr_srand
	str	r0, [r2, #0]
	str	r1, [r2, #2]
	mov	pc, lr

seed_addr_srand:
	.word	seed

; Rotina:    rand
; Descricao: Implementa um gerador congruencial linear (LCG) para gerar numeros
;            pseudo-aleatorios entre zero e RAND_MAX.
;            Interface exemplo: uint16_t rand( void );
; Entradas:  -
; Saidas:    R0 - O valor pseudo-aleatorio gerado
; Efeitos:   Altera o valor da variavel global seed
;            R0 - guarda valores temporariamente
;            R1 - guarda valores temporariamente
;            R2 - guarda valores temporariamente
;            R3 - guarda valores temporariamente
;
rand:
	; Prologo
	push	lr
	; Obter o valor atual de seed
	ldr	r2, seed_addr_rand
	ldr	r0, [r2, #0]
	ldr	r1, [r2, #2]
	; Calcular a multiplicacao a 32 bits
	mov	r2, #( 0x43FD >> 0 ) & 0xFF	; Carregar o valor 214013
	movt	r2, #( 0x43FD >> 8 ) & 0xFF
	mov	r3, #( 0x0003 >> 0 ) & 0xFF
	movt	r3, #( 0x0003 >> 8 ) & 0xFF
	bl	umull32
	; Calcular a adicao a 32 bits
	mov	r2, #( 0x9EC3 >> 0 ) & 0xFF	; Carregar o valor 2531011
	movt	r2, #( 0x9EC3 >> 8 ) & 0xFF
	mov	r3, #( 0x0026 >> 0 ) & 0xFF
	movt	r3, #( 0x0026 >> 8 ) & 0xFF
	add	r0, r0, r2
	adc	r1, r1, r3

    ; Nao e necessario implementar a divisao modulo, pois a operacao realizada
    ; com valores de 32 bits nunca ultrapassa o valor RAND_MAX (0xFFFFFFFF).
    ; No entanto, a operacao % RAND_MAX deve devolver 0 quando o novo valor de
    ; seed e exatamente igual a RAND_MAX. Assim, e suficiente verificar este
    ; caso e forcar seed a zero.
	mov	r2, #( RAND_MAX_L >> 0 ) & 0xFF
	movt	r2, #( RAND_MAX_L >> 8 ) & 0xFF
	cmp r0, r2
	bne rand_save_seed
	mov	r3, #( RAND_MAX_H >> 0 ) & 0xFF
	movt	r3, #( RAND_MAX_H >> 8 ) & 0xFF
	cmp r1, r3
	bne rand_save_seed
	mov r0, #0  	; Atribuir a seed o valor 0
	mov r1, #0

rand_save_seed:
	; Atualizar o valor de seed
	ldr	r2, seed_addr_rand
	str	r0, [r2, #0]
	str	r1, [r2, #2]
	; Epilogo
	mov	r0, r1	; Preparar o valor a devolver (16 MSb do valor)
	pop	pc

seed_addr_rand:
	.word	seed

; Rotina:    main
; Descricao: Implementa o programa de teste para a rotina rand.
;            Interface exemplo: int main( void );
; Entradas:  -
; Saidas:    -
; Efeitos:   R0 - guarda valores temporariamente
;            R1 - guarda valores temporariamente
;            R2 - guarda valores temporariamente
;            R3 - guarda valores temporariamente
;            R4 - mapeia a variavel error
;            R5 - guarda o valor da iteracao do ciclo for (i)
;
main:
	; Prologo
	push	lr
	push	r5
	push	r4

	mov	r4, #0	; Iniciar error
	; Iniciar seed com o valor 5423
	mov	r0, #( 5423 >> 0 ) & 0xFF	; Carregar o valor 5423 (tipo uint32_t)
	movt	r0, #( 5423 >> 8 ) & 0xFF
	mov	r1, #0
	bl	srand
	; Implementacao do ciclo for
	mov	r5, #0	; Iniciar i
main_loop:
	; Avaliar se error ainda toma o valor zero
	mov	r0, #0
	cmp	r4, r0
	bne	main_ret
	; Avaliar se o valor de i ainda e inferior a N
	; Nota: Considera-se que N pode tomar qualquer valor no dominio de i
	mov	r0, #( N >> 0 ) & 0xFF
	movt	r0, #( N >> 8 ) & 0xFF
	cmp	r5, r0
	bhs	main_ret
	; Obter um novo numero pseudo-aleatorio 
	bl	rand
	; Avaliar se o numero pseudo-aleatorio corresponde ao valor esperado
	ldr	r1, result_addr
	lsl	r2, r5, #1
	ldr	r3, [r1, r2]
	cmp	r0, r3
	beq	main_skip_if
	mov	r4, #1	; Atribuir o valor 1 a error
main_skip_if:
	add	r5, r5, #1
	b	main_loop
main_ret:
	; Epilogo
	mov	r0, #0	; Preparar o valor a devolver
	pop	r4
	pop	r5
	pop	pc

result_addr:
	.word	result

; Seccao:    data
; Descricao: Guarda as variaveis globais
;
	.data
	; Definicao do array com os resultados para teste, do tipo uint16_t
result:
	.word	17747, 2055, 3664, 15611, 9816

	; Definicao da variavel global do tipo uint32_t com valor inicial 1
	; Como o processador P16 utiliza ordenacao little endian, o byte menos
	; significativo (LSB) é armazenado no endereço mais baixo, logo, o valor
	; um (0x00000001) é escrito primeiro
seed:
	.word	1, 0

; Seccao:    stack
; Descricao: Implementa a pilha com a dimensao definida pelo simbolo STACK_SIZE
;
	.stack
	.space	STACK_SIZE
stack_top:
