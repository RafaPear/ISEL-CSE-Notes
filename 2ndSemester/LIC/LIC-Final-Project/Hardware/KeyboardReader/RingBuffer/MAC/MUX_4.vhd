library ieee;
use ieee.std_logic_1164.all;

entity MUX_4 is
	port(
		Se: in std_logic;
		A: in std_logic_vector(3 downto 0);
		B: in std_logic_vector(3 downto 0);
		OUTPUT: out std_logic_vector(3 downto 0)
		);
end MUX_4;

architecture mux of MUX_4 is

begin
	OUTPUT(0) <= (Se and A(0)) or (not Se and B(0));
	OUTPUT(1) <= (Se and A(1)) or (not Se and B(1));
	OUTPUT(2) <= (Se and A(2)) or (not Se and B(2));
	OUTPUT(3) <= (Se and A(3)) or (not Se and B(3));
end mux;