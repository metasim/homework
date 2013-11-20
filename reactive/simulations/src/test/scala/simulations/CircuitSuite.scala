package simulations

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CircuitSuite extends CircuitSimulator with FunSuite {
  val InverterDelay = 1
  val AndGateDelay = 3
  val OrGateDelay = 5

  test("andGate example") {
    val in1, in2, out = new Wire
    andGate(in1, in2, out)
    in1.setSignal(false)
    in2.setSignal(false)
    run

    assert(out.getSignal === false, "and 1")

    in1.setSignal(true)
    run

    assert(out.getSignal === false, "and 2")

    in2.setSignal(true)
    run

    assert(out.getSignal === true, "and 3")

    in1.setSignal(false)
    run
    assert(out.getSignal === false, "and 4")

  }

  test("orGate") {
    val in1, in2, out = new Wire
    orGate(in1, in2, out)
    in1.setSignal(false)
    in2.setSignal(false)
    run

    assert(out.getSignal === false, "or 1")

    in1.setSignal(true)
    run

    assert(out.getSignal === true, "or 2")

    in2.setSignal(true)
    run

    assert(out.getSignal === true, "or 3")

    in1.setSignal(false)
    run

    assert(out.getSignal === true, "or 4")
  }

  test("orGate2") {
    val in1, in2, out = new Wire
    orGate2(in1, in2, out)
    in1.setSignal(false)
    in2.setSignal(false)
    run

    assert(out.getSignal === false, "or 1")

    in1.setSignal(true)
    run

    assert(out.getSignal === true, "or 2")

    in2.setSignal(true)
    run

    assert(out.getSignal === true, "or 3")

    in1.setSignal(false)
    run

    assert(out.getSignal === true, "or 4")
  }

  test("demux: zero control wires") {
    val in, out = new Wire
    demux(in, List(), List(out))
    in.setSignal(false)
    run

    assert(out.getSignal === false, "initially off")

    in.setSignal(true)
    run

    assert(out.getSignal === true, "on when input is on")

    in.setSignal(false)
    run

    assert(out.getSignal === false, "off when input goes back off")
  }

  test("demux: one control wire") {
    val in, c0, out0, out1 = new Wire
    demux(in, List(c0), List(out1, out0))
    in.setSignal(false)
    run

    assert(out0.getSignal === false, "out0 initially off")
    assert(out1.getSignal === false, "out1 initially off")

    in.setSignal(true)
    run

    assert(out0.getSignal === true, "(in=1, c0=0) => (out0=1)")
    assert(out1.getSignal === false, "(in=1, c0=0) => (out1=0)")

    c0.setSignal(true)
    run

    assert(out0.getSignal === false, "(in=1, c0=1) => (out0=0)")
    assert(out1.getSignal === true, "(in=1, c0=1) => (out1=1)")

    in.setSignal(false)
    run

    assert(out0.getSignal === false, "(in=0, c0=1) => (out0=0)")
    assert(out1.getSignal === false, "(in=0, c0=1) => (out1=0)")

  }

  test("demux: two control wire") {
    val in, c0, c1, out0, out1, out2, out3 = new Wire

    demux(in, List(c1, c0), List(out3, out2, out1, out0))

    in.setSignal(false)
    run

    assert(out0.getSignal === false, "out0 initially off")
    assert(out1.getSignal === false, "out1 initially off")
    assert(out2.getSignal === false, "out2 initially off")
    assert(out3.getSignal === false, "out3 initially off")

    in.setSignal(true)
    run

    assert(out0.getSignal === true, "(in=1, c0=0, c1=0) => (out0=1)")
    assert(out1.getSignal === false, "(in=1, c0=0, c1=0) => (out1=0)")
    assert(out2.getSignal === false, "(in=1, c0=0, c1=0) => (out2=0)")
    assert(out3.getSignal === false, "(in=1, c0=0, c1=0) => (out3=0)")

    c0.setSignal(true)
    run

    assert(out0.getSignal === false, "(in=1, c0=1, c1=0) => (out0=0)")
    assert(out1.getSignal === true, "(in=1, c0=1, c1=0) => (out1=1)")
    assert(out2.getSignal === false, "(in=1, c0=1, c1=0) => (out2=0)")
    assert(out3.getSignal === false, "(in=1, c0=1, c1=0) => (out3=0)")
    in.setSignal(false)
    run

    assert(out0.getSignal === false, "(in=0, c0=1, c1=0) => (out0=0)")
    assert(out1.getSignal === false, "(in=0, c0=1, c1=0) => (out1=0)")
    assert(out2.getSignal === false, "(in=0, c0=1, c1=0) => (out2=0)")
    assert(out3.getSignal === false, "(in=0, c0=1, c1=0) => (out3=0)")
  }

  test("demux: multiple control wires") {
    val numCtrl = 2
    val numOut = 1 << numCtrl

    val ctrlWires = List.fill(numCtrl)(new Wire)
    val outWires = List.fill(numOut)(new Wire)

      /**
       * Ensure that the output pin with the given index is equal to
       *  `state`, and all other pins are false.
       */
      def checkOutputs(outVal: Int, state: Boolean): Unit = {
        printWires("out", outWires)
        outWires.zipWithIndex.foreach { t =>
          assert(!t._1.getSignal || t._2 == outVal, s"$t should be false")
        }
        assert(outWires(outVal).getSignal === state, s"output $outVal is $state")
      }

    val in = new Wire
    demux(in, ctrlWires.reverse, outWires.reverse)

    probe("in", in)
    ctrlWires.zipWithIndex.foreach(p => probe("c" + p._2, p._1))
    outWires.zipWithIndex.foreach(p => probe("out" + p._2, p._1))

    printWires("c", ctrlWires)

    in.setSignal(false)
    run
    checkOutputs(0, false)

    in.setSignal(true)
    run

    checkOutputs(0, true)

    ctrlWires(0).setSignal(true)
    printWires("c", ctrlWires)
    run
    checkOutputs(1, true)

    in.setSignal(false)
    run
    checkOutputs(1, false)

    in.setSignal(true)
    ctrlWires(1).setSignal(true)
    printWires("c", ctrlWires)

    run

    checkOutputs(3, true)

  }

  def printWires(prefix: String, ctrlWires: List[Wire]): Unit = {
    val r = 0 until ctrlWires.size
    println(r.map(prefix + _.toString).mkString("\t"))
    println(ctrlWires.map(p => if (p.getSignal) "1" else "0").mkString("\t"))

  }
  def bitString(value: Int, len: Int): String = {
    s"%${len}s".format(value.toBinaryString, len).replace(' ', '0')
  }

}
