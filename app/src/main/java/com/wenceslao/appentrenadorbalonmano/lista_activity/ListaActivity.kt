package com.wenceslao.appentrenadorbalonmano.lista_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wenceslao.appentrenadorbalonmano.controllers.TrainingController
import com.wenceslao.appentrenadorbalonmano.databinding.ActivityListaBinding

class ListaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListaBinding
    private lateinit var controller: TrainingController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = TrainingController(this)

        initRecyclerView()
        controller.observarEntrenamientos()  // Observa cambios en tiempo real
        controller.setAdapter(binding.recyclerViewTrainings)

        binding.btnAddTraining.setOnClickListener {
            controller.addTraining()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewTrainings.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.detenerObservacion()  // Detiene la observación al destruir la actividad
    }
}