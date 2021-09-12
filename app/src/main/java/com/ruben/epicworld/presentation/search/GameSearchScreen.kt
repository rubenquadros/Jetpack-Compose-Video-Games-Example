package com.ruben.epicworld.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ruben.epicworld.R
import com.ruben.epicworld.presentation.theme.*

/**
 * Created by Ruben Quadros on 12/09/21
 **/
@Composable
fun GameSearchScreen(
    navigateBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(navigateBack)
        SearchResults()
    }

}

@Composable
fun SearchBar(navigateBack: () -> Unit) {

    val searchState = remember {
        mutableStateOf(TextFieldValue())
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = searchState.value,
            onValueChange = { searchState.value = it },
            placeholder = {
                Text(
                    modifier = Modifier.background(Color.Transparent),
                    text = "Comment here...",
                    color = Black,
                    style = Typography.body2
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = PinkA400
            ),
            textStyle = TextStyle(color = Black, fontFamily = PlayFair, fontWeight = FontWeight.Normal, fontSize = 16.sp),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {
                    focusRequester.freeFocus()
                    navigateBack.invoke()
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back"
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    searchState.value = TextFieldValue()
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Back",
                        colorFilter = if (searchState.value.text.isBlank()) ColorFilter.tint(Gray300) else ColorFilter.tint(
                            PinkA100)
                    )
                }
            }
        )
    }
}

@Composable
fun SearchResults() {

}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar(navigateBack = {})
}