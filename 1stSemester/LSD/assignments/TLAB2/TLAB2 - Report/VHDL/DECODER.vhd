library ieee;
use ieee.std_logic_1164.all;

entity DECODER is
    port (
        OP0, OP1, OP2: in std_logic;
        OPA, OPB, OPC, 
        OPD, OPE, OPF: out std_logic
    );  
end DECODER;

architecture arch_decoder of DECODER is

begin
    OPA <= OP1;
    OPB <= OP0;
    OPC <= OP2;
    OPD <= OP0;
    OPE <= OP1;
    OPF <= OP2;
    
end arch_decoder;