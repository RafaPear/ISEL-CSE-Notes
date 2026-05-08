library ieee;
use ieee.std_logic_1164.all;

entity PENC is
  port (
    I: in std_logic_vector (3 downto 0);
    Y: out std_logic_vector (1 downto 0);
    GS: out std_logic
    );
end PENC;

architecture arc_penc of PENC is
    begin
      GS <= I(0) or I(1) or I(2) or I(3);
      Y(1) <= I(3) or I(2);
      Y(0) <= I(3) or (not I(2) and I(1));
end arc_penc;