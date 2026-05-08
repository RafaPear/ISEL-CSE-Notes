library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity KScan_tb is
end KScan_tb;

architecture behavioral of KScan_tb is
    component KeyScan
        port (
            CLK: in std_logic;
            RESET: in std_logic;
            Kscan: in std_logic;
            LIN: in std_logic_vector(3 downto 0);
            COL: out std_logic_vector(3 downto 0);
            Kpress: out std_logic;  
            K: out std_logic_vector(3 downto 0)
        );
    end component;

    constant MCLK_PERIOD: time := 20 ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal CLK_tb: std_logic;
    signal RESET_tb: std_logic;
    signal Kscan_tb: std_logic;
    signal LIN_tb: std_logic_vector(3 downto 0);
    signal COL_tb: std_logic_vector(3 downto 0);
    signal K_tb: std_logic_vector(3 downto 0);
    signal Kpress_tb: std_logic;
    signal S_tb: std_logic_vector(1 downto 0);
    signal D_tb: std_logic_vector(3 downto 0);


begin
    test: KeyScan port map(
        CLK => CLK_tb,
        RESET => RESET_tb,
        Kscan => Kscan_tb,
        LIN => LIN_tb,
        COL => COL_tb,
        Kpress => Kpress_tb,
        K => K_tb
    );

    CLK_gen: process
    begin
        CLK_tb <= '0';
        wait for MCLK_HALF_PERIOD;
        CLK_tb <= '1';
        wait for MCLK_HALF_PERIOD;
    end process;

    Kscan_gen: process(CLK_tb)
    begin
        if rising_edge(CLK_tb) then
            Kscan_tb <= not Kpress_tb; 
        end if;
    end process;



    stimulus: process
        variable n: unsigned(3 downto 0) := "0001";
        variable i: integer;
        variable col_2: integer := 4;
        variable col_3: integer := 8;
        variable col_4: integer := 12;
    begin
        RESET_tb <= '1';
        LIN_tb <= "1111";
        wait for MCLK_PERIOD;
        RESET_tb <= '0';

        for i in 0 to 16 * col_2 loop
            wait for MCLK_PERIOD*col_2;
            LIN_tb <= not std_logic_vector(n);
            wait for MCLK_PERIOD*col_2;
            n := shift_left(n, 1); 
            LIN_tb <= "1111"; 
        end loop;
    
        wait;
    end process;
end behavioral;