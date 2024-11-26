package me.padamchopra.numbermatch.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.padamchopra.numbermatch.ui.dimensions
import me.padamchopra.numbermatch.ui.medium
import me.padamchopra.numbermatch.ui.rounded
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object CustomTopAppBar {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NavIconOnly(
        drawableResource: DrawableResource,
        contentDescription: StringResource,
        enabled: Boolean = true,
        onClick: () -> Unit,
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            ),
            title = {},
            navigationIcon = {
                IconButton(
                    modifier = Modifier
                        .size(MaterialTheme.dimensions.button100)
                        .clip(MaterialTheme.shapes.rounded)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground.medium,
                            shape = MaterialTheme.shapes.rounded,
                        ),
                    onClick = onClick,
                    enabled = enabled,
                ) {
                    Icon(
                        modifier = Modifier
                            .size(MaterialTheme.dimensions.icon75),
                        painter = painterResource(drawableResource),
                        contentDescription = stringResource(contentDescription),
                    )
                }
            },
            windowInsets = TopAppBarDefaults.windowInsets.union(
                WindowInsets(
                    left = MaterialTheme.dimensions.screenPadding,
                    right = MaterialTheme.dimensions.screenPadding,
                )
            ),
        )
    }
}
