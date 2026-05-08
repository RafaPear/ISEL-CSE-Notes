library ieee;
use ieee.std_logic_1164.all;

entity Equality is
	port(
        A: in std_logic_vector(3 downto 0);
        B: in std_logic_vector(3 downto 0);
        O: out std_logic
		);
end Equality;

architecture arc_equality of EQUALITY is

signal out_temp: std_logic_vector(3 downto 0);

begin
out_temp(0) <= A(0) xor B(0);
out_temp(1) <= A(1) xor B(1);
out_temp(2) <= A(2) xor B(2);
out_temp(3) <= A(3) xor B(3);

O <= not (out_temp(0) or out_temp(1) or out_temp(2) or out_temp(3));
end arc_equality;