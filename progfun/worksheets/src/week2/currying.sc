package week2

object currying {

  def aggregate(f: Int => Int)(a: Int, b: Int)(op: (Int, Int) => Int, unitVal: Int): Int = {
    def applyAgg(a: Int, b: Int): Int = {
      if (a > b) unitVal
      else op(f(a), applyAgg(a + 1, b))
    }
    
    applyAgg(a, b)
  }                                               //> aggregate: (f: Int => Int)(a: Int, b: Int)(op: (Int, Int) => Int, unitVal: I
                                                  //| nt)Int


	def sum(f: Int => Int)(lower: Int, upper: Int): Int = aggregate(f)(lower, upper)(_+_, 0)
                                                  //> sum: (f: Int => Int)(lower: Int, upper: Int)Int
	
	
  def product(f: Int => Int)(lower: Int, upper: Int): Int = aggregate(f)(lower, upper)(_*_, 1)
                                                  //> product: (f: Int => Int)(lower: Int, upper: Int)Int
  def factorial(upper: Int): Int = product(x => x)(1, upper)
                                                  //> factorial: (upper: Int)Int

  product(x => x)(1, 6)                           //> res0: Int = 720
  factorial(6)                                    //> res1: Int = 720
  
  sum(x => x)(1, 5)                               //> res2: Int = 15
}