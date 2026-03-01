library ieee;
use ieee.std_logic_1164.all;

entity DataPath is
	port (
		Xi: in std_logic_vector (3 downto 0);
		clk: in std_logic;
		Step: in std_logic;
		Reset: in std_logic;
		Sum: out std_logic_vector (7 downto 0);
		SQR: out std_logic_vector (7 downto 0);
		Cout: out std_logic
	);
end DataPath;

architecture arch_datapath of DataPath is

component PulseSignal
	    port (
		    A: in std_logic;
            CLK: in std_logic;
		    S: out std_logic
	    );
    end component;

component Reg4
	port(	
		CLK : in std_logic;
		RESET : in std_logic;
		SET : in std_logic;
		D : in std_logic_vector(3 downto 0);
		EN : in std_logic;
		Q : out std_logic_vector(3 downto 0)
		);
end component;

component Square
	port (
		A: in std_logic_vector (3 downto 0);
		clk: in std_logic;
		Step: in std_logic;
		Reset: in std_logic;
		S: out std_logic_vector (7 downto 0)
	);
end component;

component SquareSum
	port (
		A: in std_logic_vector (7 downto 0);
		clk: in std_logic;
		Step: in std_logic;
		Reset: in std_logic;
		S: out std_logic_vector (7 downto 0);
		Cout: out std_logic
	);
end component;

signal S_SQR : std_logic_vector(7 downto 0);
signal S_RegXi : std_logic_vector(3 downto 0);
signal S_notStep, S_StepPulsed: std_logic;

begin
	S_notStep <= not Step;

	UPulseSignal_2: PulseSignal port map(
		A => Step,
		CLK => clk,
		S => S_StepPulsed
	);


	UReg4_0: Reg4 port map(
		CLK => clk,
		RESET => Reset,
		SET => '0' ,
		D => Xi,
		EN => S_notStep,
		Q => S_RegXi
	);

    USquare_0: Square port map(
		A => S_RegXi,
		clk => clk,
		Step => Step,
		Reset => S_StepPulsed,
        S => S_SQR
	);

    USquareSum_0: SquareSum port map(
		A => S_SQR,
		clk => clk,
		Step => Step,
		Reset => Reset,
		S => Sum,
		Cout => Cout
	);

    SQR <= S_SQR;

end arch_datapath;