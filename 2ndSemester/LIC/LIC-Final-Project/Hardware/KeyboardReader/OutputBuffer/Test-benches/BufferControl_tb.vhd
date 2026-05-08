library ieee;
use ieee.std_logic_1164.all;

entity BufferControl_tb is
end BufferControl_tb;

architecture Behavioral of BufferControl_tb is
    component BufferControl
        port (
            clk: in std_logic;
            reset: in std_logic;
            Load: in std_logic;
            ACK: in std_logic;
            Wreg: in std_logic;
            OBfree: out std_logic;
            Dval: out std_logic
        );
    end component;

    signal clk: std_logic := '0';
    signal reset: std_logic := '1';
    signal Load: std_logic := '0';
    signal ACK: std_logic := '0';
    signal Wreg: std_logic := '0';
    
    signal OBfree: std_logic;
    signal Dval: std_logic;
    
    constant MCLK_PERIOD: time := 20 ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

begin

    UUT: BufferControl port map (
        clk => clk,
        reset => reset,
        Load => Load,
        ACK => ACK,
        Wreg => Wreg,
        OBfree => OBfree,
        Dval => Dval
    );

    clk_process: process
    begin
        clk <= '0';
        wait for MCLK_HALF_PERIOD;
        clk <= '1';
        wait for MCLK_HALF_PERIOD;
    end process;

    stim_proc: process
    begin
        reset <= '1';
        wait for MCLK_PERIOD*2;
        reset <= '0';
        wait for MCLK_PERIOD;
        
        -- Teste 1: Ciclo completo de transferência
        report "Teste 1: Ciclo completo de transferência";
        Wreg <= '1';  -- Dados disponíveis do Ring Buffer
        wait for MCLK_PERIOD;
        Wreg <= '0';
        
        Load <= '1';   -- Carrega dados no registador
        wait for MCLK_PERIOD;
        Load <= '0';
        
        wait for MCLK_PERIOD*2;  -- Mantém em estado VALID
        
        ACK <= '1';    -- Consumidor confirma com acknowledge
        wait for MCLK_PERIOD;
        ACK <= '0';
        
        wait for MCLK_PERIOD*2;
        
        -- Teste 2: Tentativa de carga sem Wreg
        report "Teste 2: Tentativa de carga sem Wreg";
        Load <= '1';
        wait for MCLK_PERIOD;
        Load <= '0';
        wait for MCLK_PERIOD;
        
        -- Teste 3: Carga com atraso no Load
        report "Teste 3: Carga com atraso no Load";
        Wreg <= '1';
        wait for MCLK_PERIOD*3;  -- Espera vários ciclos antes de carregar
        Load <= '1';
        wait for MCLK_PERIOD;
        Load <= '0';
        Wreg <= '0';
        
        wait until Dval = '1';
        ACK <= '1';
        wait for MCLK_PERIOD;
        ACK <= '0';
        
        wait for MCLK_PERIOD*2;
        
        -- Teste 4: ACK mantido alto
        report "Teste 4: ACK mantido alto";
        Wreg <= '1';
        wait for MCLK_PERIOD;
        Wreg <= '0';
        
        Load <= '1';
        wait for MCLK_PERIOD;
        Load <= '0';
        
        wait until Dval = '1';
        ACK <= '1';
        wait for MCLK_PERIOD*3;  -- Mantém ACK alto por vários ciclos
        ACK <= '0';
        
        wait for MCLK_PERIOD*2;
        
        -- Teste 5: Reset durante operação
        report "Teste 5: Reset durante operação";
        Wreg <= '1';
        wait for MCLK_PERIOD;
        
        reset <= '1';  -- Reset durante LoadData
        wait for MCLK_PERIOD;
        reset <= '0';
        
        wait for MCLK_PERIOD*2;
        
        wait;
    end process;

end Behavioral;