library ieee;
use ieee.std_logic_1164.all;

entity LUcirc_tb is
end LUcirc_tb;

architecture teste of LUcirc_tb is
component LUcirc
	port(
	X, Y: in std_logic_vector(3 downto 0);
	Op: in std_logic;
	R: out std_logic_vector(3 downto 0)
	);
end component;

signal X, Y : std_logic_vector(3 downto 0);
signal Op : std_logic;
signal R : std_logic_vector(3 downto 0);

begin

U0 : LUcirc port map (X => X, Y => Y,
							 Op => Op, R => R);
process
begin
X <= "0101"; Y <= "1100"; Op <= '0';
wait for 10ns;
X <= "0101"; Y <= "1100"; Op <= '1';

wait;
end process;
end teste;
							 