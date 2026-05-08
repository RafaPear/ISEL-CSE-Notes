library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity MAC_tb is
end MAC_tb;

architecture test of MAC_tb is
    component MAC is
        port(
            clk: in std_logic;
            reset: in std_logic;
            incPut: in std_logic;
            incGet: in std_logic;
            PUTGET: in std_logic;
            ADDR: out std_logic_vector(3 downto 0);
            full: out std_logic;
            empty: out std_logic
        );
    end component;

    constant MCLK_PERIOD : time := 20 ns;
    signal clk_tb: std_logic;
    signal reset_tb: std_logic;
    signal incPut_tb: std_logic;
    signal incGet_tb: std_logic;
    signal PUTGET_tb: std_logic;
    signal ADDR_tb: std_logic_vector(3 downto 0);
    signal full_tb: std_logic;
    signal empty_tb: std_logic;
begin
    UUT: MAC
        port map(
        clk  => clk_tb,
        reset => reset_tb,
        incPut => incPut_tb,
        incGet => incGet_tb,
        PUTGET => PUTGET_tb,
        ADDR => ADDR_tb,
        full => full_tb,
        empty => empty_tb
        );

    p_CLK_gen: process
    begin
        while true loop
            clk_tb <= '0';
            wait for MCLK_PERIOD/2;
            clk_tb <= '1';
            wait for MCLK_PERIOD/2;
        end loop;
    end process;

    stimulus: process
    begin
        reset_tb <= '1';
        wait for 2 * MCLK_PERIOD;
        reset_tb <= '0';
        wait for 2 * MCLK_PERIOD;
        
        -- test PUT and full
        PUTGET_tb <= '1';
        for i in 0 to 15 loop
            wait for MCLK_PERIOD;
            incPut_tb <= '1';
            wait for MCLK_PERIOD;
            incPut_tb <= '0';
        end loop;
        assert full_tb = '1' report "Full signal should be low" severity failure;
        assert empty_tb = '0' report "Empty signal should be low" severity failure;

        wait for 4 * MCLK_PERIOD;
        -- test GET and empty
        PUTGET_tb <= '0';
        for i in 0 to 15 loop
            wait for MCLK_PERIOD;
            incGet_tb <= '1';
            wait for MCLK_PERIOD;
            incGet_tb <= '0';
        end loop;
        assert full_tb = '0' report "Full signal should be low" severity failure;
        assert empty_tb = '1' report "Empty signal should be low" severity failure;

        wait for 4 * MCLK_PERIOD;
        -- put 3
        PUTGET_tb <= '1';
        wait for 2*MCLK_PERIOD;
        incPut_tb <= '1';
        wait for MCLK_PERIOD;
        incPut_tb <= '0';
        wait for MCLK_PERIOD;
        incPut_tb <= '1';
        wait for MCLK_PERIOD;
        incPut_tb <= '0';
        wait for MCLK_PERIOD;
        incPut_tb <= '1';
        wait for MCLK_PERIOD;
        incPut_tb <= '0';

        assert ADDR_tb = "0011" report "ADDR should be 0011" severity failure;

        wait for 2*MCLK_PERIOD;
        -- get 1
        PUTGET_tb <= '0';
        wait for 2*MCLK_PERIOD;
        incGet_tb <= '1';
        wait for MCLK_PERIOD;
        incGet_tb <= '0';

        assert ADDR_tb = "0001" report "ADDR should be 0001" severity failure;

        wait for 2*MCLK_PERIOD;
        -- put 1
        PUTGET_tb <= '1';
        wait for 2*MCLK_PERIOD;
        incPut_tb <= '1';
        wait for MCLK_PERIOD;
        incPut_tb <= '0';

        assert ADDR_tb = "0100" report "ADDR should be 0100" severity failure;

        wait for 2*MCLK_PERIOD;
        -- get 3
        PUTGET_tb <= '0';
        wait for 2*MCLK_PERIOD;
        incGet_tb <= '1';
        wait for MCLK_PERIOD;
        incGet_tb <= '0';
        wait for MCLK_PERIOD;
        incGet_tb <= '1';
        wait for MCLK_PERIOD;
        incGet_tb <= '0';
        wait for MCLK_PERIOD;
        incGet_tb <= '1';
        wait for MCLK_PERIOD;
        incGet_tb <= '0';

        assert ADDR_tb = "0100" report "ADDR should be 0100" severity failure;
        assert full_tb = '0' report "Full signal should be low" severity failure;
        assert empty_tb = '1' report "Empty signal should be low" severity failure;
        wait;
    end process;

end test;