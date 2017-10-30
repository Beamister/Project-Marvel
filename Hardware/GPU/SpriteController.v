`timescale 1ns / 1ps
////Module to hold the details for each sprite, created by Luke 10/4/2017
module SpriteController(
	 input Clk,
	 input write,
    input [3:0] Instruction,
    input signed [10:0] CurrentX,
    input signed [10:0] CurrentY,
    input [22:0] Input,
    output [22:0] memoryOutput,
	 output inRange
    );

	reg signed [10:0] xPosition = 0;
	reg signed [10:0] yPosition = 0;
	reg [9:0] width = 0;
	reg [9:0] height = 0;
	reg [22:0] memoryAddress = 0;
	reg visible = 0;
	wire signed [10:0] shift;
		
	assign inRange = (visible && CurrentX >= xPosition && CurrentX < (xPosition + width)
							&& CurrentY >= yPosition && CurrentY < (yPosition + height));
							
	assign memoryOutput = (memoryAddress > 0) ? (memoryAddress + (CurrentX - xPosition) + ((CurrentY - yPosition) * width)) : 23'd0;

	assign shift = Input[10:0];

	always@(posedge Clk)
	begin
		if(write)
		case(Instruction)
			4'd1 : //Set sprite
			begin
				memoryAddress <= Input;
				visible <= 1;
			end
			4'd2 : //Set X
			begin
				xPosition <= Input[10:0];
			end
			4'd3 : //Set Y
			begin
				yPosition <= Input[10:0];
			end
			4'd4 : //Set width
			begin
				width <= Input[9:0];
			end
			4'd5 : //Set height
			begin
				height <= Input[9:0];
			end
			4'd6 : //Shift x position
			begin
				xPosition <= xPosition + shift;
			end
			4'd7 : //Shift y position
			begin
				yPosition <= yPosition + shift;
			end
			4'd8 : //Clear the sprite
			begin
				visible <= 0;
			end
			4'd9 : 
			begin
				visible <= 1;
			end
		endcase
	end

endmodule
