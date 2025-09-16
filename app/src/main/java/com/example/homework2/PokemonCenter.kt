package com.example.homework2

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
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
        val element = intent.getStringExtra("element") ?: "None"
        val subelement = intent.getStringExtra("subelement") ?: "None"
        val textView = findViewById<TextView>(R.id.textView)
        val pokemonname = findViewById<TextView>(R.id.pokemonname)
        val nextButton = findViewById<Button>(R.id.answerButton2)
        val goButton = findViewById<Button>(R.id.gobutton2)
        val blinkIndicator = findViewById<TextView>(R.id.blinkIndicator) //arrow blink
        val statsPanel = findViewById<LinearLayout>(R.id.pokemonStatsPanel) // stats box for pokemon
        val elementBubble = findViewById<TextView>(R.id.mainElementText) //element bubble in the linear layout
        val subelementBubble = findViewById<TextView>(R.id.subElementText) //sub element bubble in the linear layout
        val pokemonImage = findViewById<ImageView>(R.id.pokemonpicture)
        val pokemonDrawables = mapOf(
            "Charmander" to R.drawable.charmander,
            "Squirtle" to R.drawable.squirtle,
            "Bulbasaur" to R.drawable.bulbasaur
        )
        val button3 = findViewById<Button>(R.id.button3)

        goButton.visibility = View.GONE
        statsPanel.visibility = View.GONE

        val dialogues = listOf(
            "Nurse : Hello! Pokemon trainer. Welcome to our Pokemon center!",
            "Nurse : We can heal your Pokemon back to perfect health!",
            "Nurse : Looks like you got a little $pokemon from Oak professor.",
            ""
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
                        blinkIndicator.visibility = View.VISIBLE
                        blinkRunnable = object : Runnable {
                            override fun run() {
                                blinkIndicator.visibility =
                                    if (blinkIndicator.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
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
                blinkIndicator.visibility = View.VISIBLE
            } else {
                blinkRunnable?.let { handler.removeCallbacks(it) }
                blinkIndicator.visibility = View.GONE

                if (dialogueIndex < dialogues.size) {
                    showTextAnimated(dialogues[dialogueIndex])
                    dialogueIndex++

                    // If this is the last dialogue, switch buttons
                    if (dialogueIndex == dialogues.size) {
                        nextButton.visibility = View.GONE
                        goButton.visibility = View.VISIBLE
                    }
                }
            }
        }

        //element bubble color logic
        fun setMainElementBubbleColor(element: String?) {
            val colormain = when (element?.lowercase()) {
                "fire" -> 0xFFFFA500.toInt()  // Orange
                "water" -> 0xFF1E90FF.toInt() // Blue
                "plant", "grass" -> 0xFF32CD32.toInt() // Green
                "electric" -> 0xFFFFFF00.toInt() // Yellow
                "ice" -> 0xFF00FFFF.toInt() // Cyan
                "poison" -> 0xFF4B0076.toInt() //purple
                else -> 0xFFFFFFFF.toInt() // Default white
            }
            val background = elementBubble.background as GradientDrawable
            background.mutate()
            background.setColor(colormain)
        }
        fun setSubElementBubbleColor(subelement: String?) {
            val colorsub = when (subelement?.lowercase()) {
                "fire" -> 0xFFFFA500.toInt()  // Orange
                "water" -> 0xFF1E90FF.toInt() // Blue
                "plant", "grass" -> 0xFF32CD32.toInt() // Green
                "electric" -> 0xFFFFFF00.toInt() // Yellow
                "ice" -> 0xFF00FFFF.toInt() // Cyan
                "poison" -> 0xFF4B0076.toInt() //purple
                else -> 0xFFFFFFFF.toInt() // Default white
            }
            val background = subelementBubble.background as GradientDrawable
            background.mutate()
            background.setColor(colorsub)
        }

        // Show stats panel when goButton is clicked
        goButton.setOnClickListener {
            dialogueIndex = 3
            pokemonname.text = pokemon
            elementBubble.text =  element
            subelementBubble.text = subelement
            setMainElementBubbleColor(element)
            setSubElementBubbleColor(subelement)
            pokemonImage.setImageResource(pokemonDrawables[pokemon] ?: R.drawable.charmander)
            goButton.visibility = View.GONE
            statsPanel.visibility = View.VISIBLE
            textView.text = "You can set your pokemon stat by sliding the bar. Remember! the higher stat you set, the more cost it need to be treated."
        }

        //Stats bar activity
        val hpSeekBar = findViewById<SeekBar>(R.id.hpSeekBar)
        val atkSeekBar = findViewById<SeekBar>(R.id.atkSeekBar)
        val defSeekBar = findViewById<SeekBar>(R.id.defSeekBar)
        val spdSeekBar = findViewById<SeekBar>(R.id.spdSeekBar)
        val hpValueText = findViewById<TextView>(R.id.hpValueText)
        val atkValueText = findViewById<TextView>(R.id.atkValueText)
        val defValueText = findViewById<TextView>(R.id.defValueText)
        val spdValueText = findViewById<TextView>(R.id.spdValueText)
        hpValueText.text = "${hpSeekBar.progress}/${hpSeekBar.max}"
        atkValueText.text = "${atkSeekBar.progress}/${atkSeekBar.max}"
        defValueText.text = "${defSeekBar.progress}/${defSeekBar.max}"
        spdValueText.text = "${spdSeekBar.progress}/${spdSeekBar.max}"

        //HP
        hpSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                hpValueText.text = "$progress/${seekBar?.max}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        //ATK
        atkSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                atkValueText.text = "$progress/${seekBar?.max}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        //DEF
        defSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                defValueText.text = "$progress/${seekBar?.max}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        //SPD
        spdSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                spdValueText.text = "$progress/${seekBar?.max}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })


        button3.setOnClickListener{
            val intent = Intent(this, Pokedex ::class.java)
            intent.putExtra("pokemon", pokemon)
            intent.putExtra("element", element)
            intent.putExtra("subelement", subelement)
            intent.putExtra("HP", hpValueText.text.toString())
            intent.putExtra("ATK", atkValueText.text.toString())
            intent.putExtra("DEF", defValueText.text.toString())
            intent.putExtra("SPD", spdValueText.text.toString())
            startActivity(intent)
        }

    }
}
