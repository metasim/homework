import quickcheck.{QuickCheckBinomialHeap, BinomialHeap, QuickCheckHeap}

/**
 *
 */


object scratch extends QuickCheckHeap with BinomialHeap {

    QuickCheckBinomialHeap.genEmpty.sample

    QuickCheckBinomialHeap.genSingle.sample



    QuickCheckBinomialHeap.genNonEmpty.sample


    QuickCheckBinomialHeap.genHeap.sample


}