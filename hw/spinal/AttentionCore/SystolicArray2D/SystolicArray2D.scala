package AttentionCore.SystolicArray2D
import spinal.core._
import spinal.lib._
import spinal.core
import spinal.core.sim._
import spinal.lib.sim.{StreamMonitor, StreamDriver, StreamReadyRandomizer, ScoreboardInOrder}
/** **************************************************************
 *
 *    SystolicArray
 *
 *    SystolicArray is a systolic array to calculate the multiplication of two matrix
 *                                                                                                                                                                
 *    The main function of this module is to adjust the input matrix data fifo into the required form of Systolic Array, and then pass it into the Systolic Array, and then output the result through a fifo
 *
 * *************************************************************/
//该模块的主要作用是将输入的矩阵数据fifo之后调整成Systolic Array所需的形式，然后将其通入Systolic Array，然后将结果通过一个fifo然后输出
case class SystolicArray2D_Config
(
  in_Length_Max        : Int,  //最大的乘加次数，譬如要计算A(32,16)*B(16,64),此时乘加的次数就是16

  in_MatA_row_num             : Int=32,//一次性从A侧输入的数的数量，也就是输入的A矩阵的行数
  in_MatB_col_num             : Int=32,//一次性从B侧输入的数的数量，也就是输入的B矩阵的列数
  //这两个值也决定了整个脉动整列的尺寸

  in_MatA_element_Width   : Int=16,//输入的A矩阵的每个数的位宽
  in_MatB_element_Width   : Int=16,//输入的B矩阵的每个数的位宽

  Mat_num_inFifo:Int=2 //fifo 缓存的矩阵数量
)
{
  val in_MatA_Bus_Width=in_MatA_row_num//输入的A矩阵的Bus宽度，也就是输入的A矩阵的列数
  val in_MatB_Bus_Width=in_MatB_col_num//输入的B矩阵的Bus宽度，也就是输入的B矩阵的行数
  val in_Fifo_depth=Mat_num_inFifo*in_Length_Max//输入的fifo的深度

  val SystolicArray2DUnit_Cfg = new SystolicArray2DUnit_Config(in_Length_Max,in_MatA_element_Width,in_MatB_element_Width)
  //Z=A*B
  //根据矩阵乘法，输出的行数就是A的行数，输出的列数就是B的列数
  val out_MatZ_row_num=in_MatA_row_num//输出的Z矩阵的行数
  val out_MatZ_col_num=in_MatB_col_num//输出的Z矩阵的列数
  val out_MatZ_element_Width=SystolicArray2DUnit_Cfg.outZ_Width
  val out_Fifo_depth=Mat_num_inFifo*in_Length_Max//输出的fifo的深度
}


case class SintBundle_withFinalMark(Bus_Width: Int,element_Width:Int) extends Bundle {
  val data=Vec.fill(Bus_Width)(SInt(element_Width bits))
  val Final = Bool()
}

case class SystolicArray2D(cfg: SystolicArray2D_Config) extends Component {

  // 定义组件的输入和输出接口
  val io = new Bundle {
    //输入in_MatA_Bus是一个stream从机，主要是考虑到进入模块之后会有buffer做缓冲
    //Stream的内部是 多根SInt线 和 一根Final线，标志着一个矩阵的结束。
    val in_MatA_Bus=slave(Stream(
      new SintBundle_withFinalMark(Bus_Width=cfg.in_MatA_Bus_Width,element_Width=cfg.in_MatA_element_Width)
      ))
    val in_MatB_Bus=slave(Stream(
      new SintBundle_withFinalMark(Bus_Width=cfg.in_MatB_Bus_Width,element_Width=cfg.in_MatB_element_Width)
      ))
    val in_Mat_Fifo_flush=in Bool()
    //一次矩阵完整输出的元素数是cfg.out_MatZ_row_num*cfg.out_MatZ_col_num,而脉动阵列多久可以输出一个结果取决于最大的乘加次数，因此我将输出线的宽度定义为out_MatZ_row_num*out_MatZ_col_num/in_Length_Max
    val out_MatZ_Bus=master(Stream(
      new SintBundle_withFinalMark(Bus_Width=cfg.out_MatZ_row_num*cfg.out_MatZ_col_num/cfg.in_Length_Max,element_Width=cfg.out_MatZ_element_Width)
      ))
    val out_Mat_Fifo_flush=in Bool()
  }
  val in_MatA_Fifo=StreamFifo(
    dataType=SintBundle_withFinalMark(Bus_Width=cfg.in_MatA_Bus_Width,element_Width=cfg.in_MatA_element_Width),
    depth=cfg.in_Fifo_depth
    )
  val in_MatB_Fifo=StreamFifo(
    dataType=SintBundle_withFinalMark(Bus_Width=cfg.in_MatB_Bus_Width,element_Width=cfg.in_MatB_element_Width),
    depth=cfg.in_Fifo_depth
    )
  val out_MatZ_Fifo=StreamFifo(
    dataType=SintBundle_withFinalMark(Bus_Width=cfg.out_MatZ_row_num*cfg.out_MatZ_col_num/cfg.in_Length_Max,element_Width=cfg.out_MatZ_element_Width),
    depth=cfg.out_Fifo_depth
    )
  in_MatA_Fifo.io.push << io.in_MatA_Bus
  in_MatA_Fifo.io.flush:=io.in_Mat_Fifo_flush
  
  in_MatB_Fifo.io.push << io.in_MatB_Bus
  in_MatB_Fifo.io.flush:=io.in_Mat_Fifo_flush
    
  



  
}
object SystolicArray2D_Verilog extends App{
  val testLength=32
  val cfg = SystolicArray2D_Config(testLength)

    val FileDir = "rtl/SystolicArray2D/verilog"
    import java.io.File
    new File(FileDir).mkdirs()
    
    SpinalConfig(
    targetDirectory = FileDir,
    oneFilePerComponent = true,
    defaultConfigForClockDomains = ClockDomainConfig(resetActiveLevel = LOW)
    ).generateVerilog(new SystolicArray2D(cfg)).printPruned()
    
    tools.HDElkDiagramGen(SpinalVerilog(new SystolicArray2D(cfg)))
}

object SystolicArray2D_Sim extends App {
  val FileDir = "rtl/SystolicArray2D/verilog"
  import java.io.File
  new File(FileDir).mkdirs()

  val testLength=32
  val cfg = SystolicArray2D_Config(testLength)
  val dut = SimConfig.withFstWave.compile(new SystolicArray2D(cfg))

  dut.doSim("simple test") { dut =>
    SimTimeout(10000)

    //val scoreboard = ScoreboardInOrder[Int]()


    // drive random data and add pushed data to scoreboard
    StreamDriver(dut.io.in_MatA_Bus, dut.clockDomain) { payload =>
      payload.randomize()
      true
    }
    StreamDriver(dut.io.in_MatB_Bus, dut.clockDomain) { payload =>
      payload.randomize()
      true
    }
    //StreamMonitor(dut.io.in_MatA_Bus, dut.clockDomain) { payload =>
    //  scoreboard.pushRef(payload.toInt)}


    // randmize ready on the output and add popped data to scoreboard

    /* 
    StreamMonitor(dut.io.pop, dut.clockDomain) { payload =>
      scoreboard.pushDut(payload.toInt)
    }
       */

    dut.clockDomain.forkStimulus(10)

    dut.clockDomain.waitActiveEdgeWhere(false)
  }
}