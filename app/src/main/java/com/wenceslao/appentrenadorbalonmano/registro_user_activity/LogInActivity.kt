package com.wenceslao.appentrenadorbalonmano.registro_user_activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.wenceslao.appentrenadorbalonmano.MainActivity
import com.wenceslao.appentrenadorbalonmano.R

class LogInActivity : AppCompatActivity() {

    private lateinit var nombreUsuario: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var iniciarSesion: Button
    private lateinit var crearCuenta: Button
    private lateinit var restaurarPassword: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Verifica si el usuario ya está autenticado
        if (auth.currentUser != null) {
            // Si el usuario ya está autenticado, redirige a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        nombreUsuario = findViewById(R.id.nombreUsuario)
        password = findViewById(R.id.password)
        iniciarSesion = findViewById(R.id.iniciarSesion)
        crearCuenta = findViewById(R.id.crearCuenta)
        restaurarPassword = findViewById(R.id.restaurarPassword)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configura el clic del botón "Iniciar Sesión"
        iniciarSesion.setOnClickListener {
            val usuario = nombreUsuario.editText!!.text.toString()
            val passwordText = password.editText!!.text.toString()

            if (usuario.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(usuario).matches()) {
                nombreUsuario.error = "Formato de correo incorrecto"
            } else {
                nombreUsuario.error = null
            }

            if (passwordText.isEmpty() || passwordText.length < 8) {
                password.error = "La contraseña debe tener al menos 8 caracteres"
            } else {
                password.error = null
            }

            if (nombreUsuario.error == null && password.error == null) {
                loginFirebase(usuario, passwordText)
            }
        }

        // Configura el clic del botón "Crear Cuenta"
        crearCuenta.setOnClickListener {
            val usuario = nombreUsuario.editText!!.text.toString()
            val passwordText = password.editText!!.text.toString()

            if (usuario.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(usuario).matches()) {
                nombreUsuario.error = "Formato de correo incorrecto"
            } else {
                nombreUsuario.error = null
            }

            if (passwordText.isEmpty() || passwordText.length < 8) {
                password.error = "La contraseña debe tener al menos 8 caracteres"
            } else {
                password.error = null
            }

            if (nombreUsuario.error == null && password.error == null) {
                crearCuentaFirebase(usuario, passwordText)
            }
        }

        // Configura el clic del botón "Restaurar Contraseña"
        restaurarPassword.setOnClickListener {
            val usuario = nombreUsuario.editText!!.text.toString()

            if (usuario.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(usuario).matches()) {
                nombreUsuario.error = "Formato de correo incorrecto"
            } else {
                nombreUsuario.error = null
                restaurarPasswordFirebase(usuario)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val intent = Intent(this, MainActivity::class.java) // Redirige a MainActivity
                    startActivity(intent)
                    finish()
                } else {
                    // Si el inicio de sesión falla, muestra un mensaje al usuario.
                    Toast.makeText(baseContext, "Autenticación fallida: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun crearCuentaFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Cuenta creada exitosamente
                    Toast.makeText(baseContext, "Cuenta creada exitosamente.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Si la creación de la cuenta falla, muestra un mensaje de error.
                    Toast.makeText(baseContext, "Error al crear la cuenta: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun restaurarPasswordFirebase(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Correo de restauración enviado
                    Toast.makeText(baseContext, "Correo de restauración enviado a $email", Toast.LENGTH_SHORT).show()
                } else {
                    // Si falla, muestra un mensaje de error.
                    Toast.makeText(baseContext, "Error al enviar el correo de restauración: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun verificarSesionAbierta() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}