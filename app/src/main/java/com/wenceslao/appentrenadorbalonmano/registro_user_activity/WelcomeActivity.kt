package com.wenceslao.appentrenadorbalonmano.registro_user_activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wenceslao.appentrenadorbalonmano.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)

        // Referencia al botón
        val btnIniciarSesion = findViewById<Button>(R.id.iniciarSesion)

        // Cargar la animación
        val animacion = AnimationUtils.loadAnimation(this, R.anim.btn_pressed_animation)

        // Configurar el listener del botón
        btnIniciarSesion.setOnClickListener {
            // Aplicar la animación al botón
            btnIniciarSesion.startAnimation(animacion)

            // Iniciar la nueva actividad después de que la animación termine
            animacion.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {
                }

                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    val intent = Intent(this@WelcomeActivity, LogInActivity::class.java)
                    startActivity(intent)
                }

                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {
                }
            })
        }

        // Configurar el padding para los bordes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}