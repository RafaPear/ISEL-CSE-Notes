library ieee;
use ieee.std_logic_1164.all;

entity KeyReader_Roulette is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        K_LIN: in std_logic_vector(3 downto 0); -- KeyboardReader
        K_COL: out std_logic_vector(3 downto 0); -- KeyboardReader
        K_D: out std_logic_vector(3 downto 0); -- KeyboardReader
        K_Dval: out std_logic -- KeyboardReader
    );
end KeyReader_Roulette;

architecture arch_KeyReader_Roulette of KeyReader_Roulette is
    component UsbPort is
        port(
            inputPort : in std_logic_vector(7 downto 0);
            outputPort : out std_logic_vector(7 downto 0)
        );
    end component;

    component KeyboardReader is
        port(
        CLK: in std_logic;
        RESET: in std_logic;
        LIN: in std_logic_vector(3 downto 0);
        ACK: in std_logic;
        COL: out std_logic_vector(3 downto 0);
        D: out std_logic_vector(3 downto 0);
        Dval: out std_logic
        );
    end component;

    signal temp_inputPort, temp_outputPort: std_logic_vector(7 downto 0);
    signal temp_D: std_logic_vector(3 downto 0);
    signal temp_Dval, temp_ACK: std_logic;

begin
    -- Instantiate UsbPort
    UsbPort_inst: UsbPort
        port map(
            inputPort => temp_inputPort,
            outputPort => temp_outputPort
        );

    -- Instantiate KeyboardReader
    KeyboardReader_inst: KeyboardReader
        port map(
            CLK => CLK,
            RESET => RESET,
            LIN => K_LIN,
            ACK => temp_ACK,
            COL => K_COL,
            D => temp_D,
            Dval => temp_Dval
        );

    temp_inputPort(3 downto 0) <= temp_D;
    temp_inputPort(4) <= temp_Dval;
    temp_ACK <= temp_outputPort(7);
    K_D <= temp_D;
    K_Dval <= temp_Dval;
end arch_KeyReader_Roulette;