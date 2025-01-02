package me.padamchopra.numbermatch.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import me.padamchopra.numbermatch.Async
import me.padamchopra.numbermatch.UiState
import me.padamchopra.numbermatch.ui.components.BackHandler
import me.padamchopra.numbermatch.ui.components.CustomButton
import me.padamchopra.numbermatch.ui.components.CustomTextField
import me.padamchopra.numbermatch.ui.components.CustomTopAppBar
import me.padamchopra.numbermatch.ui.dimensions
import me.padamchopra.numbermatch.ui.high
import me.padamchopra.numbermatch.ui.medium
import me.padamchopra.numbermatch.ui.rounded
import numbermatch.composeapp.generated.resources.Res
import numbermatch.composeapp.generated.resources.back
import numbermatch.composeapp.generated.resources.continue_label
import numbermatch.composeapp.generated.resources.duplicate_username_error
import numbermatch.composeapp.generated.resources.ic_arrow_left
import numbermatch.composeapp.generated.resources.name
import numbermatch.composeapp.generated.resources.onboarding_screen_subtitle
import numbermatch.composeapp.generated.resources.onboarding_screen_title
import numbermatch.composeapp.generated.resources.username
import numbermatch.composeapp.generated.resources.username_length_hint
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

object OnboardingScreen {
    data class State(
        val placeholder: String = "",
        val backJob: Async<Unit> = Async.Uninitialized,
        val name: String = "",
        val username: String = "",
        val continueJob: Async<Unit> = Async.Uninitialized,
        val usernameMinLength: Long,
        val uniqueUsernameCheckJob: Async<UsernameCheckResult> = Async.Uninitialized,
        val usernameUnique: Boolean = false,
        val showUsernameDuplicateError: Boolean = false,
    ): UiState {
        val isContinueEnabled: Boolean by lazy {
            name.trim().length >= 3 && username.length >= usernameMinLength && usernameUnique
        }

        data class UsernameCheckResult(
            val isUnique: Boolean,
            val username: String,
        )
    }

    @Composable
    operator fun invoke(
        viewModel: OnboardingViewModel = koinViewModel<OnboardingViewModel>(),
    ) {
        val state by viewModel.collectAsState()

        Content(
            state = state,
            onBackClick = viewModel::onBackClick,
            onNameValueChange = viewModel::onNameValueChange,
            onUsernameValueChange = viewModel::onUsernameValueChange,
            onContinueClick = viewModel::onContinueClick,
        )
    }

    @Composable
    fun Content(
        state: State,
        onBackClick: () -> Unit,
        onNameValueChange: (String) -> String,
        onUsernameValueChange: (String) -> String,
        onContinueClick: () -> Unit,
    ) {
        BackHandler(
            enabled = true,
            onBack = onBackClick,
        )

        val usernameFieldFocusRequester = remember { FocusRequester() }

        Scaffold(
            topBar = {
                CustomTopAppBar.NavIconOnly(
                    drawableResource = Res.drawable.ic_arrow_left,
                    contentDescription = Res.string.back,
                    onClick = onBackClick,
                    enabled = !state.backJob.isLoadingOrSuccess,
                )
            }
        ) { safePadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(safePadding)
                    .padding(horizontal = MaterialTheme.dimensions.screenPadding)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.padding200),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.padding50),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(Res.string.onboarding_screen_title),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(Res.string.onboarding_screen_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalContentColor.current.high,
                        textAlign = TextAlign.Center,
                    )
                }
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(Res.string.name),
                    value = state.name,
                    onValueChange = onNameValueChange,
                    onDone = {
                        usernameFieldFocusRequester.requestFocus()
                    },
                    imeAction = ImeAction.Next,
                    shape = MaterialTheme.shapes.rounded,
                    enabled = !state.backJob.isLoadingOrSuccess && !state.continueJob.isLoadingOrSuccess,
                    centre = true,
                )
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(usernameFieldFocusRequester),
                    placeholder = stringResource(Res.string.username),
                    value = state.username,
                    onValueChange = onUsernameValueChange,
                    onDone = onContinueClick,
                    imeAction = ImeAction.Done,
                    shape = MaterialTheme.shapes.rounded,
                    enabled = !state.backJob.isLoadingOrSuccess && !state.continueJob.isLoadingOrSuccess,
                    centre = true,
                    isError = state.showUsernameDuplicateError,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (state.showUsernameDuplicateError) {
                        stringResource(Res.string.duplicate_username_error)
                    } else {
                        stringResource(Res.string.username_length_hint, state.usernameMinLength)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = if (state.showUsernameDuplicateError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        LocalContentColor.current.medium
                    },
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(1f))
                CustomButton.InverseSurface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.rounded,
                    enabled = state.isContinueEnabled && !state.backJob.isLoadingOrSuccess && !state.continueJob.isLoadingOrSuccess,
                    label = stringResource(Res.string.continue_label),
                    onClick = onContinueClick,
                )
            }
        }
    }
}
