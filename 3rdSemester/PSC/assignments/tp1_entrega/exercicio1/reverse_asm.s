/*
int64_t reverse(uint64_t value)

--------- IDEA ----------

swap odd and even bits
v = ((v >> 1) & 0x5555555555555555) | ((v & 0x5555555555555555) << 1);
swap consecutive pairs
v = ((v >> 2) & 0x3333333333333333) | ((v & 0x3333333333333333) << 2);
swap nibbles ...
v = ((v >> 4) & 0x0F0F0F0F0F0F0F0F) | ((v & 0x0F0F0F0F0F0F0F0F) << 4);
swap bytes
v = ((v >> 8) & 0x00FF00FF00FF00FF) | ((v & 0x00FF00FF00FF00FF) << 8);
swap 2-byte long pairs
v = ( v >> 16 & 0x0000FFFF0000FFFF) | ((v & 0x0000FFFF0000FFFF) << 16);
swap 4-byte long pairs
v = ( v >> 32 & 0x00000000FFFFFFFF) | ((v & 0x00000000FFFFFFFF) << 32);
return v;

--------- IMPORTANT NOTE ----------
based on: https://graphics.stanford.edu/~seander/bithacks.html#ReverseParallel

--------- HELPER FUNCTION ----------
Optimize to use as few instructions as possible.
swap odd and even bits
uint64_t tempA, tempB;

        tempA   = v
        tempA >>= shift
        tempA  &= mask

        tempB   = v
        tempB  &= mask
        tempB <<= shift

        tempA |= tempB
        v = tempA

        return v;
*/

.equ MASK_1  , 0x5555555555555555
.equ MASK_2  , 0x3333333333333333
.equ MASK_4  , 0x0F0F0F0F0F0F0F0F
.equ MASK_8  , 0x00FF00FF00FF00FF
.equ MASK_16 , 0x0000FFFF0000FFFF
.equ MASK_32 , 0x00000000FFFFFFFF

.equ SHIFT_1 , 1
.equ SHIFT_2 , 2
.equ SHIFT_4 , 4
.equ SHIFT_8 , 8
.equ SHIFT_16, 16
.equ SHIFT_32, 32

	.text
	.global	reverse

# registers
# %rdi - input value v
# %rsi - mask
# %rcx - shift
# %rax - return value
reverse:
        mov     $MASK_1, %rsi   # mask = 0x5555555555555555
        mov     $SHIFT_1, %cl   # shift = 1
        call    reverse_util

        mov     $MASK_2, %rsi   # mask = 0x3333333333333333
        mov     $SHIFT_2, %cl   # shift = 2
        call    reverse_util

        mov     $MASK_4, %rsi   # mask = 0x0F0F0F0F0F0F0F0F
        mov     $SHIFT_4, %cl   # shift = 4
        call    reverse_util

        mov     $MASK_8, %rsi   # mask = 0x00FF00FF00FF00FF
        mov     $SHIFT_8, %cl   # shift = 8
        call    reverse_util

        mov     $MASK_16, %rsi  # mask = 0x0000FFFF0000FFFF
        mov     $SHIFT_16, %cl  # shift = 16
        call    reverse_util

        mov     $MASK_32, %rsi  # mask = 0x00000000FFFFFFFF
        mov     $SHIFT_32, %cl  # shift = 32
        call    reverse_util

        mov     %rdi, %rax      # return v;
	ret

# registers
# %rdi - input value v - arg 1
# %rsi - mask 
# %rcx - shift
# %rax - tempA
# %rdx - tempB

# This function does not follow the standard calling convention
# It is only used internally by the reverse function above and
# the registers chosen are made to optimize the code size and speed.
# It returns the modified v value in %rdi.
reverse_util:
        # ------------ Left Side Part ------------
        mov %rdi, %rax          # tempA   = v
        shr %cl,  %rax          # tempA >>= shift
        and %rsi, %rax          # tempA  &= mask

        # ------------ Right Side Part ------------
        mov %rdi, %rdx          # tempB   = v
        and %rsi, %rdx          # tempB  &= mask
        shl %cl,  %rdx          # tempB <<= shift

        # ----------------- Merge -----------------
        or  %rdx, %rax          # tempA |= tempB
        mov %rax, %rdi          # v = tempA
        ret

        .section	.note.GNU-stack
        