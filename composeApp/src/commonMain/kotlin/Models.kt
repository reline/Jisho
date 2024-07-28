data class Result(
    val isCommon: Boolean,
    val japanese: String,
    val okurigana: String?,
    val rubies: Set<Ruby> = emptySet(),
    val senses: List<Definition>,
    val tags: List<String> = emptyList(),
    val jlpt: List<String> = emptyList(),
    val attribution: Attribution = Attribution(),
)

data class Ruby(
    val position: Long,
    val japanese: String,
    val okurigana: String?,
): Comparable<Ruby> {
    override fun compareTo(other: Ruby) = position.compareTo(other.position)
}

data class Definition(
    val values: List<String>,
    val partsOfSpeech: List<String>
)

class Attribution(
    val isJmdict: Boolean = false,
    val isJmnedict: Boolean = false,
    val dbpedia: String = false.toString()
) {
    val isDbpedia: Boolean
        get() = dbpedia != false.toString()
}
