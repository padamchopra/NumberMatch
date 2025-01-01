package me.padamchopra.numbermatch.di

import me.padamchopra.numbermatch.networking.AuthService
import me.padamchopra.numbermatch.networking.FirestoreService
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        IosImplementations.getAuthService()
    }.bind(AuthService::class)

    single {
        IosImplementations.getFirestoreService()
    }.bind(FirestoreService::class)
}

object IosImplementations {
    lateinit var getAuthService: () -> AuthService
    lateinit var getFirestoreService: () -> FirestoreService
}
