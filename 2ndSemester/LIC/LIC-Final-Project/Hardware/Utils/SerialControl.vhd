library ieee;
use ieee.std_logic_1164.all;

entity SerialControl is
    port(
        clk: in std_logic;
        en_Rx: in std_logic;
        pFlag: in std_logic;
        dFlag: in std_logic;
        RX_error: in std_logic;
        accept: in std_logic;
        init: out std_logic;
        DX_val: out std_logic;
        wr: out std_logic
    );
end SerialControl;

architecture Behavioral of SerialControl is
    signal state: std_logic_vector(2 downto 0) := "000";
    signal next_state: std_logic_vector(2 downto 0);

begin 
    process (clk)
    begin
        if rising_edge(clk) then
            state <= next_state;
        end if;
    end process;

    process (state, en_Rx, pFlag, dFlag, RX_error, accept)
    begin
        case state is
            when "000" =>
                init <= '1';
                DX_val <= '0';
                wr <= '0';
                if en_Rx = '0' then
                    next_state <= "001";
                else
                    next_state <= "000";
                end if;
                
            when "001" =>
                init <= '0';
                DX_val <= '0';
                wr <= '1';
                if dFlag = '1' then
                    next_state <= "010";
                else
                    next_state <= "001";
                end if;
                
            when "010" =>
                init <= '0';
                DX_val <= '0';
                wr <= '0';
                if (pFlag = '1' and RX_error = '0') then
                    next_state <= "011";
                elsif (pFlag = '1' and RX_error = '1') then
                    next_state <= "000";
                elsif pFlag = '0' then
                    next_state <= "010";
                else
                    next_state <= "011";
                end if;
                
            when "011" =>
                init <= '0';
                DX_val <= '1';
                wr <= '0';
                if accept = '1' then
                    next_state <= "100";
                else
                    next_state <= "011";
                end if;

            when "100" =>
                init <= '0';
                DX_val <= '0';
                wr <= '0';
                if accept = '0' then
                    next_state <= "000";
                else
                    next_state <= "100";
                end if;

            when others =>
                init <= '1';
                DX_val <= '0';
                wr <= '0';
                next_state <= "000";
        end case;
    end process;
end Behavioral;