#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <stdbool.h>

bool round_to_int(float fvalue, int *ivalue)
{
    unsigned int bits = *(int*)&fvalue;

    int sign = bits >> 31;
    int e = ((bits >> 23) & 0xFF) - 127;
    unsigned int mantissa = bits & 0x7FFFFF;

    if (e >= 31) return false; // overflow para int

    mantissa |= 1 << 23; // adiciona o bit implícito

    int shift = 23 - e;
    int integerPart;
    
    if (shift <= 0) {
        integerPart = mantissa << (-shift);
    } else {
        unsigned int integerMask = ~((1 << shift) - 1);
        integerPart = (mantissa & integerMask) >> shift;
        int floatingPart = (mantissa & ~integerMask) >> (shift - 1);
        if (floatingPart & 1) integerPart++;
    }
    
    if (sign) integerPart = -integerPart;
    *ivalue = integerPart;
    return true;
}