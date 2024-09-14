// Generator : SpinalHDL v1.10.1    git head : 2527c7c6b0fb0f95e5e1a5722a0be732b364ce43
// Component : SystolicArray2DUnit
// Git hash  : d927a680082e85a7bce9084e90db181aced08c49

`timescale 1ns/1ps 
module SystolicArray2DUnit (
  input  wire [15:0]   io_inA,
  input  wire          io_inA_Final,
  input  wire [15:0]   io_inB,
  input  wire          io_inB_Final,
  input  wire          io_Go,
  output reg  [15:0]   io_outA,
  output reg           io_outA_Final,
  output reg  [15:0]   io_outB,
  output reg           io_outB_Final,
  output reg  [31:0]   io_outZ,
  input  wire          clk,
  input  wire          resetn
);

  wire       [31:0]   ABProduct;
  reg        [31:0]   ProductSum;
  wire                when_SystolicArray2DUnit_l74;
  wire                when_SystolicArray2DUnit_l82;

  assign ABProduct = ($signed(io_inA) * $signed(io_inB));
  assign when_SystolicArray2DUnit_l74 = (io_Go == 1'b1);
  assign when_SystolicArray2DUnit_l82 = ((io_inA_Final == 1'b1) && (io_inB_Final == 1'b1));
  always @(posedge clk or negedge resetn) begin
    if(!resetn) begin
      io_outA <= 16'h0000;
      io_outA_Final <= 1'b0;
      io_outB <= 16'h0000;
      io_outB_Final <= 1'b0;
      io_outZ <= 32'h00000000;
      ProductSum <= 32'h00000000;
    end else begin
      if(when_SystolicArray2DUnit_l74) begin
        io_outA <= io_inA;
        io_outB <= io_inB;
        io_outA_Final <= io_inA_Final;
        io_outB_Final <= io_inB_Final;
        ProductSum <= ($signed(ABProduct) + $signed(ProductSum));
        if(when_SystolicArray2DUnit_l82) begin
          io_outZ <= ($signed(ABProduct) + $signed(ProductSum));
          ProductSum <= 32'h00000000;
        end
      end
    end
  end


endmodule
