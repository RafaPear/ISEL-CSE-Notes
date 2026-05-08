package serie2.part4

import kotlin.test.*

class HashMapTest {

    // Test put one entry and get value
    @Test
    fun testEmpty(){
        val map = HashMap<Int, String>()
        assertEquals(0, map.size)
        assertNull( map.get(0) )
    }

    // Test put one entry and get value
    @Test
    fun testPutAndGetOneEntry(){
        val map = HashMap<Int, String>()
        assertNull( map.put(1, "one") )
        assertEquals(1, map.size)
        assertEquals("one", map.get(1))
    }

    // Test put and get
    @Test
    fun testPutAndGet(){
        val map = HashMap<Int, String>()
        assertNull( map.put(1, "one"))
        assertNull( map.put(2, "two"))
        assertNull( map.put(3, "three"))
        assertEquals(3, map.size)
        assertEquals("one", map.get(1))
        assertEquals("three", map.get(3))
        assertEquals("two", map.get(2))
    }

    // Test put and get with same key
    @Test
    fun testPutAndGetWithSameKey(){
        val map = HashMap<Int, String>()
        assertNull( map.put(1, "one") )
        assertNull( map.put(2, "three") )
        assertEquals(2, map.size)

        assertEquals("one", map.get(1))
        assertEquals("one", map.put(1, "two"))
        assertEquals("two", map.get(1) )
        assertEquals(2, map.size)
    }

    // Test Iterator with empty map
    @Test
    fun testIteratorWithEmptyMap(){
        val map = HashMap<Int, String>()
        assertFalse( map.iterator().hasNext() )
        var count = 0
        for( entry in map){
            ++count
        }
        assertEquals(0, count)
    }

    // Test Iterator
    @Test
    fun testIterator(){
        val map = HashMap<Int, String>()
        assertNull( map.put(1, "one") )
        assertNull( map.put(2, "two") )
        assertNull( map.put(3, "three") )
        assertTrue( map.iterator().hasNext() )
        val entries = mutableListOf<MutableMap.MutableEntry<Int, String>>()
        for( entry in map){
            entries.add(entry)
        }
        entries.sortedBy { it.key }
        assertEquals(3, entries.size)
        for( i in entries.indices){
            assertEquals(i+1, entries[i].key)
            assertEquals(map.get( i+1 ), entries[i].value)
        }
    }

    // Test expand
    @Test
    fun testExpand(){
        val initCap = 5
        val map = HashMap<Int, String>(initCap, 1.0F)
        for (i in 1..initCap*5+1){
            val cap = map.capacity
            assertNull( map.put(i, i.toString()) )
            assertEquals( i.toString(), map.get(i ) )
            if (cap+1 == map.size){
                assertEquals(cap*2, map.capacity)
                //println( "Size ${map.size}: $cap - Capacity doubled to ${map.capacity}")
            }
        }
    }

}