/*
int invoke_and_test(void *result, void (*function)(), int n_args, ...);

A função invoke_and_test invoca a função apontada pelo seu segundo parâmetro - function.
A esta função são passados os argumentos recebidos na zona de parâmetros variáveis.
O número de argumentos é definido pelo seu terceiro parâmetro - n_args.
O valor de retorno da função invocada é depositado na variável apontada por pelo seu primeiro parâmetro - result.
A função invoke_and_test devolve um valor diferente de zero se algum dos registos calee saved for modificado.
O valor identifica o registo:
	1 - rbp
	2 - rbx
	3 - r15
	4 - r14
	5 - r13
	6 - r12
*/
/*
	-------------
	|    arg8   |
	-------------
	|    arg7   |
	-------------
	|    rip    |  <-- rbp + 56
	-------------
	|    rdi    |  <-- rbp + 48
	-------------
	|    r12    |  <-- rbp + 40
	-------------
	|    r13    |  <-- rbp + 32
	-------------
	|    r14    |  <-- rbp + 24
	-------------
	|    r15    |  <-- rbp + 16
	-------------
	|    rbx    |  <-- rbp + 8
	-------------
	|    rbp    |  <-- rbp
	-------------
	|           |
*/
		.text
	.global	invoke_and_test
invoke_and_test:
	push	%rdi
	push	%r12
	push	%r13
	push	%r14
	push	%r15
	push	%rbx
	push	%rbp
	mov	%rsp, %rbp
	mov	$7, %rax
invoke_and_test_while:
	cmp	%rdx, %rax
	ja	invoke_and_test_while_end
	push	8(%rbp, %rdx, 8)
	dec	%rdx
	jmp	invoke_and_test_while
invoke_and_test_while_end:
	mov	%rsi, %rax	# function
	mov	%rcx, %rdi
	mov	%r8, %rsi
	mov	%r9, %rdx
	mov	 4 * 8 + 32(%rbp), %rcx
	mov	 5 * 8 + 32(%rbp), %r8
	mov	 6 * 8 + 32(%rbp), %r9
	call	*%rax
	mov	%rbp, %rsp
	mov	48(%rsp), %rdx
	mov	%rax, (%rdx)
	mov	$6, %rax
	cmp	%r12, 40(%rsp)
	jne	invoke_and_test_return
	dec	%rax
	cmp	%r13, 32(%rsp)
	jne	invoke_and_test_return
	dec	%rax
	cmp	%r14, 24(%rsp)
	jne	invoke_and_test_return
	dec	%rax
	cmp	%r15, 16(%rsp)
	jne	invoke_and_test_return
	dec	%rax
	cmp	%rbx, 8(%rsp)
	jne	invoke_and_test_return
	dec	%rax
	cmp	%rbp, %rsp	# dá sempre igual
	jne	invoke_and_test_return
	dec	%rax
invoke_and_test_return:
	pop	%rbp
	pop	%rbx
	pop	%r15
	pop	%r14
	pop	%r13
	pop	%r12
	pop	%rdi
	ret

	.section .note.GNU-stack

