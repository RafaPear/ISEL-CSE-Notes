library ieee;
use ieee.std_logic_1164.all;

entity lab3 is
	port(
	X, Y: in std_logic_vector(3 downto 0);
	Op: in std_logic;
	R: out std_logic_vector(3 downto 0)
	);
end lab3;

architecture arc_lab3 of lab3 is

component NANDL4
	port(
	A, B: in std_logic_vector(3 downto 0);
	O: out std_logic_vector(3 downto 0)
	);
end component;

component LSL4
	port(
	A: in std_logic_vector(3 downto 0);
	O: out std_logic_vector(3 downto 0)
	);
end component;

component MUX4
	port(
	A, B: in std_logic_vector(3 downto 0);
	S: in std_logic;
	O: out std_logic_vector(3 downto 0)
	);
end component;

SIGNAL sa, sx : std_logic_vector(3 downto 0);

BEGIN

UNANDL4: NANDL4 port map(
	A => X,
	B => Y,
	O => sa
);

ULSL4: LSL4 port map(
	A => X,
	O => sx
);

UMUX4: MUX4 port map(
	A => sa,
	B => sx,
	S => Op,
	O => R
);

END;