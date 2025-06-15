package com.oliver.siloker.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.reactivex.rxjava3.core.Observable

@Composable
fun <T : Any> rememberRxState(observable: Observable<T>, initial: T): State<T> {
    val state = remember { mutableStateOf(initial) }
    DisposableEffect(Unit) {
        val disposable = observable.subscribe { state.value = it }
        onDispose { disposable.dispose() }
    }
    return state
}