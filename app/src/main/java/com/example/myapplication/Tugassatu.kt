package com.example.myapplication

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class Tugassatu : AppCompatActivity() {

    // Menu Cards Declaration
    private lateinit var cardForm: CardView
    private lateinit var cardProfile: CardView
    private lateinit var cardFood: CardView
    private lateinit var cardTemperature: CardView
    private lateinit var cardKalkulator: CardView
    private lateinit var cardExit: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tugassatu)

        // Inisialisasi semua views
        initViews()

        // Setup click listeners untuk semua card
        setupClickListeners()

        // Handle back button press dengan cara modern
        setupBackPressHandler()
    }

    /**
     * Handle perubahan konfigurasi (rotasi layar)
     * Karena ada configChanges di manifest, method ini akan dipanggil
     * dan kita perlu reload layout secara manual
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Reload layout sesuai orientasi
        setContentView(R.layout.activity_tugassatu)

        // Re-initialize views setelah layout di-reload
        initViews()

        // Re-setup click listeners
        setupClickListeners()

        // Re-setup back press handler
        setupBackPressHandler()
    }

    private fun initViews() {
        // Inisialisasi semua Cards sesuai dengan XML
        cardForm = findViewById(R.id.cardForm)
        cardProfile = findViewById(R.id.cardProfile)
        cardFood = findViewById(R.id.cardFood)
        cardTemperature = findViewById(R.id.cardTemperature)
        cardKalkulator = findViewById(R.id.cardKalkulator)
        cardExit = findViewById(R.id.cardExit)
    }

    private fun setupClickListeners() {
        // Menu Utama - Form Card → Formactivity
        cardForm.setOnClickListener {
            showToast("Membuka Form Pendaftaran")
            navigateToActivity(Formactivity::class.java)
        }

        // Menu Utama - Profile Card → Profile
        cardProfile.setOnClickListener {
            showToast("Membuka Profile")
            navigateToActivity(Profile::class.java)
        }

        // Menu Lain - Food Card → Formpemesanan
        cardFood.setOnClickListener {
            showToast("Membuka Pemesanan Makanan")
            navigateToActivity(Formpemesanan::class.java)
        }

        // Menu Lain - Temperature Card → Temperatur
        cardTemperature.setOnClickListener {
            showToast("Membuka Konversi Suhu")
            navigateToActivity(Temperatur::class.java)
        }

        // Lain-Lain - Kalkulator Card → Constrain
        cardKalkulator.setOnClickListener {
            showToast("Membuka Kalkulator")
            navigateToActivity(Constrain::class.java)
        }

        // Lain-Lain - Exit Card
        cardExit.setOnClickListener {
            showExitDialog()
        }
    }

    private fun setupBackPressHandler() {
        // Cara modern untuk handle back button (menggantikan onBackPressed)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Menampilkan dialog konfirmasi saat tombol back ditekan
                showExitDialog()
            }
        })
    }

    /**
     * Fungsi untuk navigasi ke Activity lain
     * @param activityClass Class dari Activity tujuan
     */
    private fun navigateToActivity(activityClass: Class<*>) {
        try {
            val intent = Intent(this, activityClass)
            startActivity(intent)

            // Tambahkan animasi transisi yang smooth (opsional)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } catch (e: Exception) {
            showToast("Error: Tidak dapat membuka halaman")
            e.printStackTrace()
        }
    }

    /**
     * Fungsi untuk menampilkan Toast message
     * @param message Pesan yang akan ditampilkan
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Dialog konfirmasi keluar aplikasi
     */
    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Keluar Aplikasi")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Ya") { dialog, _ ->
                dialog.dismiss()
                finishAffinity() // Menutup semua activity dan keluar aplikasi
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }
}