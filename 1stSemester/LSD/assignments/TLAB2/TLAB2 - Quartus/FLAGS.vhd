library ieee;
use ieee.std_logic_1164.all;

entity FLAGS is
	port ( 
		A3: in std_logic;
		B3: in std_logic;
		R3: in std_logic;
		iCBo: in std_logic;
		OVo: out std_logic;
		CBo: out std_logic
	);
end FLAGS;

architecture arch_flags of FLAGS is
begin 
		OVo <= (not A3 and not B3 and R3) or (A3 and B3 and not R3);
		CBo <= iCBo;
end arch_flags;
		