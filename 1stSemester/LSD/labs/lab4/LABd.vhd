library ieee;
use ieee.std_logic_1164.all;

entity LABd is 
    port(
	    dataIn: in std_logic_vector(3 downto 0);
	    RESET: in std_logic;
	    CLK: in std_logic;
	    PL: in std_logic;
	    CE: in std_logic;
	    clear: in std_logic;
	    CountValue: out std_logic_vector(3 downto 0);
	    SEG7: out std_logic_vector(7 downto 0); -- dpgfedcba
	    TC: out std_logic
    );
end LABd;

architecture arc_labd of LABd is

    component clkDIV is
        port(
            clk_in: in std_logic;
    	    clk_out: out std_logic
        );
    end component;

    component Counter is
        port(
            D: in std_logic_vector(3 downto 0);
            PL: in std_logic;
            CE: in std_logic;
            CLK: in std_logic;
            Q: out std_logic_vector(3 downto 0)
        );
    end component;

    component decoderHex is
        port(
    		A: in std_logic_vector(3 downto 0);		
    		clear: in std_logic;
    		HEX0: out std_logic_vector(7 downto 0)      
        );
    end component;

    signal temp_clk: std_logic;
    signal temp_Q: std_logic_vector(3 downto 0);

    begin

        UCLKDIV0: clkDIV port map(
            clk_in => CLK,
    	    clk_out => temp_clk
        );

        UCOUNTER0: Counter port map(
            D => dataIn,
            PL => PL,
            CE => CE,
            CLK => temp_clk,
            Q => temp_Q
        );

        UDECODER0: decoderHex port map(
            A => temp_Q,
            clear => clear,
            HEX0 => SEG7
        );

        CountValue <= temp_Q;
        TC <= not(temp_Q(0) or temp_Q(1) or temp_Q(2) or temp_Q(3));
        
end arc_labd;