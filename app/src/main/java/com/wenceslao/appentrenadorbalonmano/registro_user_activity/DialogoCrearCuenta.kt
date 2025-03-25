package com.wenceslao.appentrenadorbalonmano.registro_user_activity

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.wenceslao.appentrenadorbalonmano.R

class DialogoCrearCuenta : DialogFragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del diálogo
        val view: View = inflater.inflate(R.layout.fragment_dialogo_crear_cuenta, container, false)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Obtener referencias a las vistas
        val correo: EditText = view.findViewById(R.id.edittext_correo)
        val pass: EditText = view.findViewById(R.id.edittext_pass)
        val botonCrearCuenta: Button = view.findViewById(R.id.boton_crear_cuenta)

        // Configurar el clic del botón
        botonCrearCuenta.setOnClickListener {
            val email = correo.text.toString()
            val password = pass.text.toString()

            if (password.isNotEmpty()) {
                if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    crearCuentaFirebase(email, password)
                } else {
                    Toast.makeText(requireContext(), "Formato de correo incorrecto.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "Escriba la contraseña", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun crearCuentaFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Cuenta creada exitosamente
                    Toast.makeText(requireContext(), "Cuenta creada exitosamente.", Toast.LENGTH_SHORT).show()
                    dismiss() // Cerrar el diálogo
                } else {
                    // Si la creación de la cuenta falla, mostrar un mensaje de error
                    Toast.makeText(requireContext(), "Error al crear la cuenta: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}