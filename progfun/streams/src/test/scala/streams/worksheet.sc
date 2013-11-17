package streams

object worksheet {
    println("Welcome to the Scala worksheet")     //> Welcome to the Scala worksheet

    val bs = new BloxorzSuite                     //> bs  : streams.BloxorzSuite = streams.BloxorzSuite@5aa4215

    val l1 = new bs.Level1 {
        println(pathsFromStart)

    }                                             //> Stream((Block(Pos(1,1),Pos(1,1)),List()), ?)
                                                  //| l1  : streams.worksheet.bs.Level1 = streams.worksheet$$anonfun$main$1$$anon$
                                                  //| 1@403a6e15

    l1                                            //> res0: streams.worksheet.bs.Level1 = streams.worksheet$$anonfun$main$1$$anon$
                                                  //| 1@403a6e15
}