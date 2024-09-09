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
  in_Length   : Int,  // number of input data
  inA_Width   : Int,
  inB_Width   : Int,
)
{
    val ABProduct_Width = inA_Width+inB_Width
    val outZ_Width = ABProduct_Width + log2Up(in_Length)
}