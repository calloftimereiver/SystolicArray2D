package AttentionCore.PE
import spinal.core._
import spinal.lib._
import spinal.core
/** **************************************************************
 *
 *    SystolicArrayUnit
 *
 *    SystolicArrayUnit is one Unit of systolic array
 *                                                                                                                                   
                                        
 *
 *
 * *************************************************************/
case class SystolicArrayUnit_Config
(
  in_Length   : Int,  // number of input data
  inA_Width   : Int,
  inB_Width   : Int,
)
{
    val ABProduct_Width = inA_Width+inB_Width
    val outZ_Width = ABProduct_Width + log2Up(in_Length)
}
/* 
                                          
               inB inFinalB               
                │      │                  
                │      │                  
                ▼      ▼                  
             ┌─────────────┐              
             │             │              
             │             │              
     inA ───►│  in_Length  ├──outA──────► 
             │  inA_Width  │              
inFinalA ───►│  inB_Width  ├──outFinalA─► 
             │             │              
             │             ├───────┐      
             └─────────────┘       │      
                │      │         outZ     
              outB outFinalB       │      
                │      │           │      
                ▼      ▼           └────► 
  
 */
case class SystolicArrayUnit(cfg: SystolicArrayUnit_Config) extends Component {

  // 定义组件的输入和输出接口
  val io = new Bundle {
    
    val inA = in SInt(cfg.inA_Width bits)
    val inFinalA = in Bool()
    val inB = in SInt(cfg.inB_Width bits)
    val inFinalB = in Bool()

    val outA= out(Reg(SInt(cfg.inA_Width bits))) init 0
    val outFinalA = out(Reg(Bool())) init False
    val outB= out(Reg(SInt(cfg.inB_Width bits))) init 0
    val outFinalB = out(Reg(Bool())) init False


    val outZ = out(Reg(SInt(cfg.outZ_Width bits))) init 0
    
  }

  //传递数据
  io.outA:=io.inA
  io.outB:=io.inB
  io.outFinalA:=io.inFinalA
  io.outFinalB:=io.inFinalB

  val ABProduct=SInt(cfg.ABProduct_Width bits)
  ABProduct :=io.inA*io.inB
  val ProductSum=Reg(SInt(cfg.outZ_Width bits)) init 0
  ProductSum:=ABProduct+ProductSum
  when((io.inFinalA===True)&&(io.inFinalB===True)){
    io.outZ:=ABProduct+ProductSum
    ProductSum:=0
  }

  val Latency = 1
}

object SystolicArrayUnit_Verilog extends App{
    val cfg = SystolicArrayUnit_Config(32,16,16)

    val FileDir = "rtl/SystolicArrayUnit/verilog"
    import java.io.File
    new File(FileDir).mkdirs()
    
    SpinalConfig(
    targetDirectory = FileDir,
    oneFilePerComponent = true,
    defaultConfigForClockDomains = ClockDomainConfig(resetActiveLevel = LOW)
    ).generateVerilog(new SystolicArrayUnit(cfg)).printPruned()
    
    tools.HDElkDiagramGen(SpinalVerilog(new SystolicArrayUnit(cfg)))
}

object SystolicArrayUnit_Sim extends App {
    val FileDir = "rtl/SystolicArrayUnit/verilog"
    import java.io.File
    new File(FileDir).mkdirs()
    import spinal.core.sim._
    val testLength=32
    val cfg = SystolicArrayUnit_Config(testLength,8,8)

        SimConfig.withConfig(SpinalConfig(
        targetDirectory = FileDir,
        oneFilePerComponent = true,
        defaultConfigForClockDomains = ClockDomainConfig(resetActiveLevel = LOW)
        )).withFstWave.allOptimisation.compile(new SystolicArrayUnit(cfg)).doSim{ dut =>
        // Fork a process to generate the reset and the clock on the dut
        dut.clockDomain.forkStimulus(period = 10)
        var xout_ref=0
        var product:Int=0
        var sumproduct:Int=0
        var inA:Int=0
        var inB:Int=0
        dut.io.inFinalA#=false
        dut.io.inFinalB#=false
        for (idx <- 0 to (testLength*10)) 
        {
            if(idx % testLength == (testLength-1)){
                dut.io.inFinalA#=true
                dut.io.inFinalB#=true
            }
            else{
                dut.io.inFinalA#=false
                dut.io.inFinalB#=false
            }

            dut.clockDomain.waitRisingEdge()
            // Drive the dut inputs with random values
            dut.io.inA.randomize()
            dut.io.inB.randomize()
            inA=dut.io.inA.toInt
            inB=dut.io.inB.toInt
            product=(inA * inB)
            sumproduct=sumproduct+product
            

            if(idx % testLength == (testLength-1)){

                xout_ref=sumproduct
                sumproduct=0
            }

            println(s"${idx}:inA:${dut.io.inA.toInt};inB:${dut.io.inB.toInt};sumproduct=${sumproduct};xout_ref=${xout_ref}")
            // Wait a rising edge on the clock
            
     
            if((idx % testLength == 0)&&(idx != 0)){
                
                assert(dut.io.outZ.toInt == xout_ref)
            }
            

            //println(dut.io.xout.toInt)
            
        
        }
    }

}
