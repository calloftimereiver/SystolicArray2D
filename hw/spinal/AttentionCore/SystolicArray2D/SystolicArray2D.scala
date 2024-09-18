package AttentionCore.SystolicArray2D
import spinal.core._
import spinal.lib._
import spinal.core

/** **************************************************************
  *
  *    SystolicArray2D
  *
  *    SystolicArray2D is a systolic array to calculate the multiplication of two matrix
  *
  * ************************************************************
  */
//该模块的主要作用是将输入的矩阵数据fifo之后调整成Systolic Array所需的形式，然后将其通入Systolic Array，然后将结果通过一个fifo然后输出
case class SystolicArray2D_Config(
    in_Length_Max: Int, // 最大的乘加次数，譬如要计算A(32,16)*B(16,64),此时乘加的次数就是16
    in_Length_Min: Int = 1,// 最少的乘加次数,用于计算出缓存阵列结果所需的缓存数量

    in_MatA_row_num: Int = 12, // 一次性从A侧输入的数的数量，也就是输入的A矩阵的行数
    in_MatB_col_num: Int = 24, // 一次性从B侧输入的数的数量，也就是输入的B矩阵的列数
    // 这两个值也决定了整个脉动整列的尺寸
    in_MatA_element_Width: Int = 16, // 输入的A矩阵的每个数的位宽
    in_MatB_element_Width: Int = 16, // 输入的B矩阵的每个数的位宽

    
) {
  val SystolicArray2DUnit_Cfg = SystolicArray2DUnit_Config(
    in_Length = in_Length_Max,
    inA_Width = in_MatA_element_Width,
    inB_Width = in_MatB_element_Width
  )
  def ceilDiv(x: Int, y: Int): Int = {
  (x + y - 1) / y}
  // Z=A*B
  // 根据矩阵乘法，输出的行数就是A的行数，输出的列数就是B的列数
  val out_MatZ_row_num = in_MatA_row_num // 输出的Z矩阵的行数
  val out_MatZ_col_num = in_MatB_col_num // 输出的Z矩阵的列数
  val out_MatZ_element_Width = SystolicArray2DUnit_Cfg.outZ_Width

  // 一次矩阵完整输出的元素数是cfg.out_MatZ_row_num*cfg.out_MatZ_col_num
  val out_MatZ_Width = out_MatZ_row_num * out_MatZ_col_num

  // 理论上需要做out_MatZ_buffer_num个buffer，
  //最坏的情况下每一个周期都需要开始一个新的矩阵的计算，即Final线会恒为1。
  // 此时，就需要out_MatZ_buffer_num个buffer缓存所有的中间结果
  val diag_num: Int = in_MatB_col_num + in_MatA_row_num
  val out_MatZ_buffer_num: Int = ceilDiv(diag_num,in_Length_Min)+2 

  val IterationInterval = SystolicArray2DUnit_Cfg.IterationInterval
  val Latency = (in_MatA_row_num+in_MatB_col_num)*IterationInterval+SystolicArray2DUnit_Cfg.Latency
}

//        ┌────────────────────────────┐────┐─────────┐
//        │                            │    │    │    │
//        │                            │    │    │    │
//        │                            │    │    │    │
//        │                            │    │    │    │
//        │                            │    │    │    │
//        │                            │    │    │    │
//        │                            │    │    │    │
//        │                            │    │    │    │
//        │                            │    │    │    │
//        │                            │    │    │    │
//        │           SystolicArray    │    │    │    │
//        │                            │    │    │    │
//     ┌──┼                            │    │    │    │
//     │  │                            │    │    │    │
//     │  └────────────────────────────┘    │    │    │
//     │  │                                 │    │    │
//     │  │                              i  │    │    │
//     │  └─────────┬───────────────────────┘    │    │
//     │  │         │                            │    │
//     │  │         │                            │    │
//     │  └─────────┼──────────────────┬────────────┬─┘
//     │            │                  │            │
//     │            │                  │            │
//     │            │                  │            │ Unit2buffer_ptr()
//     │            │                  │            │
//     │            │                  │            │
//     │            │                  │            │
//     │            │                  │            │
//     │            │                  │            │
//     ▼            ▼                  ▼            ▼
// ┌────────┐   ┌────────┐         ┌────────┐   ┌────────┐
// │        │   │        │         │        │   │        │
// │        │   │        │         │        │   │        │
// │        │   │        │         │        │   │        │
// │        │   │        │   ...   │        │   │        │
// │        │   │        │         │        │   │        │
// │        │   │        │         │        │   │        │
// │        │   │        │         │        │   │        │
// └────────┘   └────────┘         └────────┘   └────────┘
//
//
//             buffer_array_output_ptr
//
//                         │
//                         │
// ┌───────────────────────▼─────────────────────────────┐
// │                                                     │
// │                   output                            │
// │                                                     │
// └─────────────────────────────────────────────────────┘
case class Sint_withFinalMark(element_Width: Int) extends Bundle {
  val data = SInt(element_Width bits)
  val Final = Bool()
}
case class in_Mats_Bundle(cfg: SystolicArray2D_Config) extends Bundle {
  val A = Vec.fill(cfg.in_MatA_row_num)(Sint_withFinalMark(cfg.in_MatA_element_Width))
  val B = Vec.fill(cfg.in_MatB_col_num)(Sint_withFinalMark(cfg.in_MatB_element_Width))
}

