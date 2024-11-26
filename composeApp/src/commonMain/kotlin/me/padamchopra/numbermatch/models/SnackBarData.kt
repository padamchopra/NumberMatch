package me.padamchopra.numbermatch.models

data class SnackBarData(
    val message: StringData,
    val actionLabel: StringData? = null,
    val type: Type = Type.Default,
    val onAction: (() -> Unit)? = null,
) {
    enum class Type {
        Default,
        Success,
        Error,
    }
}
