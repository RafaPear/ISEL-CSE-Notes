/*
void *my_memmove(void *dst, const void *src, size_t len);

--------- CONSTRAINTS ---------

This function has 2 cases to handle:
1) If the source and destination memory areas do not overlap, it should copy
	 the data from src to dst in a forward manner (from the beginning to the end).

2) If the source and destination memory areas do overlap, it should copy
	 the data from src to dst in a backward manner (from the end to the beginning).

--------- IDEA ---------
To determine if the memory areas overlap, we can compare the pointers and 
lengths. If dst is less than src or if dst is greater than or equal 
to src + len, we can safely copy forward. Otherwise, we need to copy backward.

|-------| <- src
|       |
|       |
|       |
|-------| <- src + len
|-------| <- dst
|       |
|       |
|       |
|-------| <- dst + len
/\
||
THIS IS FINE FOWARD

|-------| <- dst
|       |
|       |
|       |
|-------| <- dst + len
|-------| <- src
|       |
|       |
|       |
|-------| <- src + len
/\ 
||
THIS IS ALSO FINE FOWARD

|-------| <- src
|       |
|-------| <- dst
|       |
|-------| <- src + len
|       | 
|-------| <- dst + len
|       |
|       |
|  ...  |
/\ 
||
THIS IS OVERLAPPING, NEED TO COPY BACKWARD

So: 
	if (dst < src || dst >= src + len) {
		// Copy forward
		for (size_t i = 0; i < len; i++) {
			((char *)dst)[i] = ((const char *)src)[i];
		}
	} else {
		// Copy backward
		for (size_t i = len; i > 0; i--) {
			((char *)dst)[i - 1] = ((const char *)src)[i - 1];
		}
	}

--------- SIMPLIFY TO SINGLE INSTRUCTION ---------

We can use the 'rep movsb' instruction to perform the memory copy operation. This instruction copies a block of memory from the source to the destination
using the %rsi (source index), %rdi (destination index), and %rcx (count) registers.
*/
	.text
	.globl my_memmove

# Registers:
# %rdi - destination pointer (dst) - arg 1
# %rsi - source pointer (src) - arg 2
# %rdx - length (len) - arg 3
# %rcx - count register for rep instruction
# %rax - return value
my_memmove:
        # Save original dst pointer for return value
        mov     %rdi, %rax        # rax = dst (save for return)
        
        # Handle zero length case
        test    %rdx, %rdx        # check if len == 0
        jz      my_memmove_end    # if len == 0, return immediately
        
        # Move len to %rcx for rep instruction
        mov     %rdx, %rcx        # rcx = len
        
        # Calculate src + len and store in %r8
        # - This is a niche use of lea to perform addition with 3 operands
        #   that our teacher taught us.
        lea     (%rsi,%rdx,1), %r8   # r8 = src + len
        
        # Check if dst < src
        cmp     %rsi, %rdi        # compare dst and src
        jb      copy_forward      # if dst < src, jump to copy_forward
        
        # Check if dst >= src + len
        cmp     %r8, %rdi         # compare dst and (src + len)
        jae     copy_forward      # if dst >= src + len, jump to copy_forward
        
        # Overlapping case: copy backward
        # Set direction flag for backward copy
        std                       # set direction flag
        
        # Point to last byte of src and dst
        lea     -1(%rsi,%rdx,1), %rsi  # rsi = src + len - 1
        lea     -1(%rdi,%rdx,1), %rdi  # rdi = dst + len - 1
        
        rep     movsb             # copy bytes backward
        
        cld                       # clear direction flag
        jmp     my_memmove_end

copy_forward:
        # Forward copy (direction flag already clear)
        rep     movsb             # copy bytes forward

my_memmove_end:
        ret                       # return original dst (in rax)

	.section	.note.GNU-stack
