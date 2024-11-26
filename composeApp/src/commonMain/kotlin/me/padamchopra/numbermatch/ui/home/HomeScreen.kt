package me.padamchopra.numbermatch.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import me.padamchopra.numbermatch.UiState
import org.koin.compose.viewmodel.koinViewModel

object HomeScreen {
    data class State(
        val placeholder: String = "",
    ): UiState

    @Composable
    operator fun invoke(
        viewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
    ) {
        Text(text = "Home Screen")
    }
}
