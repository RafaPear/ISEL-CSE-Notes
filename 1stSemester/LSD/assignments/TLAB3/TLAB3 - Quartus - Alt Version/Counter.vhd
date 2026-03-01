library ieee;
use ieee.std_logic_1164.all;

entity Counter is
    port(
        D: in std_logic_vector(3 downto 0);
        PL: in std_logic;
        CE: in std_logic;
        CLK: in std_logic;
        TC: out std_logic
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

    component MUX4 is
        port(
    		A, B: in std_logic_vector (3 downto 0);
    		S: in std_logic;
    		Y: out std_logic_vector (3 downto 0)
    	);
    end component;

    component Reg4 is
        port(	
    		D : in std_logic_vector(3 downto 0);
    		SET : in std_logic;
    		RESET : in std_logic;
    		EN : in std_logic;
    		CLK : in std_logic;
    		Q : out std_logic_vector(3 downto 0)
    	);
    end component;

    signal temp_R, temp_D, temp_Q, S_MUX: std_logic_vector(3 downto 0);
    signal S_notPL, S_notPl_and_CE, temp_TC, S_CE_Nand_TC, S_PL_Pulsed, S_not_CE: std_logic;

    begin
        
        S_not_CE <= not CE;
        S_notPL <= not PL;
        S_notPl_and_CE <= S_notPL and CE;
        S_CE_Nand_TC <= CE nand temp_TC;

        

        UMUX0: MUX4 port map(
            A => temp_R,
            B => D,
            S => S_PL_Pulsed,
            Y => temp_D
        );

        UMUX1: MUX4 port map(
            A => "0000",
            B => temp_Q,
            S => S_notPl_and_CE,
            Y => S_MUX
        );

        UREG0: Reg4 port map(
            D => temp_D,
            SET => '0',
            EN => CE,
            RESET => S_not_CE,
            CLK => CLK,
            Q => temp_Q
        );

        UADDSUB0: ADDSUB port map(
            A => temp_Q,
            B(3) => '0',
            B(2) => '0',
            B(1) => '0',
            B(0) => S_CE_Nand_TC,
            Cbi => '0',
            OP => '1',
            R => temp_R
        );

        temp_TC <= (temp_Q(0) nor temp_Q(1)) nor (temp_Q(2) nor temp_Q(3));
        TC <= temp_TC;

end arc_counter;