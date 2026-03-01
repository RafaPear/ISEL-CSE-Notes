library ieee;
use ieee.std_logic_1164.all;

entity NANDL is
	port(
		A, B: in std_logic_vector (3 downto 0);
		O: out std_logic_vector (3 downto 0)	
	);
end NANDL;

architecture arc_nandl of NANDL is

begin

O(0) <= A(0) nand B(0);
O(1) <= A(1) nand B(1);
O(2) <= A(2) nand B(2);
O(3) <= A(3) nand B(3);

end arc_nandl;
		