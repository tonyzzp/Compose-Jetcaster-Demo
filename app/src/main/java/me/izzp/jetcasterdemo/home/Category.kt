package me.izzp.jetcasterdemo.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import me.izzp.jetcasterdemo.Feed
import me.izzp.jetcasterdemo.ui.theme.matTheme
import kotlin.math.absoluteValue

@Composable
fun Category(
    feed: Feed,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            rememberImagePainter(feed.img),
            null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(matTheme.shapes.medium)
                .background(Color.LightGray)
        )
        Text(
            text = feed.title,
            style = matTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(2.dp, 4.dp)
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryPager(
    feeds: List<Feed>,
    onItemSelected: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val pagerState = rememberPagerState(0)
    LaunchedEffect(pagerState.currentPage) {
        onItemSelected(pagerState.currentPage)
    }
    BoxWithConstraints {
        val layoutDirection = LocalLayoutDirection.current
        HorizontalPager(
            count = feeds.size,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(unbounded = true)
                .padding(vertical = 12.dp),
            state = pagerState,
            itemSpacing = 6.dp,
            verticalAlignment = Alignment.Top,
            key = { it },
            contentPadding = PaddingValues(
                start = maxWidth / 4 + contentPadding.calculateStartPadding(layoutDirection),
                end = maxWidth / 4 + contentPadding.calculateEndPadding(layoutDirection),
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
            )
        ) { index ->
            val offset = calculateCurrentOffsetForPage(index).absoluteValue
            val fraction = lerp(0.7f, 1f, 1 - offset)
            Category(
                feed = feeds[index],
                modifier = Modifier.graphicsLayer {
                    scaleX = fraction
                    scaleY = fraction
                    alpha = fraction
                }
            )
        }
    }
}
