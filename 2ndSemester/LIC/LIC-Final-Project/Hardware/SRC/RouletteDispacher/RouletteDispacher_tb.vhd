library ieee;
use ieee.std_logic_1164.all;

entity RouletteDispacher_tb is
end RouletteDispacher_tb;

architecture behavioral of RouletteDispacher_tb is
    component RouletteDispacher is
        port(
            Dval: in std_logic;
            Din: in std_logic_vector(7 downto 0);
            clk: in std_logic;
            Wrd: out std_logic;
            Dout: out std_logic_vector(7 downto 0);
            done: out std_logic
        );
    end component;

    -- UUT signals
    constant MCLK_PERIOD: time := 20ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal clk_tb: std_logic;
    signal Dval_tb: std_logic;
    signal Din_tb: std_logic_vector(7 downto 0);
    signal Wrl_tb: std_logic;
    signal Dout_tb: std_logic_vector(7 downto 0);
    signal done_tb: std_logic;


begin

    UUT: RouletteDispacher port map(
        Dval => Dval_tb,
        Din => Din_tb,
        clk => clk_tb,
        Wrd => Wrl_tb,
        Dout => Dout_tb,
        done => done_tb
    );

    CLK_gen: process
    begin
        clk_tb <= '0';
        wait for MCLK_HALF_PERIOD;
        clk_tb <= '1';
        wait for MCLK_HALF_PERIOD;
    end process;

    stimulus: process
    begin
        -- Test case 1: Check initial state
        Dval_tb <= '1';
        Din_tb <= "00101000";
        wait for MCLK_HALF_PERIOD;

        report "Test case 1: Check initial state";
        assert (Dout_tb = "00101000") report "Test case 1 failed (Dout != 0)" severity error;
        report "Test case 1.1 PASSED";
        assert (Wrl_tb = '0') report "Test case 1 failed (Wrl != 0)" severity error;
        report "Test case 1.2 PASSED";
        assert (done_tb = '1') report "Test case 1 failed (done != 0)" severity error;
        report "Test case 1.3 PASSED";
        report "Test case 1 passed";
        wait for MCLK_PERIOD;

        -- -- Test case 2: Check Dval = '1'
        -- Dval_tb <= '1';
        -- Din_tb <= "10101";
        -- wait for MCLK_PERIOD*2;
        
        -- report "Test case 2: Check Dval = '1'";
        -- assert (Dout_tb = "10101") report "Test case 2 failed (Dout != 10101)" severity error;
        -- report "Test case 2.1 PASSED";
        -- assert (Wrl_tb = '0') report "Test case 2 failed (Wrl != 0)" severity error;
        -- report "Test case 2.2 PASSED";
        -- assert (done_tb = '0') report "Test case 2 failed (done != 0)" severity error;
        -- report "Test case 2.3 PASSED";
        -- report "Test case 2 passed";
        -- wait for MCLK_PERIOD*2;

        -- report "Test case 3: Check Dval = '0'";
        -- assert (Dout_tb = "10101") report "Test case 3 failed (Dout != 10101)" severity error;
        -- report "Test case 3.1 PASSED";
        -- assert (Wrl_tb = '1') report "Test case 3 failed (Wrl != 1)" severity error;
        -- report "Test case 3.2 PASSED";
        -- assert (done_tb = '0') report "Test case 3 failed (done != 0)" severity error;
        -- report "Test case 3.3 PASSED";
        -- report "Test case 3 passed";
        -- wait for MCLK_PERIOD*2;

        -- report "Test case 4: Check done signal";
        -- assert (Dout_tb = "00000") report "Test case 4 failed (Dout != 0)" severity error;
        -- report "Test case 4.1 PASSED";
        -- assert (Wrl_tb = '0') report "Test case 4 failed (Wrl != 0)" severity error;
        -- report "Test case 4.2 PASSED";
        -- assert (done_tb = '1') report "Test case 4 failed (done != 1)" severity error;
        -- report "Test case 4.3 PASSED";
        -- report "Test case 4 passed";
        -- wait for MCLK_PERIOD*2;

        -- report "Test case 5: Check done signal";
        -- assert (Dout_tb = "00000") report "Test case 5 failed" severity error;
        -- report "Test case 5.1 PASSED";
        -- assert (Wrl_tb = '0') report "Test case 5 failed" severity error;
        -- report "Test case 5.2 PASSED";
        -- assert (done_tb = '0') report "Test case 5 failed" severity error;
        -- report "Test case 5.3 PASSED";
        -- report "Test case 5 passed";
        -- report "All test cases passed";
        -- report "Simulation finished";
        -- wait for MCLK_PERIOD*2;
        wait;
    end process stimulus;
end architecture behavioral;