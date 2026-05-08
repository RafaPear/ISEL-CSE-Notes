library ieee;
use ieee.std_logic_1164.all;

entity Reg8 is
	port(	
		CLK: in std_logic;
		RESET: in std_logic;
		SET: in std_logic;
		D: in std_logic_vector(7 downto 0);
		EN: in std_logic;
		Q: out std_logic_vector(7 downto 0)
		);
end Reg8;

architecture arc_reg8 of Reg8 is

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
    UFFD13: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(0),
        EN => EN,
        Q => Q(0)
    );    
    
    UFFD14: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(1),
        EN => EN,
        Q => Q(1)
    );

	UFFD15: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(2),
        EN => EN,
        Q => Q(2)
    );

	UFFD16: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(3),
        EN => EN,
        Q => Q(3)
    );

	UFFD17: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(4),
        EN => EN,
        Q => Q(4)
    );

	UFFD18: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(5),
        EN => EN,
        Q => Q(5)
    );

    UFFD19: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(6),
        EN => EN,
        Q => Q(6)
    );

    UFFD20: FFD port map(
        CLK => CLK,
        RESET => RESET,
        SET => SET,
        D => D(7),
        EN => EN,
        Q => Q(7)
    );

end arc_reg8;