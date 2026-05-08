package serie1.problema

//guarda o valor inteiro e também o seu ficheiro proveniente desse valor
data class Entry(val value: Int = 0, val file: Int = 0) : Comparable<Entry> {
    override fun compareTo(other: Entry): Int {
        return this.value.compareTo(other.value)
    }
}