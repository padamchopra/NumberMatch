package me.padamchopra.numbermatch.models

import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

@Stable
sealed class StringData {
    data class Resource(val id: StringResource) : StringData()

    data class Literal(val value: String) : StringData()

    suspend fun resolve(): String {
        return when (this) {
            is Resource -> getString(id)
            is Literal -> value
        }
    }
}
