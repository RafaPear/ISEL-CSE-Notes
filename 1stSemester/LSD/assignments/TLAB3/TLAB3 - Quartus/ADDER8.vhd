library ieee;
use ieee.std_logic_1164.all;

entity ADDER8 is
	port (
		A: in std_logic_vector (7 downto 0);
		B: in std_logic_vector (7 downto 0);
		Cin: in std_logic;
		S: out std_logic_vector (7 downto 0);
		Cout: out std_logic
	);
end ADDER8;
	
architecture arch_adder of ADDER8 is
component fulladder
	port ( 
		A: in std_logic;
		B: in std_logic;
		Cin: in std_logic;
		R: out std_logic;
		Cout: out std_logic
	);
end component;

signal C1, C2, C3, C4, C5, C6, C7: std_logic;

begin 
	FA0: fulladder port map (
		A => A(0), 
		B => B(0), 
		Cin => Cin, 
		R => S(0), 
		Cout => C1
	);

	FA1: fulladder port map (
		A => A(1), 
		B => B(1), 
		Cin => C1, 
		R => S(1), 
		Cout => C2
	);

	FA2: fulladder port map (
		A => A(2), 
		B => B(2), 
		Cin => C2, 
		R => S(2), 
		Cout => C3
	);

	FA3: fulladder port map (
		A => A(3), 
		B => B(3), 
		Cin => C3, 
		R => S(3), 
		Cout => C4
	);

	FA4: fulladder port map (
		A => A(4), 
		B => B(4), 
		Cin => C4, 
		R => S(4), 
		Cout => C5
	);

	FA5: fulladder port map (
		A => A(5), 
		B => B(5), 
		Cin => C5, 
		R => S(5), 
		Cout => C6
	);

	FA6: fulladder port map (
		A => A(6), 
		B => B(6), 
		Cin => C6, 
		R => S(6), 
		Cout => C7
	);

	FA7: fulladder port map (
		A => A(7), 
		B => B(7), 
		Cin => C7, 
		R => S(7), 
		Cout => Cout
	);

end arch_adder;		