package com.example.gash.domain.model

data class AnimalTags(
    val tag1: String? = null,
    val tag2: String? = null,
    val tag3: String? = null
) {
    fun valueFor(slot: Int): String? = when (slot) {
        1 -> tag1
        2 -> tag2
        3 -> tag3
        else -> null
    }

    fun withValue(slot: Int, value: String?): AnimalTags = when (slot) {
        1 -> copy(tag1 = value)
        2 -> copy(tag2 = value)
        3 -> copy(tag3 = value)
        else -> this
    }

    fun normalized(): AnimalTags = AnimalTags(
        tag1 = tag1.toTagValue(),
        tag2 = tag2.toTagValue(),
        tag3 = tag3.toTagValue()
    )

    companion object {
        val EMPTY = AnimalTags()
        const val MAX_VALUE_LENGTH = 32
    }
}

private fun String?.toTagValue(): String? =
    this?.trim()?.take(AnimalTags.MAX_VALUE_LENGTH)?.takeIf { it.isNotEmpty() }