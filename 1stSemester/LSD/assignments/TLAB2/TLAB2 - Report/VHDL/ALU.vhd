library ieee;
use ieee.std_logic_1164.all;

entity ALU is
port ( 
	CBi: in std_logic;
	X: in std_logic_vector(3 downto 0);
	Y: in std_logic_vector(3 downto 0);
	OP: in std_logic_vector(2 downto 0);
	R: out std_logic_vector(3 downto 0);
	CBo: out std_logic;
	OV: out std_logic;
    P: out std_logic;
	Z: out std_logic;
	GE: out std_logic;
	BE: out std_logic);
end ALU;

architecture arc_alu of ALU is

component YOR0
    port ( 
        Y: in std_logic_vector(3 downto 0);
        OPa: in std_logic;
        B: out std_logic_vector(3 downto 0)
    );
	end component;

component AU
    port ( 
        A: in std_logic_vector(3 downto 0);
        B: in std_logic_vector(3 downto 0);
        CBi: in std_logic;
        OPau: in std_logic;
        R: out std_logic_vector(3 downto 0);
        CBo: out std_logic;
        OV: out std_logic
    );
end component;
  
component DECODER
    port (
        OP0, OP1, OP2: in std_logic;
        OPA, OPB, OPC, 
		OPD, OPE, OPF: out std_logic
    ); 
end component;
  
component LU
    port ( 
		A, B: in std_logic_vector(3 downto 0);
		S: in std_logic_vector(1 downto 0);
		R: out std_logic_vector(3 downto 0);
    	CY: out std_logic
    );
end component;
 
component FLAGS_ALU
    port ( 
		iOV: in std_logic;
		iCB: in std_logic;
		OP: in std_logic;
		R: in std_logic_vector(3 downto 0);
		CY: in std_logic;
		BE: out std_logic;
		GE: out std_logic;
		P: out std_logic;
		Z: out std_logic;
		OV: out std_logic;
		CB: out std_logic
    );
end component;
 
component MUX_ALU
    port ( 
		A, B: in std_logic_vector(3 downto 0);
		S: in std_logic;
		Y: out std_logic_vector(3 downto 0)
    );
end component;

  	signal sOPA, sOPB, sOPC, 
		   sOPD, sOPE, sOPF, 
		   sCBO, sOV, sP, 
		   sZ, sGE, sBE, 
		   sCY, aluCBo, aluOV: std_logic;

  	signal sYOR0, sLU, aluR, 
		   sR: std_logic_vector(3 downto 0);

begin
  	DECODER0: DECODER port map(
      	OP0 => OP(0),
		OP1 => OP(1),
		OP2 => OP(2),
		OPA => sOPA,
		OPB => sOPB,
		OPC => sOPC,
		OPD => sOPD,
		OPE => sOPE,
		OPF => sOPF
    );

	YOR00: YOR0 port map(
        Y => Y,
	    OPa => sOPA,
		B => sYOR0
    );
	 
  	AU0: AU port map(
      	A => X,
		B => sYOR0,
		CBi => CBi,
		OPau => sOPB,
		R => aluR,
		CBo => aluCBo,
		OV => aluOV
    );

	LU0: LU port map(
      	A => X,
		B => Y,
		S(0) => sOPD,
		S(1) => sOPE,
		R => sLU,
		CY => sCY
    );
	 
  	MUX0: MUX_ALU port map(
        A => aluR,
	    B => sLU,
	    S => sOPC,
	    Y => sR
    );
	 
  	FLAGS0: FLAGS_ALU port map(
      	iOV => aluOV,
		iCB => aluCBo,
		OP => sOPF,
		R => sR,
		CY => sCY,
		BE => sBE,
		GE => sGE,
        P => sP,
		Z => sZ,
		OV => sOV,
		CB => sCBO
    );
	 
	R <= sR;
	CBo <= sCBO;
	OV <= sOV;
    P <= sP;
	Z <= sZ;
	GE <= sGE;
	BE <= sBE;
	
end arc_alu;