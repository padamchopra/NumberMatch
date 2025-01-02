package me.padamchopra.numbermatch.di

import me.padamchopra.numbermatch.remoteconfig.RemoteConfigProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun appModule() = module {
    singleOf(::RemoteConfigProvider)
}
