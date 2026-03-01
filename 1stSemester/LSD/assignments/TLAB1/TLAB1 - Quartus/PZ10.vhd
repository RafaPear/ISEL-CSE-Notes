library ieee;
use ieee.std_logic_1164.all;

entity PZ10 is
    port (
        I0, I1, I2, I3, I4, I5, I6, I7, I8, I9: in std_logic;
        P: out std_logic;
        Z: out std_logic
    );
end PZ10;

architecture arc_PZ10 of PZ10 is

    component PZ4
        port (
             I0, I1, I2, I3: in std_logic;
             P: out std_logic;
             Z: out std_logic
        );
    end component;

    signal P1_temp, Z1_temp: std_logic;
    signal P2_temp, Z2_temp: std_logic;
    signal P_temp, Z3_temp: std_logic;

begin
	
    U0: PZ4
        port map (
            I0 => I0,
            I1 => I1,
            I2 => P1_temp,
            I3 => P2_temp,
            P => P_temp,
            Z => Z3_temp
        );
	
    U1: PZ4
        port map (
            I0 => I2,
            I1 => I3,
            I2 => I4,
            I3 => I5,
            P => P1_temp,
            Z => Z1_temp
        );

    U2: PZ4
        port map (
            I0 => I6,
            I1 => I7,
            I2 => I8,
            I3 => I9,
            P => P2_temp,
            Z => Z2_temp
        );


    P <= P_temp;
    Z <= (Z1_temp and Z2_temp and Z3_temp);

end arc_PZ10;