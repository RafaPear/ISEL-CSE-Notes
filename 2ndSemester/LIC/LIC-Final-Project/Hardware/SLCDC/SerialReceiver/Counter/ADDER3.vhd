library ieee;
use ieee.std_logic_1164.all;

entity ADDER3 is
	port (
		A: in std_logic_vector (2 downto 0);
		B: in std_logic_vector (2 downto 0);
		CIN: in std_logic;
		S: out std_logic_vector (2 downto 0);
		COUT: out std_logic
	);
	end ADDER3;
	
architecture arch_adder of ADDER3 is
component fulladder
	port ( 
		A: in std_logic;
		B: in std_logic;
		Cin: in std_logic;
		R: out std_logic;
		Cout: out std_logic
	);
end component;

signal C1,C2: std_logic;

begin 
	FA2: fulladder port map (
		A => A(0), 
		B => B(0), 
		Cin => CIN, 
		R => S(0), 
		Cout => C1
	);

	FA3: fulladder port map (
		A => A(1), 
		B => B(1), 
		Cin => C1, 
		R => S(1), 
		Cout => C2
	);
	
	FA4: fulladder port map (
		A => A(2), 
		B => B(2), 
		Cin => C2, 
		R => S(2), 
		Cout => COUT
	);
end arch_adder;		