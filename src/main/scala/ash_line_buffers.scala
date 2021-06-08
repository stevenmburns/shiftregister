package line_buffers

import chisel3._
import chisel3.util._

class Copy extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(8.W))
    val z = Output(UInt(8.W))
  })

  io.z := io.a
}


class Delay extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(8.W))
    val z = Output(UInt(8.W))
  })

  io.z := RegNext(io.a)
}

class DelayN( val n: Int = 1) extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(8.W))
    val z = Output(UInt(8.W))
  })

  io.z := ShiftRegister(io.a, n, en=true.B)
}

class WindowBuffer( val n: Int = 1) extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(8.W))
    val z = Output(Vec(n,UInt(8.W)))
  })

  // in -> Reg -> Reg -> Reg ->
  //           |      |      | 
  //           v      v      v

/*
  io.z := VecInit(for { i <- 0 until n} yield {
    ShiftRegister( io.a, i+1)
  })
 */

  val state = IndexedSeq.fill(n){ Reg( io.a.cloneType)}
  state(0) := io.a
  for { i <- 0 until (n-1)} {
    state(i+1) := state(i)
  }
  io.z := VecInit( state)
}

class Im2Col_Row( val line_sz:Int, val window_sz:Int = 3) extends Module {
  val io = IO(new Bundle {
    val inp = Input(UInt(8.W))
    val out = Output(UInt(8.W))
    val win = Output(Vec(window_sz,UInt(8.W)))
  })
  io.out := ShiftRegister( io.inp, line_sz)
  val wb = Module( new WindowBuffer( window_sz))
  wb.io.a := io.out
  io.win := wb.io.z
}

class Im2Col( val line_sz:Int, val window_sz:Int = 3) extends Module {
  val io = IO(new Bundle {
    val inp = Input(UInt(8.W))
    val out = Output(UInt(8.W))
    val win = Output(Vec(window_sz,Vec(window_sz,UInt(8.W))))
  })

  val ms = for { i <- 0 until window_sz-1} yield {
    Module(new Im2Col_Row( line_sz, window_sz))
  }

  val wb = Module( new WindowBuffer( window_sz))
  wb.io.a := io.inp
  io.win(0) := wb.io.z

  ms(0).io.inp := io.inp
  io.win(1) := ms(0).io.win

  for { i <- 0 until window_sz-1} {
    ms(i+1).io.inp := ms(i).io.out
    io.win(i+1) := ms(i).io.win
  }

}
