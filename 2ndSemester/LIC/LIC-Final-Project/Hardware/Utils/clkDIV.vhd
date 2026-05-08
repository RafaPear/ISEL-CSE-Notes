library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;
  
entity CLKDIV is
generic(div: natural := 13);
port ( 
	clk_in: in std_logic;
	clk_out: out std_logic);
end CLKDIV;
  
architecture bhv of CLKDIV is
  
signal count: integer:= 1;
signal tmp: std_logic:= '0';
  
begin

process(clk_in)
begin

	if (clk_in'event and clk_in='1') then
		count <=count+1;
	if (count = div/2) then
		tmp <= NOT tmp;
		count <= 1;
	end if;
	end if;
end process;

clk_out <= tmp;
end bhv;