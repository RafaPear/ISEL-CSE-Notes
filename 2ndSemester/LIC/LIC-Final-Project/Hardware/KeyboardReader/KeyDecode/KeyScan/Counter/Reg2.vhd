library ieee;
use ieee.std_logic_1164.all;

entity Reg2 is
	port(	
		CLK: in std_logic;
		RESET: in std_logic;
		SET: in std_logic;
		D: in std_logic_vector(1 downto 0);
		EN: in std_logic;
		Q: out std_logic_vector(1 downto 0)
		);
end Reg2;

architecture arc_reg2 of Reg2 is

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

begin 
    UFFD4: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(0),
        EN => EN,
        Q => Q(0)
    );    
    
    UFFD5: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(1),
        EN => EN,
        Q => Q(1)
    );


end arc_reg2;