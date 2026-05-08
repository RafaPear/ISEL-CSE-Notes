library ieee;
use ieee.std_logic_1164.all;

entity RouletteDispacher is
    port(
        Dval: in std_logic;
        Din: in std_logic_vector(7 downto 0);
        clk: in std_logic;
        Wrd: out std_logic;
        Dout: out std_logic_vector(7 downto 0);
        done: out std_logic
    );
end RouletteDispacher;

architecture Behavioral of RouletteDispacher is

    signal state: std_logic_vector(1 downto 0) := "00";
    signal next_state: std_logic_vector(1 downto 0);

    -- Instaciate the clock divider
    signal nCLK: std_logic;

begin 

    process (clk)
    begin
        if rising_edge(clk) then
            state <= next_state;
        end if;
    end process;

    process (state, Dval, Din)
    begin
        case state is
            when "00" =>
                wrd <= '0';
                Dout <= "00000001";
                done <= '0';
                if Dval = '1' then
                    next_state <= "01";
                else
                    next_state <= "00";
                end if;
                
            when "01" =>
                Dout <= Din;
                wrd <= '0';
                done <= '0';
                next_state <= "10";

            when "10" =>
                Dout <= Din;
                wrd <= '1';
                done <= '0';
                next_state <= "11";
            
            when "11" =>
                Dout <= "00000000";
                wrd <= '0';
                done <= '1';
                next_state <= "00";

            when others =>
                wrd <= '0';
                Dout <= "00000000";
                done <= '0';
                next_state <= "00";
        end case;
    end process;
end Behavioral;