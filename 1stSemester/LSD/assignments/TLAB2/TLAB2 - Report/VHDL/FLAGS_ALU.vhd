library ieee;
use ieee.std_logic_1164.all;

entity FLAGS_ALU is
    port ( 
		iOV: in std_logic;
		iCB: in std_logic;
		OP: in std_logic;
		R: in std_logic_vector(3 downto 0);
		CY: in std_logic;
		BE: out std_logic;
		GE: out std_logic;
		P: out std_logic;
		Z: out std_logic;
		OV: out std_logic;
		CB: out std_logic
    );
end FLAGS_ALU;

architecture arch_flags of FLAGS_ALU is

signal sZ: std_logic;

begin
	BE <= (R(3) or sZ) and not OP;
    GE <= (not R(3) or sZ) and not OP;
	P <= (R(0) xor R(1)) xor (R(2) xor R(3));
	sZ <= not (R(3) or R(2) or R(1) or R(0));
    OV <= iOV and not OP;
    CB <= (not OP and iCB) or (OP and CY);
	Z <= sZ;
end arch_flags;