library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity RingBuffer_tb is
end RingBuffer_tb;

architecture test of RingBuffer_tb is
    component RingBuffer is
        port(
            clk   : in std_logic;
            reset : in std_logic;
            CTS   : in std_logic;
            DAV   : in std_logic;
            D     : in std_logic_vector(3 downto 0);
            Wreg  : out std_logic;
            Q     : out std_logic_vector(3 downto 0);
            DAC   : out std_logic
        );
    end component;

    constant MCLK_PERIOD : time := 20 ns;

    signal clk_tb   : std_logic;
    signal reset_tb : std_logic;
    signal CTS_tb   : std_logic;
    signal DAV_tb   : std_logic;
    signal D_tb     : std_logic_vector(3 downto 0);
    signal Wreg_tb  : std_logic;
    signal Q_tb     : std_logic_vector(3 downto 0);
    signal DAC_tb   : std_logic;
begin

    URingBuffer: RingBuffer
        port map (
            clk   => clk_tb,
            reset => reset_tb,
            CTS   => CTS_tb,
            DAV   => DAV_tb,
            D     => D_tb,
            Wreg  => Wreg_tb,
            Q     => Q_tb,
            DAC   => DAC_tb
        );

    p_CLK_gen: process
    begin
        while true loop
            clk_tb <= '0';
            wait for MCLK_PERIOD / 2;
            clk_tb <= '1';
            wait for MCLK_PERIOD / 2;
        end loop;
    end process;

    stimulus: process
        variable x : std_logic_vector(3 downto 0);
    begin
        reset_tb <= '1';
        wait for 2 * MCLK_PERIOD;
        reset_tb <= '0';
        wait for 2 * MCLK_PERIOD;

        -- Encher a RAM
        for i in 0 to 15 loop
            D_tb <= std_logic_vector(to_unsigned(i, 4)); -- Correção: atribuição compatível
            DAV_tb <= '1';
            wait for 3 * MCLK_PERIOD;
            assert DAC_tb = '1' report "Wreg should be high" severity failure;
            wait for 2 * MCLK_PERIOD;
            assert Wreg_tb = '0' report "Wreg should be low" severity failure;
            DAV_tb <= '0';
            wait for 2 * MCLK_PERIOD;
            assert DAC_tb = '0' report "DAC should be low" severity failure;
        end loop;

        assert DAC_tb = '0' report "DAC should be low" severity failure;
        assert Wreg_tb = '0' report "Wreg should be low" severity failure;

        --tentar escrever ccom a memoria cheia
        D_tb <= "1111";
        wait for 2 * MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 3 * MCLK_PERIOD;
        assert DAC_tb = '0' report "Wreg should be low" severity failure;
        DAV_tb <= '0';
        
        --Esvaziar a RAM
        
        for i in 0 to 15 loop
            x := std_logic_vector(to_unsigned(i, 4));
            wait for 2 * MCLK_PERIOD;
            CTS_tb <= '1';
            wait for 2 * MCLK_PERIOD;
            assert Wreg_tb = '1' report "Wreg should be high" severity failure;
            assert Q_tb = x report "Q should be equal to " & integer'image(i) severity failure;
            wait for 2 * MCLK_PERIOD;
            CTS_tb <= '0';
            wait for 2 * MCLK_PERIOD;
            assert Wreg_tb = '0' report "Wreg should be low" severity failure;
        end loop;

        assert DAC_tb = '0' report "DAC should be low" severity failure;
        assert Wreg_tb = '0' report "Wreg should be low" severity failure;

        wait for 2 * MCLK_PERIOD;
        --tentar ler com a memoria vazia
        CTS_tb <= '1';
        wait for 2 * MCLK_PERIOD;
        CTS_tb <= '0';
        assert Wreg_tb = '0' report "Wreg should be low" severity failure;
        assert DAC_tb = '0' report "DAC should be low" severity failure;
        wait for 2 * MCLK_PERIOD;

        -- inserir 3 valores, todos = "1111"
        x := "1111";
        for i in 0 to 2 loop
            D_tb <= x;
            DAV_tb <= '1';
            wait for 3 * MCLK_PERIOD;
            assert DAC_tb = '1' report "Wreg should be high" severity failure;
            wait for 2 * MCLK_PERIOD;
            assert Wreg_tb = '0' report "Wreg should be low" severity failure;
            DAV_tb <= '0';
            wait for 2 * MCLK_PERIOD;
            assert DAC_tb = '0' report "DAC should be low" severity failure;
        end loop;
        -- retirar esses mesmo 3 valores verificar se estao corretos
        for i in 0 to 2 loop
            wait for 2 * MCLK_PERIOD;
            CTS_tb <= '1';
            wait for 2 * MCLK_PERIOD;
            CTS_tb <= '0';
            assert Wreg_tb = '1' report "Wreg should be high" severity failure;
            assert Q_tb = x report "Q should be equal to " & integer'image(i) severity failure;
            wait for 2 * MCLK_PERIOD;
            assert Wreg_tb = '0' report "Wreg should be low" severity failure;
            end loop;
            wait for 2 * MCLK_PERIOD;
            --tentar ler com a memoria vazia
            CTS_tb <= '1';
            wait for 2 * MCLK_PERIOD;
            CTS_tb <= '0';
            assert Wreg_tb = '0' report "Wreg should be low" severity failure;
            assert DAC_tb = '0' report "DAC should be low" severity failure;

            wait for 2 * MCLK_PERIOD;
            -- inserir um valor x e ler ele imediatamente
            x := "0001";
            D_tb <= x;
            DAV_tb <= '1';
            wait for 3 * MCLK_PERIOD;
            assert DAC_tb = '1' report "Wreg should be high" severity failure;
            wait for 2 * MCLK_PERIOD;
            assert Wreg_tb = '0' report "Wreg should be low" severity failure;
            DAV_tb <= '0';
            wait for 2 * MCLK_PERIOD;
            assert DAC_tb = '0' report "DAC should be low" severity failure;
            wait for 2 * MCLK_PERIOD;
            CTS_tb <= '1';
            wait for 2 * MCLK_PERIOD;
            CTS_tb <= '0';
            assert Wreg_tb = '1' report "Wreg should be high" severity failure;
            assert Q_tb = x report "Q should be equal to " & integer'image(1) severity failure;
            wait for 2 * MCLK_PERIOD;
            assert Wreg_tb = '0' report "Wreg should be low" severity failure;

        wait;
    end process;
end test;