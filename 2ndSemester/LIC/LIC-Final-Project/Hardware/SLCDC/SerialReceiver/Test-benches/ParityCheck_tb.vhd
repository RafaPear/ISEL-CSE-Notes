library ieee;
use ieee.std_logic_1164.all;

entity ParityCheck_tb is
end ParityCheck_tb;

architecture Behavioral of ParityCheck_tb is
    component ParityCheck is
        port(
            clk: in std_logic;
            init: in std_logic;
            data_in: in std_logic;
            error: out std_logic
        );
    end component;

    signal clk: std_logic := '0';
    signal init: std_logic := '0';
    signal data_in: std_logic := '0';
    signal error_signal: std_logic;

    constant clk_period: time := 10 ns;

begin

    UUT: ParityCheck
        port map (
            clk => clk,
            init => init,
            data_in => data_in,
            error => error_signal
        );

    clk_process: process
    begin
        while true loop
            clk <= '0';
            wait for clk_period / 2;
            clk <= '1';
            wait for clk_period / 2;
        end loop;
    end process;

    stim_proc: process
    begin
        -- Test 1: Trama com número ímpar de 1s (10101)
        init <= '1';
        wait for clk_period;
        init <= '0';
        wait for clk_period;

        data_in <= '1'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '1'; wait for clk_period;

        assert (error_signal = '0') report "Test 1 failed: Expected P = 0 for odd number of 1s" severity error;

        -- Test 2: Trama com número par de 1s (11000)
        init <= '1';
        wait for clk_period;
        init <= '0';
        wait for clk_period;

        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;

        assert (error_signal = '1') report "Test 2 failed: Expected P = 1 for even number of 1s" severity error;

        -- Test 3: Trama com todos os bits a 0 (00000)
        init <= '1';
        wait for clk_period;
        init <= '0';
        wait for clk_period;

        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;

        assert (error_signal = '1') report "Test 3 failed: Expected P = 1 for even number of 1s" severity error;

        -- Test 4: Trama com todos os bits a 1 (11111)
        init <= '1';
        wait for clk_period;
        init <= '0';
        wait for clk_period;

        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;

        assert (error_signal = '0') report "Test 4 failed: Expected P = 0 for odd number of 1s" severity error;

        wait;
    end process;

end Behavioral;