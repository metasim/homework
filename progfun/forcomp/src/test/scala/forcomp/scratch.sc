package forcomp

import Anagrams._

object scratch {
    println("Welcome to the Scala worksheet")     //> Welcome to the Scala worksheet

    wordOccurrences("Sean")                       //> res0: forcomp.Anagrams.Occurrences = List((a,1), (e,1), (n,1), (s,1))
    
     val o = sentenceOccurrences(List("Linux", "rulez"))
                                                  //> o  : forcomp.Anagrams.Occurrences = List((e,1), (i,1), (l,2), (n,1), (r,1), 
                                                  //| (u,2), (x,1), (z,1))


		o groupBy (_._1)                  //> res1: scala.collection.immutable.Map[Char,List[(Char, Int)]] = Map(e -> List
                                                  //| ((e,1)), x -> List((x,1)), n -> List((n,1)), u -> List((u,2)), i -> List((i,
                                                  //| 1)), l -> List((l,2)), r -> List((r,1)), z -> List((z,1)))


/*
    val e = sentenceAnagrams(List("Yes", "man"))

    val r = List(
        List("en", "as", "my"),
        List("en", "my", "as"),
        List("man", "yes"),
        List("men", "say"),
        List("as", "en", "my"),
        List("as", "my", "en"),
        List("sane", "my"),
        List("Sean", "my"),
        List("my", "en", "as"),
        List("my", "as", "en"),
        List("my", "sane"),
        List("my", "Sean"),
        List("say", "men"),
        List("yes", "man")
    )

    e.toSet == r.toSet

    val sentence = List("Linux", "rulez")
    val anas = List(
        List("Rex", "Lin", "Zulu"),
        List("nil", "Zulu", "Rex"),
        List("Rex", "nil", "Zulu"),
        List("Zulu", "Rex", "Lin"),
        List("null", "Uzi", "Rex"),
        List("Rex", "Zulu", "Lin"),
        List("Uzi", "null", "Rex"),
        List("Rex", "null", "Uzi"),
        List("null", "Rex", "Uzi"),
        List("Lin", "Rex", "Zulu"),
        List("nil", "Rex", "Zulu"),
        List("Rex", "Uzi", "null"),
        List("Rex", "Zulu", "nil"),
        List("Zulu", "Rex", "nil"),
        List("Zulu", "Lin", "Rex"),
        List("Lin", "Zulu", "Rex"),
        List("Uzi", "Rex", "null"),
        List("Zulu", "nil", "Rex"),
        List("rulez", "Linux"),
        List("Linux", "rulez")
    ).length

    val q = sentenceAnagrams(sentence)
    q.length

   // q.toSet == anas.toSet
   */

}