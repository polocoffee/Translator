package com.banklannister.translator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.banklannister.translator.databinding.ActivityMainBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var items = arrayOf("English", "Thai", "Japanese")
    private var conditions = DownloadConditions.Builder().requireWifi().build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val itemAdapter: ArrayAdapter<String> =
            ArrayAdapter(
                this, android.R.layout.simple_dropdown_item_1line,
                items
            )

        binding.languageFrom.setAdapter(itemAdapter)

        binding.languageTo.setAdapter(itemAdapter)

        binding.translate.setOnClickListener {
            val options = TranslatorOptions
                .Builder()
                .setSourceLanguage(selectFrom())
                .setTargetLanguage(selectTo())
                .build()

            val translator = Translation.getClient(options)

            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {

                    translator.translate(binding.input.text.toString())
                        .addOnSuccessListener {
                            binding.output.text = it

                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun selectFrom(): String {
        return when (binding.languageFrom.text.toString()) {
            "" -> TranslateLanguage.ENGLISH
            "English" -> TranslateLanguage.ENGLISH

            "Thai" -> TranslateLanguage.THAI

            "Japanese" -> TranslateLanguage.JAPANESE

            else -> TranslateLanguage.ENGLISH
        }
    }

    private fun selectTo(): String {
        return when (binding.languageTo.text.toString()) {
            "" -> TranslateLanguage.THAI
            "English" -> TranslateLanguage.ENGLISH

            "Thai" -> TranslateLanguage.THAI

            "Japanese" -> TranslateLanguage.JAPANESE

            else -> TranslateLanguage.THAI
        }
    }
}