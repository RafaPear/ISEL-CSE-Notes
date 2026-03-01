; -----------------------------------------------------------------------------
; Ficheiro:  lab01.S
; Descricao: Codigo de suporte a realizacao da 1a atividade laboratorial de AC.
; Autor:     Tiago M Dias (tiago.dias@isel.pt)
; Data:      11-03-2022
; -----------------------------------------------------------------------------

	; r0 guarda o valor da variavel M
	; r1 guarda o valor da variavel m
	; r2 guarda o valor da variavel p
	; r3 e utilizado para guardar valores temporariamente

	mov	r2, #0
while:
	mov	r3, #0
	cmp	r3, r1
	bhs	while_end
	add	r2, r2, r0
	sub	r1, r1, #1
	b	while
while_end:
	b	.
