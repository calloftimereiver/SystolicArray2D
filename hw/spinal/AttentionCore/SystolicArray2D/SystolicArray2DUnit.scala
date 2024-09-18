package AttentionCore.SystolicArray2D
import spinal.core._
import spinal.lib._
import spinal.core
/** **************************************************************
 *
 *    SystolicArray2DUnit
 *
 *    SystolicArray2DUnit is one Unit of systolic array 2D
 *                                                                                                                                                                
 *
 *
 * *************************************************************/
case class SystolicArray2DUnit_Config
(
  in_Length   : Int,  // number of input data
  inA_Width   : Int=16,
  inB_Width   : Int=16,
)
{
    val ABProduct_Width = inA_Width+inB_Width
    val outZ_Width = ABProduct_Width + log2Up(in_Length)
    
    val Max_Latency = 1//得到全部输入后一周期就能出结果
    val IterationInterval = 1
    //对于每个元素，理想情况下每一个时钟周期都可以输入一个数据，除非下游的buffer阻塞，这种情况下本单元会传递该阻塞信号
}
/* 
                                          
               inB inB_Final               
                │      │                  
                │      │                  
                ▼      ▼                  
             ┌─────────────┐              
             │             │              
             │             │              
     inA ───►│  in_Length  ├──outA──────► 
             │  inA_Width  │              
inA_Final───►│  inB_Width  ├──outA_Final─► 
             │             │              
             │             ├───────┐      
             └─────────────┘       │      
                │      │         outZ     
              outB outB_Final      │      
                │      │           │      
                ▼      ▼           └────► 
  
 */
case class SystolicArray2DUnit(cfg: SystolicArray2DUnit_Config) extends Component {

  // 定义组件的输入和输出接口
  val io = new Bundle {
    
    val inA = in SInt(cfg.inA_Width bits)
    val inA_Final = in Bool()
    val inB = in SInt(cfg.inB_Width bits)
    val inB_Final = in Bool()
    val Go = in Bool()

    val outA= out(Reg(SInt(cfg.inA_Width bits))) init 0
    val outA_Final = out(Reg(Bool())) init False
    val outB= out(Reg(SInt(cfg.inB_Width bits))) init 0
    val outB_Final = out(Reg(Bool())) init False

    val outZ = out(Reg(SInt(cfg.outZ_Width bits))) init 0
    
  }



  val ABProduct=SInt(cfg.ABProduct_Width bits)
  val ProductSum=Reg(SInt(cfg.outZ_Width bits)) init 0
  ABProduct :=io.inA*io.inB
  when(io.Go===True){
    //传递数据
    io.outA:=io.inA
    io.outB:=io.inB
    io.outA_Final:=io.inA_Final
    io.outB_Final:=io.inB_Final
    //计算部分
    ProductSum:=ABProduct+ProductSum
    when((io.inA_Final===True)&&(io.inB_Final===True)){
      io.outZ:=ABProduct+ProductSum
      ProductSum:=0
    }
  }
  
}

object SystolicArray2DUnit_Verilog extends App{
    val cfg = SystolicArray2DUnit_Config(32,16,16)

    val FileDir = "rtl/SystolicArray2DUnit/verilog"
    import java.io.File
    new File(FileDir).mkdirs()
    
    SpinalConfig(
    targetDirectory = FileDir,
    oneFilePerComponent = true,
    defaultConfigForClockDomains = ClockDomainConfig(resetActiveLevel = LOW)
    ).generateVerilog(new SystolicArray2DUnit(cfg)).printPruned()
    
    tools.HDElkDiagramGen(SpinalVerilog(new SystolicArray2DUnit(cfg)))
}

object SystolicArray2DUnit_Sim extends App {
    val FileDir = "rtl/SystolicArray2DUnit/verilog"
    import java.io.File
    new File(FileDir).mkdirs()
    import spinal.core.sim._
    val testLength=3
    val cfg = SystolicArray2DUnit_Config(testLength,8,8)

    SimConfig.withConfig(SpinalConfig(
        targetDirectory = FileDir,
        oneFilePerComponent = true,
        defaultConfigForClockDomains = ClockDomainConfig(resetActiveLevel = LOW)
        )).withFstWave.allOptimisation.compile(new SystolicArray2DUnit(cfg)).doSim{ dut =>
        // Fork a process to generate the reset and the clock on the dut
        dut.clockDomain.forkStimulus(period = 10)
        var xout_ref=0
        var product:Int=0
        var sumproduct:Int=0
        var inA:Int=0
        var inB:Int=0
        dut.io.inA_Final#=false
        dut.io.inB_Final#=false
        var idx=0
        dut.io.Go#=true
        while(idx<(testLength*10)) 
        {
            if(idx % testLength == (testLength-1)){
                dut.io.inA_Final#=true
                dut.io.inB_Final#=true
            }
            else{
                dut.io.inA_Final#=false
                dut.io.inB_Final#=false
            }

            dut.clockDomain.waitRisingEdge()
            // Drive the dut inputs with random values
            dut.io.Go.randomize()
            dut.io.inA.randomize()
            dut.io.inB.randomize()
            inA=dut.io.inA.toInt
            inB=dut.io.inB.toInt
            if(dut.io.Go.toBoolean)
        {
            product=(inA * inB)
            sumproduct=sumproduct+product
            

            if(idx % testLength == (testLength-1)){

                xout_ref=sumproduct
                sumproduct=0
            }
          }

            println(s"${idx}:inA:${dut.io.inA.toInt};inB:${dut.io.inB.toInt};sumproduct=${sumproduct};xout_ref=${xout_ref}")
            // Wait a rising edge on the clock
            
     
            if((idx % testLength == 0)&&(idx != 0)){
                
                assert(dut.io.outZ.toInt == xout_ref)
            }
            

            //println(dut.io.xout.toInt)
            
        if(dut.io.Go.toBoolean)
        {
          idx=idx+1
        }
        }
    }

}
