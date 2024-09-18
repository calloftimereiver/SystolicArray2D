package AttentionCore.SystolicArray2D
import spinal.core.sim._
import spinal.lib.sim.{StreamMonitor, StreamDriver, StreamReadyRandomizer, ScoreboardInOrder_matrix}
import scala.util.Random

object SystolicArray2D_Sim extends App {
  val FileDir = "rtl/SystolicArray2D/verilog"
  import java.io.File
  new File(FileDir).mkdirs()
  val matrix_num=30
  val mult_dim = 10

  // 矩阵A的行数和列数
  val rowsA = 4
  val colsA = mult_dim

  // 矩阵B的行数（必须等于矩阵A的列数），列数
  val rowsB = colsA
  val colsB = 5

  val cfg = SystolicArray2D_Config(
    in_Length_Max = mult_dim,
    in_Length_Min = mult_dim,
    in_MatA_row_num = rowsA,
    in_MatB_col_num = colsB,
    in_MatA_element_Width = 8,
    in_MatB_element_Width = 8
  )
  val dut = SimConfig.withFstWave.compile(new SystolicArray2D(cfg))
  // 生成随机矩阵
  def generateRandomMatrix(rows: Int, cols: Int): Array[Array[Int]] = {
    val rand = new Random()
    Array.fill(rows, cols)(rand.nextInt(16)) // 随机生成0到9之间的整数
  }

  // 打印矩阵
  def printMatrix(matrix: Array[Array[Int]]): Unit = {
    matrix.foreach(row => println(row.mkString("\t")))
    println()
  }

  // 矩阵相乘
  def multiplyMatrices(matrixA: Array[Array[Int]], matrixB: Array[Array[Int]]): Array[Array[Int]] = {
    val rowsA = matrixA.length
    val colsA = matrixA(0).length
    val rowsB = matrixB.length
    val colsB = matrixB(0).length

    require(colsA == rowsB, "矩阵A的列数必须等于矩阵B的行数")

    val result = Array.ofDim[Int](rowsA, colsB)

    for (i <- 0 until rowsA) {
      for (j <- 0 until colsB) {
        result(i)(j) = (0 until colsA).map(k => matrixA(i)(k) * matrixB(k)(j)).sum
      }
    }
    result
  }
  import scala.collection.mutable
  val matrixA_queue=mutable.Queue[Array[Array[Int]]]()
  val matrixB_queue=mutable.Queue[Array[Array[Int]]]()
  val ref_queue=mutable.Queue[Array[Array[Int]]]()
  for(matrix_idx<-0 until matrix_num)
  // 生成两个随机矩阵
{    var m_A=generateRandomMatrix(rowsA, colsA)
  var m_B=generateRandomMatrix(rowsB, colsB)
  matrixA_queue.enqueue(m_A)
    matrixB_queue.enqueue(m_B)
    ref_queue.enqueue(multiplyMatrices(m_A, m_B))
}

  dut.doSim("simple test") { dut =>
    SimTimeout(20000)

    val scoreboard = ScoreboardInOrder_matrix()

    var StreamDriver_sending_period = 0

    var matrixA_sending=matrixA_queue.dequeue()
    var matrixB_sending=matrixB_queue.dequeue()
    // drive random data and add pushed data to scoreboard
    dut.io.in_Mats.valid #= true
    StreamDriver(dut.io.in_Mats, dut.clockDomain) { payload =>
      for (row_index <- 0 until cfg.in_MatA_row_num) {
        if (StreamDriver_sending_period < mult_dim - 1) {
          payload.A(row_index).data #= matrixA_sending(row_index)(StreamDriver_sending_period)
          payload.A(row_index).Final #= false
        } else if (StreamDriver_sending_period == mult_dim - 1) {
          payload.A(row_index).data #= matrixA_sending(row_index)(StreamDriver_sending_period)
          payload.A(row_index).Final #= true
        } else {
          payload.A(row_index).data #= 0
          payload.A(row_index).Final #= false
        }
      }
      for (col_index <- 0 until cfg.in_MatB_col_num) {
        if (StreamDriver_sending_period < mult_dim - 1) {
          payload.B(col_index).data #= matrixB_sending(StreamDriver_sending_period)(col_index)
          payload.B(col_index).Final #= false
        } else if (StreamDriver_sending_period == mult_dim - 1) {
          payload.B(col_index).data #= matrixB_sending(StreamDriver_sending_period)(col_index)
          payload.B(col_index).Final #= true
        } else {
          payload.B(col_index).data #= 0
          payload.B(col_index).Final #= false
        }
      }
      println(s"StreamDriver called:${StreamDriver_sending_period}")
      if (StreamDriver_sending_period == mult_dim - 1) { 
        StreamDriver_sending_period = 0 

        println(s"new input loaded")
        println("A:")
        printMatrix(matrixA_sending)
        println("B:")
        printMatrix(matrixB_sending)
        println("Z:")
        printMatrix(multiplyMatrices(matrixA_sending, matrixB_sending))
        matrixA_sending=matrixA_queue.dequeue()
        matrixB_sending=matrixB_queue.dequeue()
      }
      else { StreamDriver_sending_period = StreamDriver_sending_period + 1 }

      true
    }

    // add popped data to scoreboard
//StreamReadyRandomizer(dut.io.out_MatZ, dut.clockDomain)
    dut.io.out_MatZ.ready #= true
    var Dut_push_counter = 0
    StreamMonitor(dut.io.out_MatZ, dut.clockDomain) { payload =>
      var dut_result = Array.fill(cfg.in_MatA_row_num) { Array.fill(cfg.in_MatB_col_num) { 0 } }

      if (dut.io.out_MatZ.valid.toBoolean) {
        for (row_index <- 0 until cfg.in_MatA_row_num) {
          for (col_index <- 0 until cfg.in_MatB_col_num) {
            dut_result(row_index)(col_index) = payload(row_index)(col_index).toInt
          }
        }
        println(s"dut_result${Dut_push_counter}:")
        printMatrix(dut_result)
        scoreboard.pushDut(dut_result)
        Dut_push_counter = Dut_push_counter+1
      }
    }
    var Ref_push_counter = 0
    StreamMonitor(dut.io.in_Mats, dut.clockDomain) { payload =>
      if (dut.io.out_MatZ.valid.toBoolean) {
        println(s"ref_result${Ref_push_counter}:")
        var resultMatrix =ref_queue.dequeue()
        printMatrix(resultMatrix)
        scoreboard.pushRef(resultMatrix)
        Ref_push_counter=Ref_push_counter+1
      }
    }

    dut.clockDomain.forkStimulus(10)

    dut.clockDomain.waitActiveEdgeWhere(scoreboard.matches == 10)
  }
}
