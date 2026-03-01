library ieee;
use ieee.std_logic_1164.all;

entity Reg is
	port(	
		CLK : in std_logic;
		RESET : in std_logic;
		SET : in std_logic;
		D : in std_logic_vector(3 downto 0);
		EN : in std_logic;
		Q : out std_logic_vector(3 downto 0)
		);
end Reg;

architecture arc_reg of Reg is

component FFD is
    port(	
	    CLK : in std_logic;
	    RESET : in std_logic;
	    SET : in std_logic;
	    D : in std_logic;
	    EN : in std_logic;
	    Q : out std_logic
    );
end component;

begin 
    UFFD0: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(0),
        EN => EN,
        Q => Q(0)
    );    
    
    UFFD1: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(1),
        EN => EN,
        Q => Q(1)
    );    
    
    UFFD2: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(2),
        EN => EN,
        Q => Q(2)
    );  

    UFFD3: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(3),
        EN => EN,
        Q => Q(3)
    ); 

end arc_reg;