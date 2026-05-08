LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY reg_5bit IS
PORT(	CLK : in std_logic;
		RESET : in STD_LOGIC;
		SET : in std_logic;
		D : IN STD_LOGIC_VECTOR(4 downto 0);
		EN : IN STD_LOGIC;
		Q : out STD_LOGIC_VECTOR(4 downto 0)
		);
END reg_5bit;

ARCHITECTURE reg_5bit_arch OF reg_5bit IS
component FFD IS
PORT(	CLK : in std_logic;
		RESET : in STD_LOGIC;
		SET : in std_logic;
		D : IN STD_LOGIC;
		EN : IN STD_LOGIC;
		Q : out std_logic
		);
END component;
BEGIN

U0: FFD port map ( CLK => CLK, RESET => RESET, SET => SET, EN => EN, D => D(0), Q => Q(0));
U1: FFD port map ( CLK => CLK, RESET => RESET, SET => SET, EN => EN, D => D(1), Q => Q(1));
U2: FFD port map ( CLK => CLK, RESET => RESET, SET => SET, EN => EN, D => D(2), Q => Q(2));
U3: FFD port map ( CLK => CLK, RESET => RESET, SET => SET, EN => EN, D => D(3), Q => Q(3));
U4: FFD port map ( CLK => CLK, RESET => RESET, SET => SET, EN => EN, D => D(4), Q => Q(4));

end reg_5bit_arch;