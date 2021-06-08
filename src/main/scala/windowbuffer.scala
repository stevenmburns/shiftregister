package shiftregister

import chisel3._
import chisel3.util._

class WindowRow (val wsize: Int, val bitwidth: Int = 8) extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(bitwidth.W))
    val out = Output(Vec(wsize, UInt(bitwidth.W)))
  })
  
  //val state = Wire(Vec(wsize, UInt(bitwidth.W)))
  //val state = RegInit(Vec(wsize, 0.U(bitwidth.W)))

  val state = VecInit( IndexedSeq.fill( wsize){ RegInit( 0.U(bitwidth.W))})

  //val state = Reg(Vec(wsize, UInt(bitwidth.W)))

  state(0) := io.in
    
  for (i <- 1 until (wsize-1)){
      state(i) := state(i-1)
  }
      
  io.out := state
}

class LineBuff(val lsize: Int, val bitwidth: Int = 8) extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(bitwidth.W))
    val out = Output(UInt(bitwidth.W))
  })

  val state = Reg(Vec(lsize, UInt(bitwidth.W)))
    
  state(0) := io.in
    
  for (i <- 1 to (lsize-1)){
      state(i) := state(i-1)
  }
      
  io.out := state(lsize-1)
  
  printf( "%x %x %x %x %x %x\n", state(0), state(1), state(2), state(3), state(4), state(5))

  //io.out := ShiftRegister( io.in, lsize)

}

object WindowRow6 {
  def main(args: Array[String]): Unit = {
    chisel3.Driver.execute(args, () => new WindowRow(6))
  }
}
