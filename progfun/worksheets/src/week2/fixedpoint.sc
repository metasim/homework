package week2
import scala.math._

object fixedpoint {
    val tolerance = 0.0001                        //> tolerance  : Double = 1.0E-4
    def isCloseEnough(x: Double, y: Double) =
        abs((x - y) / x) / x < tolerance          //> isCloseEnough: (x: Double, y: Double)Boolean
    def fixedPoint(f: Double => Double)(firstGuess: Double) = {
        def iterate(guess: Double): Double = {
            val next = f(guess)
            if (isCloseEnough(guess, next)) next
            else iterate(next)
        }
        iterate(firstGuess)
    }                                             //> fixedPoint: (f: Double => Double)(firstGuess: Double)Double

	def averageDamp(f: Double => Double)(x: Double) = (x + f(x)) / 2
                                                  //> averageDamp: (f: Double => Double)(x: Double)Double
	
	// sqrt(x) => value y s.t. y^2 = x -> fixedPoint(y = x/y)
	
	def sqrt(x: Double) : Double = {
		fixedPoint(averageDamp(y => x/y))(1)
	}                                         //> sqrt: (x: Double)Double
	
	sqrt(2)                                   //> res0: Double = 1.4142135623746899
}