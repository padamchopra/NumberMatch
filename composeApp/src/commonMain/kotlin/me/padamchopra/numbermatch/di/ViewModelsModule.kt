package me.padamchopra.numbermatch.di

import me.padamchopra.numbermatch.AppViewModel
import me.padamchopra.numbermatch.ui.home.HomeViewModel
import me.padamchopra.numbermatch.ui.onboarding.OnboardingViewModel
import me.padamchopra.paysy.ui.auth.AuthViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun viewModelsModule() = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::HomeViewModel)
}
