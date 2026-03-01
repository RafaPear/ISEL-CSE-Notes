library ieee;
use ieee.std_logic_1164.all;

entity Square is
	port (
		A: in std_logic_vector (3 downto 0);
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

component MUX4
    port(
		A, B: in std_logic_vector (3 downto 0);
		S: in std_logic;
		Y: out std_logic_vector (3 downto 0)
	);
end component;

signal R1, R2, R3, R4 : std_logic_vector(3 downto 0);
signal S1, S2 : std_logic_vector(7 downto 0);

begin

    UMUX4_0: MUX4 port map(
        B => A,
        A(0) => '0',
        A(1) => '0',
        A(2) => '0',
        A(3) => '0',
        S => A(0),
        Y => R1
    );

    UMUX4_1: MUX4 port map(
        B => A,
        A(0) => '0',
        A(1) => '0',
        A(2) => '0',
        A(3) => '0',
        S => A(1),
        Y => R2
    );

    UMUX4_2: MUX4 port map(
        B => A,
        A(0) => '0',
        A(1) => '0',
        A(2) => '0',
        A(3) => '0',
        S => A(2),
        Y => R3
    );

    UMUX4_3: MUX4 port map(
        B => A,
        A(0) => '0',
        A(1) => '0',
        A(2) => '0',
        A(3) => '0',
        S => A(3),
        Y => R4
    );

    UADDER8_0: ADDER8 port map(
		A(0) => R1(0),
		A(1) => R1(1),
		A(2) => R1(2),
		A(3) => R1(3),
		A(4) => '0',
		A(5) => '0',
		A(6) => '0',
		A(7) => '0',
		B(0) => '0',
		B(1) => R2(0),
		B(2) => R2(1),
		B(3) => R2(2),
		B(4) => R2(3),
		B(5) => '0',
		B(6) => '0',
		B(7) => '0',
		Cin => '0',
		S => S1
    );

    UADDER8_1: ADDER8 port map(
		A(0) => '0',
		A(1) => '0',
		A(2) => R3(0),
		A(3) => R3(1),
		A(4) => R3(2),
		A(5) => R3(3),
		A(6) => '0',
		A(7) => '0',
		B(0) => '0',
		B(1) => '0',
		B(2) => '0',
		B(3) => R4(0),
		B(4) => R4(1),
		B(5) => R4(2),
		B(6) => R4(3),
		B(7) => '0',
		Cin => '0',
		S => S2
    );

    UADDER8_2: ADDER8 port map(
		A => S1,
        B => S2,
		Cin => '0',
		S => S
    );


end arch_square;