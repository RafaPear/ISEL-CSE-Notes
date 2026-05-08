package serie2.part3

import kotlin.test.*

class SplitEvensAndOddsTest {

    @Test
    fun testEmpty() {
        val l= emptyList<Int>()
        splitEvensAndOdds( l )
        assertTrue(l.isEmpty() )
    }

    @Test
    fun testOneElement(){
        val l = singletonList( 1)
        splitEvensAndOdds(l)
        assertEquals(listOf( 1 ), l.toList())
    }

    @Test
    fun testSplitWithTwoNumbers(){ // Size even
        val expected = listOf(2, 1)
        var l = listOf(1, 2).toLinkedList() // swapped
        splitEvensAndOdds(l)
        assertEquals(expected, l.toList())

        l= listOf(2, 1).toLinkedList()      // not swapped
        splitEvensAndOdds(l)
        assertEquals(expected, l.toList())

        l= listOf(4, 2).toLinkedList()      // all even
        splitEvensAndOdds( l )
        assertEquals(listOf(2, 4), l.toList().sorted())

        l= listOf(1, 3).toLinkedList()      // all odd
        splitEvensAndOdds( l )
        assertEquals(listOf(1, 3), l.toList().sorted())
    }

    /**
     * Test one sequence
     *
     * @sequence: the sequence of numbers to test
     * @oddIndex: the index of the first odd number after the split
     */
    private fun testSequence( sequence: List<Int>,  oddIndex: Int ){
        var sentinel= sequence.toLinkedList() // create doubly linked list with sentinel
        splitEvensAndOdds(sentinel)
        val r= sentinel.toList()
        for (i in 0 ..< oddIndex){          // check even numbers
            assertTrue((r[i] % 2) == 0, "Expected even number at index $i, but got ${r[i]}")
        }
        for (i in oddIndex ..< r.size){    // check odd numbers
            assertTrue((r[i] % 2) == 1, "Expected odd number at index $i, but got ${r[i]}")
        }
        assertEquals(sequence.sorted(), r.sorted(), "result not contain all numbers") // check if all numbers are present
    }

    @Test
    fun testSplitWithThreeNumbers(){ //Size odd
        testSequence(listOf(1, 2, 3), 1)
        testSequence(listOf(2, 3, 4), 2)
        testSequence(listOf(12, 4, 6), 3)
        testSequence(listOf(5, 1, 7), 0)
    }

    @Test
    fun testOnlyOneEven(){
        testSequence(listOf(1)+List(50){it*2}, 50)
        testSequence(List(50){it*2}+ listOf(1), 50)
        testSequence(List(25){it*2}+ listOf(1)+List(25){it*2}, 50)
    }

    @Test
    fun testOnlyOneOdd(){
        testSequence(listOf(2)+List(50){it*2+1}, 1)
        testSequence(List(50){it*2+1}+ listOf(2), 1)
        testSequence(List(25){it*2+1}+ listOf(2)+List(25){it*2+1}, 1)
    }

    @Test
    fun testSequences(){
        testSequence(List(124){it}, 62)
        testSequence(List(125){it}, 63)
        testSequence(List(150){it}.shuffled(), 75)
    }
}