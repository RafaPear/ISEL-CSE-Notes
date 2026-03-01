library ieee;
use ieee.std_logic_1164.all;

entity decoderHex is
	port (
		A: in std_logic_vector(3 downto 0);		
		clear: in std_logic;
		HEX0: out std_logic_vector(7 downto 0)
		);		
end decoderHex;

architecture logicFuntion of decoderHex is

component int7seg
	port(
		d: in std_logic_vector(3 downto 0);
		dOut: out std_logic_vector(7 downto 0)
		);
end component;


signal HEX0t: std_logic_vector(7 downto 0);

begin

HEX0 <= HEX0t when clear = '0' else "11111111";

U0: int7seg port map(A, HEX0t);
									
end logicFuntion;	