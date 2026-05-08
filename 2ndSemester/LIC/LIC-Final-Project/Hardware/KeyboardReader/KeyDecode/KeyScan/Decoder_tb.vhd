library ieee;
use ieee.std_logic_1164.all;

entity Decoder_tb is
end Decoder_tb;

architecture behavioral of Decoder_tb is
    
    component Decoder is
    port (
        S: in std_logic_vector(1 downto 0);
        D: out std_logic_vector(3 downto 0)
    );
    end component;

-- UUT signals
constant MCLK_PERIOD: time := 20ns;
constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

signal S_tb: std_logic_vector(1 downto 0);
signal D_tb: std_logic_vector(3 downto 0);

begin 
-- Unit Under Test
UUT: Decoder port map (
    S => S_tb, 
    D => D_tb
    );

stimulus: process
begin 
    S_tb <= "00";
    wait for MCLK_HALF_PERIOD*3;
    assert D_tb = "0001" report "D_tb should be equal to 00" severity failure;
    
    S_tb <= "01";
    wait for MCLK_HALF_PERIOD*3;
    assert D_tb = "0010" report "D_tb should be equal to 01" severity failure;
    
    S_tb <= "10";
    wait for MCLK_HALF_PERIOD*3;
    assert D_tb = "0100" report "D_tb should be equal to 10" severity failure;
    
    S_tb <= "11";
    wait for MCLK_HALF_PERIOD*3;
    assert D_tb = "1000" report "D_tb should be equal to 11" severity failure;
    
    report "UUT Decoder passed" severity note;

    wait for MCLK_HALF_PERIOD;
    wait;
end process stimulus;
end architecture behavioral;