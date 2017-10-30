`timescale 1ns / 1ps
//Controller module for VGA, designed for 25MHz clock
//Designed by Luke, 8/2/17
module VGAController(
		input Clk,
		input [7:0] ColourIn,
		output hs, vs, Blank,
		output [2:0] red, green,
		output [1:0] blue,
		output [10:0] x,
		output [10:0] y
    );

	reg [9:0] xc = 0;
	reg [9:0] yc = 0;
   reg  vga_clk = 0;

	assign Blank = ((xc < 10'd143) || (xc > 10'd783) || (yc < 10'd34) || (yc > 10'd514));
	assign hs = (xc > 10'd95);
	assign vs = (yc > 10'd1);
	assign x = ((xc < 10'd143) ? 0 : (xc - 10'd143));
	assign y = ((yc < 34 || yc > 514) ? 0 : (yc - 10'd34));
	assign red = ColourIn[7:5];
	assign green = ColourIn[4:2];
	assign blue = ColourIn[1:0];

	initial
	begin
		xc = 0;
		yc = 0;
    vga_clk = 0;
	end

  always @(posedge Clk)
  begin
   vga_clk = ~vga_clk;
  end

	always@(posedge vga_clk)
	begin
		if(xc >= 10'd799)
		begin
			xc <= 0;
			yc <= yc + 1;
		end
		else
		begin
			xc <= xc + 1;
		end
		if(yc >= 10'd524)
		begin
			yc <= 0;
		end
	end

endmodule
