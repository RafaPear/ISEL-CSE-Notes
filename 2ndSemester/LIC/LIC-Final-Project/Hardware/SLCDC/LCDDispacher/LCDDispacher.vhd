library ieee;
use ieee.std_logic_1164.all;

entity LCDDispacher is
    generic(clk_div: natural := 13);
    port(
        Dval: in std_logic;
        Din: in std_logic_vector(4 downto 0);
        clk: in std_logic;
        Wrl: out std_logic;
        Dout: out std_logic_vector(4 downto 0);
        done: out std_logic
    );
end LCDDispacher;

architecture Behavioral of LCDDispacher is

    component clkDIV is
        generic(div: natural := clk_div);
        port(
            clk_in: in std_logic;
            clk_out: out std_logic
        );
    end component;

    signal state: std_logic_vector(1 downto 0);
    signal next_state: std_logic_vector(1 downto 0);

    -- Instaciate the clock divider
    signal nCLK: std_logic;

begin 
    clkDIV_inst2: clkDIV
        port map(
            clk_in => clk,
            clk_out => nCLK
        );

    process (nCLK)
    begin
        if rising_edge(nCLK) then
            state <= next_state;
        end if;
    end process;

    process (state, Dval, Din)
    begin
        case state is
            when "00" =>
                wrl <= '0';
                Dout <= "00000";
                done <= '0';
                if Dval = '1' then
                    next_state <= "01";
                else
                    next_state <= "00";
                end if;
                
            when "01" =>
                Dout <= Din;
                wrl <= '0';
                done <= '0';
                next_state <= "10";

            when "10" =>
                Dout <= Din;
                wrl <= '1';
                done <= '0';
                next_state <= "11";
            
            when "11" =>
                Dout <= Din;
                wrl <= '0';
                done <= '1';
                next_state <= "00";

            when others =>
                wrl <= '0';
                Dout <= "00000";
                done <= '0';
                next_state <= "00";
        end case;
    end process;
end Behavioral;