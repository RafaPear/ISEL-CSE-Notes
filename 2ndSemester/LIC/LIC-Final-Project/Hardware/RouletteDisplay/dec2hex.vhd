library ieee;
use ieee.std_logic_1164.all;

entity dec2hex is
port(	d 		: in std_logic_vector(4 downto 0);
		clear : in std_logic;
		dOut	: out std_logic_vector(7 downto 0)
		);
end dec2hex;

architecture structural OF dec2hex IS

signal Ndout : std_logic_vector(7 downto 0);

begin

NdOut <= "11000000" when d = "00000" else
			"11111001" when d = "00001" else
			"10100100" when d = "00010" else
			"10110000" when d = "00011" else
			"10011001" when d = "00100" else
			"10010010" when d = "00101" else
			"10000010" when d = "00110" else
			"11111000" when d = "00111" else
			"10000000" when d = "01000" else
			"10011000" when d = "01001" else
			"10001000" when d = "01010" else
			"10000011" when d = "01011" else
			"11000110" when d = "01100" else
			"10100001" when d = "01101" else
			"10000110" when d = "01110" else
			"10001110" when d = "01111" else
			"10111111" when d = "10000" else
			"11111100" when d = "10001" else
			"11011110" when d = "10010" else
			"11001111" when d = "10011" else
			"11100111" when d = "10100" else
			"11110011" when d = "10101" else
			"11111001" when d = "10110" else
			"11001001" when d = "10111" else
			"10110110" when d = "11000" else
			"11111110" when d = "11001" else
			"11011111" when d = "11010" else
			"11101111" when d = "11011" else
			"11110111" when d = "11100" else
			"11111011" when d = "11101" else
			"11111101" when d = "11110" else
			"11111111";

dout <= "11111111" when clear = '1' else Ndout;
			
end structural;