// Generator : SpinalHDL v1.10.1    git head : 2527c7c6b0fb0f95e5e1a5722a0be732b364ce43
// Component : SystolicArrayUnit
// Git hash  : b5b5155161603a17a71f18a5be5b15bcb5e6d005

`timescale 1ns/1ps 
module SystolicArrayUnit (
  input  wire [15:0]   io_inA,
  input  wire          io_inFinalA,
  input  wire [15:0]   io_inB,
  input  wire          io_inFinalB,
  output reg  [15:0]   io_outA,
  output reg           io_outFinalA,
  output reg  [15:0]   io_outB,
  output reg           io_outFinalB,
  output reg  [36:0]   io_outZ,
  input  wire          clk,
  input  wire          resetn
);

  wire       [36:0]   _zz_ProductSum;
  wire       [36:0]   _zz_io_outZ;
  wire       [31:0]   ABProduct;
  reg        [36:0]   ProductSum;
  wire                when_SystolicArrayUnit_l76;

  assign _zz_ProductSum = {{5{ABProduct[31]}}, ABProduct};
  assign _zz_io_outZ = {{5{ABProduct[31]}}, ABProduct};
  assign ABProduct = ($signed(io_inA) * $signed(io_inB));
  assign when_SystolicArrayUnit_l76 = ((io_inFinalA == 1'b1) && (io_inFinalB == 1'b1));
  always @(posedge clk or negedge resetn) begin
    if(!resetn) begin
      io_outA <= 16'h0000;
      io_outFinalA <= 1'b0;
      io_outB <= 16'h0000;
      io_outFinalB <= 1'b0;
      io_outZ <= 37'h0000000000;
      ProductSum <= 37'h0000000000;
    end else begin
      io_outA <= io_inA;
      io_outB <= io_inB;
      io_outFinalA <= io_inFinalA;
      io_outFinalB <= io_inFinalB;
      ProductSum <= ($signed(_zz_ProductSum) + $signed(ProductSum));
      if(when_SystolicArrayUnit_l76) begin
        io_outZ <= ($signed(_zz_io_outZ) + $signed(ProductSum));
        ProductSum <= 37'h0000000000;
      end
    end
  end


endmodule
