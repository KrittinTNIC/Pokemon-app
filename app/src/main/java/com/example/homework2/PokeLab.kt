package com.example.homework2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PokeLab : AppCompatActivity() {

    private val handler = Handler()
    private var charIndex = 0 //count position of char
    private var blinkRunnable: Runnable? = null
    private var selectedPokemon: String? = null
    private var selectedPokemonelement: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.pokelab)

        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.answerButton)
        val gobutton = findViewById<Button>(R.id.gobutton)
        val blinkIndicator = findViewById<TextView>(R.id.blinkIndicator)
        val choosing = findViewById<View>(R.id.choosingbox)

        val dialogues = listOf(
            "??? : Hello there! Welcome to the world of POKEMON! My name is Oak! People call me the Pokemon professor!",
            "Oak : This world is inhabited by creatures called Pokemon! For some people, Pokemon are pets. Others use them for fights. I... myself, study Pokemon as a profession.",
            "Oak : Your very own Pokemon legend is about to unfold! A world of dreams and adventures with Pokemon awaits!",
            "Oak : Here, there are 3 Pokemon here! You can have one! Choose!"
        )

        var index = 0

        // Function to animate text like typewriter
        fun showTextAnimated(text: String) {
            charIndex = 0
            textView.text = ""
            blinkIndicator.visibility = TextView.GONE
            handler.removeCallbacksAndMessages(null) // stop any previous animations

            val runnable = object : Runnable {
                override fun run() {
                    if (charIndex < text.length) {
                        textView.append(text[charIndex].toString())
                        charIndex++
                        handler.postDelayed(this, 50) // typing speed in ms
                    } else {
                        // Typing finished → start blinking indicator
                        blinkIndicator.visibility = TextView.VISIBLE
                        blinkRunnable = object : Runnable {
                            override fun run() {
                                blinkIndicator.visibility =
                                    if (blinkIndicator.visibility == TextView.VISIBLE) TextView.INVISIBLE else TextView.VISIBLE
                                handler.postDelayed(this, 500) // blink speed
                            }
                        }
                        handler.post(blinkRunnable!!)
                    }
                }
            }

            handler.post(runnable)
        }

        showTextAnimated(dialogues[index])
        index++

        // pokemons
        val pokemon1 = findViewById<ImageView>(R.id.charmander)
        val pokemon2 = findViewById<ImageView>(R.id.squirtle)
        val pokemon3 = findViewById<ImageView>(R.id.bulbasaur)

        button.setOnClickListener {
            if (charIndex < dialogues[index - 1].length) {
                // Finish typing instantly
                handler.removeCallbacksAndMessages(null)
                textView.text = dialogues[index - 1]
                charIndex = dialogues[index - 1].length
                blinkIndicator.visibility = TextView.VISIBLE
            } else {
                // Stop previous blinking
                blinkRunnable?.let { handler.removeCallbacks(it) }
                blinkIndicator.visibility = TextView.GONE

                if (index < dialogues.size) {
                    // Show next dialogue
                    showTextAnimated(dialogues[index])
                    index++
                } else {
                    // Last dialogue done → hide text & show 3 Pokemon
                    textView.visibility = TextView.GONE
                    button.visibility = Button.GONE
                    blinkIndicator.visibility = TextView.GONE
                    pokemon1.visibility = ImageView.VISIBLE
                    pokemon2.visibility = ImageView.VISIBLE
                    pokemon3.visibility = ImageView.VISIBLE
                    choosing.visibility = View.VISIBLE
                }
            }
        }

        fun startDialogue2() {
            val chooseAgainButton = findViewById<Button>(R.id.chooseAgainButton)

            pokemon1.visibility = ImageView.GONE
            pokemon2.visibility = ImageView.GONE
            pokemon3.visibility = ImageView.GONE
            choosing.visibility = View.GONE
            textView.visibility = TextView.VISIBLE
            button.visibility = Button.VISIBLE
            chooseAgainButton.visibility = View.GONE // hidden by default
            index = 0

            val dialogues2 = listOf(
                "Oak: So... you want the $selectedPokemonelement Pokemon, $selectedPokemon ?",
                "Oak: Good choice! remember, a strong trainer respects their Pokemon and forms a bond with them.",
                "Oak: Now, go out there and start your adventure!"
            )

            fun showDialogue2Line() {
                showTextAnimated(dialogues2[index])
                if (index == 0) {
                    chooseAgainButton.visibility = View.VISIBLE
                } else {
                    chooseAgainButton.visibility = View.GONE
                }
                if (index == 2) {
                    button.visibility = View.GONE
                    gobutton.visibility = View.VISIBLE
                } else {
                    gobutton.visibility = View.GONE
                    button.visibility = View.VISIBLE
                }
            }

            // Handle Next button
            button.setOnClickListener {
                if (charIndex < dialogues2[index].length) {
                    handler.removeCallbacksAndMessages(null)
                    textView.text = dialogues2[index]
                    charIndex = dialogues2[index].length
                    blinkIndicator.visibility = TextView.VISIBLE
                } else {
                    index++
                    if (index < dialogues2.size) {
                        showDialogue2Line()
                    } else {
                        // End of dialogues
                        chooseAgainButton.visibility = View.GONE
                    }
                }
            }

            // Handle Choose Again button
            chooseAgainButton.setOnClickListener {
                // Reset everything
                handler.removeCallbacksAndMessages(null)
                blinkRunnable?.let { handler.removeCallbacks(it) }

                textView.text = ""
                textView.visibility = View.GONE
                button.visibility = View.GONE
                chooseAgainButton.visibility = View.GONE
                blinkIndicator.visibility = View.GONE

                // Show Pokémon again
                pokemon1.visibility = ImageView.VISIBLE
                pokemon2.visibility = ImageView.VISIBLE
                pokemon3.visibility = ImageView.VISIBLE
                choosing.visibility = View.VISIBLE
            }
            showDialogue2Line()
        }

        //initiate dialogue2
        pokemon1.setOnClickListener {
            selectedPokemon = "Charmander"
            selectedPokemonelement = "fire"
            startDialogue2()
        }
        pokemon2.setOnClickListener {
            selectedPokemon = "Squirtle"
            selectedPokemonelement = "water"
            startDialogue2()
        }
        pokemon3.setOnClickListener {
            selectedPokemon = "Bulbasaur"
            selectedPokemonelement = "plant"
            startDialogue2()
        }

        gobutton.setOnClickListener {
            val intent = Intent(this, PokemonCenter ::class.java)
            intent.putExtra("pokemon", selectedPokemon)
            startActivity(intent)
        }
    }
}