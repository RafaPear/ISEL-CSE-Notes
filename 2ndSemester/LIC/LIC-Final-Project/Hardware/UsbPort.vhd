-- Copyright (C) 2020  Intel Corporation. All rights reserved.
-- Your use of Intel Corporation's design tools, logic functions 
-- and other software and tools, and any partner logic 
-- functions, and any output files from any of the foregoing 
-- (including device programming or simulation files), and any 
-- associated documentation or information are expressly subject 
-- to the terms and conditions of the Intel Program License 
-- Subscription Agreement, the Intel Quartus Prime License Agreement,
-- the Intel FPGA IP License Agreement, or other applicable license
-- agreement, including, without limitation, that your use is for
-- the sole purpose of programming logic devices manufactured by
-- Intel and sold by Intel or its authorized distributors.  Please
-- refer to the applicable agreement for further details, at
-- https://fpgasoftware.intel.com/eula.

-- PROGRAM		"Quartus Prime"
-- VERSION		"Version 20.1.1 Build 720 11/11/2020 SJ Lite Edition"
-- CREATED		"Tue Mar 01 09:42:31 2022"

LIBRARY ieee;
USE ieee.std_logic_1164.all; 

LIBRARY work;

ENTITY UsbPort IS
	PORT
	(
		inputPort:  IN  STD_LOGIC_VECTOR(7 DOWNTO 0);
		outputPort :  OUT  STD_LOGIC_VECTOR(7 DOWNTO 0)
	);
END UsbPort;

ARCHITECTURE bdf_type OF UsbPort IS 

COMPONENT sld_virtual_jtag
GENERIC (lpm_type : STRING;
			sld_auto_instance_index : STRING;
			sld_instance_index : INTEGER;
			sld_ir_width : INTEGER;
			sld_sim_action : STRING;
			sld_sim_n_scan : INTEGER;
			sld_sim_total_length : INTEGER
			);
	PORT(tdo : IN STD_LOGIC;
		 ir_out : IN STD_LOGIC_VECTOR(7 DOWNTO 0);
		 tck : OUT STD_LOGIC;
		 tdi : OUT STD_LOGIC;
		 virtual_state_cdr : OUT STD_LOGIC;
		 virtual_state_sdr : OUT STD_LOGIC;
		 virtual_state_e1dr : OUT STD_LOGIC;
		 virtual_state_pdr : OUT STD_LOGIC;
		 virtual_state_e2dr : OUT STD_LOGIC;
		 virtual_state_udr : OUT STD_LOGIC;
		 virtual_state_cir : OUT STD_LOGIC;
		 virtual_state_uir : OUT STD_LOGIC;
		 tms : OUT STD_LOGIC;
		 jtag_state_tlr : OUT STD_LOGIC;
		 jtag_state_rti : OUT STD_LOGIC;
		 jtag_state_sdrs : OUT STD_LOGIC;
		 jtag_state_cdr : OUT STD_LOGIC;
		 jtag_state_sdr : OUT STD_LOGIC;
		 jtag_state_e1dr : OUT STD_LOGIC;
		 jtag_state_pdr : OUT STD_LOGIC;
		 jtag_state_e2dr : OUT STD_LOGIC;
		 jtag_state_udr : OUT STD_LOGIC;
		 jtag_state_sirs : OUT STD_LOGIC;
		 jtag_state_cir : OUT STD_LOGIC;
		 jtag_state_sir : OUT STD_LOGIC;
		 jtag_state_e1ir : OUT STD_LOGIC;
		 jtag_state_pir : OUT STD_LOGIC;
		 jtag_state_e2ir : OUT STD_LOGIC;
		 jtag_state_uir : OUT STD_LOGIC;
		 ir_in : OUT STD_LOGIC_VECTOR(7 DOWNTO 0)
	);
END COMPONENT;


BEGIN 

b2v_inst : sld_virtual_jtag
GENERIC MAP(lpm_type => "sld_virtual_jtag",
			sld_auto_instance_index => "YES",
			sld_instance_index => 0,
			sld_ir_width => 8,
			sld_sim_action => "UNUSED",
			sld_sim_n_scan => 0,
			sld_sim_total_length => 0
			)
PORT MAP(ir_out => inputPort,
		 ir_in => outputPort);

END bdf_type;