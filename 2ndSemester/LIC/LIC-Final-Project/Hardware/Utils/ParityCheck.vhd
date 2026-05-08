library ieee;
use ieee.std_logic_1164.all;

entity ParityCheck is
    port(
        clk     : in std_logic;
        init    : in std_logic;
        data_in : in std_logic;
        error   : out std_logic
    );
end ParityCheck;


architecture Behavioral of ParityCheck is
    component FFD is
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            SET: in std_logic;
            D: in std_logic;
            EN: in std_logic;
            Q: out std_logic
        );
    end component;
    signal temp_Q: std_logic := '0';
    signal Q: std_logic;
begin

    UFFD21 : FFD
        port map(
            CLK => clk,
            RESET => init,  
            SET => '0',   
            D => temp_Q, 
            EN => '1',     
            Q => Q      
        );
        
    temp_Q <= Q xor data_in;
    error <= not Q;
end Behavioral;