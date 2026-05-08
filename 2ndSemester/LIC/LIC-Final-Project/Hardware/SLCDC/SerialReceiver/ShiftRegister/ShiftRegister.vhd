library ieee;
use ieee.std_logic_1164.all;

entity ShiftRegister is
    port(
        CLK: in std_logic;
		RESET: in std_logic;
		D: in std_logic;
		EN: in std_logic;
		Q: out std_logic_vector(4 downto 0)
    );
end ShiftRegister;

architecture arc_shiftregister of ShiftRegister is
component FFD is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        SET: in std_logic;
        D: in std_logic;
        EN: in std_logic;
        Q: out std_logic
    );
end component;

signal temp_Q: std_logic_vector(4 downto 0);
signal temp_set:std_logic; 
begin
    temp_set <= '0';
--FFD_3 -> FFD_2 -> FFD_1 -> FFD_0 -> RS
-- OUT = [RS, FFD_3, FFD_2, FFD_1, FFD_0]
-- OUT(4) = RS ; OUT(3) = FFD_3 ; OUT(2) = FFD_2 ; OUT(1) = FFD_1 ; OUT(0) = FFD_0
    FFD_3: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => D,
        EN => EN,
        Q => temp_Q(3)
    );

    FFD_2: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => temp_Q(3),
        EN => EN,
        Q => temp_Q(2)
    );
    FFD_1: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => temp_Q(2),
        EN => EN,
        Q => temp_Q(1)
    );
    FFD_0 : FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => temp_Q(1),
        EN => EN,
        Q => temp_Q(0)
    );  

    RS: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => temp_Q(0),
        EN => EN,
        Q => temp_Q(4)
    );
    Q <= temp_Q;
end arc_shiftregister;        
