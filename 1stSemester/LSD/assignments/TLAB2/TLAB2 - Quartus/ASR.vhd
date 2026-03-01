library ieee;
use ieee.std_logic_1164.all;

entity ASR is
port(
A: in std_logic_vector (3 downto 0);
O: out std_logic_vector (3 downto 0);
CY: out std_logic
);
end ASR;

architecture arc_asr of ASR is

begin

CY <= A(0);
O(0) <= A(1);
O(1) <= A(2);
O(2) <= A(3);
O(3) <= A(3);

end arc_asr;