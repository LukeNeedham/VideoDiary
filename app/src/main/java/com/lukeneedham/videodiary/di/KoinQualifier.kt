package com.lukeneedham.videodiary.di

import org.koin.core.qualifier.named

/** Defines the qualifiers used in Koin DI */
object KoinQualifier {
    object Dispatcher {
        val main = name("main")
        val io = name("io")
        val default = name("default")

        private fun name(name: String) = named("Dispatcher.$name")
    }
}
