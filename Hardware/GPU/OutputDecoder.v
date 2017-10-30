`timescale 1ns / 1ps
//Module to take in all the memory addresses and choose the one from the highest indexed sprite
//Created by Luke, 11/4/2017
module OutputDecoder(
    input [255:0] InRangeSprites,
    input [5887:0] AddressInputs,
    output reg [22:0] AddressOutput
    );

	reg [3:0] index = 0;
	reg [255:0] modifiedIRS = 0;
	reg set = 1;
		
	always@(index)
	begin
		AddressOutput = AddressInputs[(index * 23) +: 23];
	end
	
	always@(InRangeSprites, modifiedIRS)
	begin
		if(set == 1)
		begin
			modifiedIRS = InRangeSprites;
			set = 0;
			index = 0;
		end
		else if(set == 0 && modifiedIRS > 1)
		begin
			modifiedIRS = modifiedIRS >> 1;
			index = index + 1;
		end
		else
		begin
			set = 1;
		end
	end
	
endmodule
