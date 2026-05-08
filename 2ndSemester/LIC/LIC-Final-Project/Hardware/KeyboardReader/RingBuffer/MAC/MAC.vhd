library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity MAC is
    Port (
        clk: in std_logic;
        reset: in std_logic;
        incPut: in std_logic;
        incGet: in std_logic;
        PUTGET: in std_logic;
        ADDR: out std_logic_vector(3 downto 0);
        full: out std_logic;
        empty: out std_logic
    );
end MAC;

architecture arc_MAC of MAC is

component ADDER4 is
    port(
        A: in std_logic_vector (3 downto 0);
        B: in std_logic_vector (3 downto 0);
        CIN: in std_logic;
        S: out std_logic_vector (3 downto 0);
        COUT: out std_logic
    );
end component;

component Reg4 is
    port(
		CLK: in std_logic;
		RESET: in std_logic;
		SET: in std_logic;
		D: in std_logic_vector(3 downto 0);
		EN: in std_logic;
		Q: out std_logic_vector(3 downto 0)
    );
end component;

component MUX_4 is
    port(
		Se: in std_logic;
		A: in std_logic_vector(3 downto 0);
		B: in std_logic_vector(3 downto 0);
		OUTPUT: out std_logic_vector(3 downto 0)
    );
end component;

component Equality is
    port(
        A: in std_logic_vector(3 downto 0);
        B: in std_logic_vector(3 downto 0);
        O: out std_logic
    );
end component;

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

signal adder_out, mux_out, putReg_out, getReg_out: std_logic_vector (3 downto 0);
signal carry, put_msb_sig, get_msb_sig : std_logic;
signal put_msb_next, get_msb_next : std_logic;
signal equ_addr : std_logic;

begin

UADDER4: ADDER4 port map(
    A => mux_out,
    B => "0000",
    CIN => '1',
    S => adder_out,
    COUT => carry
);

put_msb_next <= put_msb_sig xor carry;
get_msb_next <= get_msb_sig xor carry;

PUT_REG4: Reg4 port map(
        CLK => clk,
		RESET => reset,
		SET => '0',
		D => adder_out,
		EN => incPut,
		Q => putReg_out
);

GET_REG4: Reg4 port map(
        CLK => clk,
                RESET => reset,
                SET => '0',
                D => adder_out,
                EN => incGet,
                Q => getReg_out
);

PUT_MSB_FF: FFD port map(
    CLK => clk,
    RESET => reset,
    SET => '0',
    D => put_msb_next,
    EN => incPut,
    Q => put_msb_sig
);

GET_MSB_FF: FFD port map(
    CLK => clk,
    RESET => reset,
    SET => '0',
    D => get_msb_next,
    EN => incGet,
    Q => get_msb_sig
);

UMUX_4: MUX_4 port map(
    Se => PUTGET,
    A => putREG_out,
    B => getREG_out,
    OUTPUT => mux_out
);

UEQ: Equality port map(
    A => putReg_out,
    B => getReg_out,
    O => equ_addr
);

full <= equ_addr and (put_msb_sig xor get_msb_sig);
empty <= equ_addr and not(put_msb_sig xor get_msb_sig);

ADDR <= mux_out;
end arc_MAC;