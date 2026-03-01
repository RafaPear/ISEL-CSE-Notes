library ieee;
use ieee.std_logic_1164.all;

entity MUX is
	port(
		A, B: in std_logic_vector (3 downto 0);
		S: in std_logic;
		Y: out std_logic_vector (3 downto 0)
	);
end MUX;

architecture arc_mux of MUX is

begin
	Y(0) <= (not S and A(0)) or (S and B(0));
	Y(1) <= (not S and A(1)) or (S and B(1));
	Y(2) <= (not S and A(2)) or (S and B(2));
	Y(3) <= (not S and A(3)) or (S and B(3));
end arc_mux;