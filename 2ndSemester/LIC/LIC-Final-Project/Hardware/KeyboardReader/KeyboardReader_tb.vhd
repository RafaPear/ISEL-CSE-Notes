library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity KeyboardReader_tb is
end KeyboardReader_tb;

architecture behavioral of KeyboardReader_tb is

    component KeyboardReader
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            LIN: in std_logic_vector(3 downto 0);
            COL: out std_logic_vector(3 downto 0);
            ACK: in std_logic;
            D: out std_logic_vector(3 downto 0);
            Dval: out std_logic
        );
    end component;

    constant MCLK_PERIOD: time := 20 ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal CLK_tb: std_logic;
    signal RESET_tb: std_logic;
    signal LIN_tb: std_logic_vector(3 downto 0);
    signal COL_tb: std_logic_vector(3 downto 0);
    signal ACK_tb: std_logic;
    signal D_tb: std_logic_vector(3 downto 0);
    signal Dval_tb: std_logic;

begin

    test: KeyboardReader port map(
        CLK => CLK_tb,
        RESET => RESET_tb,
        LIN => LIN_tb,
        COL => COL_tb,
        ACK => ACK_tb,
        D => D_tb,
        Dval => Dval_tb
    );

    CLK_gen: process
    begin
        while true loop
            CLK_tb <= '0';
            wait for MCLK_HALF_PERIOD;
            CLK_tb <= '1';
            wait for MCLK_HALF_PERIOD;
        end loop;
    end process;

    stimulus: process
        variable lin_0: std_logic_vector(3 downto 0) := "1110";
        variable lin_1: std_logic_vector(3 downto 0) := "1101";
        variable lin_2: std_logic_vector(3 downto 0) := "1011";
        variable lin_3: std_logic_vector(3 downto 0) := "0111";
        variable i : integer := 0;
        variable j : integer := 0;
        variable lin_patterns : std_logic_vector(3 downto 0) := "0000";
    begin
        ACK_tb <= '0';
        RESET_tb <= '1';
        LIN_tb <= "1111";
        wait for MCLK_PERIOD;

        RESET_tb <= '0';
        wait for MCLK_PERIOD * 2;
        -----------------------------------------------------------
        -- Inserir a tecla e o control consome instantaneamente
        -----------------------------------------------------------
        
        while j < 16 loop  
            wait until COL_tb = "1110";  

            case j mod 4 is
                when 0 => lin_patterns := "1110";
                when 1 => lin_patterns := "1101";
                when 2 => lin_patterns := "1011";
                when 3 => lin_patterns := "0111";
                when others => lin_patterns := "1111";
            end case;

            LIN_tb <= lin_patterns;
            wait for MCLK_PERIOD;

            LIN_tb <= "1111"; 
            wait for MCLK_PERIOD * 4;

            j := j + 1;
        end loop;
            
        wait for MCLK_PERIOD*5;

        for i in 0 to 16 loop 
            LIN_tb <= "0111";
            wait for MCLK_PERIOD;
            LIN_tb <= "1111";
            wait for MCLK_PERIOD*4;
        end loop;

        wait for MCLK_PERIOD*5;

        while i < 20 loop
            if Dval_tb = '1' then
                wait for MCLK_PERIOD;
                ACK_tb <= '1';
                wait until Dval_tb = '0';
                ACK_tb <= '0';
                wait for MCLK_PERIOD;
                i := i + 1;
            else
                wait for MCLK_PERIOD; 
            end if;
        end loop;
        wait;
end process;

end behavioral;