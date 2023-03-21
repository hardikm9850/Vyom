package com.hardik.vyom.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hardik.vyom.databinding.ActivityMainBinding
import com.hardik.vyom.Vyom

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var url = "https://picsum.photos/500/500"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadImage(url)
        binding.btnSubmit.setOnClickListener {
            loadImage(binding.input.text.toString())
        }
        binding.input.setText(url)
    }

    private fun loadImage(url: String) {
        Vyom.get()
            .url(url)
            .target(binding.imageView)
            .load()
    }
}