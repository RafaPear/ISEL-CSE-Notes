library ieee;
use ieee.std_logic_1164.all;

entity PulseSignal is
	port (
		A: in std_logic;
        CLK: in std_logic;
		S: out std_logic
	);
end PulseSignal;

architecture arq_pulsesignal of PulseSignal is

component FFD
	port(	
		CLK : in std_logic;
		RESET : in std_logic;
		SET : in std_logic;
		D : IN std_logic;
		EN : IN std_logic;
		Q : out std_logic
		);
end component;

signal s_Q: std_logic;

begin

    UFFD: FFD port map(
        CLK => CLK,
		RESET => '0',
		SET => '0',
		D => A,
		EN => '1',
		Q => s_Q
    );

    S <= A and not s_Q;

end arq_pulsesignal;