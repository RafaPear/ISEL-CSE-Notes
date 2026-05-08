library ieee;
use ieee.std_logic_1164.all;

entity FFD is
	port(
		CLK: in std_logic;
		RESET: in std_logic;
		SET: in std_logic;
		D: in std_logic;
		EN: in std_logic;
		Q: out std_logic
		);
end FFD;

architecture arc_FFD of FFD is
begin

Q <= '0' when RESET = '1' else '1' when SET = '1' else D when rising_edge(clk) and EN = '1';


end arc_FFD;