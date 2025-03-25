package com.wenceslao.appentrenadorbalonmano.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wenceslao.appentrenadorbalonmano.R
import com.wenceslao.appentrenadorbalonmano.databinding.ItemTrainingBinding
import com.wenceslao.appentrenadorbalonmano.models.Training

class ViewHTraining(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemTrainingBinding.bind(view)

    fun renderize(
        training: Training,
        position: Int,
        onDelete: (Int) -> Unit,
        onEdit: (Int) -> Unit
    ) {
        binding.tvTrainingName.text = training.name
        binding.tvTrainingDescription.text = training.description
        binding.tvTrainingDate.text = training.date
        binding.tvTrainingDuration.text = training.duration

        // Asigna una imagen según el tipo de entrenamiento
        binding.imgTrainingType.setImageResource(
            when {
                training.name.contains("fuerza", ignoreCase = true) -> R.drawable.ic_fuerza
                training.name.contains("velocidad", ignoreCase = true) -> R.drawable.ic_velocidad
                training.name.contains("resistencia", ignoreCase = true) -> R.drawable.ic_resistencia
                else -> R.drawable.ic_placeholder
            }
        )

        // Botón de eliminar
        binding.btnDeleteTraining.setOnClickListener {
            onDelete(position)
        }

        // Botón de editar
        binding.btnEditTraining.setOnClickListener {
            onEdit(position)
        }
    }
}
