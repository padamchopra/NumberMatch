package me.padamchopra.numbermatch.networking

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import me.padamchopra.numbermatch.networking.models.FieldComparator
import me.padamchopra.paysy.networking.RemoteDataPath

class FirestoreServiceImpl(
    private val firebaseFirestore: FirebaseFirestore,
): FirestoreService {
    override suspend fun doesDocumentExist(document: RemoteDataPath.Document): Boolean {
        val docRef = firebaseFirestore
            .collection(document.collection.name)
            .document(document.id)
            .get()
            .await()
        return docRef.exists()
    }

    override suspend fun setDocument(path: RemoteDataPath, data: Map<String, Any>) {
        when (path) {
            is RemoteDataPath.Collection -> {
                firebaseFirestore
                    .collection(path.name)
                    .add(data)
                    .await()
            }
            is RemoteDataPath.Document.User -> {
                firebaseFirestore
                    .collection(path.collection.name)
                    .document(path.id)
                    .set(data, SetOptions.merge())
                    .await()
            }
        }
    }

    override suspend fun comparisonMatchSize(
        collection: RemoteDataPath.Collection,
        comparator: FieldComparator
    ): Int {
        val querySnapshot = firebaseFirestore
            .collection(collection.name).apply {
                when (comparator.operator) {
                    FieldComparator.Operator.Equal -> {
                        whereEqualTo(comparator.name, comparator.value)
                    }
                }
            }
            .get()
            .await()
        return querySnapshot.size()
    }
}
