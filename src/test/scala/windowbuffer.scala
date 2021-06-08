
package shiftregister

import chisel3._
//import chisel3.util._
import chisel3.iotesters._
import test_helpers._

class LineBuffTester( factory: () => LineBuff) extends GenericTest {
  behavior of s"LineBuff"
  it should "compile and execute without expect violations" in {
    chisel3.iotesters.Driver.execute( factory, optionsManager) { c =>
      new PeekPokeTester(c) {
        poke( c.io.in, 5)
        for { i <- 0 until 6} {
          step(1)
          println( s"${peek(c.io.out)}")
        }
        expect( c.io.out, 5)
        step(1)
      }

    } should be (true)
  }
}

class LineBuffTest_6 extends LineBuffTester( () => new LineBuff( 6))
