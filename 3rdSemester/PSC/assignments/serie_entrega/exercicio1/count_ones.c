
/*
Retorna o total de bits a 1 presentes em value
*/
int count_ones(int value)
{
    int counter = 0;
    while (value != 0)
    {
        if ((value & 1) == 1)
            counter++;
        value = (unsigned)value >> 1;
    }
    return counter;
}
