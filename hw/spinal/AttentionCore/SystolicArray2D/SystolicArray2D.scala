package AttentionCore.SystolicArray2D
import spinal.core._
import spinal.lib._
import spinal.core
import spinal.core.sim._
/** **************************************************************
 *
 *    SystolicArray2D
 *
 *    SystolicArray2D is a systolic array to calculate the multiplication of two matrix
 *                                                                                                                                                                
 *
 * *************************************************************/
//该模块的主要作用是将输入的矩阵数据fifo之后调整成Systolic Array所需的形式，然后将其通入Systolic Array，然后将结果通过一个fifo然后输出
case class SystolicArray2D_Config(
  in_Length_Max        : Int,  //最大的乘加次数，譬如要计算A(32,16)*B(16,64),此时乘加的次数就是16

  in_MatA_row_num             : Int=12,//一次性从A侧输入的数的数量，也就是输入的A矩阵的行数
  in_MatB_col_num             : Int=24,//一次性从B侧输入的数的数量，也就是输入的B矩阵的列数
  //这两个值也决定了整个脉动整列的尺寸
  in_MatA_element_Width   : Int=16,//输入的A矩阵的每个数的位宽
  in_MatB_element_Width   : Int=16,//输入的B矩阵的每个数的位宽
)
{
    val SystolicArray2DUnit_Cfg=SystolicArray2DUnit_Config(
        in_Length=in_Length_Max,
        inA_Width=in_MatA_element_Width,
        inB_Width=in_MatB_element_Width)
          //Z=A*B
    //根据矩阵乘法，输出的行数就是A的行数，输出的列数就是B的列数
    val out_MatZ_row_num=in_MatA_row_num//输出的Z矩阵的行数
    val out_MatZ_col_num=in_MatB_col_num//输出的Z矩阵的列数
    val out_MatZ_element_Width=SystolicArray2DUnit_Cfg.outZ_Width
    //一次矩阵完整输出的元素数是cfg.out_MatZ_row_num*cfg.out_MatZ_col_num
    val out_MatZ_Width=out_MatZ_row_num*out_MatZ_col_num
    //待标定，现在只是占位符
    val Latency=in_Length_Max
    val IterationInterval=1
}
case class Sint_withFinalMark(element_Width:Int) extends Bundle {
  val data=SInt(element_Width bits)
  val Final = Bool()
}
case class SystolicArray2D(cfg: SystolicArray2D_Config) extends Component {
    val io = new Bundle {
        val in_MatA=slave(Stream(Vec.fill(cfg.in_MatA_row_num)(Sint_withFinalMark(cfg.in_MatA_element_Width))))
        val in_MatB=slave(Stream(Vec.fill(cfg.in_MatB_col_num)(Sint_withFinalMark(cfg.in_MatB_element_Width))))
        val out_MatZ=master(Stream(Vec.fill(cfg.out_MatZ_Width)(Sint_withFinalMark(cfg.out_MatZ_element_Width))))
    }
    for(row_index <- 0 until cfg.in_MatA_row_num)
    {
        for(col_index <- 0 until cfg.in_MatB_col_num)
        {
            val unit=SystolicArray2DUnit(cfg.SystolicArray2DUnit_Cfg)
            unit.io.inA<>io.in_MatA.payload(row_index).data
            unit.io.inA_Final<>io.in_MatA.payload(row_index).Final
            unit.io.inB<>io.in_MatB.payload(col_index).data
            unit.io.inB_Final<>io.in_MatB.payload(col_index).Final
            //io.out_MatZ.payload(row_index*cfg.in_MatB_col_num+col_index)<>
        }
    }
    
}