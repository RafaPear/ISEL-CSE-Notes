library ieee;
use ieee.std_logic_1164.all;

entity SLCDC is
    port(
        clk: in std_logic;
        RESET: in std_logic;
        LCDsel: in std_logic;
        SCLK: in std_logic;
        SDX: in std_logic;
        E: out std_logic;
        Dout: out std_logic_vector(4 downto 0)
    );
end SLCDC;

architecture arch_SLCDC of SLCDC is
    component SerialReceiver is
        port(
            SDX: in std_logic;
            SCLK: in std_logic;
            clk_control: in std_logic;
            SS: in std_logic;
            accept: in std_logic;
            RESET: in std_logic;
            D: out std_logic_vector(4 downto 0);
            DX_val: out std_logic
        );
    end component;

    component LCDDispacher is
        port(
            Dval: in std_logic;
            Din: in std_logic_vector(4 downto 0);
            clk: in std_logic;
            Wrl: out std_logic;
            Dout: out std_logic_vector(4 downto 0);
            done: out std_logic
        );
    end component;

    signal DXval_s, done_s: std_logic;
    signal D_s: std_logic_vector(4 downto 0);

    begin
    -- Instantiate the SerialReceiver
    SerialReceiver_inst: SerialReceiver
        port map(
            SDX => SDX,
            SCLK => SCLK,
            clk_control => clk,
            SS => LCDsel,
            accept => done_s,
            RESET => RESET,
            D => D_s,
            DX_val => DXval_s
        );
    
    -- Instantiate the LCDDispacher
    LCDDispacher_inst: LCDDispacher
        port map(
            Dval => DXval_s,
            Din => D_s,
            clk => clk,
            Wrl => E,
            Dout => Dout,
            done => done_s
        );
end arch_SLCDC;