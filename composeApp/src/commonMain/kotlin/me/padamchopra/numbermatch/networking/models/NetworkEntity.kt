package me.padamchopra.numbermatch.networking.models

interface NetworkEntity {
    val id: String

    fun toMap(): Map<String, Any>
}
