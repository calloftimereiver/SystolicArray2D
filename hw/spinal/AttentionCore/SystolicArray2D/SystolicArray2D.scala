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

  in_MatA_row_num             : Int=32,//一次性从A侧输入的数的数量，也就是输入的A矩阵的行数
  in_MatB_col_num             : Int=32,//一次性从B侧输入的数的数量，也就是输入的B矩阵的列数
  //这两个值也决定了整个脉动整列的尺寸

  in_MatA_element_Width   : Int=16,//输入的A矩阵的每个数的位宽
  in_MatB_element_Width   : Int=16,//输入的B矩阵的每个数的位宽

  in_MatA_Bus_Width       : Int=16,//输入的A矩阵的Bus宽度，也就是输入的A矩阵的列数
  in_MatB_Bus_Width       : Int=16,//输入的B矩阵的Bus宽度，也就是输入的B矩阵的行数


)
{
  val SystolicArray2DUnit_Cfg = new SystolicArray2DUnit_Config(Maxin_Length,in_MatA_element_Width,in_MatB_element_Width)
  //Z=A*B
  //根据矩阵乘法，输出的行数就是A的行数，输出的列数就是B的列数
  val outZ_row_num=in_MatA_row_num//输出的Z矩阵的行数
  val outZ_col_num=in_MatB_col_num//输出的Z矩阵的列数

}


case class SystolicArray2D(cfg: SystolicArray2D_Config) extends Component {

  // 定义组件的输入和输出接口
  val io = new Bundle {
    //输入in_MatA_Bus首先是一个stream从机，主要是考虑到进入模块之后会有buffer做缓冲
    //其次Stream的内部由多根Flow组成，考虑到bus的宽度和A的实际输入宽度可能并不一样，可能会有一部分是有效的一部分是无效的情况。
    //此处flow的valid是用来标记该位的payload是否有效。
    val in_MatA_Bus=slave(Stream(Vec(Flow(SInt(cfg.in_MatA_element_Width bits)),cfg.in_MatA_Bus_Width)))
    val in_MatB_Bus=slave(Stream(Vec(Flow(SInt(cfg.in_MatB_element_Width bits)),cfg.in_MatB_Bus_Width)))
    
/* 
    val in_MatA = in SInt(cfg.in_MatA_Width bits)
    val inFin_MatAlA = in Bool()
    val in_MatB = in SInt(cfg.in_MatB_Width bits)
    val inFin_MatAlB = in Bool()

    val outA= out(Reg(SInt(cfg.in_MatA_Width bits))) init 0
    val outFin_MatAlA = out(Reg(Bool())) init False
    val outB= out(Reg(SInt(cfg.in_MatB_Width bits))) init 0
    val outFin_MatAlB = out(Reg(Bool())) init False


    val outZ = out(Reg(SInt(cfg.outZ_Width bits))) init 0
 */
  }

  //传递数据
  io.outA:=io.in_MatA
  io.outB:=io.in_MatB
  io.outFinalA:=io.inFinalA
  io.outFinalB:=io.inFinalB


  val Latency = 1
}