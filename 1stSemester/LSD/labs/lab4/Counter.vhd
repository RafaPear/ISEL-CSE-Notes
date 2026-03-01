library ieee;
use ieee.std_logic_1164.all;

entity Counter is
    port(
        D: in std_logic_vector(3 downto 0);
        PL: in std_logic;
        CE: in std_logic;
        CLK: in std_logic;
        Q: out std_logic_vector(3 downto 0)
    );
end Counter;

architecture arc_counter of Counter is 
    component ADDSUB is	
        port (
    		A: in std_logic_vector (3 downto 0);
    		B: in std_logic_vector (3 downto 0);
    		Cbi: in std_logic;
    		OP: in std_logic;
    		R: out std_logic_vector (3 downto 0)
    	);
    end component;

    component MUX is
        port(
    		A, B: in std_logic_vector (3 downto 0);
    		S: in std_logic;
    		Y: out std_logic_vector (3 downto 0)
    	);
    end component;
    component Reg is
        port(	
    		D : in std_logic_vector(3 downto 0);
    		SET : in std_logic;
    		RESET : in std_logic;
    		EN : in std_logic;
    		CLK : in std_logic;
    		Q : out std_logic_vector(3 downto 0)
    	);
    end component;

    signal temp_R, temp_D, temp_Q: std_logic_vector(3 downto 0);

    begin
        UMUX0: MUX port map(
            A => temp_R,
            B => D,
            S => PL,
            Y => temp_D
        );

        UREG0: Reg port map(
            D => temp_D,
            SET => '0',
            EN => '1',
            RESET => '0',
            CLK => CLK,
            Q => temp_Q
        );

        UADDSUB0: ADDSUB port map(
            A => temp_Q,
            B(3) => '0',
            B(2) => '0',
            B(1) => '0',
            B(0) => CE,
            Cbi => '0',
            OP => '1',
            R => temp_R
        );

        Q <= temp_Q;

end arc_counter;