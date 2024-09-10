package AttentionCore.SystolicArray2D
import spinal.core._
import spinal.lib._
import spinal.core
/** **************************************************************
 *
 *    SystolicArray
 *
 *    SystolicArray is a systolic array to calculate the multiplication of two matrix
 *                                                                                                                                                                
 *
 *
 * *************************************************************/
case class SystolicArray2D_Config
(
  Maxin_Length        : Int,  //最大的乘加次数，譬如要计算A(32,16)*B(16,64),此时乘加的次数就是16

  inA_row_num             : Int=32,//一次性从A侧输入的数的数量，也就是输入的A矩阵的行数
  inB_col_num             : Int=32,//一次性从B侧输入的数的数量，也就是输入的B矩阵的列数
  //这两个值也决定了整个脉动整列的尺寸

  inA_element_Width   : Int=16,//输入的A矩阵的每个数的位宽
  inB_element_Width   : Int=16,//输入的B矩阵的每个数的位宽

  inA_Bus_Width       : Int=16,//输入的A矩阵的Bus宽度，也就是输入的A矩阵的列数
  inB_Bus_Width       : Int=16,//输入的B矩阵的Bus宽度，也就是输入的B矩阵的行数


)
{
  val SystolicArray2DUnit_Cfg = new SystolicArray2DUnit_Config(Maxin_Length,inA_element_Width,inB_element_Width)
  //Z=A*B
  //根据矩阵乘法，输出的行数就是A的行数，输出的列数就是B的列数
  val outZ_row_num=inA_row_num//输出的Z矩阵的行数
  val outZ_col_num=inB_col_num//输出的Z矩阵的列数
  
}


case class SystolicArray2D(cfg: SystolicArray2D_Config) extends Component {

  // 定义组件的输入和输出接口
  val io = new Bundle {
    val inA=slave(Stream(Vec(Flow(Int,))))
/* 
    val inA = in SInt(cfg.inA_Width bits)
    val inFinalA = in Bool()
    val inB = in SInt(cfg.inB_Width bits)
    val inFinalB = in Bool()

    val outA= out(Reg(SInt(cfg.inA_Width bits))) init 0
    val outFinalA = out(Reg(Bool())) init False
    val outB= out(Reg(SInt(cfg.inB_Width bits))) init 0
    val outFinalB = out(Reg(Bool())) init False


    val outZ = out(Reg(SInt(cfg.outZ_Width bits))) init 0
 */
  }

  //传递数据
  io.outA:=io.inA
  io.outB:=io.inB
  io.outFinalA:=io.inFinalA
  io.outFinalB:=io.inFinalB


  val Latency = 1
}