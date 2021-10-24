package com.ruben.epicworld.presentation.commonui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ruben.epicworld.R
import com.ruben.epicworld.presentation.theme.EpicWorldTheme

/**
 * Created by Ruben Quadros on 01/08/21
 **/

@Composable
fun HomeAppBar(
    title: String,
    modifier: Modifier = Modifier,
    searchClick: () -> Unit,
    filterClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = EpicWorldTheme.typography.title1,
                color = EpicWorldTheme.colors.onBackground
            )
                },
        modifier = modifier,
        backgroundColor = EpicWorldTheme.colors.primary,
        actions = {
            IconButton(onClick = searchClick) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.home_app_bar_search_description),
                    tint = EpicWorldTheme.colors.onBackground
                )
            }
            IconButton(onClick = filterClick) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = stringResource(id = R.string.home_app_bar_filter_description),
                    tint = EpicWorldTheme.colors.onBackground
                )
            }
        }
    )
}

@Preview
@Composable
fun HomeAppBarPreview() {
    HomeAppBar(title = "Epic World", searchClick = { }, filterClick = { })
}