library ieee;
use ieee.std_logic_1164.all;

entity YOR0 is
    port (
        Y: in std_logic_vector (3 downto 0);
        OPa: in std_logic;
        B: out std_logic_vector (3 downto 0)
    );
end YOR0;

architecture arch_Yor0 of YOR0 is

begin
    B(0) <= Y(0) and OPa;
    B(1) <= Y(1) and OPa;
    B(2) <= Y(2) and OPa;
    B(3) <= Y(3) and OPa;

end arch_Yor0;