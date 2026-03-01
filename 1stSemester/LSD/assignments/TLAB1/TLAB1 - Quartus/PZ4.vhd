library ieee;
use ieee.std_logic_1164.all;

entity PZ4 is
    port (
        I0, I1, I2, I3: in std_logic;
        P: out std_logic;
		  Z: out std_logic
);
end PZ4;

architecture arc_pz4 of PZ4 is
    
begin
    
    P <= (I0 xnor I1) xor (I2 xnor I3);
    Z <= (I0 nor I1) and (I2 nor I3);
    
end arc_pz4;