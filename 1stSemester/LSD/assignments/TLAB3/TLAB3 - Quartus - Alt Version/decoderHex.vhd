LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity decoderHex IS
PORT (	bin: in std_logic_vector(7 downto 0);		
	clear : in std_logic;
	HEX0 : out std_logic_vector(7 downto 0);
	HEX1 : out std_logic_vector(7 downto 0);
	HEX2 : out std_logic_vector(7 downto 0)
);		
END decoderHex;

Architecture logicFuntion of decoderHex is

component int7seg
PORT(	d	:IN STD_LOGIC_vector(3 downto 0);
	dOut	: out std_logic_vector(7 downto 0)
	);
END component;

component bin2dec is
    Port ( bin : in  STD_LOGIC_VECTOR (7 downto 0);
           dec : out  STD_LOGIC_VECTOR (11 downto 0));
end component;

signal HEX0t: std_logic_vector(7 downto 0);
signal HEX1t: std_logic_vector(7 downto 0);
signal HEX2t: std_logic_vector(7 downto 0);
signal dec: std_logic_vector(11 downto 0);

BEGIN

HEX0 <= HEX0t when clear = '0' else "11111111";
HEX1 <= HEX1t when clear = '0' else "11111111";
HEX2 <= HEX2t when clear = '0' else "11111111";


U0: int7seg port map(dec(3 downto 0), HEX0t);
U1: int7seg port map(dec(7 downto 4), HEX1t);
U2: int7seg port map(dec(11 downto 8), HEX2t);

U3: bin2dec port map (bin => bin, dec => dec);
									
END logicFuntion;	