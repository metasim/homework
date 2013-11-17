package week2

object rationals {
	val r = new Rational(1,4)                 //> r  : week2.Rational = 1/4
	
		
}

case class Rational(numer: Int, denom: Int) {
	 	override def toString = numer + "/" + denom
}