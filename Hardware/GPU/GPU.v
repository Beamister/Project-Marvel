`timescale 1ns / 1ps
//The main GPU module, created by Luke, 12/4/2017
module GPU(
		input Clk,
		input [34:0] Instruction,
		input signed [10:0] X,
		input signed [10:0] Y,
		input [7:0] ColourIn,
		output [7:0] ColourOut,
		output [22:0] MemoryAddress
	);
	 
	wire [255:0] writeBus;
	wire [26:0] dataBus;
	wire [255:0] inRangeBus;
	wire [5887:0]memoryAddressBus ;
	
	assign ColourOut = ColourIn;
	
	InputDecoder inputDecoder(
		.DataIn(Instruction),
		.WriteBus(writeBus),
		.DataOut(dataBus)
	);
	
	OutputDecoder outputDecoder(
		.InRangeSprites(inRangeBus),
		.AddressInputs(memoryAddressBus),
		.AddressOutput(MemoryAddress)
	);
	
	genvar index;
	generate
	for (index = 0; index < 256; index = index + 1)
		begin : spriteControllersGen
			SpriteController spriteController(
				.Clk(Clk),
				.write(writeBus[index]),
				.Instruction(Instruction[34:31]),
				.CurrentX(X),
				.CurrentY(Y),
				.Input(Instruction[22:0]),
				.memoryOutput(memoryAddressBus[index * 23 +: 23]),
				.inRange(inRangeBus[index])
			);
		end
	endgenerate

endmodule
