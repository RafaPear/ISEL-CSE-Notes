seed = 5423
max_rand = 4294967295
    
# def mod(a, b):
#     while a >= b:
#         a -= b
#     return a

def mod(a, b):
    while a - b >= 0:  # Em vez de "a >= b", usamos a subtração diretamente
        a = a - b  # Apenas operações básicas
    return a

def lsr(value, shift):
    return (value % (1 << 32)) >> shift 

def rand():
    global seed
    seed = mod((seed * 214013 + 2531011), max_rand)
    # seed = ((seed * 214013 + 2531011) % max_rand)
    return lsr(seed, 16)

print(rand())
print(rand())
print(rand())
print(rand())
