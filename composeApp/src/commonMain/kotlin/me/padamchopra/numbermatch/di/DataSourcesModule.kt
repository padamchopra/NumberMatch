package me.padamchopra.numbermatch.di

import me.padamchopra.numbermatch.networking.AuthDataSource
import me.padamchopra.numbermatch.networking.OnboardingDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun dataSourcesModule() = module {
    singleOf(::AuthDataSource)
    singleOf(::OnboardingDataSource)
}
