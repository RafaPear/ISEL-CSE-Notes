library ieee;
use ieee.std_logic_1164.all;

entity CoinAcceptor is
    port(
        id : in std_logic;
        insert : in std_logic;
        accept : in std_logic;
        reset : in std_logic;
        clk : in std_logic;
        coinId : out std_logic;
        coin : out std_logic
    );
end CoinAcceptor;

architecture Behavioral of CoinAcceptor is

    signal state: std_logic_vector(1 downto 0);
    signal next_state: std_logic_vector(1 downto 0);
    signal temp_coinId: std_logic;

begin

process (clk)
begin
    if rising_edge(clk) then
        state <= next_state;
    end if;
end process;

process (id, insert, reset, accept, state)
    begin
        if reset = '1' then
            next_state <= "00"; -- Reset state
        else
            case state is
                when "00" => -- Idle state
                    coinId <= '0';
                    coin <= '0';
                    if insert = '1' then
                        next_state <= "01"; -- Transition to coin inserted state
                    else
                        next_state <= "00"; -- Stay in idle state
                    end if;

                when "01" => -- Coin inserted state
                    temp_coinId <= id; -- Store the coin ID
                    coinId <= temp_coinId; -- Output the coin ID
                    coin <= '0'; -- Indicate a coin has been accepted
                    next_state <= "10"; -- Transition back to idle state
                
                when "10" => -- Coin accepted state
                    coinId <= temp_coinId; -- Clear coin ID
                    coin <= '1'; -- Indicate a coin has been accepted
                    if accept = '1' then
                        next_state <= "11"; -- Transition back to idle state
                    else
                        next_state <= "10"; -- Stay in coin accepted state
                    end if;
                
                when "11" => -- Coin accepted and ready for next
                    coinId <= '0'; -- Clear coin ID
                    coin <= insert; -- Indicate no coin accepted
                    if insert = '0' then
                        next_state <= "00"; -- Transition to coin inserted state
                    else
                        next_state <= "11"; -- Stay in coin accepted state
                    end if;

                when others =>
                    next_state <= "00"; -- Default to idle on any other state
            end case;
        end if;
    end process;
end Behavioral;
