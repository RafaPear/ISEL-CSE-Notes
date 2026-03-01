/*
int greater_than(const void *a, const void *b) {
	return *(int *)a > (long)b;
}
*/
	.text
	.global	less_than
less_than:
	mov	(%rdi), %rdi
	mov	(%rdi), %eax
	cmp	%esi, %eax
	setl	%al
	movzbl	%al, %eax
	mov	$-1, %rdi
	mov	$-2, %rsi
	mov	$-3, %rdx
	mov	$-4, %rcx
	mov	$-5, %r8
	mov	$-6, %r9
	mov	$-7, %r10
	mov	$-8, %r11
	ret

	.section .note.GNU-stack
