package me.padamchopra.numbermatch.di

import me.padamchopra.numbermatch.utils.RemoteConfigProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun utilsModule() = module {
    singleOf(::RemoteConfigProvider)
}
