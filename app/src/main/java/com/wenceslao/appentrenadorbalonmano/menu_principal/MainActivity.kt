package com.wenceslao.appentrenadorbalonmano

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.wenceslao.appentrenadorbalonmano.databinding.ActivityMainBinding
import com.wenceslao.appentrenadorbalonmano.lista_activity.ListaActivity
import com.wenceslao.appentrenadorbalonmano.registro_user_activity.LogInActivity

import android.net.Uri
import android.widget.ImageView
import android.widget.VideoView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Verifica si el usuario está autenticado
        if (auth.currentUser == null) {
            // Si no está autenticado, redirige a LogInActivity
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupNavigationDrawer()

        // Iniciar sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.audio)
        mediaPlayer?.start()

        // Obtener referencia al ImageView
        val imageView = findViewById<ImageView>(R.id.imageView)

        // URL de la imagen de prueba
        val imageUrl = "data:image/jpeg;base64,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"
        // Cargar la imagen con Glide
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)


        // Obtener referencia al VideoView
        val videoView = findViewById<VideoView>(R.id.videoView)

// Obtener la URI del video desde la carpeta res/raw
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.mi_video}")

// Configurar el video
        videoView.setVideoURI(videoUri)

// Reproducir el video
        videoView.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun setupNavigationDrawer() {
        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Acción para "Inicio"
                Toast.makeText(this, "Inicio seleccionado", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_lista_entrenamientos -> {
                // Navegar a ListaActivity
                val intent = Intent(this, ListaActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                // Acción para "Perfil"
                Toast.makeText(this, "Perfil seleccionado", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                // Acción para "Configuración"
                Toast.makeText(this, "Configuración seleccionada", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_about -> {
                // Acción para "Acerca de"
                Toast.makeText(this, "Acerca de seleccionado", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                // Cerrar sesión
                auth.signOut()
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}