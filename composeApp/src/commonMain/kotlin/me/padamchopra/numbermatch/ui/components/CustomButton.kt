package me.padamchopra.numbermatch.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

object CustomButton {
    @Composable
    fun InverseSurface(
        modifier: Modifier = Modifier,
        shape: Shape = MaterialTheme.shapes.small,
        enabled: Boolean,
        label: String,
        onClick: () -> Unit,
    ) {
        Button(
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            onClick = onClick,
            shape = shape,
            enabled = enabled,
        ) {
            Text(
                text = label,
            )
        }
    }
}
