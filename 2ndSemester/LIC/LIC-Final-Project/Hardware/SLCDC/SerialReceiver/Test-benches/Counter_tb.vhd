library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity Counter_tb is
end Counter_tb;

architecture test of Counter_tb is
    component Counter is
        port(
            RESET: in std_logic;
            clr: in std_logic;
            CLK: in std_logic;
            Q: out std_logic_vector(2 downto 0)
        );
    end component;

    signal CLK_tb : std_logic := '0';
    signal RESET_tb : std_logic := '0';
    signal clr_tb : std_logic := '0';
    signal Q_tb : std_logic_vector(2 downto 0);

    constant MCLK_PERIOD : time := 20 ns;

begin
    UUT: Counter
        port map(
            RESET => RESET_tb,
            clr => clr_tb,
            CLK => CLK_tb,
            Q => Q_tb
        );

    p_CLK_gen: process
    begin
        while true loop
            CLK_tb <= '0';
            wait for MCLK_PERIOD/2;
            CLK_tb <= '1';
            wait for MCLK_PERIOD/2;
        end loop;
    end process;

    stim_proc: process
    begin
        -- Test reset condition
        RESET_tb <= '1';
        clr_tb <= '0';
        wait for 2*MCLK_PERIOD;

        RESET_tb <= '0';
        wait for 2*MCLK_PERIOD;

        -- Test clear condition
        clr_tb <= '1';
        wait for 2*MCLK_PERIOD;

        clr_tb <= '0';
        wait for 2*MCLK_PERIOD;

        -- Test normal counting
        wait for 20*MCLK_PERIOD;

        -- Finish simulation
        assert false report "Simulation finished" severity note;
        wait;
    end process;
end test;