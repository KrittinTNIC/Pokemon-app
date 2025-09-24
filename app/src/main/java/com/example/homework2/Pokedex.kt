package com.example.homework2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Pokedex : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.pokedex)

        // Get values from intent
        val pokemon = intent.getStringExtra("pokemon") ?: "Unknown"
        val element = intent.getStringExtra("element") ?: "None"
        val subelement = intent.getStringExtra("subelement") ?: "None"
        val hp = intent.getStringExtra("HP") ?: "0/0"
        val atk = intent.getStringExtra("ATK") ?: "0/0"
        val def = intent.getStringExtra("DEF") ?: "0/0"
        val spd = intent.getStringExtra("SPD") ?: "0/0"

        // Link UI
        val pokemonName = findViewById<TextView>(R.id.pokemonName3)
        val elementText = findViewById<TextView>(R.id.elementText)
        val subelementText = findViewById<TextView>(R.id.subelementText)
        val pokemonImage = findViewById<ImageView>(R.id.pokemonImage)
        val hpText = findViewById<TextView>(R.id.hpText)
        val atkText = findViewById<TextView>(R.id.atkText)
        val defText = findViewById<TextView>(R.id.defText)
        val spdText = findViewById<TextView>(R.id.spdText)
        val restartButton = findViewById<Button>(R.id.restartButton)

        pokemonName.text = pokemon
        elementText.text = "Element  : $element"
        subelementText.text = "Sub  : $subelement"
        hpText.text = "HP: $hp"
        atkText.text = "ATK: $atk"
        defText.text = "DEF: $def"
        spdText.text = "SPD: $spd"

        val pokemonDrawables = mapOf(
            "Charmander" to R.drawable.charmander,
            "Squirtle" to R.drawable.squirtle,
            "Bulbasaur" to R.drawable.bulbasaur
        )
        pokemonImage.setImageResource(pokemonDrawables[pokemon] ?: R.drawable.charmander)

        restartButton.setOnClickListener {
            val intent = Intent(this, PokeLab::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
