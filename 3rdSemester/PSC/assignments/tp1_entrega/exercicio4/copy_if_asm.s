/*
size_t copy_if(void *dst, void *src, size_t src_size, size_t elem_size,
				int (*predicate)(const void *, const void *), const void *context) {
	char *iter, *dst_iter = dst;
	char *last = (char *)src + src_size * elem_size;
	for (iter = src; iter < last ; iter += elem_size)
		if (predicate(iter, context)) {
			memcpy(dst_iter, iter, elem_size);
			dst_iter += elem_size;
		}
	return (dst_iter - (char*)dst) / elem_size;
}
*/
	.text
	.global	copy_if

copy_if:
    pushq %rbp
    movq  %rsp, %rbp

    pushq %rbx
    pushq %r12
    pushq %r13
    pushq %r14
    pushq %r15

    subq $32, %rsp                # allocate stack space for locals

    movq %rdi, %r14    # dst original
    movq %rdi, %r11    # dst_iter
    movq %rsi, %r10    # iter
    movq %rcx, %r12    # elem_size
    movq %r8,  %r15    # predicate
    movq %r9,  %rbx    # context

    testq %r12, %r12
    jz .return_zero

    testq %rdx, %rdx
    jz .return_zero

    movq %rdx, %rax
    imulq %r12, %rax
    addq %r10, %rax
    movq %rax, %r13    # last

.loop:
    cmpq %r13, %r10
    jge .done_loop

    movq %r10, -64(%rbp)          # save iter (in local stack area, away from saved regs)
    movq %r11, -56(%rbp)          # save dst_iter

    movq %r10, %rdi
    movq %rbx, %rsi
    call *%r15

    movq -64(%rbp), %r10          # restore iter
    movq -56(%rbp), %r11          # restore dst_iter

    testl %eax, %eax
    jz .skip_copy

    movq %r10, %rsi
    movq %r11, %rdi
    movq %r12, %rcx
    rep movsb
    addq %r12, %r11

.skip_copy:
    addq %r12, %r10
    jmp .loop

.done_loop:
    movq %r11, %rax
    subq %r14, %rax
    xorq %rdx, %rdx
    divq %r12        # rax = numero de elementos copiados

    addq $32, %rsp
    popq %r15
    popq %r14
    popq %r13
    popq %r12
    popq %rbx
    popq %rbp
    ret

.return_zero:
    xorq %rax, %rax
    addq $32, %rsp
    popq %r15
    popq %r14
    popq %r13
    popq %r12
    popq %rbx
    popq %rbp
    ret

	.section	.note.GNU-stack

