library ieee;
use ieee.std_logic_1164.all;

entity BufferControl is
    port (
        clk: in std_logic; 
        reset: in std_logic;   
        Load: in std_logic;  
        ACK: in std_logic;  
        Wreg: out std_logic;  
        OBfree: out std_logic; 
        Dval: out std_logic  
    );
end BufferControl;

architecture Behavioral of BufferControl is
    signal state: std_logic_vector(1 downto 0) := "00";
    signal next_state: std_logic_vector(1 downto 0);

begin 
    process (clk)
    begin
        if rising_edge(clk) then
            state <= next_state;
        end if;
    end process;

    process (state, Load, ACK)
    begin
        case state is
            when "00" =>
                OBfree <= '1';
                Wreg <= '0';
                Dval <= '0';
                if Load = '1' then
                    next_state <= "01";
                else
                    next_state <= "00";
                end if;
            when "01" =>
                OBfree <= '0';
                Wreg <= '1';
                Dval <= '0';
                if Load = '0' then
                    next_state <= "10";
                else
                    next_state <= "01";
                end if;
            when "10" =>
                OBfree <= '0';
                Wreg <= '0';
                Dval <= '1';
                if ACK = '1' then
                    next_state <= "11";
                else
                    next_state <= "10";
                end if;
            when "11" =>
                OBfree <= '0';
                Wreg <= '0';
                Dval <= '0';
                if ACK = '0' then
                    next_state <= "00";
                else
                    next_state <= "11";
                end if;
            when others =>
				OBfree <= '1';
                Wreg <= '0';
                Dval <= '0';
                next_state <= "00";
        end case;
    end process;
end Behavioral;