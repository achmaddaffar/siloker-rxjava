package com.oliver.siloker.presentation.util

import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * A thread-safe and concise way to update the state of a BehaviorSubject.
 *
 * @param T The type of the state object.
 * @param updater A lambda function that takes the current state and returns the new state.
 */
fun <T : Any> BehaviorSubject<T>.updateState(updater: (currentState: T) -> T) {
    // 1. Get the current value safely. If for some reason it's null, do nothing.
    val currentState = this.value ?: return

    // 2. Apply the user-provided lambda to create the new state.
    val newState = updater(currentState)

    // 3. Push the new state into the stream.
    this.onNext(newState)
}