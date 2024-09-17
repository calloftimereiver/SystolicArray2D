package spinal.lib.sim

import spinal.core.Data
import spinal.core.sim._

import scala.collection.mutable


case class ScoreboardInOrder_matrix() {
  val dut,ref = mutable.Queue[Array[Array[Int]]]()
  var matches = 0

  if(Phase.isUsed){
    Phase.check{
      checkEmptyness()
    }
  }

  def pushDut(that : Array[Array[Int]]) : Unit = {
    dut.enqueue(that)
    check()
  }

  def pushRef(that : Array[Array[Int]]) : Unit = {
    ref.enqueue(that)
    check()
  }

  def compare(ref : Array[Array[Int]], dut : Array[Array[Int]]) = !(ref.deep != dut.deep)

  def check(): Unit ={
    if(ref.nonEmpty && dut.nonEmpty){
      val dutHead = dut.dequeue()
      val refHead = ref.dequeue()
      if(!compare(refHead, dutHead)){
        println("Transaction mismatch :")
        println("REF :")
        println(refHead)
        println("DUT :")
        println(dutHead)
        simFailure()
      }
      matches += 1
    }
  }

  def checkEmptyness(): Unit ={
    if(dut.nonEmpty || ref.nonEmpty){
      if(dut.nonEmpty){
        println("Unmatched DUT transaction : \n")
        dut.foreach(d => println(d))
      }

      if(ref.nonEmpty){
        println("Unmatched reference transaction :\n")
        ref.foreach(d => println(d))
      }
      if(Phase.isUsed) Phase.check.onEnd(simFailure()) else simFailure()
    }
  }
}