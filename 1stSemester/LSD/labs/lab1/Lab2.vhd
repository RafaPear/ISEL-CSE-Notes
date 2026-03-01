LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY Lab2 IS
	PORT(A, B, C: IN STD_LOGIC;
	F: OUT STD_LOGIC);
END Lab2;

ARCHITECTURE logicFunction OF Lab2 IS
BEGIN
F <= (A AND NOT C) OR (NOT A AND B);
END logicFunction;
