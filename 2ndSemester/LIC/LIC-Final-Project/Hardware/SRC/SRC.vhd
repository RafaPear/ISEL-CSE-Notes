library ieee;
use ieee.std_logic_1164.all;

entity SRC is
    port(
        clk: in std_logic;
        RESET: in std_logic;
        Rsel: in std_logic;
        SCLK: in std_logic;
        SDX: in std_logic;
        set: out std_logic;
        Dout: out std_logic_vector(7 downto 0)
    );
end SRC;

architecture arch_SRC of SRC is
    component SerialReceiver8 is
        port(
            SDX: in std_logic;
            SCLK: in std_logic;
            clk_control: in std_logic;
            SS: in std_logic;
            accept: in std_logic;
            RESET: in std_logic;
            D: out std_logic_vector(7 downto 0);
            DX_val: out std_logic
        );
    end component;

    component RouletteDispacher is
        port(
            Dval: in std_logic;
            Din: in std_logic_vector(7 downto 0);
            clk: in std_logic;
            Wrd: out std_logic;
            Dout: out std_logic_vector(7 downto 0);
            done: out std_logic
        );
    end component;

    signal DXval_s, done_s: std_logic;
    signal D_s: std_logic_vector(7 downto 0);

    begin
    -- Instantiate the SerialReceiver8
    SerialReceiver8_inst: SerialReceiver8
        port map(
            SDX => SDX,
            SCLK => SCLK,
            clk_control => clk,
            SS => Rsel,
            accept => done_s,
            RESET => RESET,
            D => D_s,
            DX_val => DXval_s
        );
    
    -- Instantiate the RouletteDispacher
    RouletteDispacher_inst: RouletteDispacher
        port map(
            Dval => DXval_s,
            Din => D_s,
            clk => clk,
            Wrd => set,
            Dout => Dout,
            done => done_s
        );
end arch_SRC;