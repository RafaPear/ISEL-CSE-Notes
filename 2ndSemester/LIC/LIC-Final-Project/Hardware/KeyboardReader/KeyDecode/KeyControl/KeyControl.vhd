library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity KeyControl is
    port (
        clk: in std_logic;
        rst: in std_logic;
        Kack: in std_logic;
        Kpress: in std_logic;
        Kscan: out std_logic;
        Kval: out std_logic
    );
end KeyControl;

architecture behavioral of KeyControl is
    signal state, next_state: std_logic_vector(1 downto 0);
    signal temp_Kscan: std_logic;
    
begin
    process (clk, rst, next_state)
    begin
        if rst = '1' then
            state <= "00";
        elsif rising_edge(clk) then
            state <= next_state;
        end if;
    end process;

    process (state, Kack, Kpress)
    begin
        case state is
            when "00" =>
                Kscan <= '1';
                Kval <= '0';
                if Kpress = '1' then
                    next_state <= "01";
                else
                    next_state <= "00";
                end if;
            
            when "01" =>
                Kscan <= '0';
                Kval <= '1';
                if Kack = '1' then
                    next_state <= "10";
                else
                    next_state <= "01";
                end if;

            when "10" =>
                Kscan <= '0';
                Kval <= '0';
                if Kpress = '0' and Kack = '0' then
                    next_state <= "00";
                else
                    next_state <= "10";
                end if;
            
            when others =>
				Kscan <= '1';
                Kval <= '0';
                next_state <= "00";
        end case;
    end process;
end behavioral;