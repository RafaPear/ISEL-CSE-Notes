library ieee;
use ieee.std_logic_1164.all;

entity RingBufferControl is
    Port (
        clk: in std_logic;
        reset: in std_logic;
        DAV: in std_logic;
        CTS: in std_logic;
        full: in std_logic;
        empty: in std_logic;
        DAC: out std_logic;
        selPG: out std_logic := '0';
        wr: out std_logic;
        W_reg: out std_logic;
        incPut: out std_logic;
        incGet: out std_logic
    );
end RingBufferControl;

architecture behavioral of RingBufferControl is
    signal state, next_state: std_logic_vector(2 downto 0);
begin

    process (clk, reset)
    begin
        if reset = '1' then
            state <= "000";
        elsif rising_edge(clk) then
            state <= next_state;
        end if;
    end process;

    process(state, DAV, CTS, full, empty)
    begin
        DAC    <= '0';
        incPut <= '0';
        incGet <= '0';
        W_reg  <= '0';
        wr     <= '0';

        case state is
            when "000" =>
				-- Não altera selPG neste estado: mantem valor anterior de propósito
                DAC    <= '0';
                incPut <= '0';
                incGet <= '0';
                W_reg  <= '0';
                wr     <= '0';
                if CTS = '1' and empty = '0' then
                    next_state <= "001";
                else 
                    if DAV = '1' and full = '0' then
                        next_state <= "100";
                    else 
                        next_state <= "000";
                    end if;
                end if;

            when "001" =>
                DAC    <= '0';
                incPut <= '0';
                incGet <= '0';
                W_reg  <= '0';
                wr     <= '0';
                selPG  <= '0';
                next_state <= "010";
            when "010" =>
                DAC    <= '0';
                incPut <= '0';
                incGet <= '0';
                W_reg  <= '1';
                wr     <= '0';
                selPG  <= '0';
                next_state <= "011";
            when "011" =>
                DAC    <= '0';
                incPut <= '0';
                incGet <= '1';
                W_reg  <= '0';
                wr     <= '0';
                selPG  <= '0';
                next_state <= "000";
            when "100" =>
                DAC    <= '0';
                incPut <= '0';
                incGet <= '0';
                W_reg  <= '0';
                wr     <= '0';
                selPG  <= '1';
                next_state <= "101";

            when "101" =>
                DAC    <= '0';
                incPut <= '0';
                incGet <= '0';
                W_reg  <= '0';
                wr     <= '1';
                selPG  <= '1';
                next_state <= "110";
            when "110" =>
                DAC    <= '1';
                incPut <= '0';
                incGet <= '0';
                W_reg  <= '0';
                wr     <= '0';
                selPG  <= '1';
                if DAV = '1' then
                    next_state <= "110";
                else 
                    next_state <= "000";
                    incPut <= '1';
                end if;
            when others =>
                next_state <= "000";
        end case;
    end process;

end behavioral;
