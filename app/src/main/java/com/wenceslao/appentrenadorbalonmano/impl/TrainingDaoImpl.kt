package com.wenceslao.appentrenadorbalonmano.dao

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.wenceslao.appentrenadorbalonmano.models.Training
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TrainingDaoImpl : TrainingDao {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("trainings")

    override suspend fun getTrainings(): List<Training> {
        return collection.get().await().toObjects(Training::class.java)
    }

    override suspend fun addTraining(training: Training): Training {
        training.id = UUID.randomUUID().toString()
        Log.d("TrainingDaoImpl", "ID generado manualmente: ${training.id}")

        // Agregar el entrenamiento a Firestore con el ID generado
        collection.document(training.id).set(training).await()
        return training // Devuelve el objeto Training con el ID generado
    }

    override suspend fun updateTraining(training: Training) {
        if (training.id.isNotEmpty()) {  // Verifica que el ID no esté vacío
            collection.document(training.id).set(training).await()
        } else {
            throw IllegalArgumentException("El ID del entrenamiento no puede estar vacío")
        }
    }

    override suspend fun deleteTraining(id: String) {
        if (id.isNotEmpty()) {  // Verifica que el ID no esté vacío
            collection.document(id).delete().await()
        } else {
            throw IllegalArgumentException("El ID del entrenamiento no puede estar vacío")
        }
    }

    // Método para observar cambios en tiempo real
    fun observarEntrenamientos(
        onChange: (List<Training>) -> Unit,
        onFailure: (Exception) -> Unit
    ): ListenerRegistration {
        return collection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                onFailure(e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val entrenamientos = snapshot.toObjects(Training::class.java)
                onChange(entrenamientos)
            }
        }
    }

    companion object {
        val instance: TrainingDaoImpl by lazy { TrainingDaoImpl() }
    }
}