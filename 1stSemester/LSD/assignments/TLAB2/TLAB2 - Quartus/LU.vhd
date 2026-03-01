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
		A: in std_logic_vector (3 downto 0);
		O: out std_logic_vector (3 downto 0);
		CY: out std_logic
		);
end component;

component ASR
	port(
		A: in std_logic_vector (3 downto 0);
		O: out std_logic_vector (3 downto 0);
        CY: out std_logic
		);
end component;

component LSL
	port(
		A: in std_logic_vector (3 downto 0);
		O: out std_logic_vector (3 downto 0);
		CY: out std_logic
		);
end component;

component NANDL
	port(
		A, B: in std_logic_vector (3 downto 0);
		O: out std_logic_vector (3 downto 0)
		);
end component;

component MUX
	port(
		A, B: in std_logic_vector (3 downto 0);
		S: in std_logic;
		Y: out std_logic_vector (3 downto 0)
		);
end component;

SIGNAL sa, sx, sl, ss, se, si: std_logic_vector(3 downto 0);

begin

    LSR0: LSR port map(
        A => A,
        O => sa
        );

    ASR0: ASR port map(
        A => A,
        O => sx
        );

    LSL0: LSL port map(
        A => A,
        O => sl
        );

    NANDL0: NANDL port map(
        A => A,
        B => B,
        O => ss
        );

    UMUX0: MUX port map (
    	A => sa,
    	B => sx,
    	S => S(0),
    	Y => se
        );

    UMUX1: MUX port map (
    	A => sl,
    	B => ss,
    	S => S(0),
    	Y => si
        );

    UMUX2: MUX port map (
    	A => se,
    	B => si,
    	S => S(1),
    	Y => R
        );
    
    CY <= si(0);

end;