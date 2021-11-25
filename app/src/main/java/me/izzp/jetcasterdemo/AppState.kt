package me.izzp.jetcasterdemo

import android.content.Context
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import me.izzp.jetcasterdemo.home.HomeViewModel
import me.izzp.jetcasterdemo.ui.theme.ColorState
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Immutable
class AppState(
    context: Context,
    val colorState: ColorState,
    val navHostController: NavHostController,
) {
    val imageLoader = ImageLoader
        .Builder(context)
        .allowRgb565(true)
        .okHttpClient(
            OkHttpClient
                .Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .cache(CoilUtils.createDefaultCache(context))
                .build()
        )
        .build()

    val feedRepository = FeedRepository(context)

    val homeViewModel = HomeViewModel(feedRepository)

    init {
        Coil.setImageLoader(imageLoader)
    }

    fun navigateToPost(postId: String) {
        val id = Base64.encodeToString(postId.toByteArray(), Base64.DEFAULT)
        navHostController.navigate("post/$id")
    }

    fun navigateToHome() {
        navHostController.navigate("home") {
            popUpTo("home")
            launchSingleTop = true
        }
    }
}

@Composable
fun rememberAppState(
    context: Context,
    color: Color,
    textColor: Color,
    navHostController: NavHostController,
) = remember {
    AppState(
        context = context,
        colorState = ColorState(context, color, textColor),
        navHostController = navHostController,
    )
}

val LocalAppState = staticCompositionLocalOf<AppState> { error("no value ") }
