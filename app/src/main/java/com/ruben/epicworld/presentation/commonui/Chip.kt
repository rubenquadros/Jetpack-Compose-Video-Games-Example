package com.ruben.epicworld.presentation.commonui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.ruben.epicworld.presentation.theme.EpicWorldTheme

/**
 * Created by Ruben Quadros on 15/01/22
 **/
@Composable
fun Chip(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    chipId: Any,
    content: @Composable () -> Unit,
    onValueChange: (chipId: Any) -> Unit
) {
    var isSelected by remember {
        mutableStateOf(selected)
    }

    Box(
        modifier = modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    isSelected = it
                    onValueChange.invoke(chipId)
                }
            )
            .background(
                color = if (isSelected) EpicWorldTheme.colors.primaryVariant else Color.Transparent,
                shape = RoundedCornerShape(size = 20.dp)
            )
            .border(
                color = if (isSelected) Color.Transparent else EpicWorldTheme.colors.primaryVariant,
                width = 1.dp,
                shape = RoundedCornerShape(size = 20.dp),
            )
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChip(@PreviewParameter(ProvideSelected::class) isSelected: Boolean) {
    Chip(
        chipId = 1,
        content = {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Action"
            )
        },
        selected = isSelected,
        onValueChange = {}
    )
}

class ProvideSelected: PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> =
        sequenceOf(
            false,
            true
        )
}