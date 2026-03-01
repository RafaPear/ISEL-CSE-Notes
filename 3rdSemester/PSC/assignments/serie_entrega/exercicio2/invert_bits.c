/*
Retorna o valor original com os nbits a partir de position invertidos.
- Note que: nbits a -1 inverte tudo a partir de position ate ao final.

Cria uma mascara que consiste apenas nos bits a inverter a 1 e
aplica-a ao value invertido. De seguida aplica a mascara invertida
ao value (o inverso do passo anterior). Por ultimo soma as duas partes
resultando no valor desejado.
*/
int invert_bits(int value, int position, int nbits)
{
  if (position + nbits > 32) nbits = 32 - position; // limita para não ultrapassar 32 bits
  int mask = ~((~0) << nbits) << position;
  return value ^ mask;
}
