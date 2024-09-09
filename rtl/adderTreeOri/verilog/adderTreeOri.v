// Generator : SpinalHDL v1.10.1    git head : 2527c7c6b0fb0f95e5e1a5722a0be732b364ce43
// Component : adderTreeOri
// Git hash  : b5b5155161603a17a71f18a5be5b15bcb5e6d005

`timescale 1ns/1ps 
module adderTreeOri (
  input  wire [19:0]   io_xin_0,
  input  wire [19:0]   io_xin_1,
  input  wire [19:0]   io_xin_2,
  input  wire [19:0]   io_xin_3,
  input  wire [19:0]   io_xin_4,
  input  wire [19:0]   io_xin_5,
  input  wire [19:0]   io_xin_6,
  input  wire [19:0]   io_xin_7,
  input  wire [19:0]   io_xin_8,
  input  wire [19:0]   io_xin_9,
  input  wire [19:0]   io_xin_10,
  input  wire [19:0]   io_xin_11,
  input  wire [19:0]   io_xin_12,
  input  wire [19:0]   io_xin_13,
  input  wire [19:0]   io_xin_14,
  input  wire [19:0]   io_xin_15,
  input  wire [19:0]   io_xin_16,
  input  wire [19:0]   io_xin_17,
  input  wire [19:0]   io_xin_18,
  input  wire [19:0]   io_xin_19,
  input  wire [19:0]   io_xin_20,
  input  wire [19:0]   io_xin_21,
  input  wire [19:0]   io_xin_22,
  input  wire [19:0]   io_xin_23,
  input  wire [19:0]   io_xin_24,
  input  wire [19:0]   io_xin_25,
  input  wire [19:0]   io_xin_26,
  input  wire [19:0]   io_xin_27,
  input  wire [19:0]   io_xin_28,
  input  wire [19:0]   io_xin_29,
  input  wire [19:0]   io_xin_30,
  input  wire [19:0]   io_xin_31,
  input  wire [19:0]   io_xin_32,
  input  wire [19:0]   io_xin_33,
  input  wire [19:0]   io_xin_34,
  input  wire [19:0]   io_xin_35,
  input  wire [19:0]   io_xin_36,
  input  wire [19:0]   io_xin_37,
  input  wire [19:0]   io_xin_38,
  input  wire [19:0]   io_xin_39,
  input  wire [19:0]   io_xin_40,
  input  wire [19:0]   io_xin_41,
  input  wire [19:0]   io_xin_42,
  input  wire [19:0]   io_xin_43,
  input  wire [19:0]   io_xin_44,
  input  wire [19:0]   io_xin_45,
  input  wire [19:0]   io_xin_46,
  input  wire [19:0]   io_xin_47,
  input  wire [19:0]   io_xin_48,
  input  wire [19:0]   io_xin_49,
  input  wire [19:0]   io_xin_50,
  input  wire [19:0]   io_xin_51,
  input  wire [19:0]   io_xin_52,
  input  wire [19:0]   io_xin_53,
  input  wire [19:0]   io_xin_54,
  input  wire [19:0]   io_xin_55,
  input  wire [19:0]   io_xin_56,
  input  wire [19:0]   io_xin_57,
  input  wire [19:0]   io_xin_58,
  input  wire [19:0]   io_xin_59,
  input  wire [19:0]   io_xin_60,
  input  wire [19:0]   io_xin_61,
  input  wire [19:0]   io_xin_62,
  input  wire [19:0]   io_xin_63,
  output reg  [25:0]   io_xout,
  input  wire          clk,
  input  wire          resetn
);

  wire       [21:0]   _zz_xstg1_0;
  wire       [21:0]   _zz_xstg1_0_1;
  wire       [21:0]   _zz_xstg1_0_2;
  wire       [21:0]   _zz_xstg1_0_3;
  wire       [21:0]   _zz_xstg1_0_4;
  wire       [21:0]   _zz_xstg1_0_5;
  wire       [21:0]   _zz_xstg1_1;
  wire       [21:0]   _zz_xstg1_1_1;
  wire       [21:0]   _zz_xstg1_1_2;
  wire       [21:0]   _zz_xstg1_1_3;
  wire       [21:0]   _zz_xstg1_1_4;
  wire       [21:0]   _zz_xstg1_1_5;
  wire       [21:0]   _zz_xstg1_2;
  wire       [21:0]   _zz_xstg1_2_1;
  wire       [21:0]   _zz_xstg1_2_2;
  wire       [21:0]   _zz_xstg1_2_3;
  wire       [21:0]   _zz_xstg1_2_4;
  wire       [21:0]   _zz_xstg1_2_5;
  wire       [21:0]   _zz_xstg1_3;
  wire       [21:0]   _zz_xstg1_3_1;
  wire       [21:0]   _zz_xstg1_3_2;
  wire       [21:0]   _zz_xstg1_3_3;
  wire       [21:0]   _zz_xstg1_3_4;
  wire       [21:0]   _zz_xstg1_3_5;
  wire       [21:0]   _zz_xstg1_4;
  wire       [21:0]   _zz_xstg1_4_1;
  wire       [21:0]   _zz_xstg1_4_2;
  wire       [21:0]   _zz_xstg1_4_3;
  wire       [21:0]   _zz_xstg1_4_4;
  wire       [21:0]   _zz_xstg1_4_5;
  wire       [21:0]   _zz_xstg1_5;
  wire       [21:0]   _zz_xstg1_5_1;
  wire       [21:0]   _zz_xstg1_5_2;
  wire       [21:0]   _zz_xstg1_5_3;
  wire       [21:0]   _zz_xstg1_5_4;
  wire       [21:0]   _zz_xstg1_5_5;
  wire       [21:0]   _zz_xstg1_6;
  wire       [21:0]   _zz_xstg1_6_1;
  wire       [21:0]   _zz_xstg1_6_2;
  wire       [21:0]   _zz_xstg1_6_3;
  wire       [21:0]   _zz_xstg1_6_4;
  wire       [21:0]   _zz_xstg1_6_5;
  wire       [21:0]   _zz_xstg1_7;
  wire       [21:0]   _zz_xstg1_7_1;
  wire       [21:0]   _zz_xstg1_7_2;
  wire       [21:0]   _zz_xstg1_7_3;
  wire       [21:0]   _zz_xstg1_7_4;
  wire       [21:0]   _zz_xstg1_7_5;
  wire       [21:0]   _zz_xstg1_8;
  wire       [21:0]   _zz_xstg1_8_1;
  wire       [21:0]   _zz_xstg1_8_2;
  wire       [21:0]   _zz_xstg1_8_3;
  wire       [21:0]   _zz_xstg1_8_4;
  wire       [21:0]   _zz_xstg1_8_5;
  wire       [21:0]   _zz_xstg1_9;
  wire       [21:0]   _zz_xstg1_9_1;
  wire       [21:0]   _zz_xstg1_9_2;
  wire       [21:0]   _zz_xstg1_9_3;
  wire       [21:0]   _zz_xstg1_9_4;
  wire       [21:0]   _zz_xstg1_9_5;
  wire       [21:0]   _zz_xstg1_10;
  wire       [21:0]   _zz_xstg1_10_1;
  wire       [21:0]   _zz_xstg1_10_2;
  wire       [21:0]   _zz_xstg1_10_3;
  wire       [21:0]   _zz_xstg1_10_4;
  wire       [21:0]   _zz_xstg1_10_5;
  wire       [21:0]   _zz_xstg1_11;
  wire       [21:0]   _zz_xstg1_11_1;
  wire       [21:0]   _zz_xstg1_11_2;
  wire       [21:0]   _zz_xstg1_11_3;
  wire       [21:0]   _zz_xstg1_11_4;
  wire       [21:0]   _zz_xstg1_11_5;
  wire       [21:0]   _zz_xstg1_12;
  wire       [21:0]   _zz_xstg1_12_1;
  wire       [21:0]   _zz_xstg1_12_2;
  wire       [21:0]   _zz_xstg1_12_3;
  wire       [21:0]   _zz_xstg1_12_4;
  wire       [21:0]   _zz_xstg1_12_5;
  wire       [21:0]   _zz_xstg1_13;
  wire       [21:0]   _zz_xstg1_13_1;
  wire       [21:0]   _zz_xstg1_13_2;
  wire       [21:0]   _zz_xstg1_13_3;
  wire       [21:0]   _zz_xstg1_13_4;
  wire       [21:0]   _zz_xstg1_13_5;
  wire       [21:0]   _zz_xstg1_14;
  wire       [21:0]   _zz_xstg1_14_1;
  wire       [21:0]   _zz_xstg1_14_2;
  wire       [21:0]   _zz_xstg1_14_3;
  wire       [21:0]   _zz_xstg1_14_4;
  wire       [21:0]   _zz_xstg1_14_5;
  wire       [21:0]   _zz_xstg1_15;
  wire       [21:0]   _zz_xstg1_15_1;
  wire       [21:0]   _zz_xstg1_15_2;
  wire       [21:0]   _zz_xstg1_15_3;
  wire       [21:0]   _zz_xstg1_15_4;
  wire       [21:0]   _zz_xstg1_15_5;
  wire       [23:0]   _zz_xstg2_0;
  wire       [23:0]   _zz_xstg2_0_1;
  wire       [23:0]   _zz_xstg2_0_2;
  wire       [23:0]   _zz_xstg2_0_3;
  wire       [23:0]   _zz_xstg2_0_4;
  wire       [23:0]   _zz_xstg2_0_5;
  wire       [23:0]   _zz_xstg2_1;
  wire       [23:0]   _zz_xstg2_1_1;
  wire       [23:0]   _zz_xstg2_1_2;
  wire       [23:0]   _zz_xstg2_1_3;
  wire       [23:0]   _zz_xstg2_1_4;
  wire       [23:0]   _zz_xstg2_1_5;
  wire       [23:0]   _zz_xstg2_2;
  wire       [23:0]   _zz_xstg2_2_1;
  wire       [23:0]   _zz_xstg2_2_2;
  wire       [23:0]   _zz_xstg2_2_3;
  wire       [23:0]   _zz_xstg2_2_4;
  wire       [23:0]   _zz_xstg2_2_5;
  wire       [23:0]   _zz_xstg2_3;
  wire       [23:0]   _zz_xstg2_3_1;
  wire       [23:0]   _zz_xstg2_3_2;
  wire       [23:0]   _zz_xstg2_3_3;
  wire       [23:0]   _zz_xstg2_3_4;
  wire       [23:0]   _zz_xstg2_3_5;
  wire       [25:0]   _zz_io_xout;
  wire       [25:0]   _zz_io_xout_1;
  wire       [25:0]   _zz_io_xout_2;
  wire       [25:0]   _zz_io_xout_3;
  wire       [25:0]   _zz_io_xout_4;
  wire       [25:0]   _zz_io_xout_5;
  reg        [21:0]   xstg1_0;
  reg        [21:0]   xstg1_1;
  reg        [21:0]   xstg1_2;
  reg        [21:0]   xstg1_3;
  reg        [21:0]   xstg1_4;
  reg        [21:0]   xstg1_5;
  reg        [21:0]   xstg1_6;
  reg        [21:0]   xstg1_7;
  reg        [21:0]   xstg1_8;
  reg        [21:0]   xstg1_9;
  reg        [21:0]   xstg1_10;
  reg        [21:0]   xstg1_11;
  reg        [21:0]   xstg1_12;
  reg        [21:0]   xstg1_13;
  reg        [21:0]   xstg1_14;
  reg        [21:0]   xstg1_15;
  reg        [23:0]   xstg2_0;
  reg        [23:0]   xstg2_1;
  reg        [23:0]   xstg2_2;
  reg        [23:0]   xstg2_3;

  assign _zz_xstg1_0 = ($signed(_zz_xstg1_0_1) + $signed(_zz_xstg1_0_4));
  assign _zz_xstg1_0_1 = ($signed(_zz_xstg1_0_2) + $signed(_zz_xstg1_0_3));
  assign _zz_xstg1_0_2 = {{2{io_xin_0[19]}}, io_xin_0};
  assign _zz_xstg1_0_3 = {{2{io_xin_1[19]}}, io_xin_1};
  assign _zz_xstg1_0_4 = {{2{io_xin_2[19]}}, io_xin_2};
  assign _zz_xstg1_0_5 = {{2{io_xin_3[19]}}, io_xin_3};
  assign _zz_xstg1_1 = ($signed(_zz_xstg1_1_1) + $signed(_zz_xstg1_1_4));
  assign _zz_xstg1_1_1 = ($signed(_zz_xstg1_1_2) + $signed(_zz_xstg1_1_3));
  assign _zz_xstg1_1_2 = {{2{io_xin_4[19]}}, io_xin_4};
  assign _zz_xstg1_1_3 = {{2{io_xin_5[19]}}, io_xin_5};
  assign _zz_xstg1_1_4 = {{2{io_xin_6[19]}}, io_xin_6};
  assign _zz_xstg1_1_5 = {{2{io_xin_7[19]}}, io_xin_7};
  assign _zz_xstg1_2 = ($signed(_zz_xstg1_2_1) + $signed(_zz_xstg1_2_4));
  assign _zz_xstg1_2_1 = ($signed(_zz_xstg1_2_2) + $signed(_zz_xstg1_2_3));
  assign _zz_xstg1_2_2 = {{2{io_xin_8[19]}}, io_xin_8};
  assign _zz_xstg1_2_3 = {{2{io_xin_9[19]}}, io_xin_9};
  assign _zz_xstg1_2_4 = {{2{io_xin_10[19]}}, io_xin_10};
  assign _zz_xstg1_2_5 = {{2{io_xin_11[19]}}, io_xin_11};
  assign _zz_xstg1_3 = ($signed(_zz_xstg1_3_1) + $signed(_zz_xstg1_3_4));
  assign _zz_xstg1_3_1 = ($signed(_zz_xstg1_3_2) + $signed(_zz_xstg1_3_3));
  assign _zz_xstg1_3_2 = {{2{io_xin_12[19]}}, io_xin_12};
  assign _zz_xstg1_3_3 = {{2{io_xin_13[19]}}, io_xin_13};
  assign _zz_xstg1_3_4 = {{2{io_xin_14[19]}}, io_xin_14};
  assign _zz_xstg1_3_5 = {{2{io_xin_15[19]}}, io_xin_15};
  assign _zz_xstg1_4 = ($signed(_zz_xstg1_4_1) + $signed(_zz_xstg1_4_4));
  assign _zz_xstg1_4_1 = ($signed(_zz_xstg1_4_2) + $signed(_zz_xstg1_4_3));
  assign _zz_xstg1_4_2 = {{2{io_xin_16[19]}}, io_xin_16};
  assign _zz_xstg1_4_3 = {{2{io_xin_17[19]}}, io_xin_17};
  assign _zz_xstg1_4_4 = {{2{io_xin_18[19]}}, io_xin_18};
  assign _zz_xstg1_4_5 = {{2{io_xin_19[19]}}, io_xin_19};
  assign _zz_xstg1_5 = ($signed(_zz_xstg1_5_1) + $signed(_zz_xstg1_5_4));
  assign _zz_xstg1_5_1 = ($signed(_zz_xstg1_5_2) + $signed(_zz_xstg1_5_3));
  assign _zz_xstg1_5_2 = {{2{io_xin_20[19]}}, io_xin_20};
  assign _zz_xstg1_5_3 = {{2{io_xin_21[19]}}, io_xin_21};
  assign _zz_xstg1_5_4 = {{2{io_xin_22[19]}}, io_xin_22};
  assign _zz_xstg1_5_5 = {{2{io_xin_23[19]}}, io_xin_23};
  assign _zz_xstg1_6 = ($signed(_zz_xstg1_6_1) + $signed(_zz_xstg1_6_4));
  assign _zz_xstg1_6_1 = ($signed(_zz_xstg1_6_2) + $signed(_zz_xstg1_6_3));
  assign _zz_xstg1_6_2 = {{2{io_xin_24[19]}}, io_xin_24};
  assign _zz_xstg1_6_3 = {{2{io_xin_25[19]}}, io_xin_25};
  assign _zz_xstg1_6_4 = {{2{io_xin_26[19]}}, io_xin_26};
  assign _zz_xstg1_6_5 = {{2{io_xin_27[19]}}, io_xin_27};
  assign _zz_xstg1_7 = ($signed(_zz_xstg1_7_1) + $signed(_zz_xstg1_7_4));
  assign _zz_xstg1_7_1 = ($signed(_zz_xstg1_7_2) + $signed(_zz_xstg1_7_3));
  assign _zz_xstg1_7_2 = {{2{io_xin_28[19]}}, io_xin_28};
  assign _zz_xstg1_7_3 = {{2{io_xin_29[19]}}, io_xin_29};
  assign _zz_xstg1_7_4 = {{2{io_xin_30[19]}}, io_xin_30};
  assign _zz_xstg1_7_5 = {{2{io_xin_31[19]}}, io_xin_31};
  assign _zz_xstg1_8 = ($signed(_zz_xstg1_8_1) + $signed(_zz_xstg1_8_4));
  assign _zz_xstg1_8_1 = ($signed(_zz_xstg1_8_2) + $signed(_zz_xstg1_8_3));
  assign _zz_xstg1_8_2 = {{2{io_xin_32[19]}}, io_xin_32};
  assign _zz_xstg1_8_3 = {{2{io_xin_33[19]}}, io_xin_33};
  assign _zz_xstg1_8_4 = {{2{io_xin_34[19]}}, io_xin_34};
  assign _zz_xstg1_8_5 = {{2{io_xin_35[19]}}, io_xin_35};
  assign _zz_xstg1_9 = ($signed(_zz_xstg1_9_1) + $signed(_zz_xstg1_9_4));
  assign _zz_xstg1_9_1 = ($signed(_zz_xstg1_9_2) + $signed(_zz_xstg1_9_3));
  assign _zz_xstg1_9_2 = {{2{io_xin_36[19]}}, io_xin_36};
  assign _zz_xstg1_9_3 = {{2{io_xin_37[19]}}, io_xin_37};
  assign _zz_xstg1_9_4 = {{2{io_xin_38[19]}}, io_xin_38};
  assign _zz_xstg1_9_5 = {{2{io_xin_39[19]}}, io_xin_39};
  assign _zz_xstg1_10 = ($signed(_zz_xstg1_10_1) + $signed(_zz_xstg1_10_4));
  assign _zz_xstg1_10_1 = ($signed(_zz_xstg1_10_2) + $signed(_zz_xstg1_10_3));
  assign _zz_xstg1_10_2 = {{2{io_xin_40[19]}}, io_xin_40};
  assign _zz_xstg1_10_3 = {{2{io_xin_41[19]}}, io_xin_41};
  assign _zz_xstg1_10_4 = {{2{io_xin_42[19]}}, io_xin_42};
  assign _zz_xstg1_10_5 = {{2{io_xin_43[19]}}, io_xin_43};
  assign _zz_xstg1_11 = ($signed(_zz_xstg1_11_1) + $signed(_zz_xstg1_11_4));
  assign _zz_xstg1_11_1 = ($signed(_zz_xstg1_11_2) + $signed(_zz_xstg1_11_3));
  assign _zz_xstg1_11_2 = {{2{io_xin_44[19]}}, io_xin_44};
  assign _zz_xstg1_11_3 = {{2{io_xin_45[19]}}, io_xin_45};
  assign _zz_xstg1_11_4 = {{2{io_xin_46[19]}}, io_xin_46};
  assign _zz_xstg1_11_5 = {{2{io_xin_47[19]}}, io_xin_47};
  assign _zz_xstg1_12 = ($signed(_zz_xstg1_12_1) + $signed(_zz_xstg1_12_4));
  assign _zz_xstg1_12_1 = ($signed(_zz_xstg1_12_2) + $signed(_zz_xstg1_12_3));
  assign _zz_xstg1_12_2 = {{2{io_xin_48[19]}}, io_xin_48};
  assign _zz_xstg1_12_3 = {{2{io_xin_49[19]}}, io_xin_49};
  assign _zz_xstg1_12_4 = {{2{io_xin_50[19]}}, io_xin_50};
  assign _zz_xstg1_12_5 = {{2{io_xin_51[19]}}, io_xin_51};
  assign _zz_xstg1_13 = ($signed(_zz_xstg1_13_1) + $signed(_zz_xstg1_13_4));
  assign _zz_xstg1_13_1 = ($signed(_zz_xstg1_13_2) + $signed(_zz_xstg1_13_3));
  assign _zz_xstg1_13_2 = {{2{io_xin_52[19]}}, io_xin_52};
  assign _zz_xstg1_13_3 = {{2{io_xin_53[19]}}, io_xin_53};
  assign _zz_xstg1_13_4 = {{2{io_xin_54[19]}}, io_xin_54};
  assign _zz_xstg1_13_5 = {{2{io_xin_55[19]}}, io_xin_55};
  assign _zz_xstg1_14 = ($signed(_zz_xstg1_14_1) + $signed(_zz_xstg1_14_4));
  assign _zz_xstg1_14_1 = ($signed(_zz_xstg1_14_2) + $signed(_zz_xstg1_14_3));
  assign _zz_xstg1_14_2 = {{2{io_xin_56[19]}}, io_xin_56};
  assign _zz_xstg1_14_3 = {{2{io_xin_57[19]}}, io_xin_57};
  assign _zz_xstg1_14_4 = {{2{io_xin_58[19]}}, io_xin_58};
  assign _zz_xstg1_14_5 = {{2{io_xin_59[19]}}, io_xin_59};
  assign _zz_xstg1_15 = ($signed(_zz_xstg1_15_1) + $signed(_zz_xstg1_15_4));
  assign _zz_xstg1_15_1 = ($signed(_zz_xstg1_15_2) + $signed(_zz_xstg1_15_3));
  assign _zz_xstg1_15_2 = {{2{io_xin_60[19]}}, io_xin_60};
  assign _zz_xstg1_15_3 = {{2{io_xin_61[19]}}, io_xin_61};
  assign _zz_xstg1_15_4 = {{2{io_xin_62[19]}}, io_xin_62};
  assign _zz_xstg1_15_5 = {{2{io_xin_63[19]}}, io_xin_63};
  assign _zz_xstg2_0 = ($signed(_zz_xstg2_0_1) + $signed(_zz_xstg2_0_4));
  assign _zz_xstg2_0_1 = ($signed(_zz_xstg2_0_2) + $signed(_zz_xstg2_0_3));
  assign _zz_xstg2_0_2 = {{2{xstg1_0[21]}}, xstg1_0};
  assign _zz_xstg2_0_3 = {{2{xstg1_1[21]}}, xstg1_1};
  assign _zz_xstg2_0_4 = {{2{xstg1_2[21]}}, xstg1_2};
  assign _zz_xstg2_0_5 = {{2{xstg1_3[21]}}, xstg1_3};
  assign _zz_xstg2_1 = ($signed(_zz_xstg2_1_1) + $signed(_zz_xstg2_1_4));
  assign _zz_xstg2_1_1 = ($signed(_zz_xstg2_1_2) + $signed(_zz_xstg2_1_3));
  assign _zz_xstg2_1_2 = {{2{xstg1_4[21]}}, xstg1_4};
  assign _zz_xstg2_1_3 = {{2{xstg1_5[21]}}, xstg1_5};
  assign _zz_xstg2_1_4 = {{2{xstg1_6[21]}}, xstg1_6};
  assign _zz_xstg2_1_5 = {{2{xstg1_7[21]}}, xstg1_7};
  assign _zz_xstg2_2 = ($signed(_zz_xstg2_2_1) + $signed(_zz_xstg2_2_4));
  assign _zz_xstg2_2_1 = ($signed(_zz_xstg2_2_2) + $signed(_zz_xstg2_2_3));
  assign _zz_xstg2_2_2 = {{2{xstg1_8[21]}}, xstg1_8};
  assign _zz_xstg2_2_3 = {{2{xstg1_9[21]}}, xstg1_9};
  assign _zz_xstg2_2_4 = {{2{xstg1_10[21]}}, xstg1_10};
  assign _zz_xstg2_2_5 = {{2{xstg1_11[21]}}, xstg1_11};
  assign _zz_xstg2_3 = ($signed(_zz_xstg2_3_1) + $signed(_zz_xstg2_3_4));
  assign _zz_xstg2_3_1 = ($signed(_zz_xstg2_3_2) + $signed(_zz_xstg2_3_3));
  assign _zz_xstg2_3_2 = {{2{xstg1_12[21]}}, xstg1_12};
  assign _zz_xstg2_3_3 = {{2{xstg1_13[21]}}, xstg1_13};
  assign _zz_xstg2_3_4 = {{2{xstg1_14[21]}}, xstg1_14};
  assign _zz_xstg2_3_5 = {{2{xstg1_15[21]}}, xstg1_15};
  assign _zz_io_xout = ($signed(_zz_io_xout_1) + $signed(_zz_io_xout_4));
  assign _zz_io_xout_1 = ($signed(_zz_io_xout_2) + $signed(_zz_io_xout_3));
  assign _zz_io_xout_2 = {{2{xstg2_0[23]}}, xstg2_0};
  assign _zz_io_xout_3 = {{2{xstg2_1[23]}}, xstg2_1};
  assign _zz_io_xout_4 = {{2{xstg2_2[23]}}, xstg2_2};
  assign _zz_io_xout_5 = {{2{xstg2_3[23]}}, xstg2_3};
  always @(posedge clk) begin
    xstg1_0 <= ($signed(_zz_xstg1_0) + $signed(_zz_xstg1_0_5));
    xstg1_1 <= ($signed(_zz_xstg1_1) + $signed(_zz_xstg1_1_5));
    xstg1_2 <= ($signed(_zz_xstg1_2) + $signed(_zz_xstg1_2_5));
    xstg1_3 <= ($signed(_zz_xstg1_3) + $signed(_zz_xstg1_3_5));
    xstg1_4 <= ($signed(_zz_xstg1_4) + $signed(_zz_xstg1_4_5));
    xstg1_5 <= ($signed(_zz_xstg1_5) + $signed(_zz_xstg1_5_5));
    xstg1_6 <= ($signed(_zz_xstg1_6) + $signed(_zz_xstg1_6_5));
    xstg1_7 <= ($signed(_zz_xstg1_7) + $signed(_zz_xstg1_7_5));
    xstg1_8 <= ($signed(_zz_xstg1_8) + $signed(_zz_xstg1_8_5));
    xstg1_9 <= ($signed(_zz_xstg1_9) + $signed(_zz_xstg1_9_5));
    xstg1_10 <= ($signed(_zz_xstg1_10) + $signed(_zz_xstg1_10_5));
    xstg1_11 <= ($signed(_zz_xstg1_11) + $signed(_zz_xstg1_11_5));
    xstg1_12 <= ($signed(_zz_xstg1_12) + $signed(_zz_xstg1_12_5));
    xstg1_13 <= ($signed(_zz_xstg1_13) + $signed(_zz_xstg1_13_5));
    xstg1_14 <= ($signed(_zz_xstg1_14) + $signed(_zz_xstg1_14_5));
    xstg1_15 <= ($signed(_zz_xstg1_15) + $signed(_zz_xstg1_15_5));
    xstg2_0 <= ($signed(_zz_xstg2_0) + $signed(_zz_xstg2_0_5));
    xstg2_1 <= ($signed(_zz_xstg2_1) + $signed(_zz_xstg2_1_5));
    xstg2_2 <= ($signed(_zz_xstg2_2) + $signed(_zz_xstg2_2_5));
    xstg2_3 <= ($signed(_zz_xstg2_3) + $signed(_zz_xstg2_3_5));
    io_xout <= ($signed(_zz_io_xout) + $signed(_zz_io_xout_5));
  end


endmodule
