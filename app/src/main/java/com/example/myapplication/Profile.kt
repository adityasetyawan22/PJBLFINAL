package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Profile : AppCompatActivity() {

    // Deklarasi View Components
    private lateinit var profileImage: ImageView
    private lateinit var whatsappLayout: LinearLayout
    private lateinit var instagramLayout: LinearLayout
    private lateinit var emailLayout: LinearLayout

    // Data Kontak
    private val name = "Desiderius Aditya S."
    private val phoneNumberDisplay = "+62-857-0151-6287"
    private val instagramUsername = "riusssz"
    private val emailAddress = "akuadit1209@gmail.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inisialisasi Views
        initViews()

        // Setup Click Listeners
        setupClickListeners()
    }

    private fun initViews() {
        profileImage = findViewById(R.id.profileImage)
        whatsappLayout = findViewById(R.id.whatsappLayout)
        instagramLayout = findViewById(R.id.instagramLayout)
        emailLayout = findViewById(R.id.emailLayout)
    }

    private fun setupClickListeners() {
        // Profile Image Click - Opsional untuk melihat foto besar
        profileImage.setOnClickListener {
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
        }

        // WhatsApp Click Listener dengan Long Click untuk Copy
        whatsappLayout.setOnClickListener {
            openWhatsApp()
        }

        whatsappLayout.setOnLongClickListener {
            showContactOptions("WhatsApp", phoneNumberDisplay, phoneNumberDisplay, ContactType.WHATSAPP)
            true
        }

        // Instagram Click Listener dengan Long Click untuk Copy
        instagramLayout.setOnClickListener {
            openInstagram()
        }

        instagramLayout.setOnLongClickListener {
            showContactOptions("Instagram", "@$instagramUsername", instagramUsername, ContactType.INSTAGRAM)
            true
        }

        // Email Click Listener dengan Long Click untuk Copy
        emailLayout.setOnClickListener {
            openEmail()
        }

        emailLayout.setOnLongClickListener {
            showContactOptions("Email", emailAddress, emailAddress, ContactType.EMAIL)
            true
        }
    }

    private fun openWhatsApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "https://wa.me/6285701516287".toUri()
            startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(this, "WhatsApp tidak terinstall", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openInstagram() {
        try {
            // Coba buka Instagram app
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "https://www.instagram.com/riusssz?igsh=MWNxNTV0M3gyYTVuYQ==".toUri()
            intent.setPackage("com.instagram.android")
            startActivity(intent)
        } catch (_: Exception) {
            // Jika gagal, buka di browser
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = "https://www.instagram.com/riusssz?igsh=MWNxNTV0M3gyYTVuYQ==".toUri()
                startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(this, "Tidak dapat membuka Instagram", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openEmail() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = "mailto:$emailAddress".toUri()
            intent.putExtra(Intent.EXTRA_SUBJECT, "Hello!")
            intent.putExtra(Intent.EXTRA_TEXT, "Halo $name,\n\n")

            // Langsung gunakan createChooser tanpa resolveActivity
            startActivity(Intent.createChooser(intent, "Kirim Email"))
        } catch (_: Exception) {
            Toast.makeText(this, "Tidak dapat membuka email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showContactOptions(title: String, displayText: String, copyText: String, type: ContactType) {
        val options = when (type) {
            ContactType.WHATSAPP -> arrayOf("Buka WhatsApp", "Copy Nomor", "Bagikan Nomor")
            ContactType.INSTAGRAM -> arrayOf("Buka Instagram", "Copy Username", "Bagikan Username")
            ContactType.EMAIL -> arrayOf("Kirim Email", "Copy Email", "Bagikan Email")
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(displayText)
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Buka aplikasi
                        when (type) {
                            ContactType.WHATSAPP -> openWhatsApp()
                            ContactType.INSTAGRAM -> openInstagram()
                            ContactType.EMAIL -> openEmail()
                        }
                    }
                    1 -> {
                        // Copy ke clipboard
                        copyToClipboard(title, copyText)
                    }
                    2 -> {
                        // Bagikan
                        shareContact(title, displayText)
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun copyToClipboard(label: String, text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "$label disalin ke clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun shareContact(title: String, text: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Kontak $name")
            intent.putExtra(Intent.EXTRA_TEXT, "$title: $text")
            startActivity(Intent.createChooser(intent, "Bagikan $title"))
        } catch (_: Exception) {
            Toast.makeText(this, "Tidak dapat membagikan", Toast.LENGTH_SHORT).show()
        }
    }

    enum class ContactType {
        WHATSAPP,
        INSTAGRAM,
        EMAIL
    }
}