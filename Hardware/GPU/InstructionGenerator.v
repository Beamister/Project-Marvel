`timescale 1ns / 1ps
//Module used to test the GPU by generating instructions for it
//Created by Luke, 12/4/2017
module InstructionGenerator(
    input Clk,
    output [34:0] Instruction
    );

	reg [7:0] programCounter = 0;
	reg [3:0] currentCode = 0;
	reg [7:0] currentIndex = 0;
	reg [22:0] currentData = 0;

	assign Instruction = {currentCode, currentIndex, currentData};

	always@(posedge Clk)
	begin
		case(programCounter)
			8'd33 ://Set the first sprite
			begin
				currentCode <= 4'd1;
				currentIndex <= 8'd0;
				currentData <= 23'd5;
			end
			8'd34 ://Set sprites x position
			begin
				currentCode <= 4'd2;
				currentIndex <= 8'd0;
				currentData <= 23'd0;
			end
			8'd35 ://Set sprites y position
			begin
				currentCode <= 4'd3;
				currentIndex <= 8'd0;
				currentData <= 23'd0;
			end
			8'd36 ://Set sprites height
			begin
				currentCode <= 4'd4;
				currentIndex <= 8'd0;
				currentData <= 23'd40;
			end
			8'd37 ://Set sprites width
			begin
				currentCode <= 4'd5;
				currentIndex <= 8'd0;
				currentData <= 23'd30;
			end
			default :
			begin
				currentCode <= 4'd0;
				currentIndex <= 8'd0;
				currentData <= 23'd0;
			end
		endcase
		programCounter <= programCounter + 1;
	end

endmodule
