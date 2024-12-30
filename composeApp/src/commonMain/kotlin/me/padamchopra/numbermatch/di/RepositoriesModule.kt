package me.padamchopra.numbermatch.di

import me.padamchopra.numbermatch.repositories.AuthRepository
import me.padamchopra.numbermatch.repositories.OnboardingRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun repositoriesModule() = module {
    singleOf(::AuthRepository)
    singleOf(::OnboardingRepository)
}