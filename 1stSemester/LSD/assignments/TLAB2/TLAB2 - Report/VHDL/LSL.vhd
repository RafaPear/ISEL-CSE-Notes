library ieee;
use ieee.std_logic_1164.all;

entity LSL is
	port(
		A: in std_logic_vector (3 downto 0);
		O: out std_logic_vector (3 downto 0);
		CY: out std_logic
	);
end LSL;

architecture arc_lsl of LSL is

begin
	O(0) <= '0';
	O(1) <= A(0);
	O(2) <= A(1);
	O(3) <= A(2);
	CY <= A(3);
end arc_lsl;