package me.padamchopra.paysy.networking

sealed class RemoteDataPath {
    sealed class Collection(
        val name: String,
    ): RemoteDataPath() {
        data object Users: Collection(name = "users")
    }

    sealed class Document(
        val collection: Collection,
        open val id: String,
    ): RemoteDataPath() {
        data class User(
            override val id: String,
        ): Document(collection = Collection.Users, id = id)
    }
}
