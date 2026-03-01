.equ INPORT_ADDR, 0xA000   ; Endereço do porto de entrada
.equ OUTPORT_ADDR, 0xA001  ; Endereço do porto de saída

.text
    b main        ; Início do programa

main:
    ; Inicializa o stack pointer
    ldr sp, stack_top_addr

loop:
    ; Lê o valor atual do INPORT
    ldr r0, inport_addr
    ldrb r1, [r0]      ; Lê o byte do INPORT
    
    ; Extrai os bits 0-2 (valor entre 0 e 7)
    mov r0, #0x07
    and r1, r1, r0
    
    ; Cria a máscara com todos os bits a '1'
    mov r2, #0xFF
    
    ; Cria a máscara com um bit a '0' na posição indicada
    mov r3, #1
lsl_loop:
    sub r1, r1, #1
    blt lsl_stop
    lsl r3, r3, #1         ; Desloca o bit para a posição correta
    b lsl_loop
lsl_stop:
    eor r2, r2, r3         ; Inverte o bit na posição (1->0)
    
    ; Escreve o resultado no OUTPORT
    ldr r0, outport_addr
    strb r2, [r0]
    
    ; Repete o processo indefinidamente
    b loop

; Endereços dos dispositivos
inport_addr:
    .word INPORT_ADDR

outport_addr:
    .word OUTPORT_ADDR

stack_top_addr:
    .word stack_top

stack_top:                 ; Topo da pilha
