
package shiftregister

import chisel3._
//import chisel3.util._
import chisel3.iotesters._
import test_helpers._

class ShiftRegisterTester( factory: () => ShiftRegisterIfc[UInt]) extends GenericTest {
  behavior of s"ShiftRegister"
  it should "compile and execute without expect violations" in {
    chisel3.iotesters.Driver.execute( factory, optionsManager) { c =>
      new PeekPokeTester(c) {
        val mask = (BigInt(1)<<c.io.a.getWidth)-1
        for { i <- 0 until 1000} {
          poke( c.io.a, i)
          if (i >= c.n) {
            expect( c.io.z, (i-c.n) & mask)
          }
          step(1)
        }
      }
    } should be (true)
  }
}

class ShiftRegisterImpl0Test_3 extends ShiftRegisterTester( () => new ShiftRegisterImpl0( UInt(32.W), 3))

class ShiftRegisterImpl1Test_1 extends ShiftRegisterTester( () => new ShiftRegisterImpl1( UInt(32.W), 1))
class ShiftRegisterImpl1Test_2 extends ShiftRegisterTester( () => new ShiftRegisterImpl1( UInt(32.W), 2))
class ShiftRegisterImpl1Test_3 extends ShiftRegisterTester( () => new ShiftRegisterImpl1( UInt(32.W), 3))
class ShiftRegisterImpl1Test_4 extends ShiftRegisterTester( () => new ShiftRegisterImpl1( UInt(32.W), 4))
class ShiftRegisterImpl1Test_5 extends ShiftRegisterTester( () => new ShiftRegisterImpl1( UInt(32.W), 5))


class ShiftRegisterImpl1Test_15 extends ShiftRegisterTester( () => new ShiftRegisterImpl1( UInt(32.W), 15))
class ShiftRegisterImpl1Test_16 extends ShiftRegisterTester( () => new ShiftRegisterImpl1( UInt(32.W), 16))
class ShiftRegisterImpl1Test_17 extends ShiftRegisterTester( () => new ShiftRegisterImpl1( UInt(32.W), 17))
