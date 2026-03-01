library ieee;
use ieee.std_logic_1164.all;

entity SquareSum is
	port (
		A: in std_logic_vector (7 downto 0);
		clk: in std_logic;
		Step: in std_logic;
		Reset: in std_logic;
		S: out std_logic_vector (7 downto 0);
		Cout: out std_logic
	);
end SquareSum;

architecture arch_squaresum of SquareSum is
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

component ADDER8
	port (
		A: in std_logic_vector (7 downto 0);
		B: in std_logic_vector (7 downto 0);
		Cin: in std_logic;
		S: out std_logic_vector (7 downto 0);
		Cout: out std_logic
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

signal S_Adder, S_MUX, S_Reg : std_logic_vector(7 downto 0);
signal S_sPulsed, S_notsPulsed: std_logic;

begin 
	S_notsPulsed <= not S_sPulsed;

	UPulseSignal_0: PulseSignal port map(
		A => Step,
		CLK => clk,
		S => S_sPulsed
	);

	UReg_0: Reg8 port map(
		CLK => clk,
		RESET => Reset,
		SET => '0',
		D => S_Adder,
		EN => S_sPulsed,
		Q => S_Reg
	);

	UMUX8_0: MUX8 port map(
		A => S_Reg,
		B => "00000000",
		S => S_notsPulsed,
		Y => S_MUX
	);

    UADDER8_4: ADDER8 port map(
		A => A,
        B => S_MUX,
		Cin => '0',
		S => S_Adder,
		Cout => Cout
    );	

	S <= S_Reg;

end arch_squaresum;