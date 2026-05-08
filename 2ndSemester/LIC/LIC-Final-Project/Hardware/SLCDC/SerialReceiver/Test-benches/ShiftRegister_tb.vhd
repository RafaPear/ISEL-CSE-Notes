library ieee;
use ieee.std_logic_1164.all;

entity ShiftRegister_tb is
end ShiftRegister_tb;

architecture behavioral of ShiftRegister_tb is
    component ShiftRegister is
    port (
        CLK: in std_logic;
		RESET: in std_logic;
		D: in std_logic;
		EN: in std_logic;
		Q: out std_logic_vector(4 downto 0)
    );
    end component;

    -- UUT signals
    constant MCLK_PERIOD: time := 20ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal CLK_tb: std_logic;
    signal RESET_tb: std_logic;
    signal D_tb: std_logic;
    signal EN_tb: std_logic;
    signal Q_tb: std_logic_vector(4 downto 0);

    signal DATA: std_logic_vector(5 downto 0);
    signal CONTROL_OUT: std_logic_vector(4 downto 0);
    signal P: std_logic;


begin 
    DATA_gen: process(CLK_tb)
    begin
        if rising_edge(CLK_tb) then
            DATA(0) <= CONTROL_OUT(4);
            DATA(1) <= CONTROL_OUT(0);
            DATA(2) <= CONTROL_OUT(1);
            DATA(3) <= CONTROL_OUT(2);
            DATA(4) <= CONTROL_OUT(3);
            DATA(5) <= P;
        end if;
    end process;

    
    UUT: ShiftRegister port map (
        CLK => CLK_tb,
        RESET => RESET_tb,
        D => D_tb,
        EN => EN_tb,
        Q => Q_tb
        );
        
    CLK_gen: process
    begin
        CLK_tb <= '0';
        wait for MCLK_HALF_PERIOD;
        CLK_tb <= '1';
        wait for MCLK_HALF_PERIOD;
    end process;	

    stimulus: process
        variable i: integer;
    begin 
        -- DATA input em serie do ShiftRegister e CONTROL_OUT saida esperada por este
        CONTROL_OUT <= "11111";
        P <= '1'; -- bit paridade

        RESET_tb <= '0';
        wait for MCLK_PERIOD;
        RESET_tb <= '1';
        wait for MCLK_PERIOD;
        RESET_tb <= '0';
        wait for MCLK_PERIOD;
        
        
        for i in 0 to 5 loop -- percorre os 6 bits do DATA
            wait for MCLK_PERIOD;
            EN_tb <= '1';
            
            D_tb <= DATA(i);
        
            if i > 4 then --quando chega no bit 5, o EN deve ser 0
                EN_tb <= '0';
            end if;
        end loop;
        wait for MCLK_PERIOD;
        assert Q_tb = CONTROL_OUT report "Test 1/5 Failed" severity failure;
        report "Test 1/5 Passed" severity note;
        wait for MCLK_PERIOD;
--------------------------------------------------------------------------------------------------
--test 2
--------------------------------------------------------------------------------------------------
        -- DATA input em serie do ShiftRegister e CONTROL_OUT saida esperada por este
        CONTROL_OUT <= "00100";
        P <= '1'; -- bit paridade

        for i in 0 to 5 loop -- percorre os 6 bits do DATA
            wait for MCLK_PERIOD;
            EN_tb <= '1';
            
            D_tb <= DATA(i);
        
            
            if i > 4 then --quando chega no bit 5, o EN deve ser 0
                EN_tb <= '0';
            end if;
        end loop;
        wait for MCLK_PERIOD;
        assert Q_tb = CONTROL_OUT report "Test 2/5 Failed" severity failure;
        report "Test 2/5 Passed" severity note;
        wait for MCLK_PERIOD;
--------------------------------------------------------------------------------------------------
--test 3
--------------------------------------------------------------------------------------------------
        -- DATA input em serie do ShiftRegister e CONTROL_OUT saida esperada por este
        CONTROL_OUT <= "11011";
        P <= '1'; -- bit paridade

        for i in 0 to 5 loop -- percorre os 6 bits do DATA
            wait for MCLK_PERIOD;
            EN_tb <= '1';
            
            D_tb <= DATA(i);
            
            if i > 4 then --quando chega no bit 5, o EN deve ser 0
                EN_tb <= '0';
            end if;
        end loop;
        wait for MCLK_PERIOD;
        assert Q_tb = CONTROL_OUT report "Test 3/5 Failed" severity failure;
        report "Test 3/5 Passed" severity note;
        wait for MCLK_PERIOD;
--------------------------------------------------------------------------------------------------
--test 4
--------------------------------------------------------------------------------------------------
        -- DATA input em serie do ShiftRegister e CONTROL_OUT saida esperada por este
        CONTROL_OUT <= "10101";
        P <= '0'; -- bit paridade

        for i in 0 to 5 loop -- percorre os 6 bits do DATA
            wait for MCLK_PERIOD;
            EN_tb <= '1';
            
            D_tb <= DATA(i);
            
            if i > 4 then --quando chega no bit 5, o EN deve ser 0
                EN_tb <= '0';
            end if;
        end loop;
        wait for MCLK_PERIOD;
        assert Q_tb = CONTROL_OUT report "Test 4/5 Failed" severity failure;
        report "Test 4/5 Passed" severity note;
        wait for MCLK_PERIOD;
--------------------------------------------------------------------------------------------------
--test 5
--------------------------------------------------------------------------------------------------
        -- DATA input em serie do ShiftRegister e CONTROL_OUT saida esperada por este
        CONTROL_OUT <= "01101";
        P <= '1'; -- bit paridade

        for i in 0 to 5 loop -- percorre os 6 bits do DATA
            wait for MCLK_PERIOD;
            EN_tb <= '1';
            
            D_tb <= DATA(i);
            
            if i > 4 then --quando chega no bit 5, o EN deve ser 0
                EN_tb <= '0';
            end if;
        end loop;
        wait for MCLK_PERIOD;
        assert Q_tb = CONTROL_OUT report "Test 5/5 Failed" severity failure;
        report "Test 5/5 Passed" severity note;
        wait for MCLK_PERIOD;

        report "ALL tests passed" severity note;
        wait;
    end process stimulus;

end architecture behavioral;