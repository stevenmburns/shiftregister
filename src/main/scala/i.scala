package shiftregister

import chisel3._
import chisel3.util._

class ShiftRegisterIfc[T <: Data](proto : T, val n : Int) extends Module {
  val io = IO(new Bundle{
    val a = Input(proto.cloneType)
    val z = Output(proto.cloneType)
  })
}

class ShiftRegisterImpl0[T <: Data](proto : T, n : Int) extends ShiftRegisterIfc(proto,n) {
  io.z := ShiftRegister( io.a, n)
}

class ShiftRegisterImpl1[T <: Data](proto : T, n : Int) extends ShiftRegisterIfc(proto,n) {

  val wcnt = RegInit( UInt((1+log2Ceil(n)).W), init=0.U)
  val rcnt = RegInit( UInt((1+log2Ceil(n)).W), init=1.U)

  wcnt := wcnt + 1.U
  when ( wcnt === (n-1).U) {
    wcnt := 0.U
  }

  rcnt := rcnt + 1.U
  when ( rcnt === (n-1).U) {
    rcnt := 0.U
  }

  val mem = SyncReadMem( n, io.a.cloneType)
  mem.write( wcnt, io.a)
  io.z := mem.read( rcnt)
}
