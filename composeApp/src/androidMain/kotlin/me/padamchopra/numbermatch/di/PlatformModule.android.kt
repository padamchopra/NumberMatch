package me.padamchopra.numbermatch.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.padamchopra.numbermatch.networking.AuthService
import me.padamchopra.numbermatch.networking.AuthServiceImpl
import me.padamchopra.numbermatch.networking.FirestoreService
import me.padamchopra.numbermatch.networking.FirestoreServiceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single { Firebase.auth }
        singleOf(::AuthServiceImpl).bind(AuthService::class)

        single { Firebase.firestore }
        singleOf(::FirestoreServiceImpl).bind(FirestoreService::class)
    }
}