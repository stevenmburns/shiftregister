
package line_buffers

import org.scalatest.{ Matchers, FlatSpec, GivenWhenThen}

import chisel3._
import chisel3.iotesters._

import test_helpers._

class CopyTester( factory: () => Copy) extends GenericTest {
  behavior of s"Copy"
  it should "compile and execute without expect violations" in {
    chisel3.iotesters.Driver.execute( factory, optionsManager) { c =>
      new PeekPokeTester(c) {
        poke(c.io.a, 1)
        expect(c.io.z, 1)
        step(1)
        poke(c.io.a, 0)
        expect(c.io.z, 0)
        step(1)
      }
    } should be {true}
  }
}

class DelayTester( factory: () => Delay) extends GenericTest {
  behavior of s"Delay"
  it should "compile and execute without expect violations" in {
    chisel3.iotesters.Driver.execute( factory, optionsManager) { c =>
      new PeekPokeTester(c) {
        poke(c.io.a, 1)
        step(1)
        poke(c.io.a, 0)
        expect(c.io.z, 1)
        step(1)
        poke(c.io.a, 1)
        expect(c.io.z, 0)
      }
    } should be {true}
  } 
}

class DelayNTester( factory: () => DelayN) extends GenericTest {
  behavior of s"DelayN"
  it should "compile and execute without expect violations" in {
    chisel3.iotesters.Driver.execute( factory, optionsManager) { c =>
      new PeekPokeTester(c) {
        for { i <- 0 until (c.n+10) } {
          if ( i >= c.n) {
            expect( c.io.z, i-c.n)
          }
          poke(c.io.a, i)
          step(1)
        }
      }
    } should be {true}
  } 

}

class WindowBufferTester( factory: () => WindowBuffer) extends GenericTest {
  behavior of s"WindowBuffer"
  it should "compile and execute without expect violations" in {
    chisel3.iotesters.Driver.execute( factory, optionsManager) { c =>
      new PeekPokeTester(c) {
        for { i <- 0 until (c.n+10) } {
          if ( i >= c.n) {
            // Clean this up; it only works for c.n == 3
            expect( c.io.z(c.n-3), i-c.n+2)
            expect( c.io.z(c.n-2), i-c.n+1)
            expect( c.io.z(c.n-1), i-c.n)
          }
          poke(c.io.a, i)
          step(1)
        }
      }
    } should be {true}
  } 
}

class CopyTest extends CopyTester( () => new Copy)
class DelayTest extends DelayTester( () => new Delay)
class DelayNTest3 extends DelayNTester( () => new DelayN( 3))
class DelayNTest5 extends DelayNTester( () => new DelayN( 5))
class WindowBufferTest3 extends WindowBufferTester( () => new WindowBuffer( 3))
