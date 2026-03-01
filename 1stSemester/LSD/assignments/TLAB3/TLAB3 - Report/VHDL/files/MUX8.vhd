library ieee;
use ieee.std_logic_1164.all;

entity MUX8 is
	port(
		A, B: in std_logic_vector (7 downto 0);
		S: in std_logic;
		Y: out std_logic_vector (7 downto 0)
	);
end MUX8;

architecture arc_mux of MUX8 is

begin
	Y(0) <= (not S and A(0)) or (S and B(0));
	Y(1) <= (not S and A(1)) or (S and B(1));
	Y(2) <= (not S and A(2)) or (S and B(2));
	Y(3) <= (not S and A(3)) or (S and B(3));
	Y(4) <= (not S and A(4)) or (S and B(4));
	Y(5) <= (not S and A(5)) or (S and B(5));
	Y(6) <= (not S and A(6)) or (S and B(6));
	Y(7) <= (not S and A(7)) or (S and B(7));
	
end arc_mux;