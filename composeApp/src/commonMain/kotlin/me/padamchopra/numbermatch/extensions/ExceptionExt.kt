package me.padamchopra.numbermatch.extensions

import me.padamchopra.numbermatch.models.AppExceptions
import me.padamchopra.numbermatch.models.StringData
import numbermatch.composeapp.generated.resources.Res
import numbermatch.composeapp.generated.resources.generic_exception

fun Exception.getErrorMessage(): StringData {
    return when (this) {
        is AppExceptions -> StringData.Resource(messageResource)
        else -> StringData.Resource(Res.string.generic_exception)
    }
}
