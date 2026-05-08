library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity SRC_tb is
end SRC_tb;

architecture test of SRC_tb is
    component SRC is
        port(
        clk: in std_logic;
        RESET: in std_logic;
        Rsel: in std_logic;
        SCLK: in std_logic;
        SDX: in std_logic;
        set: out std_logic;
        Dout: out std_logic_vector(7 downto 0)
        );
    end component;
    
    signal clk_tb : std_logic;
    signal clk_LCD_tb : std_logic;
    signal RESET_tb : std_logic;
    signal Rsel_tb : std_logic;
    signal SCLK_tb : std_logic;
    signal SDX_tb : std_logic;
    signal E_tb : std_logic;
    signal Dout_tb : std_logic_vector(7 downto 0);
    constant MCLK_PERIOD : time := 20 ns;
    constant MCLK_PERIOD_LCD : time := 250 ns;

begin
    UUT: SRC
        port map(
        clk => clk_tb,
        RESET => RESET_tb,
        Rsel => Rsel_tb,
        SCLK => SCLK_tb,
        SDX => SDX_tb,
        Set => E_tb,
        Dout => Dout_tb
        );

    p_CLK_LCD_gen: process
    begin
        while true loop
            clk_LCD_tb <= '0';
            wait for MCLK_PERIOD_LCD/2;
            clk_LCD_tb <= '1';
            wait for MCLK_PERIOD_LCD/2;
        end loop;
    end process;

    p_CLK_gen: process
    begin
        while true loop
            clk_tb <= '0';
            wait for MCLK_PERIOD/2;
            clk_tb <= '1';
            wait for MCLK_PERIOD/2;
        end loop;
    end process;
    
    stim_proc: process
        variable min_time_E_ON : time := 230 ns;
        variable min_time_E_OFF : time := 270 ns;

        variable SDX_in  : std_logic_vector(8 downto 0);
        variable expected_data : std_logic_vector(7 downto 0);
        variable expected_E_tb : std_logic;
        --teste 1
        variable data_in_1  : std_logic_vector(7 downto 0) := "10100000";
        variable P_in_1     : std_logic := '1';
        variable error_1   : std_logic := '0';
        --teste 2
        variable data_in_2  : std_logic_vector(7 downto 0) := "00100000";
        variable P_in_2     : std_logic := '1';
        variable error_2   : std_logic := '1';

    begin
        --teste 1 ; a trama tem que sair valida
        SDX_in := P_in_1 & data_in_1;
        expected_data := data_in_1;
        expected_E_tb:= not error_1;

        SCLK_tb <= '0';
        Rsel_tb <= '1';
        RESET_tb <= '1';
        wait for 2*MCLK_PERIOD;

        RESET_tb <= '0';
        wait for 2*MCLK_PERIOD;
        Rsel_tb <= '0';
        wait for MCLK_PERIOD;

        for i in 0 to 8 loop
            SDX_tb <= SDX_in(i);
            wait for MCLK_PERIOD;
            SCLK_tb <= '1';
            wait for 2*MCLK_PERIOD;
            SCLK_tb <= '0';
            wait for MCLK_PERIOD;
        end loop;

        wait until E_tb = '1';
        assert Dout_tb = expected_data report "Incorrect data output" severity failure;
        wait for min_time_E_ON;
        assert E_tb = '1' report "Incorrect E_tb" severity failure;
        
        wait until E_tb = '0';
        assert E_tb = '0' report "E should be low" severity failure;
        wait for min_time_E_OFF;
        assert E_tb = '0' report "E should be low" severity failure;

        wait for 3*MCLK_PERIOD;
        
        --teste 2 ; trama tem que sair invalida
        SDX_in := P_in_2 & data_in_2;
        expected_data := data_in_2;
        expected_E_tb := not error_2;

        wait for 2*MCLK_PERIOD;
        
        for i in 0 to 8 loop
            SDX_tb <= SDX_in(i);
            wait for MCLK_PERIOD;
            SCLK_tb <= '1';
            wait for 2*MCLK_PERIOD;
            SCLK_tb <= '0';
            wait for MCLK_PERIOD;
        end loop;

        wait for MCLK_PERIOD_LCD;
        assert E_tb = '0' report "Incorrect E_tb" severity failure;
        wait;
    end process;

end test;
