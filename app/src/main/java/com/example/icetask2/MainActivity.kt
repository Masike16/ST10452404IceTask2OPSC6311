/*
ST10452404 Masike
ICE TASK 2
*/

package com.example.icetask2

import android.os.Bundle
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etInput: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var tvOutput: TextView
    private lateinit var tvError: TextView

    // Uses South African Locale for ZAR currency formatting
    // This forces the app to display "R" symbol and correct decimal formatting for South Africa
    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        etInput = findViewById(R.id.etDecimalInput)
        seekBar = findViewById(R.id.sbSlider)
        tvOutput = findViewById(R.id.tvOutput)
        tvError = findViewById(R.id.tvError)

        // Set up SeekBar change listener (real‑time)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Perform calculation on every slider movement
                calculateAndDisplay()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    /**
     * Input Validation Logic & Exception Prevention
     * This method prevents the Null Pointer and Number Format exceptions through:
     * Empty string check: If the input space is empty, there shows a warning and returns early
     * Safe parsing: Uses toDoubleOrNull() instead of toDouble() to handle invalid numbers
     * Null checking: Verifies that the parsed value is not null before calculation.
     */
    private fun calculateAndDisplay() {
        val inputText = etInput.text.toString()

        // Input Validation: Prevent Null Pointer by checking for empty input first
        if (inputText.isEmpty()) {
            tvError.text = "Please enter a value"
            tvError.visibility = TextView.VISIBLE
            tvOutput.text = ""   // Clear output while input is missing
            return
        }

        // Hides error if the validation passes
        tvError.visibility = TextView.GONE

        // Safe Number Parsing: Prevent Number Format Exception using toDoubleOrNull()
        val inputValue = inputText.toDoubleOrNull()
        if (inputValue == null) {
            tvError.text = "Invalid number"
            tvError.visibility = TextView.VISIBLE
            return  // Early return prevents calculation with invalid data
        }


        val sliderProgress = seekBar.progress

        // Calculates result: input * (slider / 100)
        val result = inputValue * (sliderProgress.toDouble() / 100.0)

        // Format as South African Rands using pre-configured Locale
        val formattedResult = currencyFormat.format(result)


        tvOutput.text = formattedResult

        // Conditional Styling Threshold: R500.00
        // Placement choice: After calculation and formatting, but before method end
        // Reasoning: This ensures styling is applied every time the result updates
        if (result > 500.0) {
            // Green color for amounts exceeding R500.00 (positive highlight)
            tvOutput.setTextColor(getColor(android.R.color.holo_green_dark))
        } else {
            // Default black color for amounts R500.00 or below
            tvOutput.setTextColor(getColor(android.R.color.black))
        }
    }
}