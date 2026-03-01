library ieee;
use ieee.std_logic_1164.all;

entity LU is
	port(
		A, B: in std_logic_vector(3 downto 0);
		S: in std_logic_vector(1 downto 0);
		R: out std_logic_vector(3 downto 0);
    	CY: out std_logic
	);
end LU;

architecture arc_lu of LU is

component LSR
	port(
		A: in std_logic_vector(3 downto 0);
		O: out std_logic_vector(3 downto 0);
		CY: out std_logic
	);
end component;

component ASR
	port(
		A: in std_logic_vector(3 downto 0);
		O: out std_logic_vector(3 downto 0);
        CY: out std_logic
	);
end component;

component LSL
	port(
		A: in std_logic_vector(3 downto 0);
		O: out std_logic_vector(3 downto 0);
		CY: out std_logic
	);
end component;

component NANDL
	port(
		A, B: in std_logic_vector(3 downto 0);
		O: out std_logic_vector(3 downto 0)
	);
end component;

component MUX_LU
	port(
		A, B: in std_logic_vector(3 downto 0);
		S: in std_logic;
		Y: out std_logic_vector(3 downto 0)
	);
end component;

signal o_LSR, o_ASR, o_LSL, 
	   o_NAND, o_MUXa, 
	   o_MUXb : std_logic_vector(3 downto 0);
signal o_CYa, o_CYb, o_CYc: std_logic;
begin

    LSR0: LSR port map(
        A => A,
		O => o_LSR,
        CY => o_CYa
    );

    ASR0: ASR port map(
        A => A,
		O => o_ASR,
        CY => o_CYb
    );

    LSL0: LSL port map(
        A => A,
		O => o_LSL,
        CY => o_CYc
    );

    NANDL0: NANDL port map(
        A => A,
        B => B,
		O => o_NAND
    );

    UMUX0: MUX_LU port map (
    	A => o_LSR,
    	B => o_ASR,
    	S => S(0),
    	Y => o_MUXa
    );

    UMUX1: MUX_LU port map (
    	A => o_LSL,
    	B => o_NAND,
    	S => S(0),
    	Y => o_MUXb
    );

    UMUX2: MUX_LU port map (
    	A => o_MUXa,
    	B => o_MUXb,
    	S => S(1),
    	Y => R
    );

	CY <= (not S(1) and (o_CYa or o_CYb)) or 
		  (S(1) and o_CYc);
    
end arc_lu;