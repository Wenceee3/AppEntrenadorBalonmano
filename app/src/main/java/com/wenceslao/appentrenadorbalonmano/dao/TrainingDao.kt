package com.wenceslao.appentrenadorbalonmano.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.wenceslao.appentrenadorbalonmano.models.Training

interface TrainingDao {
    suspend fun addTraining(training: Training): Training
    suspend fun getTrainings(): List<Training>
    suspend fun updateTraining(training: Training)
    suspend fun deleteTraining(id: String)
}