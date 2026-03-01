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
        OPA, OPB, OPC, OPD, OPE, OPF, 
		OP0, OP1, OP2: out std_logic
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
		oGE: out std_logic;
		P: out std_logic;
		Z: out std_logic;
		oOV: out std_logic;
		oCB: out std_logic
    );
end component;
 
component MUX_ALU
    port ( 
		A, B: in std_logic_vector (3 downto 0);
		S: in std_logic;
		Y: out std_logic_vector (3 downto 0)
    );
end component;

  signal sOPA, sOPB, sOPC, sOPD, sOPE, sOPF, sCBO, sOV, sP, sZ, sGE, sBE, LabCCBo, LabCOV, sCY	: STD_LOGIC;
  signal sYOR0, sLOGCR, LabCR, sR : std_logic_vector(3 downto 0);
begin

  DECODER0: DECODER
    port map (
      	OP0 => OP(0),
		OP1 => not OP(1),
		OP0 => not OP(2),
		OPA => sOPA,
		OPB => sOPB,
		OPC => sOPC,
		OPD => sOPD,
		OPE => sOPE,
		OPF => sOPF
    );

	YOR00: YOR0
    port map (
        Y => Y,
	    OPa => sOPA,
		B => sYOR0
    );
	 
  AU0: AU
    port map (
      A => X,
		B => sYOR0,
		CBi => CBi,
		OPau => sOPB,
		R => LabCR,
		CBo => LabCCBo,
		OV => LabCOV
    );

	LU0: LU
    port map (
      A => X,
		B => Y,
		S(0) => sOPD,
		S(1) => sOPE,
		R => sLOGCR,
		CY => sCY
    );
	 
  MUX0: MUX_ALU
    port map (
        A => LabCR,
	    B => sLOGCR,
	    S => sOPc,
	    Y => sR
    );
	 
  FLAGS0: FLAGS_ALU
    port map (
      iOV => LabCOV,
		iCB => LabCCBo,
		OP => sOPF,
		R => sR,
		CY => sCY,
		BE => sBE,
		oGE => sGE,
        P => sP,
		Z => sZ,
		oOV => sOV,
		oCB => sCBO
    );
	 
		R <= sR;
		CBo <= sCBO;
		OV <= sOV;
        P <= sP;
		Z <= sZ;
		GE <= sGE;
		BE <= sBE;
  
end arc_alu;