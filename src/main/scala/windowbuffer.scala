class WindowRow (val wsize: Int, val bitwidth: Int = 8) extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(bitwidth.W))
    val out = Output(Vec(wsize, UInt(bitwidth.W)))
  })
  
  //val state = Wire(Vec(wsize, UInt(bitwidth.W)))
  //val state = RegInit(Vec(wsize, 0.U(bitwidth.W)))
  val state = Vec(wsize, RegInit(0.U(bitwidth.W)))
    
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

  val state = Vec(lsize, RegInit(0.U(bitwidth.W)))
    
  state(0) := io.in
    
  for (i <- 1 until (lsize-1)){
      state(i) := state(i-1)
  }
      
  io.out := state(lsize-1)
  
}

test(new LineBuff(6)) { c =>
    c.io.in.poke(5.U)
    c.clock.step(6)
    c.io.out.expect(5.U)
  
}
println("SUCCESS!!")