library ieee;
use ieee.std_logic_1164.all;

entity OutputRegister is
    port (
        clk: in std_logic;
        reset: in std_logic;
        D: in std_logic_vector(3 downto 0);
        Q: out std_logic_vector(3 downto 0)
    );
end OutputRegister;

architecture arc_OutputRegister of OutputRegister is
component FFD is
    port (
		CLK: in std_logic;
		RESET: in std_logic;
		SET: in std_logic;
		D: in std_logic;
		EN: in std_logic;
		Q: out std_logic
    );
end component;
begin
    UFFD0: FFD
        port map (
            CLK => clk,
            RESET => reset,
            SET => '0',
            D => D(0),
            EN => '1',
            Q => Q(0)
        );
    UFFD1: FFD
        port map (
            CLK => clk,
            RESET => reset,
            SET => '0',
            D => D(1),
            EN => '1',
            Q => Q(1)
        );
    UFFD2: FFD
        port map (
            CLK => clk,
            RESET => reset,
            SET => '0',
            D => D(2),
            EN => '1',
            Q => Q(2)
        );
    UFFD3: FFD
        port map (
            CLK => clk,
            RESET => reset,
            SET => '0',
            D => D(3),
            EN => '1',
            Q => Q(3)
        );
end arc_OutputRegister;