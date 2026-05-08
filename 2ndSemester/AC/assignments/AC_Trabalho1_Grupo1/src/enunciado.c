#include <stdint.h>
#include <stdio.h>

#define N 5
#define MAX_RAND_VALUE 0xFFFFFFFF

uint16_t result[N] = {17747, 2055, 3664, 15611, 9816};

uint32_t seed = 1;

uint32_t umull32(uint32_t M, uint32_t m) {
    int64_t M_ext = M;
    int64_t result = m;
    uint8_t p_1 = 0;

    for (uint16_t i = 0; i < 32; i++) {
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

void srand(uint32_t nseed) {
    seed = nseed;
}

uint16_t rand(void) {
    seed = (umull32(seed, 214013) + 2531011) % MAX_RAND_VALUE;
    return (seed >> 16);
}

int main(void) {

    uint8_t error = 0;
    uint16_t rand_number;
    uint16_t i;

    srand(5423);
    printf("Seed: %d\n", seed);
    for (i = 0; error == 0 && i < N; i++) {
        printf("umull32: %d\n", (umull32(seed, 214013) + 2531011));
        rand_number = rand();
        printf("Seed: %d\n", seed);
        if (rand_number != result[i]) {
            break;
        }
    }
    return 0;
}