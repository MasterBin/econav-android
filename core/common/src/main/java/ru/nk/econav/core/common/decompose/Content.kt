package ru.nk.econav.core.common.decompose

import androidx.compose.runtime.Composable

typealias Content = @Composable () -> Unit

fun <T : Any> T.asContent(content : @Composable (T) -> Unit) : Content = { content(this) }