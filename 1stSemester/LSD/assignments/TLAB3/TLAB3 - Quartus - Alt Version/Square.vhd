library ieee;
use ieee.std_logic_1164.all;

entity Square is
	port (
		A: in std_logic_vector (3 downto 0);
		clk: in std_logic;
		Step: in std_logic;
		Reset: in std_logic;
		S: out std_logic_vector (7 downto 0)
	);
end Square;

architecture arch_square of Square is
component ADDER8
	port (
		A: in std_logic_vector (7 downto 0);
		B: in std_logic_vector (7 downto 0);
		Cin: in std_logic;
		S: out std_logic_vector (7 downto 0);
		Cout: out std_logic
	);
end component;

component Counter
	port(
        D: in std_logic_vector(3 downto 0);
        PL: in std_logic;
        CE: in std_logic;
        CLK: in std_logic;
        TC: out std_logic
    );
end component;

component Reg8
	port(	
		CLK : in std_logic;
		RESET : in std_logic;
		SET : in std_logic;
		D : in std_logic_vector(7 downto 0);
		EN : in std_logic;
		Q : out std_logic_vector(7 downto 0)
		);
end component;

component MUX8
    port(
		A, B: in std_logic_vector (7 downto 0);
		S: in std_logic;
		Y: out std_logic_vector (7 downto 0)
	);
end component;

component PulseSignal
	port (
		A: in std_logic;
        CLK: in std_logic;
		S: out std_logic
	);
end component;


signal S_ADDER8, S_Reg8, S_MUX8: std_logic_vector(7 downto 0);
signal S_Step_and_notTC, temp_TC, S_Step_Pulsed, S_Not_Step_Pulsed: std_logic;
signal S_Step_and_Not_Step_Pulsed: std_logic;

begin
	S_Step_and_Not_Step_Pulsed <= S_Not_Step_Pulsed and Step;
	S_Not_Step_Pulsed <= not S_Step_Pulsed;
	S_Step_and_notTC <= S_Step_and_Not_Step_Pulsed and not temp_TC;

	UPulseSignal_1: PulseSignal port map(
		A => Step,
        CLK => CLK,
		S => S_Step_Pulsed
    );

	UCounter_0: Counter port map(
        D => A,
        PL => S_Step_Pulsed,
        CE => Step,
        CLK => clk,
        TC => temp_TC
    );

	UReg_1: Reg8 port map(
		CLK => clk,
		RESET => Reset,
		SET => '0',
		D => S_ADDER8,
		EN => S_Step_and_notTC,
		Q => S_Reg8
	);

    UADDER8_0: ADDER8 port map(
		A(0) => A(0),
		A(1) => A(1),
		A(2) => A(2),
		A(3) => A(3),
		A(4) => '0',
		A(5) => '0',
		A(6) => '0',
		A(7) => '0',
		B => S_Reg8,
		Cin => '0',
		S => S_ADDER8
    );

	S <= S_Reg8;


end arch_square;