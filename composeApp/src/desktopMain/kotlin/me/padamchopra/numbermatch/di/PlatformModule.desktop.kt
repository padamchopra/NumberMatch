package me.padamchopra.numbermatch.di

import me.padamchopra.numbermatch.networking.AuthService
import me.padamchopra.numbermatch.networking.FirestoreService
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        DesktopImplementations.getAuthService()
    }.bind(AuthService::class)

    single {
        DesktopImplementations.getFirestoreService()
    }.bind(FirestoreService::class)
}

object DesktopImplementations {
    lateinit var getAuthService: () -> AuthService
    lateinit var getFirestoreService: () -> FirestoreService
}
