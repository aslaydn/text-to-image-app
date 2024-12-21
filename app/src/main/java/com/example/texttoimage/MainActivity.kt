package com.example.texttoimage

import android.graphics.BitmapFactory

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import android.os.Bundle
import androidx.activity.ComponentActivity


import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope

import com.example.texttoimage.api.ApiService
import com.example.texttoimage.api.JsonRequest
import com.example.texttoimage.api.RetrofitClient
import com.example.texttoimage.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers


import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

import androidx.activity.result.contract.ActivityResultContracts
import android.app.AlertDialog



class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var turkishEnglishTranslator: Translator
    private val apiService = RetrofitClient.instance.create(ApiService::class.java)
    private var currentBitmap: Bitmap? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "İzin verildi", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "İzin reddedildi. Görselleri kaydedemeyeceksiniz.", Toast.LENGTH_LONG).show()
        }
    }

    // HuggingFace API token'ını buraya ekleyin
    private val apiToken = "Bearer ${BuildConfig.HUGGING_FACE_API_KEY}"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeTranslator()
        setupUI()
        checkPermissions()

    }

    private fun checkPermissions() {
        when {
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.P -> {
                // Android 9 (P) ve altı için depolama izni
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // İzin zaten verilmiş
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                        // İzin açıklaması göster
                        showPermissionRationaleDialog()
                    }
                    else -> {
                        // İzin iste
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13 ve üstü için medya izni
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // İzin zaten verilmiş
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                        // İzin açıklaması göster
                        showPermissionRationaleDialog()
                    }
                    else -> {
                        // İzin iste
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                }
            }
        }
    }
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("İzin Gerekli")
            .setMessage("Oluşturulan görselleri galeriye kaydedebilmek için bu izne ihtiyacımız var.")
            .setPositiveButton("İzin Ver") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            .setNegativeButton("Reddet") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(
                    this,
                    "İzin olmadan görselleri kaydedemezsiniz",
                    Toast.LENGTH_LONG
                ).show()
            }
            .create()
            .show()
    }

    private fun initializeTranslator() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.TURKISH)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        turkishEnglishTranslator = Translation.getClient(options)

        // Çeviri modelini indir
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                turkishEnglishTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        // Model başarıyla indirildi
                    }
                    .addOnFailureListener { exception ->
                        // Model indirilemedi
                        lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "Çeviri modeli yüklenemedi: ${exception.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Model indirme hatası: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private suspend fun translateToEnglish(turkishText: String): String {
        return withContext(Dispatchers.IO) {
            try {
                suspendCancellableCoroutine { continuation ->
                    turkishEnglishTranslator.translate(turkishText)
                        .addOnSuccessListener { translatedText ->
                            continuation.resume(translatedText)
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
            } catch (e: Exception) {
                throw Exception("Çeviri hatası: ${e.localizedMessage}")
            }
        }
    }
    private fun setupUI() {
        binding.generateButton.setOnClickListener {
            val prompt = binding.promptEditText.text.toString()
            if (prompt.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.generateButton.isEnabled = false

                        // Türkçe metni İngilizceye çevir
                        val translatedPrompt = translateToEnglish(prompt)
                        // Çevrilmiş metin ile görsel oluştur
                        generateImage(translatedPrompt)
                    } catch (e: Exception) {
                        showError(e.localizedMessage ?: "Bir hata oluştu")
                        binding.progressBar.visibility = View.GONE
                        binding.generateButton.isEnabled = true
                    }
                }
            } else {
                Toast.makeText(this, "Lütfen bir açıklama girin", Toast.LENGTH_SHORT).show()
            }
        }

        // Kaydetme butonu için yeni bir buton ekleyin (XML'de button_save id'li bir buton oluşturun)
        binding.buttonSave.setOnClickListener {
            currentBitmap?.let { bitmap ->
                lifecycleScope.launch(Dispatchers.IO) {
                    saveImageToGallery(bitmap)
                }
            } ?: run {
                Toast.makeText(this, "Kaydedilecek görsel bulunamadı", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private suspend fun saveImageToGallery(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            try {
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val filename = "AI_Generated_$timestamp.jpg"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }

                    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    uri?.let {
                        contentResolver.openOutputStream(it)?.use { stream ->
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, "Görsel galeriye kaydedildi", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val image = File(imagesDir, filename)
                    FileOutputStream(image).use { stream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Görsel galeriye kaydedildi", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Görsel kaydedilemedi: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun generateImage(prompt: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.generateButton.isEnabled = false
        binding.resultImageView.setImageBitmap(null)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val requestBody = JsonRequest.create(prompt)
                val response = apiService.generateImage(apiToken, requestBody)

                if (response.isSuccessful) {
                    val imageBytes = response.body()?.bytes()
                    if (imageBytes != null) {
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        currentBitmap = bitmap  // Bitmap'i sınıf değişkenine kaydet
                        withContext(Dispatchers.Main) {
                            binding.resultImageView.setImageBitmap(bitmap)
                            binding.buttonSave.isEnabled = true  // Kaydetme butonunu aktif et
                        }
                    } else {
                        showError("Görüntü oluşturulamadı")
                    }
                } else {
                    showError("API hatası: ${response.code()}")
                }
            } catch (e: Exception) {
                showError("Hata oluştu: ${e.localizedMessage}")
            } finally {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.generateButton.isEnabled = true
                }
            }
        }
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }
    }

}
