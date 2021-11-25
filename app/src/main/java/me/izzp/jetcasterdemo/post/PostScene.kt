package me.izzp.jetcasterdemo.post

import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.izzp.jetcasterdemo.LocalAppState
import me.izzp.jetcasterdemo.Post
import me.izzp.jetcasterdemo.ui.theme.matTheme
import me.izzp.jetcasterdemo.ui.theme.updateStatusBarColor
import java.nio.charset.Charset
import kotlin.math.floor


private fun formatTime(seconds: Long): String {
    val hours = floor(seconds / 3600f).toInt()
    val mins = floor((seconds - hours * 3600) / 60f).toInt()
    val s = (seconds - hours * 3600 - mins * 60).toInt()
    println("$seconds, $hours, $mins, $s")
    return String.format("%1$02d:%2$02d:%3$02d", hours, mins, s)
}

@Composable
fun PostScene(postId: String) {
    val id = Base64.decode(postId.toByteArray(), Base64.DEFAULT).toString(Charset.forName("utf-8"))
    val appState = LocalAppState.current
    val post = appState.feedRepository.getPost(id)
    if (post == null || post.audio == null) {
        NotFound()
    } else {
        Body(post)
    }
}

@Composable
private fun NotFound() {
    val appState = LocalAppState.current
    rememberSystemUiController().setStatusBarColor(Color.Transparent, true)
    TextButton(
        onClick = {
            appState.navigateToHome()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Text("post not exists")
    }
}

@Composable
private fun Body(
    post: Post,
) {
    val appState = LocalAppState.current
    val primaryColor = matTheme.colors.primary
    updateStatusBarColor()
    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(primaryColor, primaryColor.copy(0.5f))))
            .systemBarsPadding()
    ) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = {
                        appState.navigateToHome()
                    }
                ) { Icon(Icons.Default.ArrowBack, null) }
            },
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            contentColor = matTheme.colors.onPrimary,
        )
        Spacer(Modifier.height(28.dp))

        Image(
            rememberImagePainter(post.img),
            null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .clip(matTheme.shapes.large)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.heightIn(20.dp))

        Text(
            text = post.title,
            style = matTheme.typography.h6,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            color = matTheme.colors.onPrimary
        )
        Spacer(Modifier.weight(1f))

        Slider(
            value = 0f,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().padding(8.dp, 0.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0", color = matTheme.colors.onPrimary)
            Text(formatTime(post.audio!!.length), color = matTheme.colors.onPrimary)
        }
        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            listOf(
                Icons.Default.SkipPrevious,
                Icons.Default.Replay10,
                Icons.Default.PlayCircle,
                Icons.Default.Forward10,
                Icons.Default.SkipNext
            ).forEach { icon ->
                val isPlay = icon === Icons.Default.PlayCircle
                println(isPlay)
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(if (isPlay) 68.dp else 56.dp)
                ) {
                    Icon(
                        icon,
                        null,
                        modifier = Modifier.size(if (isPlay) 56.dp else 48.dp),
                        tint = matTheme.colors.onPrimary,
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}