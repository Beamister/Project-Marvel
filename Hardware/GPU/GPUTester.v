`timescale 1ns / 1ps
//Module for testing the GPU, created by Luke 10/4/2017
module GPUTester(
		input Clk,
		output [2:0] Red,
		output [2:0] Green,
		output [1:0] Blue,
		output HS,
		output VS
	);
	
	wire blank = 0;
	wire signed [10:0] currentX = 0;
	wire signed [10:0] currentY = 0;
	wire [22:0] memoryAddress = 0;
	wire [7:0] colour = 0;
	wire [34:0] currentInstruction = 0;
	
	assign colour = blank ? 8'd0 : memoryAddress[7:0];
	
	InstructionGenerator tester(
		.Clk(Clk),
		.Instruction(currentInstruction)
	);
	
	GPU gpu(
		.Clk(Clk),
		.Instruction(currentInstruction),
		.X(currentX),
		.Y(currentY),
		.ColourIn(colour),
		.ColourOut(colour),
		.MemoryAddress(memoryAddress)
	);

	VGAController vga(
		.Clk(Clk),
		.ColourIn(colour),
		.hs(HS),
		.vs(VS),
		.Blank(blank),
		.red(Red),
		.green(Green),
		.blue(Blue),
		.x(currentX),
		.y(currentY)
	);

endmodule
