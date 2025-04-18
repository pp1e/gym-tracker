package com.example.gymtracker.utils

inline fun <IN1 : Any, IN2 : Any, OUT : Any> safeLet(
    arg1: IN1?,
    arg2: IN2?,
    block: (IN1, IN2) -> OUT?,
): OUT? =
    if (arg1 != null && arg2 != null) {
        block(arg1, arg2)
    } else {
        null
    }

inline fun <IN1 : Any, IN2 : Any, IN3 : Any, OUT : Any> safeLet(
    arg1: IN1?,
    arg2: IN2?,
    arg3: IN3?,
    block: (IN1, IN2, IN3) -> OUT?,
): OUT? =
    if (arg1 != null && arg2 != null && arg3 != null) {
        block(arg1, arg2, arg3)
    } else {
        null
    }

inline fun <IN1 : Any, IN2 : Any, IN3 : Any, IN4 : Any, OUT : Any> safeLet(
    arg1: IN1?,
    arg2: IN2?,
    arg3: IN3?,
    arg4: IN4?,
    block: (IN1, IN2, IN3, IN4) -> OUT?,
): OUT? =
    if (arg1 != null && arg2 != null && arg3 != null && arg4 != null) {
        block(arg1, arg2, arg3, arg4)
    } else {
        null
    }

inline fun <IN1 : Any, IN2 : Any, IN3 : Any, IN4 : Any, IN5 : Any, OUT : Any> safeLet(
    arg1: IN1?,
    arg2: IN2?,
    arg3: IN3?,
    arg4: IN4?,
    arg5: IN5?,
    block: (IN1, IN2, IN3, IN4, IN5) -> OUT?,
): OUT? =
    if (arg1 != null && arg2 != null && arg3 != null && arg4 != null && arg5 != null) {
        block(arg1, arg2, arg3, arg4, arg5)
    } else {
        null
    }
