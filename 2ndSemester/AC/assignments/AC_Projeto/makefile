FILE_NAME = teste

$(FILE_NAME).hex: $(FILE_NAME).s
	p16as $(FILE_NAME).s

run: $(FILE_NAME).hex
	cmd /c start p16dbg.exe
	p16sim -c sdp16_config.txt -i $(FILE_NAME).hex

sim: $(FILE_NAME).hex
	p16sim -c sdp16_config.txt -i $(FILE_NAME).hex

dbg: $(FILE_NAME).hex
	p16dbg $(FILE_NAME).hex