library ieee;
use ieee.std_logic_1164.all;

entity Reg3 is
	port(	
		CLK: in std_logic;
		RESET: in std_logic;
		SET: in std_logic;
		D: in std_logic_vector(2 downto 0);
		EN: in std_logic;
		Q: out std_logic_vector(2 downto 0)
		);
end Reg3;

architecture arc_reg3 of Reg3 is

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
    UFFD6: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(0),
        EN => EN,
        Q => Q(0)
    );    
    
    UFFD7: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(1),
        EN => EN,
        Q => Q(1)
    );

	UFFD8: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(2),
        EN => EN,
        Q => Q(2)
    );


end arc_reg3;