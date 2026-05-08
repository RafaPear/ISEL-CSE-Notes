library ieee;
use ieee.std_logic_1164.all;

entity KeyboardReader is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        LIN: in std_logic_vector(3 downto 0);
        ACK: in std_logic;
        COL: out std_logic_vector(3 downto 0);
        D: out std_logic_vector(3 downto 0);
        Dval: out std_logic
    );
end KeyboardReader;

architecture arch_KeyboardReader of KeyboardReader is
    component KeyDecode is
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            Kack: in std_logic;
            LIN: in std_logic_vector(3 downto 0);
            Kval: out std_logic;
            COL: out std_logic_vector(3 downto 0);
            K: out std_logic_vector(3 downto 0)
        );
    end component;

    component RingBuffer is
        port(
            clk: in std_logic;
            reset: in std_logic;
            CTS: in std_logic;
            DAV: in std_logic;
            D: in std_logic_vector(3 downto 0);
            Wreg: out std_logic;
            Q: out std_logic_vector(3 downto 0);
            DAC: out std_logic
        );
    end component;

    component OutputBuffer is
        port(
            clk: in std_logic;
            reset: in std_logic;
            D: in std_logic_vector(3 downto 0);
            Load: in std_logic;
            ACK: in std_logic;
            Q: out std_logic_vector(3 downto 0);
            OBfree: out std_logic;
            Dval: out std_logic
        );
    end component;
    
    signal Kval_s, DAC_s, Wreg_s, OBfree_s: std_logic;
    signal Q_s, K_s: std_logic_vector(3 downto 0);

    begin
    KeyDecode_inst: KeyDecode
        port map(
            CLK => CLK,
            RESET => RESET,
            Kack => DAC_s,
            LIN => LIN,
            Kval => Kval_s,
            COL => COL,
            K => K_s
        );

    RingBuffer_inst: RingBuffer
        port map(
            clk => CLK,
            reset => RESET,
            CTS => OBfree_s,
            DAV => Kval_s,
            D => K_s,
            Wreg => Wreg_s,
            Q => Q_s,
            DAC => DAC_s
        );

    OutputBuffer_inst: OutputBuffer
        port map(
            clk => CLK,
            reset => RESET,
            D => Q_s,
            Load => Wreg_s,
            ACK => ACK,
            OBfree => OBfree_s,
            Dval => Dval,
            Q => D
        );
end arch_KeyboardReader;