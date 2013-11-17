object scratch {

  val l = List(('a', 1), ('b', 2), ('c', 3), ('d', 4))
                                                  //> l  : List[(Char, Int)] = List((a,1), (b,2), (c,3), (d,4))
  
 	l ::: ('t', 534) :: Nil                   //> res0: List[(Char, Int)] = List((a,1), (b,2), (c,3), (d,4), (t,534))
 	
 	l match {
 		case ('a', n) :: xs => ('c', 3443) :: xs
 	}                                         //> res1: List[(Char, Int)] = List((c,3443), (b,2), (c,3), (d,4))
}