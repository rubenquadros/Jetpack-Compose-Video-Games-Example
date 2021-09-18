package com.ruben.epicworld.presentation.commonui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruben.epicworld.R
import com.ruben.epicworld.presentation.theme.Black
import com.ruben.epicworld.presentation.theme.PinkA100
import com.ruben.epicworld.presentation.theme.Typography

/**
 * Created by Ruben Quadros on 18/09/21
 **/
@Composable
fun GetGamesError(buttonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.home_screen_error_message),
            textAlign = TextAlign.Center,
            color = Black,
            style = Typography.h5
        )
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = buttonClick
        ) {
            Text(
                text = stringResource(id = R.string.home_screen_retry),
                style = Typography.button
            )
        }
    }
}

@Composable
fun NoResultsView() {
    Text(
        modifier = Modifier.padding(16.dp),
        text = stringResource(id = R.string.game_search_no_matches),
        style = Typography.h5,
        color = PinkA100
    )
}


@Preview(showBackground = true)
@Composable
fun ErrorItemPreview() {
    GetGamesError {
        //do nothing
    }
}