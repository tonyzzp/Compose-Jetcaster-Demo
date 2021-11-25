package me.izzp.jetcasterdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

typealias ComposableFunction = @Composable () -> Unit

typealias VoidFunction = () -> Unit

@Composable
fun rememberBoolean(value: Boolean = false) = remember { mutableStateOf(value) }

@Composable
fun rememberString(value: String = "") = remember { mutableStateOf(value) }

@Composable
fun rememberInt(value: Int = 0) = remember { mutableStateOf(0) }