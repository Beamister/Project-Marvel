`timescale 1ns / 1ps

//Test fixture for testing the GPU
//Created by Luke 12/4/2017

module TestFixture;

	// Inputs
	reg Clk;

	// Outputs
	wire [2:0] Red;
	wire [2:0] Green;
	wire [1:0] Blue;
	wire HS;
	wire VS;

	// Instantiate the Unit Under Test (UUT)
	GPUTester uut (
		.Clk(Clk), 
		.Red(Red), 
		.Green(Green), 
		.Blue(Blue), 
		.HS(HS), 
		.VS(VS)
	);

	always
	begin
		#50;
		Clk = ~Clk;
	end

	initial begin
		// Initialize Inputs
		Clk = 0;

		// Wait 100 ns for global reset to finish
		#100;
      
		// Add stimulus here
		#5000;
	end
      
endmodule

