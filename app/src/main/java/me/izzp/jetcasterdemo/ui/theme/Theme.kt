package me.izzp.jetcasterdemo.ui.theme

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.izzp.jetcasterdemo.ComposableFunction
import kotlin.math.max
import kotlin.math.min

class ColorState(
    private val context: Context,
    defColor: Color,
    defTextColor: Color,
) {
    var color by mutableStateOf(defColor)
        private set

    var textColor by mutableStateOf(defTextColor)
        private set

    private val cache = LruCache<String, Palette.Swatch>(12)

    suspend fun updateColorFromUrl(url: String) {
        val swatch = cache.get(url)
        if (swatch != null) {
            color = Color(swatch.rgb)
            textColor = Color(swatch.bodyTextColor).copy(1f)
        } else {
            val request = ImageRequest.Builder(context)
                .data(url)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .scale(Scale.FILL)
                .size(128)
                .build()
            Coil.execute(request).drawable?.also { drawable ->
                Palette
                    .from(drawable.toBitmap())
                    .resizeBitmapArea(0)
                    .addFilter { rgb, hsl ->
                        val a = Color(rgb).luminance() + 0.05
                        val b = Color.Black.luminance() + 0.05
                        val c = Color.White.luminance() + 0.05
                        max(a, b) / min(a, b) >= 3 && max(a, c) / min(a, c) >= 3
                    }
                    .generate()
                    .swatches
                    .maxByOrNull { it.population }
                    ?.also { swatch ->
                        println("swatch rgb=${Color(swatch.rgb)}, hsl=${swatch.hsl.joinToString()}")
                        cache.put(url, swatch)
                        color = Color(swatch.rgb)
                        textColor = Color(swatch.bodyTextColor).copy(1f)
                    }
            }
        }
    }
}

@Composable
fun AppTheme(
    primaryColor: Color,
    onPrimaryColor: Color,
    content: ComposableFunction,
) {
    val ripple = rememberRipple()
    CompositionLocalProvider(
        LocalIndication provides ripple,
    ) {
        val primary by animateColorAsState(primaryColor)
        val text by animateColorAsState(onPrimaryColor)
        val colors = lightColors(
            primary = primary,
            primaryVariant = primary,
            onPrimary = text,
        )
        MaterialTheme(
            colors = colors,
            content = content,
            shapes = Shapes(
                small = RoundedCornerShape(4.dp),
                medium = RoundedCornerShape(8.dp),
                large = RoundedCornerShape(16.dp),
            )
        )
    }
}

@Composable
fun updateStatusBarColor() {
    val systemUiController = rememberSystemUiController()
    val color = matTheme.colors.primary
    LaunchedEffect(color) {
        println("updateStatusBarColor color=$color, luminance=${color.luminance()}")
        systemUiController.setStatusBarColor(Color.Transparent, color.luminance() > 0.5f)
    }
}

val matTheme = MaterialTheme