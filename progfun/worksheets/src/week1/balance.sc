package week1

object balance {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def count(opens: Int, remain: List[Char]): Int = {
    	if(opens < 0)
    		opens
    	else if(remain.size == 0)
    		opens
      else if(remain.head == '(')
      	count(opens + 1, remain.tail)
      else if(remain.head == ')')
      	count(opens - 1, remain.tail)
      else
      	count(opens, remain.tail)
      		
    }

    return count(0, chars) == 0
  }                                               //> balance: (chars: List[Char])Boolean

  balance("(if (zero? x) max (/ 1 x))".toList)    //> res0: Boolean = true
	!balance("())(".toList)                   //> res1: Boolean = true
	
	!balance("(())()".toList)                 //> res2: Boolean = false
}