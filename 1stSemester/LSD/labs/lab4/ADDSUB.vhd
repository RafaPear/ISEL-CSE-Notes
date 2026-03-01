library ieee;
use ieee.std_logic_1164.all;

entity ADDSUB is
	port (
		A: in std_logic_vector (3 downto 0);
		B: in std_logic_vector (3 downto 0);
		Cbi: in std_logic;
		OP: in std_logic;
		R: out std_logic_vector (3 downto 0);
		B3: out std_logic;
		CBo: out std_logic
	);
	end ADDSUB;
	
architecture arch_addsub of ADDSUB is
component ADDER is
	port (
		A: in std_logic_vector (3 downto 0);
		B: in std_logic_vector (3 downto 0);
		C0: in std_logic;
		S: out std_logic_vector (3 downto 0);
		C4: out std_logic
	);
end component;
	
signal Cox, Cix: std_logic;
signal Bx: std_logic_vector (3 downto 0);

begin 
	A0: ADDER port map (
		A => A, 
		B => Bx, 
		C0 => Cix, 
		S => R, 
		C4 => Cox
	);
	
	Cix <= Cbi xor OP;
	Bx(0) <= B(0) xor OP;
	Bx(1) <= B(1) xor OP;
	Bx(2) <= B(2) xor OP;
	Bx(3) <= B(3) xor OP;
	Cbo <= Cox xor OP;
	B3 <= Bx(3);

end arch_addsub;		