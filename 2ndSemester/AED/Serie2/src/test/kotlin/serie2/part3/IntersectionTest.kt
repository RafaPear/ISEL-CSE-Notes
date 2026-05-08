package serie2.part3

import kotlin.test.*

class IntersectionTest {
    @Test
    fun testWithOneElement(){
        val l1 = listOf(1).toLinkedList()
        val l2 = listOf(1).toLinkedList()
        val l = intersection(l1, l2,  Int::compareTo)
        if ( l == null ){
            assertFails {  "Intersection should not be null" }
        }
        else {
            assertEquals( 1, l.value)
            assertNull( l.next, "Next should be null")
            assertNull( l.previous, "Previous should be null")
            assertTrue( l1.isEmpty(), "after intersection list1 should be empty")
            assertTrue( l2.isEmpty(), "after intersection list2 should be empty" )
        }
        assertNull( intersection(listOf(1).toLinkedList(), listOf(2).toLinkedList(), Int::compareTo) )
        assertNull( intersection(listOf(2).toLinkedList(), listOf(1).toLinkedList(), Int::compareTo) )
    }

    // Test with two elements
    @Test
    fun testWithTwoElements(){
        val l1 = listOf(1, 3).toLinkedList()
        val l2 = listOf(1, 3).toLinkedList()
        val l = intersection(l1, l2,  Int::compareTo)
        if ( l == null ){
            assertFails {  "Intersection should not be null" }
        }
        else {
            assertEquals(  listOf(1, 3), nodeToList(l))
            assertTrue( l1.isEmpty())
            assertTrue( l2.isEmpty())
        }
        assertNull( intersection(listOf(1, 4).toLinkedList(),
                                 listOf(2).toLinkedList(), Int::compareTo),
                          "intersection should be null")
        assertNull( intersection(listOf(2, 3).toLinkedList(),
                                 listOf(1).toLinkedList(), Int::compareTo),
                                 "intersection should be null")
    }


    @Test
    fun testWithEmptyIntersection(){
        val l1Test = List(10) { 2*it }
        val l2Test=  List(15) {2*it+1}
        val l1 = l1Test.toLinkedList()
        val l2= l2Test.toLinkedList()
        assertNull( intersection(l1, l2, Int::compareTo) )
        assertEquals( l1Test, l1.toList() )
        assertEquals( l2Test, l2.toList())
        }

    @Test
    fun testOnlyFirstIntersect(){
        val l1 = List(5) { it }.toLinkedList()
        val l2= listOf(0, 20, 30,40).toLinkedList()
        val l= intersection(l1, l2, Int::compareTo)
        assertEquals(listOf(0), nodeToList(l))
        assertEquals( List(4) { it+1 }, l1.toList())
        assertEquals( listOf(20, 30,40), l2.toList())
    }

    @Test
    fun testOnlyLastIntersect(){
        val l1 = List(5) { it*5 }.toLinkedList()
        val l2= listOf(1, 3, 7, 14, 20).toLinkedList()
        val l= intersection(l1, l2, Int::compareTo)
        assertEquals( listOf(20), nodeToList(l))
        assertEquals( List(4) { it*5 }, l1.toList() )
        assertEquals( listOf(1, 3, 7, 14), l2.toList())
    }

    @Test
    fun testIntersection(){
        val l1 = List(125) { it*2 }.toLinkedList()
        val l2=  List(100){it*3}.toLinkedList()
        val l= intersection(l1, l2, Int::compareTo)
        val expected = mutableListOf<Int>()
        for (i in 0 until 250){
            if ( i % 2 == 0 && i % 3 == 0)
                expected.add(i)
        }
        assertEquals( expected, nodeToList(l))
        for( v in l1.toList() )
            assertFalse(v % 3 == 0)
        for( v in l2.toList() )
            assertFalse( v < 250 && v % 2 == 0)
    }
}