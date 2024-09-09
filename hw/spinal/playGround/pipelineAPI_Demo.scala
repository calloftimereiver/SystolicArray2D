package playGround

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.lib.misc.pipeline._

class PipelineAPI_BasicDemo extends Component {
  val io = new Bundle {
    val up = slave Stream (UInt(16 bits))
    val down = master Stream (UInt(16 bits))
  }

  // Let's define 3 Nodes for our pipeline
  val n0, n1, n2 = Node()

  // Let's connect those nodes by using simples registers
  val s01 = StageLink(n0, n1)
  val s12 = StageLink(n1, n2)

  // Let's define a few Payload things that can go through the pipeline
  val VALUE = Payload(UInt(16 bits))
  val RESULT = Payload(UInt(16 bits))

  // Let's bind io.up to n0
  io.up.ready := n0.ready
  n0.valid := io.up.valid
  n0(VALUE) := io.up.payload

  // Let's do some processing on n1
  n1(RESULT) := n1(VALUE) + 0x1200

  // Let's bind n2 to io.down
  n2.ready := io.down.ready
  io.down.valid := n2.valid
  io.down.payload := n2(RESULT)

  // Let's ask the builder to generate all the required hardware
  Builder(s01, s12)

}
import spinal.lib.sim.{StreamMonitor, StreamDriver, StreamReadyRandomizer, ScoreboardInOrder}

object PipelineAPI_BasicDemo_Sim extends App {
    tools.HDElkDiagramGen(SpinalVerilog(new PipelineAPI_BasicDemo()))
  Config.sim.compile(new PipelineAPI_BasicDemo()).doSim { dut =>
    // Fork a process to generate the reset and the clock on the dut
    
    val scoreboard = ScoreboardInOrder[Int]()
    // drive random data and add pushed data to scoreboard
    StreamDriver(dut.io.up, dut.clockDomain) { payload =>
      payload.randomize()
      true
    }
    StreamMonitor(dut.io.up, dut.clockDomain) { payload =>
      scoreboard.pushRef(payload.toInt)
    }
    // randmize ready on the output and add popped data to scoreboard
    StreamReadyRandomizer(dut.io.down, dut.clockDomain)
    StreamMonitor(dut.io.down, dut.clockDomain) { payload =>
      scoreboard.pushDut(payload.toInt)
    }
    dut.clockDomain.forkStimulus(10)

    dut.clockDomain.waitActiveEdgeWhere(scoreboard.matches == 100)

    
  }
}