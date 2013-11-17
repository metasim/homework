package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || r == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def count(opens: Int, remain: List[Char]): Int = {
      if (opens < 0)
        opens
      else if (remain.size == 0)
        opens
      else if (remain.head == '(')
        count(opens + 1, remain.tail)
      else if (remain.head == ')')
        count(opens - 1, remain.tail)
      else
        count(opens, remain.tail)

    }

    count(0, chars) == 0
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (coins.isEmpty) {
      0
    } else if (money == 0) {
      1
    } else if (money / coins.head > 0) {
      countChange(money - coins.head, coins) + countChange(money, coins.tail)
    } else {
      countChange(money, coins.tail)
    }
  }
}
