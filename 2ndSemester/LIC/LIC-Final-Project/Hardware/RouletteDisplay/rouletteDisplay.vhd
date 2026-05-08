library ieee;
use ieee.std_logic_1164.all;

entity rouletteDisplay is
port(	set	: in std_logic;
		cmd	: in std_logic_vector(2 downto 0);
		data	: in std_logic_vector(4 downto 0);
		HEX0	: out std_logic_vector(7 downto 0);
		HEX1	: out std_logic_vector(7 downto 0);
		HEX2	: out std_logic_vector(7 downto 0);
		HEX3	: out std_logic_vector(7 downto 0);
		HEX4	: out std_logic_vector(7 downto 0);
		HEX5	: out std_logic_vector(7 downto 0)
		);
end rouletteDisplay;

architecture structural of rouletteDisplay is

component reg_5bit IS
PORT(	CLK : in std_logic;
		RESET : in STD_LOGIC;
		SET : in std_logic;
		D : IN STD_LOGIC_VECTOR(4 downto 0);
		EN : IN STD_LOGIC;
		Q : out STD_LOGIC_VECTOR(4 downto 0)
		);
END component;

component FFD IS
PORT(	CLK : in std_logic;
		RESET : in STD_LOGIC;
		SET : in std_logic;
		D : IN STD_LOGIC;
		EN : IN STD_LOGIC;
		Q : out std_logic
		);
END component;

component dec2hex IS
PORT(	d : IN STD_LOGIC_vector(4 downto 0);
		clear : in std_logic;
		dOut: out std_logic_vector(7 downto 0)
		);
END component;

component dec_3_8 is
port( addr 	: 	in std_logic_vector(2 downto 0);
		en		:	in std_logic;
		dout	: 	out std_logic_vector(7 downto 0)
		);
end component;

type register_array is array (0 to 5) of std_logic_vector(4 downto 0);
signal reg_values, out_values : register_array;
type seg_array is array (0 to 5) of std_logic_vector(7 downto 0);
signal display_values : seg_array;
signal en_digit : std_logic_vector(7 downto 0);
signal clear : std_logic;
begin

decoder : dec_3_8 port map(addr => cmd, en => '1', dout => en_digit);

circuit_gen : for ii in 0 to 5 generate
	in_reg 		: reg_5bit 	port map(clk => set, reset => '0' , set => '0', d => data, en => en_digit(ii), q => reg_values(ii));
	out_reg 		: reg_5bit 	port map(clk => set, reset => '0' , set => '0', d => reg_values(ii), en => en_digit(6), q => out_values(ii));
	hex_digit	: dec2hex	port map(d => out_values(ii),clear => clear, dOut => display_values(ii));
end generate circuit_gen;

clear_reg: FFD port map ( clk => set, reset => '0' , set => '0', en => en_digit(7), d => data(0), Q => clear);

HEX0 <= display_values(0);
HEX1 <= display_values(1);
HEX2 <= display_values(2);
HEX3 <= display_values(3);
HEX4 <= display_values(4);
HEX5 <= display_values(5);

end structural;