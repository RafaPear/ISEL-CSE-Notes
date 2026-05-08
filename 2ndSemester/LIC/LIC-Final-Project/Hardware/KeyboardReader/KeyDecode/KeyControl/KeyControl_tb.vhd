library ieee;
use ieee.std_logic_1164.all;

entity KeyControl_tb is
end KeyControl_tb;

architecture behavioral of KeyControl_tb is
    component KeyControl
        port (
            clk: in std_logic;
            rst: in std_logic;
            Kack: in std_logic;
            Kpress: in std_logic;
            Kscan: out std_logic;
            Kval: out std_logic
        );
    end component;

    constant MCLK_PERIOD: time := 20 ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal CLK_tb: std_logic;
    signal RST_tb: std_logic;
    signal KACK_tb: std_logic;
    signal KPRESS_tb: std_logic;
    signal KSCAN_tb: std_logic;
    signal KVAL_tb: std_logic;
begin
    UUT: KeyControl port map (
        clk => CLK_tb,
        rst => RST_tb,
        Kack => KACK_tb,
        Kpress => KPRESS_tb,
        Kscan => KSCAN_tb,
        Kval => KVAL_tb
    );

    CLK_gen: process
    begin
        CLK_tb <= '0';
        wait for MCLK_HALF_PERIOD;
        CLK_tb <= '1';
        wait for MCLK_HALF_PERIOD;
    end process;

    stimulus: process
    begin
        RST_tb <= '1';
        wait for MCLK_PERIOD;

        assert (KSCAN_tb = '1' and KVAL_tb = '0') report "Test 1/7 Failed" severity failure;
        report "Test 1/7 Passed" severity note;
        RST_tb <= '0';
        Kpress_tb <= '0';
        wait for MCLK_PERIOD;

        assert (KSCAN_tb = '1' and KVAL_tb = '0') report "Test 2/7 Failed" severity failure;
        report "Test 2/7 Passed" severity note;
        Kpress_tb <= '1';
        wait for MCLK_PERIOD;

        assert (KSCAN_tb = '0' and KVAL_tb = '1') report "Test 3/7 Failed" severity failure;
        report "Test 3/7 Passed" severity note;
        Kack_tb <= '0';
        Kpress_tb <= '0';
        wait for MCLK_PERIOD;

        assert (KSCAN_tb = '0' and KVAL_tb = '1') report "Test 4/7 Failed" severity failure;
        report "Test 4/7 Passed" severity note;
        Kack_tb <= '0';
        Kpress_tb <= '1';
        wait for MCLK_PERIOD;

        assert (KSCAN_tb = '0' and KVAL_tb = '1') report "Test 5/7 Failed" severity failure;
        report "Test 5/7 Passed" severity note;
        Kack_tb <= '1';
        Kpress_tb <= '1';
        wait for MCLK_PERIOD;

        assert (KSCAN_tb = '0' and KVAL_tb = '1') report "Test 6/7 Failed" severity failure;
        report "Test 6/7 Passed" severity note;
        Kack_tb <= '1';
        Kpress_tb <= '0';
        wait for MCLK_PERIOD;

        assert (KSCAN_tb = '1' and KVAL_tb = '0') report "Test 7/7 Failed" severity failure;
        report "Test 7/7 Passed" severity note;
        wait for MCLK_PERIOD;

        report "All tests passed" severity note;
        wait;
    end process;
end behavioral;
