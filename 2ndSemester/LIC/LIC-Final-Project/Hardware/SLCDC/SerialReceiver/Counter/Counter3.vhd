library ieee;
use ieee.std_logic_1164.all;

entity Counter3 is
    port(
        RESET: in std_logic;
        clr: in std_logic;
        CLK: in std_logic;
        Q: out std_logic_vector(2 downto 0)
    );
end Counter3;

architecture arc_counter3 of Counter3 is
component ADDER3 is
    port (
		A: in std_logic_vector (2 downto 0);
		B: in std_logic_vector (2 downto 0);
		CIN: in std_logic;
		S: out std_logic_vector (2 downto 0);
		COUT: out std_logic
	);
end component;

component REG3 is
    port(	
		D: in std_logic_vector(2 downto 0);
		RESET: in std_logic;
        SET: in std_logic;
		EN: in std_logic;
		CLK: in std_logic;
		Q: out std_logic_vector(2 downto 0)
	);
end component;

signal temp_S: std_logic_vector(2 downto 0);
signal temp_Q: std_logic_vector(2 downto 0);
signal clr_temp: std_logic;
begin
    clr_temp <= reset or clr;

    ADDER3_inst: ADDER3 port map(
        A => "000",
        B => temp_Q,
        CIN => '1',
        S => temp_S,
        COUT => open
    );

    REG3_inst: REG3 port map(
        D => temp_S,
        RESET => clr_temp,
        SET => '0',
        EN => '1',
        CLK => CLK,
        Q => temp_Q
    );

    Q <= temp_Q;
end arc_counter3;