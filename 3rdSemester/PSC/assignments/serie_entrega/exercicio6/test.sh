#!/bin/sh

#echo "------------------------------------------------------------------------"
#echo " - Testes com erros nos argumentos do programa"
#echo

chmod +x mysort_ref

./mysort -x 2> /dev/null
if [ $? -ne 0 ]; then
	echo "[00] - OK"
else
	echo "[00] - Error"
	exit 1
fi

./mysort -o  2> /dev/null
if [ $? -ne 0 ]; then
	echo "[01] - OK"
else
	echo "[01] - Error"
	exit 1
fi

./mysort -i nonexistent.txt -o nonexistent.sort.txt 2> /dev/null
if [ $? -ne 0 ]; then
	echo "[02] - OK"
else
	echo "[02] - Error"
	exit 1
fi

#echo "------------------------------------------------------------------------"
#echo " - Testes com redirecionamento"
#echo

./mysort_ref < smallfile.txt > smallfile.sort.ref.txt 2> /dev/null
./mysort < smallfile.txt >  smallfile.sort.txt 2> /dev/null

cmp  smallfile.sort.ref.txt smallfile.sort.txt
if [ $? -eq 0 ]; then
	echo "[10] - OK"
else
	echo "[10] - Error"
fi

read foo

rm smallfile.sort.ref.txt smallfile.sort.txt


#echo "------------------------------------------------------------------------"
#echo " - Testes com ficheiro médio"
#echo

./mysort_ref -i mediumfile.txt -o mediumfile.sort.ref.txt 2> /dev/null
./mysort -i mediumfile.txt -o  mediumfile.sort.txt 2> /dev/null

cmp  mediumfile.sort.ref.txt mediumfile.sort.txt
if [ $? -eq 0 ]; then
	echo "[20] - OK"
else
	echo "[20] - Error"
fi

rm mediumfile.sort.ref.txt mediumfile.sort.txt

./mysort_ref -i mediumfile.txt -o mediumfile.sort.ref.txt -c 2> /dev/null
./mysort -i mediumfile.txt -o mediumfile.sort.txt -c 2> /dev/null

cmp  mediumfile.sort.ref.txt mediumfile.sort.txt
if [ $? -eq 0 ]; then
	echo "[21] - OK"
else
	echo "[21] - Error"
fi

rm mediumfile.sort.ref.txt mediumfile.sort.txt

./mysort_ref -i mediumfile.txt -o mediumfile.sort.ref.txt -r 2> /dev/null
./mysort -i mediumfile.txt -o mediumfile.sort.txt -r 2> /dev/null

cmp  mediumfile.sort.ref.txt mediumfile.sort.txt
if [ $? -eq 0 ]; then
	echo "[22] - OK"
else
	echo "[22] - Error"
fi

rm mediumfile.sort.ref.txt mediumfile.sort.txt

./mysort_ref -i mediumfile.txt -o mediumfile.sort.ref.txt -cr 2> /dev/null
./mysort -i mediumfile.txt -o mediumfile.sort.txt -cr 2> /dev/null

cmp  mediumfile.sort.ref.txt mediumfile.sort.txt
if [ $? -eq 0 ]; then
	echo "[23] - OK"
else
	echo "[23] - Error"
fi

rm mediumfile.sort.ref.txt mediumfile.sort.txt

#echo "------------------------------------------------------------------------"
#echo " - Testes com ficheiro grande"
#echo

echo "O TESTE COM FICHEIRO GRANDE PODE DEMORAR MAIS DE 3 MINUTOS"

./mysort_ref -i bigfile.txt -o bigfile.sort.ref.txt 2> /dev/null
./mysort -i bigfile.txt -o  bigfile.sort.txt 2> /dev/null

cmp  bigfile.sort.ref.txt bigfile.sort.txt
if [ $? -eq 0 ]; then
	echo "[30] - OK"
else
	echo "[30] - Error"
fi

rm bigfile.sort.ref.txt bigfile.sort.txt
# wait enter
read foo