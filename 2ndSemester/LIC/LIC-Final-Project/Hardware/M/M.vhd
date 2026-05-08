library ieee;
use ieee.std_logic_1164.all;

entity M is
    port(
        reset : in std_logic;
        key : in std_logic;
        clk : in std_logic;
        M : out std_logic
    );
end M;

architecture Behavioral of M is

    signal state: std_logic;
    signal next_state: std_logic;
begin

process (clk)
begin
    if rising_edge(clk) then
        state <= next_state;
    end if;
end process;

process (key, reset, state)
    begin
        if reset = '1' then
            next_state <= '0'; 
        else
            case state is
                when '0' => 
                    M <= '0';
                    if key = '1' then
                        next_state <= '1'; 
                    else
                        next_state <= '0'; 
                    end if;
                when '1' =>
                    M <= '1';
                    if key = '0' then
                        next_state <= '0';
                    else
                        next_state <= '1';
                    end if;
                when others =>
                    next_state <= '0'; 
            end case;
        end if;
    end process;
end Behavioral;
