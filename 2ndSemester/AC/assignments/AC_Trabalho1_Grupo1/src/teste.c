#include <stdint.h>
#include <stdio.h>

uint32_t umull32(uint32_t M, uint32_t m) {
    int64_t M_ext = M;
    int64_t result = m;
    uint8_t p_1 = 0;

    for (uint16_t i = 0; i < 32; i++) {
        printf("result & 0x1: %d\n", (result & 0x1));
        if ((result & 0x1) == 0 && p_1 == 1) {
            result += M_ext << 32;
        } else if ((result & 0x1) == 1 && p_1 == 0) {
            result -= M_ext << 32;
        }
        p_1 = result & 0x1;
        result >>= 1;
    }
    return result;
}

int main(void) {
    uint32_t M = 2;
    uint32_t m = 2;
    uint32_t result = umull32(M, m);
    printf("result: %d\n", result);
    return 0;
}