library ieee;
use ieee.std_logic_1164.all;

entity DECODER is
    port (
        OP0, OP1, OP2: in std_logic;
        OPA, OPB, OPC, OPD, OPE, OPF: out std_logic
    );  
end DECODER;

architecture arch_decoder of DECODER is

begin
    OPA <= OP2 and OP1; -- 001 ou 000
    OPB <= OP0;
    OPC <= OP2;
    OPD <= OP0;
    OPE <= OP1;
    OPF <= OP2 or (not OP2 and not OP1); 
end arch_decoder;