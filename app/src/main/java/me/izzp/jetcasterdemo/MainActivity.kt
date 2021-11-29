package me.izzp.jetcasterdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.izzp.jetcasterdemo.home.HomeScene
import me.izzp.jetcasterdemo.post.PostScene
import me.izzp.jetcasterdemo.ui.theme.AppTheme
import me.izzp.jetcasterdemo.ui.theme.ColorState
import me.izzp.jetcasterdemo.ui.theme.matTheme
import com.google.accompanist.navigation.animation.composable as animatedComposable


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val primaryColor = matTheme.colors.primary
            val textColor = matTheme.colors.onPrimary
            val navHostController = rememberAnimatedNavController()  // rememberNavController()
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
                        Gate2(appState.navHostController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Test() {
    rememberSystemUiController().setStatusBarColor(Color.Transparent, false)
    val navController = rememberAnimatedNavController()
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta)
    AnimatedNavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = {
            slideInHorizontally(tween(), { it })
        },
        exitTransition = {
            slideOutHorizontally(tween(), { -it })
        },
        popEnterTransition = {
            slideInHorizontally(tween(), { -it })
        },
        popExitTransition = {
            slideOutHorizontally(tween(), { it })
        }
    ) {
        animatedComposable("home") {
            Column(
                Modifier.fillMaxSize().systemBarsPadding()
            ) {
                colors.forEach { color ->
                    Button(
                        onClick = {
                            navController.navigate(color.toString())
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = color.toString())
                    }
                }
            }
        }
        colors.forEach { color ->
            animatedComposable(color.toString()) {
                Box(
                    modifier = Modifier.fillMaxSize().background(color).systemBarsPadding(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(color.toString())
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Gate2(navHostController: NavHostController) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = "home",
        enterTransition = {
            slideInHorizontally(spring(), { it })
        },
        exitTransition = {
            slideOutHorizontally(spring(), { -it })
        },
        popEnterTransition = {
            slideInHorizontally(spring(), { -it })
        },
        popExitTransition = {
            slideOutHorizontally(spring(), { it })
        }
    ) {
        animatedComposable(
            route = "home",
        ) { HomeScene() }
        animatedComposable(
            route = "post/{id}",
        ) { PostScene(it.arguments!!.getString("id")!!) }
    }
}