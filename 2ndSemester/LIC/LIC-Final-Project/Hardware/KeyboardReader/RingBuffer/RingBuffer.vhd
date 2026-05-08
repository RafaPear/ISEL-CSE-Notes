library ieee;
use ieee.std_logic_1164.all;

entity RingBuffer is
    port(
        clk: in std_logic;
        reset: in std_logic;
        CTS: in std_logic;
        DAV: in std_logic;
        D: in std_logic_vector(3 downto 0);
        Wreg: out std_logic;
        Q: out std_logic_vector(3 downto 0);
        DAC: out std_logic
    );
end RingBuffer;

architecture ringBuffer_arch of RingBuffer is

component RingBufferControl is
    port(
        clk: in std_logic;
        reset: in std_logic;
        DAV: in std_logic;
        CTS: in std_logic;
        full: in std_logic;
        empty: in std_logic;
        DAC: out std_logic;
        selPG: out std_logic;
        wr: out std_logic;
        W_reg: out std_logic;
        incPut: out std_logic;
        incGet: out std_logic
    );
end component;

component RAM is
    generic(
        ADDRESS_WIDTH : natural := 3;
        DATA_WIDTH : natural := 4
    );
    port(
        address : in std_logic_vector(ADDRESS_WIDTH - 1 downto 0);
        wr: in std_logic;
        din: in std_logic_vector(DATA_WIDTH - 1 downto 0);
        dout: out std_logic_vector(DATA_WIDTH - 1 downto 0)
    );
end component;

component MAC is
    port(
        clk: in std_logic;
        reset: in std_logic;
        incPut: in std_logic;
        incGet: in std_logic;
        PUTGET: in std_logic;
        ADDR: out std_logic_vector(3 downto 0);
        full: out std_logic;
        empty: out std_logic
    );
end component;
signal full_temp, empty_temp, Wr_temp, selPG_temp, incPut_temp, incGet_temp: std_logic;
signal address_temp: std_logic_vector(3 downto 0);
begin
    URingBufferControl: RingBufferControl
        port map(
            clk => clk,
            reset => reset,
            DAV => DAV,
            CTS => CTS,
            full => full_temp,
            empty => empty_temp,
            DAC => DAC,
            selPG => selPG_temp,
            wr => Wr_temp,
            W_reg => Wreg,
            incPut => incPut_temp,
            incGet => incGet_temp
        );
    UMAC: MAC
        port map(
            clk => clk,
            reset => reset,
            incPut => incPut_temp,
            incGet => incGet_temp,
            PUTGET => selPG_temp,
            ADDR => address_temp,
            full => full_temp,
            empty => empty_temp
        );
    URAM: RAM 
        generic map (
        ADDRESS_WIDTH => 4,
        DATA_WIDTH    => 4
        )
        port map(
            address => address_temp,
            wr => Wr_temp,
            din => D,
            dout => Q
        );
end ringBuffer_arch;