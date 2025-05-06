package com.example.gymtracker.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsOne
import com.badoo.reaktive.base.setCancellable
import com.badoo.reaktive.coroutinesinterop.completableFromCoroutine
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.flatMapSingle
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.flatMapCompletable
import com.badoo.reaktive.single.flatMapObservable
import kotlinx.coroutines.CoroutineScope

fun <T : Any, R> Single<Query<T>>.observe(get: suspend (Query<T>) -> R): Observable<R> =
    flatMapObservable { it.observed() }
        .observeOn(ioScheduler)
        .flatMapSingle { singleFromCoroutine { get(it) } }

private fun <T : Any> Query<T>.observed(): Observable<Query<T>> =
    observable { emitter ->
        val listener =
            Query.Listener { emitter.onNext(this@observed) }

        emitter.onNext(this@observed)
        addListener(listener)
        emitter.setCancellable { removeListener(listener) }
    }

fun <T : Any> Single<T>.execute(block: suspend CoroutineScope.(T) -> Unit) =
    this
        .flatMapCompletable {
            completableFromCoroutine {
                block(it)
            }
        }

suspend fun <T : Any> Query<T>.awaitMaxId(getMax: (T) -> Long?) =
    this
        .awaitAsOne()
        .let(getMax)
        ?.plus(1)
        ?: 1
