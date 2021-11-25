package me.izzp.jetcasterdemo.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.izzp.jetcasterdemo.*
import me.izzp.jetcasterdemo.ui.theme.matTheme
import me.izzp.jetcasterdemo.ui.theme.updateStatusBarColor

@Composable
fun HomeScene() {
    val appState = LocalAppState.current
    val homeViewModel = remember { HomeViewModel(appState.feedRepository) }
    val homeState = homeViewModel.homeState.collectAsState().value
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(Color.Transparent, false)
    when (homeState) {
        is HomeState.HomeStateLoading -> {
            ContentLoading()
        }
        is HomeState.HomeStateFailed -> {
            ContentFailed {
                homeViewModel.reload()
            }
        }
        is HomeState.HomeStateContent -> {
            ContentFeeds(homeState)
        }
    }
}

@Composable
private fun ContentLoading() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ContentFailed(
    onClick: VoidFunction,
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Load failed\nClick to reload", textAlign = TextAlign.Center)
    }
}

@Composable
private fun ContentFeeds(
    state: HomeState.HomeStateContent,
) {
    updateStatusBarColor()
    Column(
        Modifier.fillMaxSize()
    ) {
        var index by rememberInt()
        FeedHeader(
            feeds = state.feeds,
            onFeedSelected = { index = it },
        )
        val lazyListStates = state.feeds.map {
            key(it.feedId) {
                rememberLazyListState()
            }
        }
        PostList(state.feeds[index], lazyListStates[index])
    }
}

@Composable
private fun FeedHeader(
    feeds: List<Feed>,
    onFeedSelected: (Int) -> Unit,
) {
    val appState = LocalAppState.current
    var currentIndex by rememberInt()
    LaunchedEffect(currentIndex) {
        feeds[currentIndex].img?.also { img ->
            appState.colorState.updateColorFromUrl(img)
        }
    }
    CategoryPager(
        feeds = feeds,
        onItemSelected = {
            currentIndex = it
            onFeedSelected(it)
        },
        modifier = Modifier
            .background(Brush.verticalGradient(listOf(matTheme.colors.primary, Color.White))),
        contentPadding = PaddingValues(
            top = LocalSystemBars.current.statusBarsHeight * 2,
            bottom = 16.dp,
        )
    )
}
