package me.izzp.jetcasterdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets

@Immutable
class SystemBars(
    val statusBarsHeight: Dp,
    val navigationBarsHeight: Dp,
)

val LocalSystemBars = staticCompositionLocalOf {
    SystemBars(0.dp, 0.dp)
}

@Composable
fun ProvideSystemBars(content: ComposableFunction) {
    ProvideWindowInsets(false, true) {
        val insets = LocalWindowInsets.current
        val systemBars = with(LocalDensity.current) {
            SystemBars(
                insets.statusBars.top.toDp(),
                insets.navigationBars.bottom.toDp(),
            )
        }
        CompositionLocalProvider(LocalSystemBars provides systemBars, content = content)
    }
}