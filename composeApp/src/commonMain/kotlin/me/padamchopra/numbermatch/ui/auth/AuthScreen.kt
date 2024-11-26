package me.padamchopra.numbermatch.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import me.padamchopra.numbermatch.Async
import me.padamchopra.numbermatch.UiState
import me.padamchopra.numbermatch.extensions.debounceClickable
import me.padamchopra.numbermatch.ui.appColors
import me.padamchopra.numbermatch.ui.components.CustomButton
import me.padamchopra.numbermatch.ui.components.CustomTextField
import me.padamchopra.numbermatch.ui.dimensions
import me.padamchopra.paysy.ui.auth.AuthViewModel
import numbermatch.composeapp.generated.resources.Res
import numbermatch.composeapp.generated.resources.already_have_an_account
import numbermatch.composeapp.generated.resources.continue_label
import numbermatch.composeapp.generated.resources.dont_have_an_account
import numbermatch.composeapp.generated.resources.email_address
import numbermatch.composeapp.generated.resources.forgot_password
import numbermatch.composeapp.generated.resources.password
import numbermatch.composeapp.generated.resources.sign_in
import numbermatch.composeapp.generated.resources.sign_in_title
import numbermatch.composeapp.generated.resources.sign_up
import numbermatch.composeapp.generated.resources.sign_up_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

object AuthScreen {
    data class State(
        val authType: AuthType = AuthType.SignIn,
        val email: String = "",
        val password: String = "",
        val continueJob: Async<AuthType> = Async.Uninitialized,
        val hasFinishedOnboardingJob: Async<Boolean> = Async.Uninitialized,
        val forgotPasswordJob: Async<Unit> = Async.Uninitialized,
    ): UiState {
        enum class AuthType {
            SignIn,
            SignUp,
        }

        val continueButtonEnabled = email.isNotBlank() && password.isNotBlank() && continueJob !is Async.Loading && continueJob !is Async.Success
        val fieldEditingEnabled = continueJob !is Async.Loading && continueJob !is Async.Success
    }

    @Composable
    operator fun invoke(
        viewModel: AuthViewModel = koinViewModel<AuthViewModel>(),
    ) {
        val state by viewModel.collectAsState()

        Content(
            state = state,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onSwitchAuthTypeClick = viewModel::onSwitchAuthTypeClick,
            onForgotPasswordClick = viewModel::onForgotPasswordClick,
            onSubmitButtonClick = viewModel::onContinueButtonClick,
        )
    }

    @Composable
    fun Content(
        state: State = State(),
        onEmailChange: (String) -> String = { it },
        onPasswordChange: (String) -> String = { it },
        onSubmitButtonClick: () -> Unit = { },
        onForgotPasswordClick: () -> Unit = { },
        onSwitchAuthTypeClick: () -> Unit = { },
    ) {
        val passwordFocusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.tileBackground)
                .clickable {
                    keyboardController?.hide()
                }
                .imePadding(),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimensions.screenPadding)
                    .align(Alignment.Center),
                colors = CardDefaults.elevatedCardColors(),
            ) {
                val spacedBy = MaterialTheme.dimensions.padding300
                Column(
                    modifier = Modifier.padding(MaterialTheme.dimensions.padding300),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.padding100),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(shape = MaterialTheme.shapes.small)
                                .background(MaterialTheme.appColors.tileBackground)
                                .shadow(1.dp, shape = MaterialTheme.shapes.small),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "1",
                                color = MaterialTheme.appColors.blue,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = when (state.authType) {
                                State.AuthType.SignIn -> stringResource(Res.string.sign_in_title)
                                State.AuthType.SignUp -> stringResource(Res.string.sign_up_title)
                            },
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.dimensions.padding200.plus(spacedBy)),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.small)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = MaterialTheme.shapes.small,
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = stringResource(Res.string.email_address),
                                value = state.email,
                                onValueChange = onEmailChange,
                                onDone = {
                                    passwordFocusRequester.requestFocus()
                                },
                                imeAction = ImeAction.Next,
                                shape = RoundedCornerShape(0.dp),
                                autoCorrectEnabled = false,
                                enabled = state.fieldEditingEnabled,
                            )
                            HorizontalDivider()
                            CustomTextField.PasswordField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(passwordFocusRequester),
                                placeholder = stringResource(Res.string.password),
                                value = state.password,
                                onValueChange = onPasswordChange,
                                onDone = onSubmitButtonClick,
                                imeAction = ImeAction.Done,
                                shape = RoundedCornerShape(0.dp),
                                enabled = state.fieldEditingEnabled,
                            )
                        }
                    }
                    AnimatedVisibility(state.authType == State.AuthType.SignIn) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = spacedBy)
                                .debounceClickable(onClick = onForgotPasswordClick),
                            text = stringResource(Res.string.forgot_password),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                        )
                    }
                    CustomButton.InverseSurface(
                        modifier = Modifier
                            .padding(top = spacedBy)
                            .fillMaxWidth(),
                        enabled = state.continueButtonEnabled,
                        label = stringResource(Res.string.continue_label),
                        onClick = onSubmitButtonClick,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(MaterialTheme.dimensions.screenPadding)
                    .navigationBarsPadding()
                    .debounceClickable(onClick = onSwitchAuthTypeClick),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = when (state.authType) {
                        State.AuthType.SignIn -> stringResource(Res.string.dont_have_an_account)
                        State.AuthType.SignUp -> stringResource(Res.string.already_have_an_account)
                    },
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = when (state.authType) {
                        State.AuthType.SignIn -> stringResource(Res.string.sign_up)
                        State.AuthType.SignUp -> stringResource(Res.string.sign_in)
                    },
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
