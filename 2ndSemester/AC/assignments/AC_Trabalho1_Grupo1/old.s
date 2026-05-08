mov r7, #0  ; r7 = error = 0
    mov r4, #0  ; r4 = i = 0

    ; srand(5423)
    ; 5423 = 0x152F (16 bits)
    mov r0, #0x2F   ; parte inferior
    movt r0, #0x15  ; parte superior
    mov r1, #0  ; parte superior (só zeros)
    bl srand

main_for_loop:
    ; for (i = 0; error == 0 && i < n; i++)
    ; error == 0 && i < n
    mov r5, #0 ; r5 = 0
    cmp r7, r5  ; error == 0
    bne main_end_for_loop   ; se error != 0, dá break

    ; load no n para a comparação (i < n)
    mov r5, #n
    cmp r4, r5  ; i < n
    bge main_end_for_loop   ; se i >= n, dá break

    bl rand     ; rand() -> retorna em r0 = rand_number

    ; result[i] = rand_number (result é um array de uint16_t [2 bytes cada elemento] -> offset = i * 2)
    ldr r5, result_addr
    lsl r6, r4, #1 ; r6 = i * 2
    ldr r6, [r5, #2] ; r6 = result[i]

    cmp r0, r6 ; compara result[i] com rand_number
    bne error
    add r4, r4, #1  ; i++
    b main_for_loop

result_addr:
    .word result

error:
    mov r7, #1  ; error = 1 se forem diferentes

main_end_for_loop:
    mov r0, r7  ; r0 = error
    