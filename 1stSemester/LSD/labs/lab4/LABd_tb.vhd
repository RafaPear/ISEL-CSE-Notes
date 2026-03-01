library ieee;
use ieee.std_logic_1164.all;

entity LABd_tb is
end entity;

architecture LABd_tb_arch of LABd_tb is

component LABd
port(
	dataIn: in std_logic_vector(3 downto 0);
	RESET: in std_logic;
	CLK: in std_logic;
	PL: in std_logic;
	CE: in std_logic;
	clear: in std_logic;
	CountValue: out std_logic_vector(3 downto 0);
	SEG7: out std_logic_vector(7 downto 0); -- dpgfedcba
	TC: out std_logic);
end component;

-- UUT signals
signal CLK_TB: std_logic := '0';
signal PL_TB, CE_TB, TC_TB, CLR_TB, RESET_TB: std_logic;
signal DATA_IN_TB, COUNT_VALUE_TB: std_logic_vector(3 downto 0);
signal SEG7_TB: std_logic_vector(7 downto 0);

constant MCLK_PERIOD: time := 20 ns;
constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;
constant CLK_PERIOD: time := 20 ns;

begin

	CLK_TB <= not CLK_TB after MCLK_HALF_PERIOD;
	UUT: LABd port map(	
				dataIn => DATA_IN_TB, 
				RESET	=> RESET_TB,
				CLK => CLK_TB, 
				PL => PL_TB, 
				CE => CE_TB, 
				clear => CLR_TB, 
				CountValue => COUNT_VALUE_TB, 
				SEG7 => SEG7_TB, 
				TC => TC_TB);
	
stimulus: process 
begin
	RESET_TB <= '1';	
	wait for CLK_PERIOD;
	RESET_TB <= '0';	

	-- Regista no contador o valor 4
	CE_TB <= '1';
	PL_TB <= '1';
	DATA_IN_TB <= "0100";
	CLR_TB <= '0';
	wait for CLK_PERIOD;
		
	-- Decrementa o contador até dar uma volta completa (termina no valor 4)
	PL_TB <= '0';
	wait for CLK_PERIOD * 16;
	
	-- Inibe o contador de contar durante dois períodos de clock
	CE_TB <= '0';
	wait for CLK_PERIOD*2;
	
	-- Decrementa até atingir TC para o valor de contagem 4
	CE_TB <= '1';
	DATA_IN_TB <= "0110";
	wait for CLK_PERIOD * 4;
	
	-- Carrega contador com o valor 6 em situação de TC
	PL_TB <= '1';
	wait for CLK_PERIOD;
	-- Faz clear ao display de 7 segmentos
	PL_TB <= '0';
	CLR_TB <= '1';

	wait;
		
		
end process;

end architecture;