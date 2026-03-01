library ieee;
use ieee.std_logic_1164.all;

entity AU is
	port (
		A: in std_logic_vector (3 downto 0);
		B: in std_logic_vector (3 downto 0);
		CBi: in std_logic;
		OPau: in std_logic;
		R: out std_logic_vector (3 downto 0);
		CBo: out std_logic;
		OV: out std_logic
	);
end AU;

architecture arch_au of AU is

component ADDSUB is
	port (
		A: in std_logic_vector (3 downto 0);
		B: in std_logic_vector (3 downto 0);
		Cbi: in std_logic;
		OP: in std_logic;
		R: out std_logic_vector (3 downto 0);
		B3: out std_logic;
		CBo: out std_logic
	);
end component;

component FLAGS is
	port (
		A3: in std_logic;
		B3: in std_logic;
		R3: in std_logic;
		iCBo: in std_logic;
		OVo: out std_logic;
		CBo: out std_logic
	);
end component;

signal B3_temp, iCBo_temp, R3_temp: std_logic;

begin

A0: ADDSUB port map(
	A => A,
	B => B,
	OP => OPau, 
	Cbi => CBi,
	R(3) => R3_temp, R(2) => R(2), R(1) => R(1), R(0) => R(0), 
	B3 => B3_temp, CBo => iCBo_temp
	);

F0: FLAGS port map(
	A3 => A(3), B3 => B3_temp, iCBo => iCBo_temp, R3 => R3_temp,
	Ovo => OV, CBo => CBo
	);
	
R(3) <= R3_temp;
	
end arch_au;
		