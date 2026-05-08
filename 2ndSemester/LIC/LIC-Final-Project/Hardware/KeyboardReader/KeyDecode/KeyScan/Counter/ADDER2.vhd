library ieee;
use ieee.std_logic_1164.all;

entity ADDER2 is
	port (
		A: in std_logic_vector (1 downto 0);
		B: in std_logic_vector (1 downto 0);
		CIN: in std_logic;
		S: out std_logic_vector (1 downto 0);
		COUT: out std_logic
	);
	end ADDER2;
	
architecture arch_adder of ADDER2 is
component fulladder
	port ( 
		A: in std_logic;
		B: in std_logic;
		Cin: in std_logic;
		R: out std_logic;
		Cout: out std_logic
	);
end component;

signal C1: std_logic;

begin 
	FA0: fulladder port map (
		A => A(0), 
		B => B(0), 
		Cin => CIN, 
		R => S(0), 
		Cout => C1
	);

	FA1: fulladder port map (
		A => A(1), 
		B => B(1), 
		Cin => C1, 
		R => S(1), 
		Cout => COUT
	);
end arch_adder;		