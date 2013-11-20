package simulations

import common._

class Wire {
  private var sigVal = false
  private var actions: List[Simulator#Action] = List()

  def getSignal: Boolean = sigVal

  def setSignal(s: Boolean) {
    if (s != sigVal) {
      sigVal = s
      actions.foreach(action => action())
    }
  }

  def addAction(a: Simulator#Action) {
    actions = a :: actions
    a()
  }
}

abstract class CircuitSimulator extends Simulator {

  val InverterDelay: Int
  val AndGateDelay: Int
  val OrGateDelay: Int

  def probe(name: String, wire: Wire) {
    wire addAction {
      () =>
        afterDelay(0) {
          println(
            "  " + currentTime + ": " + name + " -> " + wire.getSignal)
        }
    }
  }

  def inverter(input: Wire, output: Wire) {
      def invertAction() {
        val inputSig = input.getSignal
        afterDelay(InverterDelay) { output.setSignal(!inputSig) }
      }
    input addAction invertAction
  }

  def andGate(a1: Wire, a2: Wire, output: Wire) {
      def andAction() {
        val a1Sig = a1.getSignal
        val a2Sig = a2.getSignal
        afterDelay(AndGateDelay) { output.setSignal(a1Sig & a2Sig) }
      }
    a1 addAction andAction
    a2 addAction andAction
  }

  //
  // to complete with orGates and demux...
  //

  def orGate(a1: Wire, a2: Wire, output: Wire) {
      def orAction() {
        val a1Sig = a1.getSignal
        val a2Sig = a2.getSignal
        afterDelay(OrGateDelay) { output.setSignal(a1Sig | a2Sig) }
      }
    a1 addAction orAction
    a2 addAction orAction
  }

  def orGate2(a1: Wire, a2: Wire, output: Wire) {
    val notIn1, notIn2, notOut = new Wire
    inverter(a1, notIn1)
    inverter(a2, notIn2)
    andGate(notIn1, notIn2, notOut)
    inverter(notOut, output)
  }

  /**
   * Write the method demux which realizes a demultiplexer with n control wires
   * (and 2 to the power n output wires). The demultiplexer directs the signal
   *  from an input wire based on the control signal. The rest of the output signals
   *  are set to 0. The demux method has the following signature:
   *
   * def demux(in: Wire, c: List[Wire], out: List[Wire])
   *
   * The list of control wires (c) and the the list of output wires (out) are assumed to be
   * sorted by decreasing index: e.g. the head of the c list contains the control wire of index n-1.
   *
   * Your implementation should be recursive and based on the gates that you
   * implemented previously. As a hint, think of the base case: the simplest demultiplexer has 0
   * control wires and 1 output wire. Refer to the image below for some intuition on this last part.
   */
  def demux(in: Wire, c: List[Wire], out: List[Wire]) {
    c match {
      case Nil =>
        in addAction (() => out.head.setSignal(in.getSignal))
      case c :: xs =>
        val nc, a0, a1 = new Wire
        
        inverter(c, nc)
        
        andGate(in, nc, a0)
        andGate(in, c, a1)
        
        val (leftOut, rightOut) = out.splitAt(out.length/2)
        
        demux(a0, xs, rightOut)
        demux(a1, xs, leftOut)
    }
  }
}

object Circuit extends CircuitSimulator {
  val InverterDelay = 1
  val AndGateDelay = 3
  val OrGateDelay = 5

  def andGateExample {
    val in1, in2, out = new Wire
    andGate(in1, in2, out)
    probe("in1", in1)
    probe("in2", in2)
    probe("out", out)
    in1.setSignal(false)
    in2.setSignal(false)
    run

    in1.setSignal(true)
    run

    in2.setSignal(true)
    run
  }

  //
  // to complete with orGateExample and demuxExample...
  //
}

object CircuitMain extends App {
  // You can write tests either here, or better in the test class CircuitSuite.
  Circuit.andGateExample
}
