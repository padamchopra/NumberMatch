package me.padamchopra.numbermatch.networking

import me.padamchopra.numbermatch.networking.models.FieldComparator
import me.padamchopra.paysy.networking.RemoteDataPath

interface FirestoreService {
    suspend fun doesDocumentExist(document: RemoteDataPath.Document): Boolean

    suspend fun setDocument(path: RemoteDataPath, data: Map<String, Any>)

    suspend fun comparisonMatchSize(collection: RemoteDataPath.Collection, comparator: FieldComparator): Int
}
