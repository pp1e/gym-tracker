package com.example.gymtracker.database.utils

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.zip
import kotlinx.coroutines.CoroutineScope

fun <QUERY1: Any, QUERY2: Any> zipAndExecute(
    query1: Single<QUERY1>,
    query2: Single<QUERY2>,
    block: suspend CoroutineScope.(QUERY1, QUERY2) -> Unit,
) = zip(query1, query2) { query1, query2 ->
    Pair(query1, query2)
}.execute { (query1, query2) ->
    block(query1, query2)
}

fun <QUERY1: Any, QUERY2: Any, QUERY3: Any> zipAndExecute(
    query1: Single<QUERY1>,
    query2: Single<QUERY2>,
    query3: Single<QUERY3>,
    block: suspend CoroutineScope.(QUERY1, QUERY2, QUERY3) -> Unit,
) = zip(query1, query2, query3) { query1, query2, query3 ->
    Triple(query1, query2, query3)
}.execute { (query1, query2, query3) ->
    block(query1, query2, query3)
}

fun <QUERY1: Any, QUERY2: Any, QUERY3: Any, QUERY4: Any> zipAndExecute(
    query1: Single<QUERY1>,
    query2: Single<QUERY2>,
    query3: Single<QUERY3>,
    query4: Single<QUERY4>,
    block: suspend CoroutineScope.(QUERY1, QUERY2, QUERY3, QUERY4) -> Unit,
) = zip(query1, query2, query3, query4) { query1, query2, query3, query4 ->
    QueryTuple4(query1, query2, query3, query4)
}.execute { (query1, query2, query3, query4) ->
    block(query1, query2, query3, query4)
}

fun <QUERY1: Any, QUERY2: Any, QUERY3: Any, QUERY4: Any, QUERY5: Any> zipAndExecute(
    query1: Single<QUERY1>,
    query2: Single<QUERY2>,
    query3: Single<QUERY3>,
    query4: Single<QUERY4>,
    query5: Single<QUERY5>,
    block: suspend CoroutineScope.(QUERY1, QUERY2, QUERY3, QUERY4, QUERY5) -> Unit,
) = zip(query1, query2, query3, query4, query5) { query1, query2, query3, query4, query5 ->
    QueryTuple5(query1, query2, query3, query4, query5)
}.execute { (query1, query2, query3, query4, query5) ->
    block(query1, query2, query3, query4, query5)
}
