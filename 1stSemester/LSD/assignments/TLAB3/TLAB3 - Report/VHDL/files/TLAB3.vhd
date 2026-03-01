library ieee;
use ieee.std_logic_1164.all;

entity TLAB3 is
	port(	
		CLK: in std_logic;
		Step: in std_logic;
		Start: in std_logic;
		Xi: in std_logic_vector(3 downto 0);
		Cy: out std_logic;
		HEX0 : out std_logic_vector(7 downto 0);
		HEX1 : out std_logic_vector(7 downto 0);
		HEX2 : out std_logic_vector(7 downto 0)
		);
end TLAB3;

architecture arq_tlab3 of TLAB3 is

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
		Cy: out std_logic;
        R: out std_logic_vector(7 downto 0)
		);
end component;

component DataPath
    port (
		Xi: in std_logic_vector (3 downto 0);
		clk: in std_logic;
		Step: in std_logic;
		Reset: in std_logic;
		Sum: out std_logic_vector (7 downto 0);
		SQR: out std_logic_vector (7 downto 0);
		Cout: out std_logic
	);
end component;

component decoderHex
	port (	
		bin: in std_logic_vector(7 downto 0);		
		clear : in std_logic;
		HEX0 : out std_logic_vector(7 downto 0);
		HEX1 : out std_logic_vector(7 downto 0);
		HEX2 : out std_logic_vector(7 downto 0)
	);		
end component;

component CLKDIV	
	port ( 
		clk_in: in std_logic;
		clk_out: out std_logic
	);
end component;

--Controller Signals
signal S_StepOut, S_Reset, S_Enable: std_logic;
signal S_R: std_logic_vector(7 downto 0);

--DataPath Signals
signal S_Cout: std_logic;
signal S_Sum, S_SQR: std_logic_vector(7 downto 0);

--Tlab3 Signals
signal S_notStart, CLK_Div: std_logic;

begin
	S_notStart <= not Start;

	UClockDiv_0: CLKDIV port map( 
		clk_in => CLK,
		clk_out => CLK_Div
	);

	
    UController_0: Controller port map(
		CLK => CLK_Div,
		Step => Step,
		Start => Start,
		Cout => S_Cout,
		SQR => S_SQR,
		Sum => S_Sum,
		StepOut => S_StepOut,
		Reset => S_Reset,
		Cy => Cy,
        R => S_R
	);

    UDataPath_0: DataPath port map(
		Xi => Xi,
		clk => CLK_Div,
		Step => S_StepOut,
		Reset => S_Reset,
		Sum => S_Sum,
		SQR => S_SQR,
		Cout => S_Cout
	);

	UdecoderHex_0: decoderHex port map(
		bin => S_R,
		clear => S_notStart,
		HEX0 => HEX0,
		HEX1 => HEX1,
		HEX2 => HEX2
	);
	
end arq_tlab3;