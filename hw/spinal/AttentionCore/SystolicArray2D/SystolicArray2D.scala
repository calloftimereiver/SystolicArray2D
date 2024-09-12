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

  out_MatZ_buffer_num: Int=12
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
    //先看两个维度哪个比较大，定义大的哪个维度为theoretical_out_MatZ_buffer_num
    val is_col_wider:Boolean = in_MatB_col_num > in_MatA_row_num
    //理论上需要做theoretical_out_MatZ_buffer_num个buffer，因为最坏的情况下每一个周期都需要开始一个新的矩阵的计算，即Final线会恒为1。
    //此时，就需要theoretical_out_MatZ_buffer_num个buffer缓存所有的中间结果
    val theoretical_out_MatZ_buffer_num:Int=if(is_col_wider){in_MatB_col_num}else {in_MatA_row_num}
    //待标定，现在只是占位符
    val Latency=in_Length_Max
    val IterationInterval=1
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
//     │  │                                 │    │    │                               
//     │  └─────────┬───────────────────────┘    │    │                               
//     │  │         │                            │    │                               
//     │  │         │                            │    │                               
//     │  └─────────┼──────────────────┬────────────┬─┘                               
//     │            │                  │            │                                 
//     │            │                  │            │                                 
//     │            │                  │            │ SystolicArray2DUnit_buffer_ptr()
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
case class Sint_withFinalMark(element_Width:Int) extends Bundle {
  val data=SInt(element_Width bits)
  val Final = Bool()
}

case class SystolicArray2D(cfg: SystolicArray2D_Config) extends Component {
    val io = new Bundle {
        val in_MatA=slave(Stream(Vec.fill(cfg.in_MatA_row_num)(Sint_withFinalMark(cfg.in_MatA_element_Width))))
        val in_MatB=slave(Stream(Vec.fill(cfg.in_MatB_col_num)(Sint_withFinalMark(cfg.in_MatB_element_Width))))
        val out_MatZ=master(Stream(Vec.fill(cfg.in_MatA_row_num)(Vec.fill(cfg.in_MatB_col_num)(SInt(cfg.out_MatZ_element_Width bits)))))
    }
    assert(cfg.out_MatZ_buffer_num<=cfg.theoretical_out_MatZ_buffer_num)
    
    val interconnect_outA_with_inA=Vec.fill(cfg.in_MatA_row_num)(Vec.fill(cfg.in_MatB_col_num)(new Bundle{
        val data=SInt(cfg.SystolicArray2DUnit_Cfg.inA_Width bits)
        val Final=Bool()
    }))//used to connect outA with inA of next unit

    val interconnect_outB_with_inB=Vec.fill(cfg.in_MatA_row_num)(Vec.fill(cfg.in_MatB_col_num)(new Bundle{
        val data=SInt(cfg.SystolicArray2DUnit_Cfg.inB_Width bits)
        val Final=Bool()
    }))//used to connect outB with inB of next unit

    val out_MatZ_Bundle_wire=Vec.fill(cfg.in_MatA_row_num)(Vec.fill(cfg.in_MatB_col_num)(Sint_withFinalMark(cfg.out_MatZ_element_Width)))//阵列的输出线
    //迭代例化乘加器实体并连线
    for(row_index <- 0 until cfg.in_MatA_row_num)
    {
        for(col_index <- 0 until cfg.in_MatB_col_num)
        {
            //横向连接线
            val unit=SystolicArray2DUnit(cfg.SystolicArray2DUnit_Cfg)
            if(col_index==0){
                //第一层，和模块的输入相连
                unit.io.inA<>io.in_MatA.payload(row_index).data
                unit.io.inA_Final<>io.in_MatA.payload(row_index).Final
            }
            else{
                //不是第一层，和上一层的输出相连
                unit.io.inA<>interconnect_outA_with_inA(row_index)(col_index-1).data
                unit.io.inA_Final<>interconnect_outA_with_inA(row_index)(col_index-1).Final
            }
            //所有的输出先连接到interconnect_outA_with_inA线束
            interconnect_outA_with_inA(row_index)(col_index).data<>unit.io.outA
            interconnect_outA_with_inA(row_index)(col_index).Final<>unit.io.outA_Final
            //纵向连接线
            if(row_index==0){
                //第一层，和模块的输入相连
                unit.io.inB<>io.in_MatB.payload(col_index).data
                unit.io.inB_Final<>io.in_MatB.payload(col_index).Final
            }
            else{
                //不是第一层，和上一层的输出相连
                unit.io.inB<>interconnect_outB_with_inB(row_index-1)(col_index).data
                unit.io.inB_Final<>interconnect_outB_with_inB(row_index-1)(col_index).Final
            }
            //所有的输出先连接到interconnect_outB_with_inB线束
            interconnect_outB_with_inB(row_index)(col_index).data<>unit.io.outB
            interconnect_outB_with_inB(row_index)(col_index).Final<>unit.io.outB_Final

            unit.io.Go<>io.out_MatZ.ready

            unit.io.outZ<>out_MatZ_Bundle_wire(row_index)(col_index).data
            out_MatZ_Bundle_wire(row_index)(col_index).Final:=unit.io.outA_Final&&unit.io.outB_Final
        }
    }
    //先定义一个buffer结构
    case class out_MatZ_buffer(cfg:SystolicArray2D_Config) extends Bundle {
        val data=Vec.fill(cfg.in_MatA_row_num)(Vec.fill(cfg.in_MatB_col_num)(Reg(SInt(cfg.out_MatZ_element_Width bits)) init 0))
        val Occupied = Reg(Bool()) init False//说明已经有一部分数据在其中，正在等待剩余的数据传输完成。
        val Valid = Reg(Bool()) init False//说明所有的数据都已经就绪，可以输出了
    }
    //理论上需要做theoretical_out_MatZ_buffer_num个buffer，因为最坏的情况下每一个周期都需要开始一个新的矩阵的计算，即Final线会恒为1。
    //此时，就需要theoretical_out_MatZ_buffer_num个buffer缓存所有的中间结果
    val buffer_array=Vec.fill(cfg.out_MatZ_buffer_num)(out_MatZ_buffer(cfg))//这里看需求决定例化多少个buffer，theoretical_out_MatZ_buffer_num是最坏情况
    //为每个潜在的输出矩阵定义一个缓存，可以参看最上面的图
    val SystolicArray2DUnit_buffer_ptr=Vec.fill(cfg.out_MatZ_buffer_num)(Reg(UInt(log2Up(cfg.out_MatZ_buffer_num) bits)) init 0)
    //按照索引规定乘加器的输出行为(简单来说就是输出到哪个buffer)
    for(i <- 0 until cfg.out_MatZ_buffer_num)
    {
        when(out_MatZ_Bundle_wire(0)(0).Final)
        {
            
        }otherwise
        {

        }

    }

    


    
    val buffer_array_output_ptr=Reg(UInt(log2Up(cfg.out_MatZ_buffer_num) bits)) init 0
    
    io.out_MatZ.payload<>buffer_array(buffer_array_output_ptr).data


    
}

object SystolicArray2D_Verilog extends App{
  val testLength=3
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