case class SystolicArray2D(cfg: SystolicArray2D_Config) extends Component {

  val io = new Bundle {
    val in_Mats = slave(Stream(in_Mats_Bundle(cfg)))
    val out_MatZ = master(
      Stream(Vec.fill(cfg.in_MatA_row_num)(Vec.fill(cfg.in_MatB_col_num)(SInt(cfg.out_MatZ_element_Width bits))))
    )
  }

  val Go = Bool()
  Go := (io.out_MatZ.ready) && (io.in_Mats.valid) // 判定前级和后级是否正在运转，如果不满足输出条件就暂停整个模块
  val in_MatA_AfterDelay = Vec.fill(cfg.in_MatA_row_num) { Sint_withFinalMark(cfg.in_MatA_element_Width) }
  val init_Sint_withFinalMark_MatA = Sint_withFinalMark(cfg.in_MatA_element_Width)
  init_Sint_withFinalMark_MatA.data := 0
  init_Sint_withFinalMark_MatA.Final := False
  val init_Sint_withFinalMark_MatB = Sint_withFinalMark(cfg.in_MatB_element_Width)
  init_Sint_withFinalMark_MatB.data := 0
  init_Sint_withFinalMark_MatB.Final := False
  for (row_index <- 0 until cfg.in_MatA_row_num) {
    // 遍历模块的A输入线
    // 对row_index等于几，这条信号线就需要延迟多少个时钟周期

    in_MatA_AfterDelay(row_index) := Delay(
      io.in_Mats.payload.A(row_index),
      row_index,
      when = Go,
      init = init_Sint_withFinalMark_MatA
    )

  }
  val in_MatB_AfterDelay = Vec.fill(cfg.in_MatB_col_num) { Sint_withFinalMark(cfg.in_MatB_element_Width) }
  for (col_index <- 0 until cfg.in_MatB_col_num) {
    // 遍历模块的B输入线
    in_MatB_AfterDelay(col_index) := Delay(
      io.in_Mats.payload.B(col_index),
      col_index,
      when = Go,
      init = init_Sint_withFinalMark_MatB
    )
  }

  val interconnect_outA_with_inA = Vec.fill(cfg.in_MatA_row_num)(Vec.fill(cfg.in_MatB_col_num)(new Bundle {
    val data = SInt(cfg.SystolicArray2DUnit_Cfg.inA_Width bits)
    val Final = Bool()
  })) // used to connect outA with inA of next unit

  val interconnect_outB_with_inB = Vec.fill(cfg.in_MatA_row_num)(Vec.fill(cfg.in_MatB_col_num)(new Bundle {
    val data = SInt(cfg.SystolicArray2DUnit_Cfg.inB_Width bits)
    val Final = Bool()
  })) // used to connect outB with inB of next unit

  val out_MatZ_Bundle_wire = Vec.fill(cfg.in_MatA_row_num)(
    Vec.fill(cfg.in_MatB_col_num)(Sint_withFinalMark(cfg.out_MatZ_element_Width))
  ) // 阵列的输出线
  // 迭代例化乘加器实体并连线
  for (row_index <- 0 until cfg.in_MatA_row_num) {
    for (col_index <- 0 until cfg.in_MatB_col_num) {
      // 横向连接线
      val unit = SystolicArray2DUnit(cfg.SystolicArray2DUnit_Cfg)
      if (col_index == 0) {
        // 第一层，和已经完成延迟对齐的输入相连
        unit.io.inA <> in_MatA_AfterDelay(row_index).data
        unit.io.inA_Final <> (in_MatA_AfterDelay(row_index).Final && Go)
      } else {
        // 不是第一层，和上一层的输出相连
        unit.io.inA <> interconnect_outA_with_inA(row_index)(col_index - 1).data
        unit.io.inA_Final <> interconnect_outA_with_inA(row_index)(col_index - 1).Final
      }
      // 所有的输出先连接到interconnect_outA_with_inA线束
      interconnect_outA_with_inA(row_index)(col_index).data <> unit.io.outA
      interconnect_outA_with_inA(row_index)(col_index).Final <> unit.io.outA_Final
      // 纵向连接线
      if (row_index == 0) {
        // 第一层，和已经完成延迟对齐的输入相连
        unit.io.inB <> in_MatB_AfterDelay(col_index).data
        unit.io.inB_Final <> (in_MatB_AfterDelay(col_index).Final && Go)
      } else {
        // 不是第一层，和上一层的输出相连
        unit.io.inB <> interconnect_outB_with_inB(row_index - 1)(col_index).data
        unit.io.inB_Final <> interconnect_outB_with_inB(row_index - 1)(col_index).Final
      }
      // 所有的输出先连接到interconnect_outB_with_inB线束
      interconnect_outB_with_inB(row_index)(col_index).data <> unit.io.outB
      interconnect_outB_with_inB(row_index)(col_index).Final <> unit.io.outB_Final

      unit.io.Go <> Go

      unit.io.outZ <> out_MatZ_Bundle_wire(row_index)(col_index).data
      out_MatZ_Bundle_wire(row_index)(col_index).Final := unit.io.outA_Final && unit.io.outB_Final
    }
  }
  case class out_MatZ_buffer(cfg: SystolicArray2D_Config) extends Bundle {
    val data =
      Vec.fill(cfg.in_MatA_row_num)(Vec.fill(cfg.in_MatB_col_num)(Reg(SInt(cfg.out_MatZ_element_Width bits)) init 0))
    val Occupied = Reg(Bool()) init False // 说明已经有一部分数据在其中，正在等待剩余的数据传输完成。
    val Valid = Reg(Bool()) init False // 说明所有的数据都已经就绪，可以输出了
  }
  val buffer_array = Vec.fill(cfg.out_MatZ_buffer_num)(out_MatZ_buffer(cfg))
  // 为每个潜在的输出矩阵定义一个缓存，可以参看最上面的图
  val Unit2buffer_ptr = Vec.fill(cfg.diag_num)(Reg(UInt(log2Up(cfg.out_MatZ_buffer_num) bits)) init 0)
  // 定义一个输出指针，指定模块的输出端口连接到哪个buffer
  val buffer_array_output_ptr = Reg(UInt(log2Up(cfg.out_MatZ_buffer_num) bits)) init 0
  val max_Unit2buffer_ptr=cfg.out_MatZ_buffer_num-1
  val min_Unit2buffer_ptr=0
  val next_Unit2buffer_ptr_0 = ((Unit2buffer_ptr(0) + 1) < (max_Unit2buffer_ptr)) ? (Unit2buffer_ptr(0) + 1) | (min_Unit2buffer_ptr)
  val ready_to_output = RegNextWhen(out_MatZ_Bundle_wire(cfg.in_MatA_row_num - 1)(cfg.in_MatB_col_num - 1).Final,cond=Go) init False
  when(Go) { // 先定义一个buffer结构

    // 按照索引规定乘加器的输出行为(简单来说就是输出到哪个buffer)
    for (i <- (1 until cfg.diag_num).reverse) // i<-{cfg.diag_num-1,cfg.diag_num-2,...,1}
      {
        // 更新索引号
        Unit2buffer_ptr(i) := Unit2buffer_ptr(i - 1)

      }

    when((in_MatA_AfterDelay(0).Final) && (in_MatB_AfterDelay(0).Final)) {
      // 当索引为00的单元被输入final信号是，说明下一个周期该单元会输出一个 新的矩阵 的结果，0号索引号+1，并且如果超出了缓存数量，则从0重新开始
      Unit2buffer_ptr(0) := next_Unit2buffer_ptr_0
      buffer_array(next_Unit2buffer_ptr_0).Occupied := True
    }

    when(ready_to_output) {
      // 当索引为(in_MatA_row_num-1,in_MatB_col_num-1)的单元输出结果时，说明已经完成了一个矩阵的计算，此时，对应的buffer的Valid应该被置位
      buffer_array(Unit2buffer_ptr(cfg.diag_num - 1)).Valid := True
      buffer_array_output_ptr := Unit2buffer_ptr(cfg.diag_num - 1)
    } otherwise {
      buffer_array(Unit2buffer_ptr(cfg.diag_num - 1)).Valid := False
    }
    // 所有情况下，矩阵的输出数据都会被写入对应的缓存中
    for (i <- (0 until cfg.diag_num)) // i<-{0,1,...,23}
      {
        when(buffer_array(Unit2buffer_ptr(i)).Occupied) {
          for (row_index <- 0 until cfg.in_MatA_row_num) {
            for (col_index <- 0 until cfg.in_MatB_col_num) {
              if ((row_index+col_index)==i) { 
                when(out_MatZ_Bundle_wire(row_index)(col_index).Final) {
                    buffer_array(Unit2buffer_ptr(i)).data(row_index)(col_index) := out_MatZ_Bundle_wire(row_index)(col_index).data
                  }
              }
            }
          }


        }
      }
    for(i<-0 until cfg.out_MatZ_buffer_num)
    {
        when(buffer_array(i).Occupied){
        // 对于每一个buffer，如果当前时钟周期valid为1，下个周期就会将其复位
          when(buffer_array(i).Valid) {
            buffer_array(i).Valid := False
            buffer_array(i).Occupied := False
          }}
    }
  }
  io.out_MatZ.payload <> buffer_array(buffer_array_output_ptr).data
  io.out_MatZ.valid := (buffer_array(buffer_array_output_ptr).Valid)&&(io.in_Mats.valid)
  io.in_Mats.ready <> io.out_MatZ.ready

}

object SystolicArray2D_Verilog extends App {
  val testLength = 1
  val cfg = SystolicArray2D_Config(
    in_Length_Max = testLength,
    in_Length_Min = testLength,
    in_MatA_row_num = 3,
    in_MatB_col_num = 5
  )

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
