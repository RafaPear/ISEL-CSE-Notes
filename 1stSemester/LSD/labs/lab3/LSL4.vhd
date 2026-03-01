library ieee;
use ieee.std_logic_1164.all;

entity LSL4 is
	port(
	A: in std_logic_vector(3 downto 0);
	O: out std_logic_vector(3 downto 0)
	);
end LSL4;

architecture arq_LSL4 of LSL4 is

begin 
O(0) <= '0';
O(1) <= A(0);
O(2) <= A(1);
O(3) <= A(2);
end arq_LSL4;
