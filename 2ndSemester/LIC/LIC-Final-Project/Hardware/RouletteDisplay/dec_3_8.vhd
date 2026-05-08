library ieee;
use ieee.std_logic_1164.all;

entity dec_3_8 is
port( addr 	: 	in std_logic_vector(2 downto 0);
		en		:	in std_logic;
		dout	: 	out std_logic_vector(7 downto 0)
		);
end dec_3_8;

architecture structural of dec_3_8 is
begin

dout(0) <= en and not 	addr(2) and not 	addr(1) and not 	addr(0);
dout(1) <= en and not 	addr(2) and not 	addr(1) and 	 	addr(0);
dout(2) <= en and not 	addr(2) and 	 	addr(1) and not 	addr(0);
dout(3) <= en and not	addr(2) and 	 	addr(1) and 	 	addr(0);
dout(4) <= en and 	 	addr(2) and not 	addr(1) and not 	addr(0);
dout(5) <= en and 	 	addr(2) and not 	addr(1) and 	 	addr(0);
dout(6) <= en and 	 	addr(2) and 	 	addr(1) and not 	addr(0);
dout(7) <= en and 		addr(2) and 		addr(1) and  		addr(0);

end structural;