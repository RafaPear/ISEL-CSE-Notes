library ieee;
use ieee.std_logic_1164.all;

entity OutputBuffer is
    port (
        clk: in std_logic;
        reset: in std_logic;
        D: in std_logic_vector(3 downto 0);
        Load: in std_logic;
        ACK: in std_logic;
        Q: out std_logic_vector(3 downto 0);
        OBfree: out std_logic;
        Dval: out std_logic
    );
end OutputBuffer;

architecture arc_OutputBuffer of OutputBuffer is
component OutputRegister is
    port (
        clk: in std_logic;
        reset: in std_logic;
        D: in std_logic_vector(3 downto 0);
        Q: out std_logic_vector(3 downto 0)
    );
end component;
component BufferControl is
    port (
        clk: in std_logic;
        reset: in std_logic;
        Load: in std_logic;
        ACK: in std_logic;
        Wreg: out std_logic;
        OBfree: out std_logic;
        Dval: out std_logic
    );
end component;

signal Wreg_temp: std_logic;
begin
    UBufferControl: BufferControl
        port map (
            clk => clk,
            reset => reset,
            Load => Load,
            ACK => ACK, 
            Wreg => Wreg_temp, 
            OBfree => OBfree,
            Dval => Dval 
        );
        UOutputRegister: OutputRegister
            port map (
                clk => Wreg_temp,
                reset => reset,
                D => D,
                Q => Q
            );
end arc_OutputBuffer;