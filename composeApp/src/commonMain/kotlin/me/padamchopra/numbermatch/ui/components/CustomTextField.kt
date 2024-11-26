package me.padamchopra.numbermatch.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import numbermatch.composeapp.generated.resources.Res
import numbermatch.composeapp.generated.resources.ic_visibility
import numbermatch.composeapp.generated.resources.ic_visibility_off
import org.jetbrains.compose.resources.painterResource

object CustomTextField {
    @Composable
    fun defaultColors() = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    )

    @Composable
    operator fun invoke(
        modifier: Modifier = Modifier,
        placeholder: String,
        value: String,
        onValueChange: (String) -> String,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        shape: CornerBasedShape = MaterialTheme.shapes.small,
        imeAction: ImeAction = ImeAction.Done,
        colors: TextFieldColors = defaultColors(),
        onDone: () -> Unit,
        enabled: Boolean = true,
        autoCorrectEnabled: Boolean? = null,
        centre: Boolean = false,
        isError: Boolean = false,
    ) {
        var textState by remember { mutableStateOf(TextFieldValue(text = value)) }
        val controller = LocalSoftwareKeyboardController.current

        LaunchedEffect(value) {
            if (textState.text != value) {
                textState = textState.copy(text = value)
            }
        }

        val textStyle = LocalTextStyle.current

        TextField(
            modifier = modifier,
            value = textState,
            onValueChange = { newValue ->
                textState = newValue.copy(
                    text = onValueChange(newValue.text),
                )
            },
            isError = isError,
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = placeholder,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (centre) TextAlign.Center else null,
                )
            },
            textStyle = textStyle.copy(
                textAlign = if (centre) TextAlign.Center else textStyle.textAlign,
            ),
            maxLines = 1,
            colors = colors,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = imeAction,
                autoCorrectEnabled = autoCorrectEnabled,
            ),
            enabled = enabled,
            visualTransformation = visualTransformation,
            shape = shape,
            keyboardActions = KeyboardActions(
                onDone = {
                    controller?.hide()
                    onDone()
                },
                onSearch = {
                    controller?.hide()
                    onDone()
                },
                onNext = {
                    controller?.hide()
                    onDone()
                },
            ),
        )
    }

    @Composable
    fun PasswordField(
        modifier: Modifier = Modifier,
        placeholder: String,
        value: String,
        onValueChange: (String) -> String,
        shape: CornerBasedShape = MaterialTheme.shapes.small,
        imeAction: ImeAction = ImeAction.Done,
        colors: TextFieldColors = defaultColors(),
        onDone: () -> Unit,
        enabled: Boolean = true,
    ) {
        var textState by remember { mutableStateOf(TextFieldValue(text = value)) }
        var passwordHidden by rememberSaveable { mutableStateOf(true) }
        val controller = LocalSoftwareKeyboardController.current

        LaunchedEffect(value) {
            if (textState.text != value) {
                textState = textState.copy(text = value)
            }
        }

        TextField(
            modifier = modifier,
            value = textState,
            onValueChange = { newValue ->
                textState = newValue.copy(
                    text = onValueChange(newValue.text),
                )
            },
            placeholder = {
                Text(
                    text = placeholder,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            maxLines = 1,
            colors = colors,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction,
                autoCorrectEnabled = false,
            ),
            enabled = enabled,
            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            shape = shape,
            keyboardActions = KeyboardActions(
                onDone = {
                    controller?.hide()
                    onDone()
                },
                onSearch = {
                    controller?.hide()
                    onDone()
                },
                onNext = {
                    controller?.hide()
                    onDone()
                },
            ),
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val painterResource = if (passwordHidden) Res.drawable.ic_visibility else Res.drawable.ic_visibility_off
                    // Please provide localized description for accessibility services
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(
                        painter = painterResource(resource = painterResource),
                        contentDescription = description,
                    )
                }
            }
        )
    }
}
