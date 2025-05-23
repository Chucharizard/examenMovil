package com.example.examen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.File
import java.io.FileOutputStream

class RegistroActivity : AppCompatActivity() {

    private lateinit var imgPerfil: ImageView
    private lateinit var btnCargarFoto: Button
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etUsuario: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnIrAlLogin: Button

    private var imagenUri: Uri? = null
    private var empleadoRegistrado: Empleado? = null

    private val galeriaLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imagenUri = it
            imgPerfil.setImageURI(imagenUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_registro)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Registro de Empleado"

        imgPerfil = findViewById(R.id.imgPerfil)
        btnCargarFoto = findViewById(R.id.btnCargarFoto)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etUsuario = findViewById(R.id.etUsuario)
        etContrasena = findViewById(R.id.etContrasena)
        etEmail = findViewById(R.id.etEmail)
        etTelefono = findViewById(R.id.etTelefono)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnIrAlLogin = findViewById(R.id.btnIrAlLogin)

        btnCargarFoto.setOnClickListener {
            galeriaLauncher.launch("image/*")
        }

        btnRegistrar.setOnClickListener {
            registrarEmpleado()
        }

        btnIrAlLogin.setOnClickListener {
            navegarAlLogin()
        }
    }

    private fun registrarEmpleado() {
        if (etNombre.text.isEmpty() || etApellido.text.isEmpty() || etUsuario.text.isEmpty() ||
            etContrasena.text.isEmpty() || etEmail.text.isEmpty() || etTelefono.text.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (imagenUri == null) {
            Toast.makeText(this, "Por favor selecciona una foto de perfil", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val imagenPath = guardarImagen(imagenUri!!)

            empleadoRegistrado = Empleado(
                nombre = etNombre.text.toString(),
                apellido = etApellido.text.toString(),
                usuario = etUsuario.text.toString(),
                contrasena = etContrasena.text.toString(),
                email = etEmail.text.toString(),
                telefono = etTelefono.text.toString(),
                imagenPath = imagenPath
            )

            Toast.makeText(this, "Registro exitoso. Ahora puedes ir al Login.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al registrar: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun guardarImagen(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(filesDir, "perfil_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    private fun navegarAlLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        empleadoRegistrado?.let { empleado ->
            loginIntent.putExtra("USUARIO", empleado.usuario)
            loginIntent.putExtra("CONTRASENA", empleado.contrasena)
            loginIntent.putExtra("NOMBRE", empleado.nombre)
            loginIntent.putExtra("APELLIDO", empleado.apellido)
            loginIntent.putExtra("EMAIL", empleado.email)
            loginIntent.putExtra("TELEFONO", empleado.telefono)
            loginIntent.putExtra("IMAGEN_PATH", empleado.imagenPath)
        }
        startActivity(loginIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_registro, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_login -> {
                navegarAlLogin()
                true
            }
            R.id.menu_inicio -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
