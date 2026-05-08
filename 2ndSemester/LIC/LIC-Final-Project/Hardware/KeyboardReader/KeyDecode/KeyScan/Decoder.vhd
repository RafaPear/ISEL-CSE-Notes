library ieee;
use ieee.std_logic_1164.all;

entity DECODER is
    port (
        S: in std_logic_vector(1 downto 0);
        D: out std_logic_vector(3 downto 0)
    );
end DECODER;

architecture arch_decoder of DECODER is

begin
    D(0) <= not S(0) and not S(1);
    D(1) <= S(0) and not S(1);
    D(2) <= not S(0) and S(1);
    D(3) <= S(0) and S(1);
end arch_decoder;
