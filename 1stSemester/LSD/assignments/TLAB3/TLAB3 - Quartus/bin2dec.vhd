library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity bin2dec is
  port ( bin : in std_logic_vector (7 downto 0);
         dec : out std_logic_vector (11 downto 0));
end bin2dec;

architecture Behavioral of bin2dec is
    signal zeros : std_logic_vector (11 downto 0);
	 
begin
  zeros <= (others => '0');

  b2b: process (bin, zeros)
    variable aux : std_logic_vector (19 downto 0);
    variable dig, ndig : std_logic_vector (3 downto 0);
  begin
    aux(19 downto 8) := zeros;
    aux(7 downto 0) := bin;

    for I in 0 to 2 loop 
      aux(I+8 downto 1) := aux(I+7 downto 0);
    end loop;  -- first 3 shifts do not need add3
    for I in 3 to 7 loop
      for J in 0 to 2 loop
        dig := aux(4*J+11 downto 4*J+8);
        case dig is
          when X"5" => ndig := X"8";
          when X"6" => ndig := X"9";
          when X"7" => ndig := X"A";
          when X"8" => ndig := X"B";
          when X"9" => ndig := X"C";
          when others => ndig := dig;
        end case;
        aux(4*J+11 downto 4*J+8) := ndig;
      end loop;  -- J
      aux(19 downto 1) := aux(18 downto 0);
    end loop;  -- I
    dec <= aux(19 downto 8);
	 
  end process b2b;
  
end Behavioral;	 