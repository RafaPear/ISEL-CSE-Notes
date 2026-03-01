LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY int7seg IS
PORT(	d : IN STD_LOGIC_vector(3 downto 0);
		dOut: out std_logic_vector(7 downto 0)
		);
END int7seg;

ARCHITECTURE logicFunction OF int7seg IS

signal Ndout : std_logic_vector(7 downto 0);

BEGIN

dOut <= 		"11000000" when d = "0000" else
			"11111001" when d = "0001" else
			"10100100" when d = "0010" else
			"10110000" when d = "0011" else
			"10011001" when d = "0100" else
			"10010010" when d = "0101" else
			"10000010" when d = "0110" else
			"11111000" when d = "0111" else
			"10000000" when d = "1000" else
			"10011000" when d = "1001" else
			"10001000" when d = "1010" else
			"10000011" when d = "1011" else
			"11000110" when d = "1100" else
			"10100001" when d = "1101" else
			"10000110" when d = "1110" else
			"10001110";

END logicFunction;