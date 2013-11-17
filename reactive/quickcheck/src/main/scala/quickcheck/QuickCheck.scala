package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

    property("min1") = forAll {
        a: Int =>
            val h = insert(a, empty)
            (findMin(h) == a) :| "minimum after single insertion should be item"
    }

    property("gen1") = forAll {
        (h: H) =>
            val m = if (isEmpty(h)) 0 else findMin(h)
            findMin(insert(m, h)) == m
    }

    property("meldEmpty") = forAll {
        (h: H) =>
            (meld(h, empty) == h) :| "left meld" &&
                (meld(empty, h) == h) :| "right meld"
    }

    property("detectsEmpty") = isEmpty(empty)

    property("noMinInEmpty") = throws(classOf[NoSuchElementException])(findMin(empty))

    property("noRemoveForEmpty") = throws(classOf[NoSuchElementException])(deleteMin(empty))

    property("detectsEmptyAfterRemove") = forAll {
        (i: Int) =>
            val h = insert(i, empty)
            isEmpty(deleteMin(h))
    }

    property("minOfPair") = forAll {
        (l: Int, r: Int) =>
            val h = insert(l, insert(r, empty))
            findMin(h) == Math.min(l, r)
    }

    property("remove") = forAll {
        (i: Int) =>
            val h = insert(i, empty)
            deleteMin(h) == empty
    }

    property("meldIsCommutative") = forAll {
        (h1: H, h2: H) =>
            val m1 = meld(h1, h2)
            val m2 = meld(h2, h1)

            length(m1) == length(m2) &&
            seqValues(m1) == seqValues(m2)
    }

    property("meldMaintainsMins") = forAll(genNonEmpty, genNonEmpty) {
        (h1: H, h2: H) =>
            val h = meld(h1, h2)
            val m = findMin(h)
            m == findMin(h1) || m == findMin(h2)
    }

    property("meldAggregatesSize") = forAll {
        (h1: H, h2: H) =>
            val h = meld(h1, h2)
            length(h) == length(h1) + length(h2)
    }

    property("properlyOrdered") = forAll {
        (h: H) =>
            val extracted = seqValues(h)
            extracted == extracted.sorted
    }

    property("insertionKeepsOrder") = forAll {
        (h: H, i: Int) =>
            val s1 = seqValues(h)
            val h2 = insert(i, h)
            val s2 = seqValues(h2)
            val s3 = s2.diff(List(i))

            s1 == s3

    }

    property("propertySized") = forAll(genNonEmpty) {
        (h: H) =>
            val shorter = deleteMin(h)
            length(h) - 1 == length(shorter)
    }

    property("sameValueMultipleTimes") = forAll(genNonEmpty) {
        (h: H) =>
            classify(length(h) > 5, "large", "small") {
                val min = findMin(h)
                val num = if (min > Int.MinValue) min - 1 else min
                val extra = Seq.fill(3)(num)
                val updated = extra.foldLeft(h)((h, i) => insert(i, h))
                (length(updated) == length(h) + extra.size) :| "grew" &&
                    (findMin(updated) == num) :| "min maintained" &&
                    (findMin(deleteMin(updated)) == num) :| "min after delete"
            }
    }

    private def seqValues(heap: H): List[Int] = {
        if (isEmpty(heap)) List[Int]()
        else findMin(heap) :: seqValues(deleteMin(heap))
    }


    private def length(h: H): Int =
        if (isEmpty(h)) 0
        else 1 + length(deleteMin(h))

    private def genEmpty: Gen[H] = value(empty)

    private def genSingle: Gen[H] = for {
        i <- arbitrary[Int]
    } yield insert(i, empty)

    private def genNonEmpty: Gen[H] = for {
        h1 <- genSingle
        h2 <- frequency((30, genNonEmpty), (1, genEmpty))
    } yield meld(h1, h2)

    lazy val genHeap: Gen[H] = frequency((10, genNonEmpty), (1, genEmpty))

    implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)
}
