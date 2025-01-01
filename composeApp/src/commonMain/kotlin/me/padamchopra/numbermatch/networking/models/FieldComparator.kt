package me.padamchopra.numbermatch.networking.models

data class FieldComparator(
    val name: String,
    val value: Any,
    val operator: Operator,
) {
    enum class Operator {
        Equal
    }
}
