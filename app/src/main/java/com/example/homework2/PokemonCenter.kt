package com.example.homework2

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PokemonCenter : AppCompatActivity() {

    private val handler = Handler()
    private var charIndex = 0
    private var blinkRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.pokemoncenter)

        val pokemon = intent.getStringExtra("pokemon") ?: "unknown Pokémon"
        val textView = findViewById<TextView>(R.id.textView)
        val nextButton = findViewById<Button>(R.id.answerButton2)
        val blinkIndicator = findViewById<TextView>(R.id.blinkIndicator)

        val dialogues = listOf(
            "Nurse : Hello! Pokemon trainer. Welcome to our Pokemon center!",
            "Nurse : We can heal your Pokemon back to perfect health!",
            "Nurse : Looks like you got a little $pokemon from Oak professor, let's take care of it!"
        )

        var dialogueIndex = 0

        fun showTextAnimated(text: String) {
            charIndex = 0
            textView.text = ""
            blinkIndicator.visibility = TextView.GONE
            handler.removeCallbacksAndMessages(null)

            val runnable = object : Runnable {
                override fun run() {
                    if (charIndex < text.length) {
                        textView.append(text[charIndex].toString())
                        charIndex++
                        handler.postDelayed(this, 50)
                    } else {
                        blinkIndicator.visibility = TextView.VISIBLE
                        blinkRunnable = object : Runnable {
                            override fun run() {
                                blinkIndicator.visibility =
                                    if (blinkIndicator.visibility == TextView.VISIBLE) TextView.INVISIBLE else TextView.VISIBLE
                                handler.postDelayed(this, 500)
                            }
                        }
                        handler.post(blinkRunnable!!)
                    }
                }
            }

            handler.post(runnable)
        }

        showTextAnimated(dialogues[dialogueIndex])
        dialogueIndex++

        nextButton.setOnClickListener {
            if (charIndex < dialogues[dialogueIndex - 1].length) {
                handler.removeCallbacksAndMessages(null)
                textView.text = dialogues[dialogueIndex - 1]
                charIndex = dialogues[dialogueIndex - 1].length
                blinkIndicator.visibility = TextView.VISIBLE
            } else {
                blinkRunnable?.let { handler.removeCallbacks(it) }
                blinkIndicator.visibility = TextView.GONE

                if (dialogueIndex < dialogues.size) {
                    showTextAnimated(dialogues[dialogueIndex])
                    dialogueIndex++
                } else {
                    nextButton.isEnabled = false
                }
            }
        }
    }
}

