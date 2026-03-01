library IEEE;
use IEEE.std_logic_1164.all;

entity Controller_tb is
end entity;

architecture behavioral of Controller_tb is

component Controller
	port(	
		CLK: in std_logic;
		Step: in std_logic;
		Start: in std_logic;
		Cout: in std_logic;
		SQR: in std_logic_vector(7 downto 0);
		Sum: in std_logic_vector(7 downto 0);
		StepOut: out std_logic;
		Reset: out std_logic;
		Enable: out std_logic;
		Cy: out std_logic;
        R: out std_logic_vector(7 downto 0)
		);
end component;

-- UUT signals
signal CLK_tb, Step_tb, Start_tb, Cout_tb: std_logic;
signal SQR_tb, Sum_tb: std_logic_vector(7 downto 0);

signal StepOut_tb, Reset_tb, Enable_tb, Cy_tb: std_logic;
signal R_tb: std_logic_vector(7 downto 0);

begin
	
	UUT: Controller port map(
		CLK => CLK_tb,
		Step => Step_tb,
		Start => Start_tb,
		Cout => Cout_tb,
		SQR => SQR_tb,
		Sum => Sum_tb,
		StepOut => StepOut_tb,
		Reset => Reset_tb,
		Enable => Enable_tb,
		Cy => Cy_tb,
        R => R_tb
		);
	
	stimulus_generator: process
	begin

		CLK_tb <= '0';
		Step_tb <= '0';
		Start_tb <= '1';
		Cout_tb <= '0';
		SQR_tb <= "00000000";
		Sum_tb <= "00000000";

		wait for 10 ns;
		--R = Sum
		CLK_tb <= '1';
		wait for 10 ns;

		CLK_tb <= '0';
		Step_tb <= '0';
		Start_tb <= '0';
		Cout_tb <= '0';
		SQR_tb <= "00000000";
		Sum_tb <= "00000000";

		wait for 10 ns;
		--R = Sum, Reset = 1
		CLK_tb <= '1';
		wait for 10 ns;
		
		CLK_tb <= '0';
		Start_TB <= '1';
		Cout_tb <= '1';
		SQR_tb <= "00000010";
		Sum_tb <= "11111111";

		wait for 10 ns;
		--R = Sum, Cy = 1
		CLK_tb <= '1';
		wait for 10 ns;

		CLK_tb <= '0';
		Step_TB <= '1';

		wait for 10 ns;
		--R = SQR, Cy = 1
		CLK_tb <= '1';
		wait;

	end process;


End behavioral;