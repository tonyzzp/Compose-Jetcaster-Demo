package me.izzp.jetcasterdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.izzp.jetcasterdemo.home.HomeScene
import me.izzp.jetcasterdemo.post.PostScene
import me.izzp.jetcasterdemo.ui.theme.AppTheme
import me.izzp.jetcasterdemo.ui.theme.ColorState
import me.izzp.jetcasterdemo.ui.theme.matTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val primaryColor = matTheme.colors.primary
            val textColor = matTheme.colors.onPrimary
            val navHostController = rememberNavController()
            val colorState = remember {
                ColorState(this, primaryColor, textColor)
            }
            rememberSystemUiController().setSystemBarsColor(Color.Transparent, false)
            val appState = rememberAppState(
                context = this,
                colorState = colorState,
                navHostController = navHostController,
            )
            ProvideSystemBars {
                CompositionLocalProvider(LocalAppState provides appState) {
                    AppTheme(
                        primaryColor = appState.colorState.color,
                        onPrimaryColor = appState.colorState.textColor,
                    ) {
                        Gate(appState.navHostController)
                    }
                }
            }
        }
    }
}

@Composable
private fun Gate(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = "home",
    ) {
        composable("home") { HomeScene() }
        composable("post/{id}") { PostScene(it.arguments!!.getString("id")!!) }
    }
}