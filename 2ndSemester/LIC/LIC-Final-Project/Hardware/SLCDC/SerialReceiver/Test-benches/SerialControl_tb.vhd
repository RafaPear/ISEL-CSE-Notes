library ieee;
use ieee.std_logic_1164.all;

entity SerialControl_tb is
end SerialControl_tb;

architecture behavioral of SerialControl_tb is
    component SerialControl is
    port (
        clk: in std_logic;
        en_Rx: in std_logic;
        pFlag: in std_logic;
        dFlag: in std_logic;
        RX_error: in std_logic;
        accept: in std_logic;
        init: out std_logic;
        DX_val: out std_logic;
        wr: out std_logic
    );
    end component;

    -- UUT signals
    constant MCLK_PERIOD: time := 20ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal clk_tb: std_logic;
    signal en_Rx_tb: std_logic;
    signal pFlag_tb: std_logic;
    signal dFlag_tb: std_logic;
    signal RX_error: std_logic;
    signal accept_tb: std_logic;
    signal init_tb: std_logic;
    signal DX_val_tb: std_logic;
    signal wr_tb: std_logic;

begin 
    DATA_gen: process(CLK_tb)
    begin

    end process;


    UUT: SerialControl port map (
        clk => clk_tb,
        en_Rx => en_Rx_tb,
        pFlag => pFlag_tb,
        dFlag => dFlag_tb,
        RX_error => RX_error,
        accept => accept_tb,
        init => init_tb,
        DX_val => DX_val_tb,
        wr => wr_tb
        );

    CLK_gen: process
    begin
        CLK_tb <= '0';
        wait for MCLK_HALF_PERIOD;
        CLK_tb <= '1';
        wait for MCLK_HALF_PERIOD;
    end process;

    stimulus: process
        variable i: integer;
    begin 
        -- Start
        en_Rx_tb <= '1';
        pFlag_tb <= '0';
        dFlag_tb <= '0';
        RX_error <= '0';
        accept_tb <= '0';

        wait for MCLK_PERIOD*2;


        --000 next state
        en_Rx_tb <= '1';
        wait for MCLK_PERIOD;
        assert init_tb = '1' report "SerialControl failed to initialize" severity failure;
        assert wr_tb = '0' report "SerialControl failed to set wr" severity failure;
        assert DX_val_tb = '0' report "SerialControl failed to set DX_val" severity failure;

        --001 next state
        wait for MCLK_PERIOD*2;
        en_Rx_tb <= '0';
        wait for MCLK_PERIOD;
        assert wr_tb = '1' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '0' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '0' report "SerialControl failed to set DX_val" severity failure;
        
        --001 next state
        wait for MCLK_PERIOD*2;
        dFlag_tb <= '0';
        wait for MCLK_PERIOD;
        assert wr_tb = '1' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '0' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '0' report "SerialControl failed to set DX_val" severity failure;
        
        --010 next state
        wait for MCLK_PERIOD*2;
        dFlag_tb <= '1';
        wait for MCLK_PERIOD;
        assert wr_tb = '0' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '0' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '0' report "SerialControl failed to set DX_val" severity failure;

        --010 next state
        wait for MCLK_PERIOD*2;
        pFlag_tb <= '0';
        wait for MCLK_PERIOD;
        assert wr_tb = '0' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '0' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '0' report "SerialControl failed to set DX_val" severity failure;

        --000 next state
        wait for MCLK_PERIOD*2;
        pFlag_tb <= '1';
        RX_error <= '1';
        wait for MCLK_PERIOD;
        assert wr_tb = '0' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '1' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '0' report "SerialControl failed to set DX_val" severity failure;

        --011 next state
        wait for MCLK_PERIOD*2;
        en_Rx_tb <= '0';
        wait for MCLK_PERIOD*2;
        dflag_tb <= '1';
        wait for MCLK_PERIOD*2;
        pflag_tb <= '1';
        wait for MCLK_PERIOD*2;
        RX_error <= '0';

        wait for MCLK_PERIOD;
        assert wr_tb = '0' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '0' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '1' report "SerialControl failed to set DX_val" severity failure;

        --011 next state
        wait for MCLK_PERIOD*2;
        accept_tb <= '0';
        wait for MCLK_PERIOD;
        assert wr_tb = '0' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '0' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '1' report "SerialControl failed to set DX_val" severity failure;

        --100 next state
        wait for MCLK_PERIOD*2;
        accept_tb <= '1';
        wait for MCLK_PERIOD;
        assert wr_tb = '0' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '0' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '0' report "SerialControl failed to set DX_val" severity failure;

        --100 next state
        wait for MCLK_PERIOD*2;
        accept_tb <= '1';
        wait for MCLK_PERIOD;
        assert wr_tb = '0' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '0' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '0' report "SerialControl failed to set DX_val" severity failure;

        --000 next state
        wait for MCLK_PERIOD*2;
        accept_tb <= '0';
        wait for MCLK_PERIOD;
        assert wr_tb = '0' report "SerialControl failed to set wr" severity failure;
        assert init_tb = '1' report "SerialControl failed to reset" severity failure;
        assert DX_val_tb = '0' report "SerialControl failed to set DX_val" severity failure;
--------------------------------------------------------------------------------------------------
--test 2
--------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------
--test 3
--------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------
--test 4
--------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------
--test 5
--------------------------------------------------------------------------------------------------

        wait;
    end process stimulus;

end architecture behavioral;