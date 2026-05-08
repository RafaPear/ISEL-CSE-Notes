library ieee;
use ieee.std_logic_1164.all;

entity PENC_tb is
end PENC_tb;

architecture behavioral of PENC_tb is
    component PENC
        port (
            I: in std_logic_vector (3 downto 0);
            Y: out std_logic_vector (1 downto 0);
            GS: out std_logic
        );
    end component;

    constant MCLK_PERIOD: time := 20 ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal I_TB: std_logic_vector (3 downto 0);
    signal Y_tb: std_logic_vector (1 downto 0);
    signal GS_tb: std_logic;

begin

    UUT: PENC port map(
        I => I_TB,
        Y => Y_tb,
        GS => GS_tb
    );

    stimulus: process
    begin
        I_TB <= "0000";
        wait for 10 ns;
        assert (GS_tb = '0') report "GS_tb should be equals to 1" severity failure;
        report "Test 1/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "0001";
        wait for 10 ns;
        assert (Y_tb = "00") report "Y_tb should be diferent than 00" severity failure;
        assert (GS_tb = '1') report "GS_tb should be equals to 1" severity failure;
        report "Test 2/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "0010";
        wait for 10 ns;
        assert (Y_tb = "01") report "Y_tb should be diferent than 01" severity failure;
        assert (GS_tb = '1') report "GS_tb should be equals to 1" severity failure;
        report "Test 3/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "0100";
        wait for 10 ns;
        assert (Y_tb = "10") report "Y_tb should be diferent than 10" severity failure;
        assert (GS_tb = '1') report "GS_tb should be equals to 1" severity failure;
        report "Test 4/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "1000";
        wait for 10 ns;
        assert (Y_tb = "11") report "Y_tb should be diferent than 11" severity failure;
        assert (GS_tb = '1') report "GS_tb should be equals to 1" severity failure;
        report "Test 5/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "1001";
        wait for 10 ns;
        assert (Y_tb = "11") report "Y_tb should be diferent than 11" severity failure;
        assert (GS_tb = '1') report "GS_tb should be equals to 1" severity failure;
        report "Test 6/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "1010";
        wait for 10 ns;
        assert (Y_tb = "11") report "Y_tb should be diferent than 11" severity failure;
        assert (GS_tb = '1') report " should be equals to 0" severity failure;
        report "Test 7/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "1100";
        wait for 10 ns;
        assert (Y_tb = "11") report "Y_tb should be diferent than 11" severity failure;
        assert (GS_tb = '1') report "GS_tb should be equals to 1" severity failure;
        report "Test 8/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "0101";
        wait for 10 ns;
        assert (Y_tb = "10") report "Y_tb should be diferent than 10" severity failure;
        assert (GS_tb = '1') report "GS_tbGS_tb should be equals to 1" severity failure;
        report "Test 9/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "0110";
        wait for 10 ns;
        assert (Y_tb = "10") report "Y_tb should be diferent than 10" severity failure;
        assert (GS_tb = '1') report "GS_tbGS_tb should be equals to 1" severity failure;
        report "Test 10/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "0111";
        wait for 10 ns;
        assert (Y_tb = "10") report "Y_tb should be diferent than 10" severity failure;
        assert (GS_tb = '1') report "GS_tbGS_tb should be equals to 1" severity failure;
        report "Test 11/12 Passed!" severity note;

        wait for MCLK_HALF_PERIOD * 3;
        I_TB <= "0011";
        wait for 10 ns;
        assert (Y_tb = "01") report "Y_tb should be diferent than 01" severity failure;
        assert (GS_tb = '1') report "GS_tbGS_tb should be equals to 1" severity failure;
        report "Test 12/12 Passed!" severity note;

        I_TB <= "0000";
        wait;
        report "UUT Penc passed all tests!" severity note;
    end process stimulus;
end architecture behavioral;
