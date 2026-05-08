library ieee;
use ieee.std_logic_1164.all;

entity FULLADDER is
	port (
	A: in std_logic;
	B: in std_logic;
	Cin: in std_logic;
	R: out std_logic;
	Cout: out std_logic
	);
end FULLADDER;

architecture logicfulladder of FULLADDER is
begin 
	R <= A xor B xor Cin;
	Cout <= (A and B) or ((A xor B) and Cin);
end logicfulladder;