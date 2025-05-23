package com.example.examen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MensajeriaActivity : AppCompatActivity() {

    private lateinit var imgPerfilMensajeria: ImageView
    private lateinit var tvUsuarioMensajeria: TextView
    private lateinit var etMensaje: EditText
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var ivImagenSeleccionada: ImageView
    private lateinit var btnWhatsapp: Button
    private lateinit var btnTelegram: Button
    private lateinit var btnOtrasApps: Button

    private var empleado: Empleado? = null
    private var imagenSeleccionadaUri: Uri? = null

    private val seleccionarImagenLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imagenSeleccionadaUri = it
            ivImagenSeleccionada.setImageURI(it)
            ivImagenSeleccionada.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajeria)

        // Configurar la Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar_mensajeria)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Mensajería"

        // Inicializar vistas
        imgPerfilMensajeria = findViewById(R.id.imgPerfilMensajeria)
        tvUsuarioMensajeria = findViewById(R.id.tvUsuarioMensajeria)
        etMensaje = findViewById(R.id.etMensaje)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        ivImagenSeleccionada = findViewById(R.id.ivImagenSeleccionada)
        btnWhatsapp = findViewById(R.id.btnWhatsapp)
        btnTelegram = findViewById(R.id.btnTelegram)
        btnOtrasApps = findViewById(R.id.btnOtrasApps)

        empleado = intent.getParcelableExtra("EMPLEADO")

        empleado?.let { emp ->
            tvUsuarioMensajeria.text = "${emp.nombre} ${emp.apellido}"
            try {
                imgPerfilMensajeria.setImageURI(Uri.parse(emp.imagenPath))
            } catch (e: Exception) {
                Toast.makeText(this, "Error al cargar la imagen de perfil", Toast.LENGTH_SHORT).show()
            }
        }

        btnSeleccionarImagen.setOnClickListener {
            seleccionarImagen()
        }

        btnWhatsapp.setOnClickListener {
            enviarMensaje("com.whatsapp")
        }

        btnTelegram.setOnClickListener {
            enviarMensaje("org.telegram.messenger")
        }

        btnOtrasApps.setOnClickListener {
            enviarMensajeGenerico()
        }
    }

    private fun seleccionarImagen() {
        seleccionarImagenLauncher.launch("image/*")
    }

    private fun enviarMensaje(paquete: String) {
        val mensaje = etMensaje.text.toString().trim()

        if (mensaje.isEmpty() && imagenSeleccionadaUri == null) {
            Toast.makeText(this, "Escribe un mensaje o selecciona una imagen", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val intent = Intent(Intent.ACTION_SEND)

            if (imagenSeleccionadaUri != null) {
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, imagenSeleccionadaUri)
            } else {
                intent.type = "text/plain"
            }

            if (mensaje.isNotEmpty()) {
                intent.putExtra(Intent.EXTRA_TEXT, mensaje)
            }

            intent.setPackage(paquete)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "No se pudo abrir la aplicación. Asegúrate de que esté instalada.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun enviarMensajeGenerico() {
        val mensaje = etMensaje.text.toString().trim()

        if (mensaje.isEmpty() && imagenSeleccionadaUri == null) {
            Toast.makeText(this, "Escribe un mensaje o selecciona una imagen", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(Intent.ACTION_SEND)

        if (imagenSeleccionadaUri != null) {
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, imagenSeleccionadaUri)
        } else {
            intent.type = "text/plain"
        }

        if (mensaje.isNotEmpty()) {
            intent.putExtra(Intent.EXTRA_TEXT, mensaje)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(Intent.createChooser(intent, "Compartir con..."))
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
