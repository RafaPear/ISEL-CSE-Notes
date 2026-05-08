library ieee;
use ieee.std_logic_1164.all;

entity ShiftRegister8 is
    port(
        CLK: in std_logic;
		RESET: in std_logic;
		D: in std_logic;
		EN: in std_logic;
		Q: out std_logic_vector(7 downto 0)
    );
end ShiftRegister8;

architecture arc_shiftregister8 of ShiftRegister8 is
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

signal temp_Q: std_logic_vector(7 downto 0);
signal temp_set:std_logic;
begin
    temp_set <= '0';
--FFD_3 -> FFD_2 -> FFD_1 -> FFD_0 -> RS
-- OUT = [RS, FFD_3, FFD_2, FFD_1, FFD_0]
-- OUT(4) = RS ; OUT(3) = FFD_3 ; OUT(2) = FFD_2 ; OUT(1) = FFD_1 ; OUT(0) = FFD_0
    FFD_7: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => D,
        EN => EN,
        Q => temp_Q(7)
    );

    FFD_6: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => temp_Q(7),
        EN => EN,
        Q => temp_Q(6)
    );

    FFD_5: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => temp_Q(6),
        EN => EN,
        Q => temp_Q(5)
    );

    FFD_4: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => temp_Q(5),
        EN => EN,
        Q => temp_Q(4)
    );

    FFD_3: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => temp_set,
        D => temp_Q(4),
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

    Q <= temp_Q;
end arc_shiftregister8;
