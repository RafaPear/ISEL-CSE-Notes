library ieee;
use ieee.std_logic_1164.all;

entity Controller is
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
end Controller;

architecture arq_controller of Controller is

component MUX8
    port(
		A, B: in std_logic_vector (7 downto 0);
		S: in std_logic;
		Y: out std_logic_vector (7 downto 0)
	);
end component;

component FFD is
    port(	
	    CLK : in std_logic;
	    RESET : in std_logic;
	    SET : in std_logic;
	    D : in std_logic;
	    EN : in std_logic;
	    Q : out std_logic
    );
end component;

component Reg
	port(	
		CLK: in std_logic;
		RESET: in std_logic;
		SET: in std_logic;
		D: in std_logic_vector(7 downto 0);
		EN: in std_logic;
		Q: out std_logic_vector(7 downto 0)
		);
end component;

signal S_FFD, S_Not_FFD, S_Not_Start, S_Not_Step, S_and, S_NotCLK: std_logic;
signal S_MUX1, S_Reg: std_logic_vector(7 downto 0);

begin
	S_Not_FFD <= not S_FFD;
	S_Not_Start <= not Start;
	S_Not_Step <= not Step;
	S_NotCLK <= not CLK;

	UFFD8: FFD port map(
        CLK => S_NotCLK,
        RESET => S_Not_Start,
        SET => '0',
        D => Cout,
        EN => S_Not_FFD,
        Q => S_FFD
    );

	UMUX8_1: MUX8 port map(
		A => SQR,
		B => Sum,
		S => S_Not_Step,
		Y => S_MUX1
	);

	UMUX8_2: MUX8 port map(
		A => "00000000",
		B => S_MUX1,
		S => Start,
		Y => R
	);

	StepOut <= Step;
	Reset <= not Start;	
	Cy <= S_FFD;
	
end arq_controller;