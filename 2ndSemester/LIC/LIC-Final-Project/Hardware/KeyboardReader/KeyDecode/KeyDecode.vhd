library ieee;
use ieee.std_logic_1164.all;

entity KeyDecode is
    generic(clk_div: natural := 625000);
    port(
        CLK: in std_logic;
        RESET: in std_logic;
		Kack: in std_logic;
        LIN: in std_logic_vector(3 downto 0);
        Kval: out std_logic;
        COL: out std_logic_vector(3 downto 0);
        K: out std_logic_vector(3 downto 0)
    );
end KeyDecode;

architecture arch_KeyDecode of KeyDecode is
    component KeyScan is
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            Kscan: in std_logic;
            LIN: in std_logic_vector(3 downto 0);
            COL: out std_logic_vector(3 downto 0);
            Kpress: out std_logic;
            K: out std_logic_vector(3 downto 0)
        );
    end component;

    component KeyControl is
        port (
            clk: in std_logic;
            rst: in std_logic;
            Kack: in std_logic;
            Kpress: in std_logic;
            Kscan: out std_logic;
            Kval: out std_logic
        );
    end component;

    component clkDIV is
        generic(div: natural := clk_div);
        port(
            clk_in: in std_logic;
            clk_out: out std_logic
        );
    end component;

    signal temp_Kpress, temp_Kscan, nCLK: std_logic;

begin
    clkDIV_inst2: clkDIV
        port map(
            clk_in => clk,
            clk_out => nCLK
        );

    KeyScan_inst: KeyScan port map(
        CLK => nCLK,
        RESET => RESET,
        Kscan => temp_Kscan,
        LIN => LIN,
        COL => COL,
        Kpress => temp_Kpress,
        K => K
    );

    KeyControl_inst: KeyControl port map(
        clk => nCLK,
        rst => RESET,
        Kack => Kack,
        Kpress => temp_Kpress,
        Kscan => temp_Kscan,
        Kval => Kval
    );


end arch_KeyDecode;