.text
    ldr r0, var2_addr
    loop:
        ldrb r1, [ r0, # 0 ]
        add r1, r1, # 1
        strb r1, [ r0, # 0 ]
        b loop

var2_addr:
    .word var2 ; 10

    .data
    var1:
        .byte 0xAC ; 12
    var2:
        .byte 126 ; 13
    var3:
        .word 0x2022 ; 14
