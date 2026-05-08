library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity SerialReceiver_tb is
end SerialReceiver_tb;

architecture test of SerialReceiver_tb is
    component SerialReceiver8 is
        port(
            SDX: in  std_logic;
            SCLK: in  std_logic;
            clk_control: in  std_logic;
            SS: in  std_logic;
            accept: in  std_logic;
            RESET: in  std_logic;
            D: out std_logic_vector(7 downto 0);
            DX_val: out std_logic
        );
    end component;

    signal s_SDX     : std_logic := '0';
    signal s_SCLK    : std_logic := '0';
    signal s_clk_control : std_logic := '0';
    signal s_SS      : std_logic := '0';
    signal s_accept  : std_logic := '0';
    signal s_RESET   : std_logic := '0';
    signal s_D       : std_logic_vector(7 downto 0);
    signal s_DX_val  : std_logic;

    constant MCLK_PERIOD : time := 20 ns;

begin
    UUT: SerialReceiver8
        port map(
            SDX => s_SDX,
            SCLK => s_SCLK,
            clk_control => s_clk_control,
            SS => s_SS,
            accept => s_accept,
            RESET => s_RESET,
            D => s_D,
            DX_val => s_DX_val
        );

    p_CLK_gen: process
    begin
        while true loop
            s_clk_control <= '0';
            wait for MCLK_PERIOD/2;
            s_clk_control <= '1';
            wait for MCLK_PERIOD/2;
        end loop;
    end process;
    
    stim_proc: process
        variable SDX_in  : std_logic_vector(8 downto 0);
        variable expected_data : std_logic_vector(7 downto 0);
        variable expected_DX_val : std_logic;
        --teste 1
        variable data_in_1  : std_logic_vector(7 downto 0) := "10101010";
        variable P_in_1     : std_logic := '0';
        variable error_1   : std_logic := '1';
        --teste 2
        variable data_in_2  : std_logic_vector(7 downto 0) := "00101000";
        variable P_in_2     : std_logic := '1';
        variable error_2   : std_logic := '0';

    begin
        --teste 1
        SDX_in := P_in_1 & data_in_1;
        expected_data := data_in_1;
        expected_DX_val := not error_1;

        s_SCLK <= '0';
        s_SS <= '1';
        s_RESET <= '1';
        wait for 2*MCLK_PERIOD;

        s_RESET <= '0';
        wait for 2*MCLK_PERIOD;
        s_accept <= '1';
        wait for 2*MCLK_PERIOD;
        s_accept <= '0';
        wait for 2*MCLK_PERIOD;
        s_SS <= '0';
        wait for MCLK_PERIOD;

        for i in 0 to 8 loop
            s_SDX <= SDX_in(i);
            wait for MCLK_PERIOD;
            s_SCLK <= '1';
            wait for 2*MCLK_PERIOD;
            s_SCLK <= '0';
            wait for MCLK_PERIOD;
        end loop;
        
        assert s_D = expected_data report "Incorrect data output" severity failure;
        assert s_DX_val = expected_DX_val report "Incorrect DX_val" severity failure;
        wait for 2*MCLK_PERIOD;
        
        --teste 2
        SDX_in := P_in_2 & data_in_2;
        expected_data := data_in_2;
        expected_DX_val := not error_2;

        s_accept <= '1';
        wait for 2*MCLK_PERIOD;
        s_accept <= '0';
        wait for 2*MCLK_PERIOD;
        
        for i in 0 to 8 loop
            s_SDX <= SDX_in(i);
            wait for MCLK_PERIOD;
            s_SCLK <= '1';
            wait for 2*MCLK_PERIOD;
            s_SCLK <= '0';
            wait for MCLK_PERIOD;
        end loop;

        assert s_D = expected_data report "Incorrect data output" severity failure;
        assert s_DX_val = expected_DX_val report "Incorrect DX_val" severity failure;
        wait;
    end process;

end test;
