library ieee;
use ieee.std_logic_1164.all;

entity LSR is
	port(
		A: in std_logic_vector (3 downto 0);
		O: out std_logic_vector (3 downto 0);
		CY: out std_logic
	);
end LSR;

architecture arc_lsr of LSR is

begin
	CY <= A(0);
	O(0) <= A(1);
	O(1) <= A(2);
	O(2) <= A(3);
	O(3) <= '0';
end arc_lsr;