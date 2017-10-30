`timescale 1ns / 1ps
//Module to set the write signals and pass data to the sprite controllers
//Created by Luke, 11/4/2017
module InputDecoder(
    input [34:0] DataIn,
	 output [255:0] WriteBus,
    output [26:0] DataOut
    );
	 
	 reg [7:0] index;
	 
	 //Pass the instruction and the data onto the data out bus
	 assign DataOut = {DataIn[34:31], DataIn[22:0]};
	 //If there is an instruction on the data bu then set the corresponding write signal
	 assign WriteBus = (DataIn[34:31] > 0) ? 1 << (DataIn[30:23]) : 255'd0;

endmodule
