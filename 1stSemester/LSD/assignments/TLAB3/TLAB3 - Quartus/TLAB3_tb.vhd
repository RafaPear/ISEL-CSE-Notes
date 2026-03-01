library IEEE;
use IEEE.std_logic_1164.all;

entity TLAB3_tb is
end entity;

architecture behavioral of TLAB3_tb is

component TLAB3
	port(	
		CLK: in std_logic;
		Step: in std_logic;
		Start: in std_logic;
		Xi: in std_logic_vector(3 downto 0);
		Cy: out std_logic;
        R: out std_logic_vector(7 downto 0)
		);
end component;

-- UUT signals
signal clk_tb, Step_tb, Start_tb: std_logic;
signal Xi_tb: std_logic_vector(3 downto 0);

signal Cy_tb: std_logic;
signal R_tb: std_logic_vector(7 downto 0);

begin
	
	UUT: TLAB3 port map(
		CLK => clk_tb,
		Step => Step_tb,
		Start => Start_tb,
		Xi => Xi_tb,
		Cy => Cy_tb,
        R => R_tb
		);
	
	stimulus_generator: process
	begin
		clk_tb <= '0';
		Step_tb <= '0';
		Start_tb <= '1';
		Xi_tb <= "0010";

		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;
		clk_tb <= '0';
		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;

		clk_tb <= '0';
		Step_tb <= '1';

		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;
		clk_tb <= '0';
		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;

		clk_tb <= '0';
		Step_tb <= '0';

		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;
		clk_tb <= '0';
		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;

		clk_tb <= '0';
		Xi_tb <= "0001";

		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;
		clk_tb <= '0';
		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;

		clk_tb <= '0';
		Step_tb <= '1';

		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;
		clk_tb <= '0';
		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;

		clk_tb <= '0';
		Step_tb <= '0';

		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;
		clk_tb <= '0';
		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;

		clk_tb <= '0';
		Xi_tb <= "0100";

		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;
		clk_tb <= '0';
		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;

		clk_tb <= '0';
		Step_tb <= '1';

		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;
		clk_tb <= '0';
		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;

		clk_tb <= '0';
		Step_tb <= '0';

		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;
		clk_tb <= '0';
		wait for 10 ns;
		clk_tb <= '1';
		wait for 10 ns;

		wait;

	end process;


End behavioral;