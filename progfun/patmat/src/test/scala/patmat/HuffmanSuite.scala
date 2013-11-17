package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
    trait TestTrees {
        val t1 = Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5)
        val t2 = Fork(Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5), Leaf('d', 4), List('a', 'b', 'd'), 9)
        val helloTwice = string2Chars("hello, world, hello")
        val helloCT = createCodeTree(helloTwice)
    }

    trait TestTables {
        val helloTwice = string2Chars("hello, world, hello")
        val encoder = encode(createCodeTree(helloTwice))(_)
        val tab0: CodeTable = List(
            ('l', encoder(List('l')))
        )
        
        val tab1: CodeTable = List(
            ('l', encoder(List('l'))),
            ('h', encoder(List('h')))
        )

        val tab2: CodeTable = List(
            ('o', encoder(List('o'))),
            ('w', encoder(List('w')))
        )

        val tab3: CodeTable = List(
            ('l', encoder(List('l'))),
            ('h', encoder(List('h'))),
            ('o', encoder(List('o'))),
            ('w', encoder(List('w')))
        )

    }

    test("weight of a larger tree") {
        new TestTrees {
            assert(weight(t1) === 5)
        }
    }

    test("chars of a larger tree") {
        new TestTrees {
            assert(chars(t2) === List('a', 'b', 'd'))
        }
    }

    test("string2chars(\"hello, world\")") {
        assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
    }

    test("makeOrderedLeafList for some frequency table") {
        assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 3)))
        assert(makeOrderedLeafList(List(('t', 1), ('e', 1), ('x', 1))) === List(Leaf('e', 1), Leaf('t', 1), Leaf('x', 1)))
        assert(makeOrderedLeafList(List(('z', 1), ('y', 2), ('x', 2))) === List(Leaf('z', 1), Leaf('x', 2), Leaf('y', 2)))
    }

    test("combine of some leaf list") {
        val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
        assert(combine(leaflist) === List(Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3), Leaf('x', 4)))
    }

    test("decode and encode a very short text should be identity") {
        new TestTrees {
            assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
        }
    }

    test("properly count frequency") {
        new TestTrees {
            val counts = times(helloTwice).sortBy(t => (t._2, t._1))
            assert(counts === List(('d', 1), ('r', 1), ('w', 1), (' ', 2), (',', 2), ('e', 2), ('h', 2), ('o', 3), ('l', 5)))
        }
    }

    test("until applies input functions and stops") {
        new TestTrees {
            val leaves = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4), Leaf('a', 8))
            val branched = until(singleton, combine)(leaves)
            assert(branched.size == 1)
            assert(branched === List(Fork(Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3), Fork(Leaf('x', 4), Leaf('a', 8), List('x', 'a'), 12), List('e', 't', 'x', 'a'), 15)))
        }
    }

    test("createCodeTree") {
        new TestTrees {
            val tree = createCodeTree(helloTwice)
            assert(chars(tree).sortWith(_ < _) === string2Chars("hello, world, hello").distinct.sortWith(_ < _))
        }
    }

    test("get secret message") {
        assert(decodedSecret != null, "is not null")
        assert(decodedSecret.size > 0)
        assert(decodedSecret === string2Chars("huffmanestcool"))
    }

    test("encoding builds tree") {
        new TestTrees {
            val encoded = encode(helloCT)(helloTwice)
            val decoded = decode(helloCT, encoded)
            assert(decoded === helloTwice)
        }
    }

    test("merging code tables") {
        new TestTables {
            assert(mergeCodeTables(tab0, tab0) === tab0)
            assert(mergeCodeTables(tab1, tab2) === tab3)
        }
    }
    
    test("quickEncoding") {
        new TestTrees {
            val encoded = quickEncode(helloCT)(helloTwice)
            val decoded = decode(helloCT, encoded)
            assert(decoded === helloTwice)
        }
    }
}
