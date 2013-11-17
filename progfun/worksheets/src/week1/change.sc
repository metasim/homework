package week1

object change {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val c = List(500, 5, 50, 100, 20, 200, 10)      //> c  : List[Int] = List(500, 5, 50, 100, 20, 200, 10)
  //val c = List(1, 2)
  val s = c sortWith (_ > _)                      //> s  : List[Int] = List(500, 200, 100, 50, 20, 10, 5)

  def test(amt: Int, coins: List[Int]): Int = {
    if (coins.isEmpty) {
      0
    } else if (amt == 0) {
      1
    } else if (amt / coins.head > 0) {
      test(amt - coins.head, coins) + test(amt, coins.tail)
    } else {
      test(amt, coins.tail)
    }

  }                                               //> test: (amt: Int, coins: List[Int])Int

  test(4, s)                                      //> res0: Int = 0

  test(5, s)                                      //> res1: Int = 1
  test(10, s)                                     //> res2: Int = 2
  test(15, s)                                     //> res3: Int = 2
  test(20, s)                                     //> res4: Int = 4
}