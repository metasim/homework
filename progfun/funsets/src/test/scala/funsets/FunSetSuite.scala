package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    
    val all = union(union(s1, s2), s3)

    val lower = singletonSet(-bound)
    val upper = singletonSet(bound)

    val expandedSet = union(union(all, lower), upper)

  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersection contains no elements") {
    new TestSets {
      val s = intersect(s1, s2)

      assert(!contains(s, 1), "Interect 1")
      assert(!contains(s, 2), "Interect 2")
      assert(!contains(s, 3), "Interect 3")
    }
  }

  test("intersection of same contains single element") {
    new TestSets {
      val s = intersect(s1, s1)
      assert(contains(s, 1))
      assert(!contains(s, 2))
      assert(!contains(s, 3))

    }
  }

  test("intersection of union contains single element") {
    new TestSets {
      val s = intersect(union(s1, s2), union(s2, s3));

      assert(!contains(s, 1))
      assert(contains(s, 2))
      assert(!contains(s, 3))
    }
  }

  test("diff removes element") {
    new TestSets {
      val d = diff(all, s2)
      assert(contains(d, 1))
      assert(!contains(d, 2))
      assert(contains(d, 3))

      val d2 = diff(d, s1)
      assert(!contains(d2, 1))
      assert(!contains(d2, 2))
      assert(contains(d2, 3))

    }
  }
  
  test("multivalue set") {
      val ms1 = multiValueSet(1,3,5,7,9)
      assert(forall(ms1, _ % 2 != 0), "odd numbers")
      assert(!exists(ms1, _ % 2 == 0), "no even numbers")
      assert(!contains(ms1, bound))
  }
  
  test("diff is not symmetric") {
      val ms1 = multiValueSet(1,3,4,5,7,1000)
      val ms2 = multiValueSet(1,2,3,4)
      
      val d1 = diff(ms1, ms2)
      
      assert(setsEqual(d1, multiValueSet(5, 7, 1000)))
      
      val ms3 = multiValueSet(-1000, 0)
      
      val d2 = diff(ms2, ms3)
      assert(setsEqual(d2, multiValueSet(1,2,3,4)))
  }

  test("filter all but one") {
    new TestSets {
      val a = union(union(s1, s2), s3)
      val f = filter(a, _ == 3)

      assert(!contains(f, 1))
      assert(!contains(f, 2))
      assert(contains(f, 3))
    }

  }

  test("filter one") {
    new TestSets {
      val f = filter(all, _ != 3)

      assert(contains(f, 1))
      assert(contains(f, 2))
      assert(!contains(f, 3))
    }

  }

  test("forAll") {
    new TestSets {
      assert(forall(expandedSet, x => x >= -bound && x <= bound))
      assert(forall(expandedSet, x => x == -bound || x == bound || contains(s1, x) || contains(s2, x) || contains(s3, x)))
      assert(forall(expandedSet, x => x != 0))
      assert(!forall(expandedSet, x => x > 0))
      assert(!forall(expandedSet, x => (x % 2) == 0))
      assert(forall(expandedSet, x => contains(expandedSet, x)))
      
      assert(forall(emptySet(), x => x == x*x))
    }
  }

  test("exists") {
    new TestSets {

      assert(exists(expandedSet, x => x >= -bound && x <= bound))
      assert(exists(expandedSet, x => x == -bound || x == bound || contains(s1, x) || contains(s2, x) || contains(s3, x)))
      assert(exists(expandedSet, x => x != 0))
      assert(exists(expandedSet, x => x > 0))
      assert(exists(expandedSet, x => (x % 2) == 0))
      assert(exists(expandedSet, x => contains(expandedSet, x)))
      assert(!exists(expandedSet, x => x > bound))
      assert(!exists(expandedSet, x => x < -bound))
      assert(!exists(expandedSet, x => x > Integer.MAX_VALUE))
    }
  }

  test("map") {
    new TestSets {
        val m1 = map(all, x => x + 1)
        
        assert(contains(m1, 4))
        assert(contains(m1, 2))
        assert(!contains(m1, 5))
        assert(!contains(m1, 1))
        
        val m2 = map(emptySet(), x => x + 1)
        assert(!contains(m2, 1))
    }
  }
}
