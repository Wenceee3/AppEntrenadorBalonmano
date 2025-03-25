package com.wenceslao.appentrenadorbalonmano.controllers

import AdapterTraining
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ListenerRegistration
import com.wenceslao.appentrenadorbalonmano.R
import com.wenceslao.appentrenadorbalonmano.dao.TrainingDaoImpl
import com.wenceslao.appentrenadorbalonmano.models.Training
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrainingController(private val context: Context) {
    private val trainingDao = TrainingDaoImpl.instance
    var trainingList: MutableList<Training> = mutableListOf()
    lateinit var adapterTraining: AdapterTraining
    private var firestoreListener: ListenerRegistration? = null

    fun observarEntrenamientos() {
        firestoreListener = trainingDao.observarEntrenamientos(
            onChange = { nuevosEntrenamientos ->
                Log.d("TrainingController", "Nuevos entrenamientos recibidos: ${nuevosEntrenamientos.size}")

                // Actualiza la lista local
                trainingList.clear()
                trainingList.addAll(nuevosEntrenamientos)

                // Notifica al adaptador que los datos han cambiado
                adapterTraining.notifyDataSetChanged()
            },
            onFailure = { e ->
                Log.e("TrainingController", "Error al observar entrenamientos: ${e.message}")
            }
        )
    }

    // Método para detener la observación
    fun detenerObservacion() {
        firestoreListener?.remove()
    }

    fun setAdapter(recyclerView: RecyclerView) {
        adapterTraining = AdapterTraining(
            trainingList,
            onDeleteItem = { position ->
                deleteTraining(position)
            },
            onEditItem = { position ->
                editTraining(position)
            }
        )
        recyclerView.adapter = adapterTraining
    }

    private fun deleteTraining(position: Int) {
        if (position >= 0 && position < trainingList.size) {
            val training = trainingList[position]
            if (training.id.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        trainingDao.deleteTraining(training.id)
                        withContext(Dispatchers.Main) {
                            trainingList.removeAt(position)
                            adapterTraining.notifyItemRemoved(position)
                            Toast.makeText(context, "Entrenamiento eliminado", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error al eliminar entrenamiento: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Error: ID del entrenamiento no válido", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Error: Índice fuera de límites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editTraining(position: Int) {
        val training = trainingList[position]

        if (training.id.isNotEmpty()) {  // Verifica que el ID no esté vacío
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_training, null)
            val nameInput = dialogView.findViewById<EditText>(R.id.editTrainingName)
            val descriptionInput = dialogView.findViewById<EditText>(R.id.editTrainingDescription)
            val dateInput = dialogView.findViewById<EditText>(R.id.editTrainingDate)
            val durationInput = dialogView.findViewById<EditText>(R.id.editTrainingTime)
            val imageUrlInput = dialogView.findViewById<EditText>(R.id.editTrainingImageUrl)

            nameInput.setText(training.name)
            descriptionInput.setText(training.description)
            dateInput.setText(training.date)
            durationInput.setText(training.duration)
            imageUrlInput.setText(training.imageUrl)

            AlertDialog.Builder(context)
                .setTitle("Editar Entrenamiento")
                .setView(dialogView)
                .setPositiveButton("Guardar") { _, _ ->
                    // Actualiza los valores del entrenamiento
                    training.name = nameInput.text.toString()
                    training.description = descriptionInput.text.toString()
                    training.date = dateInput.text.toString()
                    training.duration = durationInput.text.toString()
                    training.imageUrl = imageUrlInput.text.toString()

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            trainingDao.updateTraining(training)  // Guarda los cambios en Firestore
                            withContext(Dispatchers.Main) {
                                adapterTraining.notifyItemChanged(position)  // Notifica al adaptador
                                Toast.makeText(context, "Entrenamiento actualizado", Toast.LENGTH_SHORT).show()  // Mensaje de éxito
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error al actualizar entrenamiento: ${e.message}", Toast.LENGTH_SHORT).show()  // Mensaje de error
                            }
                            Log.e("TrainingController", "Error al actualizar entrenamiento: ${e.message}")
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .create()
                .show()
        } else {
            Toast.makeText(context, "Error: ID del entrenamiento no válido", Toast.LENGTH_SHORT).show()
        }
    }

    fun addTraining() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_training, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.editTrainingName)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.editTrainingDescription)
        val dateInput = dialogView.findViewById<EditText>(R.id.editTrainingDate)
        val durationInput = dialogView.findViewById<EditText>(R.id.editTrainingTime)
        val imageUrlInput = dialogView.findViewById<EditText>(R.id.editTrainingImageUrl)

        AlertDialog.Builder(context)
            .setTitle("Agregar Nuevo Entrenamiento")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val newTraining = Training(
                    name = nameInput.text.toString(),
                    description = descriptionInput.text.toString(),
                    date = dateInput.text.toString(),
                    duration = durationInput.text.toString(),
                    imageUrl = imageUrlInput.text.toString(),
                    id = ""
                )

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Firestore genera el ID y devuelve el objeto Training actualizado
                        val trainingConId = trainingDao.addTraining(newTraining)
                        Log.d("TrainingController", "ID generado: ${trainingConId.id}")

                        // No agregamos manualmente a la lista local
                        // La lista local se actualizará automáticamente a través de observarEntrenamientos
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Entrenamiento agregado", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error al agregar entrenamiento: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }
}