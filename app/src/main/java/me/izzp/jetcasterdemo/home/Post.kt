package me.izzp.jetcasterdemo.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.navigationBarsHeight
import me.izzp.jetcasterdemo.Feed
import me.izzp.jetcasterdemo.LocalAppState
import me.izzp.jetcasterdemo.Post
import me.izzp.jetcasterdemo.ui.theme.matTheme
import kotlin.math.min


@Composable
fun PostItem(post: Post) {
    val appState = LocalAppState.current
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { appState.navigateToPost(post.postId) }
            .padding(8.dp)
    ) {
        Row(
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = post.title,
                style = matTheme.typography.subtitle1,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.width(4.dp))
            post.img?.also { img ->
                Image(
                    rememberImagePainter(img),
                    null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(48.dp).clip(matTheme.shapes.small)
                )
            }
        }
        Text(
            text = post.desc
                .replace("<p>", "")
                .replace("</p>", "")
                .trimIndent(),
            style = matTheme.typography.body1,
            maxLines = 3,
        )
    }
}

@Composable
fun PostList(
    feed: Feed,
    lazyListState: LazyListState,
) {
    val appState = LocalAppState.current
    val posts = feed.posts.subList(0, min(30, feed.posts.size))
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = lazyListState,
    ) {
        items(
            items = posts,
            key = { it.hashCode() }
        ) { item ->
            PostItem(item)
            Divider()
        }
        item("SPACE") {
            Spacer(Modifier.navigationBarsHeight())
        }
    }
}