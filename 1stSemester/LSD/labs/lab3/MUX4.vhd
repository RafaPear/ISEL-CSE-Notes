library ieee;
use ieee.std_logic_1164.all;

entity MUX4 is
	port(
	A, B: in std_logic_vector(3 downto 0);
	S: in std_logic;
	O: out std_logic_vector(3 downto 0)
	);
end MUX4;

architecture arq_MUX4 of MUX4 is

begin 
O(0) <= (not S AND A(0)) OR (S AND B(0));
O(1) <= (not S AND A(1)) OR (S AND B(1));
O(2) <= (not S AND A(2)) OR (S AND B(2));
O(3) <= (not S AND A(3)) OR (S AND B(3));
end arq_MUX4;
