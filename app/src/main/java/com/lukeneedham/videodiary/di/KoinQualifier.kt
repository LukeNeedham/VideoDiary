package com.lukeneedham.videodiary.di

import org.koin.core.qualifier.named

/** Defines the qualifiers used in Koin DI */
object KoinQualifier {
    object Dispatcher {
        val io = name("io")
        val default = name("default")
        val main = name("main")

        private fun name(name: String) = named("Dispatcher.$name")
    }
}
