package com.example.examen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class LoginActivity : AppCompatActivity() {

    private lateinit var imgPerfilLogin: ImageView
    private lateinit var tvNombreLogin: TextView
    private lateinit var etUsuarioLogin: EditText
    private lateinit var etContrasenaLogin: EditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var btnIrAlRegistro: Button

    private var usuarioRegistrado: String? = null
    private var contrasenaRegistrada: String? = null
    private var nombreRegistrado: String? = null
    private var apellidoRegistrado: String? = null
    private var imagenPathRegistrada: String? = null
    private var emailRegistrado: String? = null
    private var telefonoRegistrado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_login)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Iniciar Sesión"

        imgPerfilLogin = findViewById(R.id.imgPerfilLogin)
        tvNombreLogin = findViewById(R.id.tvNombreLogin)
        etUsuarioLogin = findViewById(R.id.etUsuarioLogin)
        etContrasenaLogin = findViewById(R.id.etContrasenaLogin)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnIrAlRegistro = findViewById(R.id.btnIrAlRegistro)

        recuperarDatosDeRegistro()

        btnIniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        btnIrAlRegistro.setOnClickListener {
            val registroIntent = Intent(this, RegistroActivity::class.java)
            startActivity(registroIntent)
        }
    }

    private fun recuperarDatosDeRegistro() {
        try {
            usuarioRegistrado = intent.getStringExtra("USUARIO")
            contrasenaRegistrada = intent.getStringExtra("CONTRASENA")
            nombreRegistrado = intent.getStringExtra("NOMBRE")
            apellidoRegistrado = intent.getStringExtra("APELLIDO")
            imagenPathRegistrada = intent.getStringExtra("IMAGEN_PATH")
            emailRegistrado = intent.getStringExtra("EMAIL")
            telefonoRegistrado = intent.getStringExtra("TELEFONO")

            if (!usuarioRegistrado.isNullOrEmpty()) {
                etUsuarioLogin.setText(usuarioRegistrado)

                if (!nombreRegistrado.isNullOrEmpty() && !apellidoRegistrado.isNullOrEmpty()) {
                    tvNombreLogin.text = "$nombreRegistrado $apellidoRegistrado"
                    tvNombreLogin.visibility = android.view.View.VISIBLE

                    try {
                        if (!imagenPathRegistrada.isNullOrEmpty()) {
                            imgPerfilLogin.setImageURI(Uri.parse(imagenPathRegistrada))
                            imgPerfilLogin.visibility = android.view.View.VISIBLE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al recuperar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun iniciarSesion() {
        val usuarioIngresado = etUsuarioLogin.text.toString().trim()
        val contrasenaIngresada = etContrasenaLogin.text.toString().trim()

        // Validar que se hayan ingresado datos
        if (usuarioIngresado.isEmpty() || contrasenaIngresada.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        if (!usuarioRegistrado.isNullOrEmpty() && !contrasenaRegistrada.isNullOrEmpty()) {
            if (usuarioIngresado == usuarioRegistrado && contrasenaIngresada == contrasenaRegistrada) {
                navegarAMensajeria()
            } else {

                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No hay usuarios registrados. Por favor registrate primero.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navegarAMensajeria() {
        try {

            val empleado = Empleado(
                nombre = nombreRegistrado ?: "",
                apellido = apellidoRegistrado ?: "",
                usuario = usuarioRegistrado ?: "",
                contrasena = contrasenaRegistrada ?: "",
                email = emailRegistrado ?: "",
                telefono = telefonoRegistrado ?: "",
                imagenPath = imagenPathRegistrada ?: ""
            )


            val mensajeriaIntent = Intent(this, MensajeriaActivity::class.java)
            mensajeriaIntent.putExtra("EMPLEADO", empleado)
            startActivity(mensajeriaIntent)
            
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al iniciar sesión: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
