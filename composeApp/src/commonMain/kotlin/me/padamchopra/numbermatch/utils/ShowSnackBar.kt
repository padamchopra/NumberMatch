package me.padamchopra.numbermatch.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.padamchopra.numbermatch.models.SnackBarData
import me.padamchopra.numbermatch.models.StringData

object ShowSnackBar {
    private val _dataFlow = MutableSharedFlow<SnackBarData>()
    val dataFlow = _dataFlow.asSharedFlow()

    suspend fun success(message: StringData) {
        _dataFlow.emit(SnackBarData(type = SnackBarData.Type.Success, message = message))
    }

    suspend fun error(message: StringData) {
        _dataFlow.emit(SnackBarData(type = SnackBarData.Type.Error, message = message))
    }
}
