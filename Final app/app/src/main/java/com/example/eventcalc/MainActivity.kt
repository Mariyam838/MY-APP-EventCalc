package com.example.eventcalc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var btnWedding: Button
    private lateinit var btnBirthday: Button
    private lateinit var btnCorporate: Button
    private lateinit var btnBabyShower: Button
    private lateinit var btnOther: Button

    private lateinit var editTextGuests: TextInputEditText
    private lateinit var spinnerStyle: Spinner
    private lateinit var spinnerVenue: Spinner
    private lateinit var spinnerCountry: Spinner
    private lateinit var etCurrency: EditText
    private lateinit var tvCurrencyCode: TextView
    private lateinit var etTotalBudget: EditText

    private lateinit var seekBarVenue: SeekBar
    private lateinit var seekBarFood: SeekBar
    private lateinit var seekBarDecor: SeekBar
    private lateinit var seekBarAttire: SeekBar
    private lateinit var seekBarPhotography: SeekBar
    private lateinit var seekBarEntertainment: SeekBar
    private lateinit var seekBarMisc: SeekBar

    private lateinit var tvVenuePercentage: TextView
    private lateinit var tvFoodPercentage: TextView
    private lateinit var tvDecorPercentage: TextView
    private lateinit var tvAttirePercentage: TextView
    private lateinit var tvPhotographyPercentage: TextView
    private lateinit var tvEntertainmentPercentage: TextView
    private lateinit var tvMiscPercentage: TextView

    private lateinit var etVenueBudget: EditText
    private lateinit var etFoodBudget: EditText
    private lateinit var etDecorBudget: EditText
    private lateinit var etAttireBudget: EditText
    private lateinit var etPhotographyBudget: EditText
    private lateinit var etEntertainmentBudget: EditText
    private lateinit var etMiscBudget: EditText

    private lateinit var btnCalculate: Button

    private var selectedEventType = "Wedding"
    private var totalBudget = 10000

    private val countryCurrencyMap = mapOf(
        "United Arab Emirates" to "AED",
        "United States" to "USD",
        "United Kingdom" to "GBP",
        "Euro Zone" to "EUR",
        "India" to "INR",
        "Australia" to "AUD",
        "Canada" to "CAD",
        "Japan" to "JPY",
        "China" to "CNY",
        "Singapore" to "SGD"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupEventTypeButtons()
        setupCountrySpinner()
        setupBudgetEditTexts()
        setupSeekBars()
        setupCalculateButton()
    }

    private fun initializeViews() {
        btnWedding = findViewById(R.id.btnWedding)
        btnBirthday = findViewById(R.id.btnBirthday)
        btnCorporate = findViewById(R.id.btnCorporate)
        btnBabyShower = findViewById(R.id.btnBabyShower)
        btnOther = findViewById(R.id.btnOther)

        editTextGuests = findViewById(R.id.editTextGuests)
        spinnerStyle = findViewById(R.id.spinnerStyle)
        spinnerVenue = findViewById(R.id.spinnerVenue)

        spinnerCountry = findViewById(R.id.spinnerCountry)
        etCurrency = findViewById(R.id.etCurrency)
        tvCurrencyCode = findViewById(R.id.tvCurrencyCode)
        etTotalBudget = findViewById(R.id.etTotalBudget)

        tvVenuePercentage = findViewById(R.id.tvVenuePercentage)
        tvFoodPercentage = findViewById(R.id.tvFoodPercentage)
        tvDecorPercentage = findViewById(R.id.tvDecorPercentage)
        tvAttirePercentage = findViewById(R.id.tvAttirePercentage)
        tvPhotographyPercentage = findViewById(R.id.tvPhotographyPercentage)
        tvEntertainmentPercentage = findViewById(R.id.tvEntertainmentPercentage)
        tvMiscPercentage = findViewById(R.id.tvMiscPercentage)

        seekBarVenue = findViewById(R.id.seekBarVenue)
        seekBarFood = findViewById(R.id.seekBarFood)
        seekBarDecor = findViewById(R.id.seekBarDecor)
        seekBarAttire = findViewById(R.id.seekBarAttire)
        seekBarPhotography = findViewById(R.id.seekBarPhotography)
        seekBarEntertainment = findViewById(R.id.seekBarEntertainment)
        seekBarMisc = findViewById(R.id.seekBarMisc)

        etVenueBudget = findViewById(R.id.etVenueBudget)
        etFoodBudget = findViewById(R.id.etFoodBudget)
        etDecorBudget = findViewById(R.id.etDecorBudget)
        etAttireBudget = findViewById(R.id.etAttireBudget)
        etPhotographyBudget = findViewById(R.id.etPhotographyBudget)
        etEntertainmentBudget = findViewById(R.id.etEntertainmentBudget)
        etMiscBudget = findViewById(R.id.etMiscBudget)

        btnCalculate = findViewById(R.id.btnCalculate)
    }

    private fun setupEventTypeButtons() {
        val eventButtons = listOf(btnWedding, btnBirthday, btnCorporate, btnBabyShower, btnOther)
        setSelectedButton(btnWedding)

        for (button in eventButtons) {
            button.setOnClickListener {
                setSelectedButton(button)
                selectedEventType = button.text.toString()
                setDefaultBudgetDistributionForEventType(selectedEventType)
            }
        }
    }

    private fun setSelectedButton(selectedButton: Button) {
        val buttons = listOf(btnWedding, btnBirthday, btnCorporate, btnBabyShower, btnOther)
        for (button in buttons) {
            if (button == selectedButton) {
                button.setBackgroundResource(R.drawable.button_event_selected)
                button.setTextColor(resources.getColor(android.R.color.white, theme))
            } else {
                button.setBackgroundResource(R.drawable.button_event_default)
                button.setTextColor(resources.getColor(android.R.color.black, theme))
            }
        }
    }

    private fun setupCountrySpinner() {
        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = parent?.getItemAtPosition(position).toString()
                val currency = countryCurrencyMap[selectedCountry] ?: "Enter Currency"
                etCurrency.setText(currency)
                tvCurrencyCode.text = currency
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                etCurrency.setText("Enter Currency")
                tvCurrencyCode.text = "AED"
            }
        }
    }

    private fun setupBudgetEditTexts() {
        etTotalBudget.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    try {
                        totalBudget = s.toString().toInt()
                        updateBudgetValues()
                    } catch (e: NumberFormatException) {
                        etTotalBudget.setText("10000")
                        totalBudget = 10000
                    }
                }
            }
        })

        setupBudgetEditTextWatcher(etVenueBudget, seekBarVenue, tvVenuePercentage)
        setupBudgetEditTextWatcher(etFoodBudget, seekBarFood, tvFoodPercentage)
        setupBudgetEditTextWatcher(etDecorBudget, seekBarDecor, tvDecorPercentage)
        setupBudgetEditTextWatcher(etAttireBudget, seekBarAttire, tvAttirePercentage)
        setupBudgetEditTextWatcher(etPhotographyBudget, seekBarPhotography, tvPhotographyPercentage)
        setupBudgetEditTextWatcher(etEntertainmentBudget, seekBarEntertainment, tvEntertainmentPercentage)
        setupBudgetEditTextWatcher(etMiscBudget, seekBarMisc, tvMiscPercentage)
    }

    private fun setupBudgetEditTextWatcher(editText: EditText, seekBar: SeekBar, percentageText: TextView) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (editText.hasFocus()) {
                    if (!s.isNullOrEmpty()) {
                        try {
                            val budgetValue = s.toString().toInt()
                            val percentage = ((budgetValue.toFloat() / totalBudget.toFloat()) * 100).roundToInt()
                            val clampedPercentage = percentage.coerceIn(0, 100)
                            seekBar.progress = clampedPercentage
                            percentageText.text = "$clampedPercentage%"
                        } catch (e: NumberFormatException) {
                            editText.setText("0")
                        }
                    }
                }
            }
        })
    }

    private fun setupSeekBars() {
        setupSeekBarListener(seekBarVenue, etVenueBudget, tvVenuePercentage)
        setupSeekBarListener(seekBarFood, etFoodBudget, tvFoodPercentage)
        setupSeekBarListener(seekBarDecor, etDecorBudget, tvDecorPercentage)
        setupSeekBarListener(seekBarAttire, etAttireBudget, tvAttirePercentage)
        setupSeekBarListener(seekBarPhotography, etPhotographyBudget, tvPhotographyPercentage)
        setupSeekBarListener(seekBarEntertainment, etEntertainmentBudget, tvEntertainmentPercentage)
        setupSeekBarListener(seekBarMisc, etMiscBudget, tvMiscPercentage)
    }

    private fun setupSeekBarListener(seekBar: SeekBar, budgetEdit: EditText, percentageText: TextView) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                percentageText.text = "$progress%"
                val budgetValue = ((progress.toFloat() / 100) * totalBudget).roundToInt()
                if (!budgetEdit.hasFocus()) {
                    budgetEdit.setText(budgetValue.toString())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setDefaultBudgetDistributionForEventType(eventType: String) {
        when (eventType) {
            "Wedding" -> {
                updateSeekBar(seekBarVenue, 30)
                updateSeekBar(seekBarFood, 40)
                updateSeekBar(seekBarDecor, 10)
                updateSeekBar(seekBarAttire, 5)
                updateSeekBar(seekBarPhotography, 7)
                updateSeekBar(seekBarEntertainment, 5)
                updateSeekBar(seekBarMisc, 3)
            }
            "Birthday" -> {
                updateSeekBar(seekBarVenue, 25)
                updateSeekBar(seekBarFood, 35)
                updateSeekBar(seekBarDecor, 15)
                updateSeekBar(seekBarAttire, 5)
                updateSeekBar(seekBarPhotography, 10)
                updateSeekBar(seekBarEntertainment, 7)
                updateSeekBar(seekBarMisc, 3)
            }
            "Corporate" -> {
                updateSeekBar(seekBarVenue, 35)
                updateSeekBar(seekBarFood, 30)
                updateSeekBar(seekBarDecor, 5)
                updateSeekBar(seekBarAttire, 0)
                updateSeekBar(seekBarPhotography, 5)
                updateSeekBar(seekBarEntertainment, 20)
                updateSeekBar(seekBarMisc, 5)
            }
            "Baby Shower" -> {
                updateSeekBar(seekBarVenue, 20)
                updateSeekBar(seekBarFood, 30)
                updateSeekBar(seekBarDecor, 25)
                updateSeekBar(seekBarAttire, 5)
                updateSeekBar(seekBarPhotography, 10)
                updateSeekBar(seekBarEntertainment, 5)
                updateSeekBar(seekBarMisc, 5)
            }
            "Other" -> {
                updateSeekBar(seekBarVenue, 30)
                updateSeekBar(seekBarFood, 30)
                updateSeekBar(seekBarDecor, 10)
                updateSeekBar(seekBarAttire, 5)
                updateSeekBar(seekBarPhotography, 10)
                updateSeekBar(seekBarEntertainment, 10)
                updateSeekBar(seekBarMisc, 5)
            }
        }

        updateBudgetValues()
    }

    private fun updateSeekBar(seekBar: SeekBar, progress: Int) {
        seekBar.progress = progress
    }

    private fun updateBudgetValues() {
        val percentages = listOf(
            seekBarVenue.progress,
            seekBarFood.progress,
            seekBarDecor.progress,
            seekBarAttire.progress,
            seekBarPhotography.progress,
            seekBarEntertainment.progress,
            seekBarMisc.progress
        )
        val budgetFields = listOf(
            etVenueBudget,
            etFoodBudget,
            etDecorBudget,
            etAttireBudget,
            etPhotographyBudget,
            etEntertainmentBudget,
            etMiscBudget
        )
        val percentageTexts = listOf(
            tvVenuePercentage,
            tvFoodPercentage,
            tvDecorPercentage,
            tvAttirePercentage,
            tvPhotographyPercentage,
            tvEntertainmentPercentage,
            tvMiscPercentage
        )

        for (i in percentages.indices) {
            budgetFields[i].setText(((percentages[i].toFloat() / 100) * totalBudget).roundToInt().toString())
            percentageTexts[i].text = "${percentages[i]}%"
        }
    }

    private fun setupCalculateButton() {
        btnCalculate.setOnClickListener {
            val totalPercentage = seekBarVenue.progress + seekBarFood.progress +
                    seekBarDecor.progress + seekBarAttire.progress +
                    seekBarPhotography.progress + seekBarEntertainment.progress +
                    seekBarMisc.progress

            if (totalPercentage != 100) {
                Toast.makeText(this, "Budget allocations must total 100%. Current total: $totalPercentage%",
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val numGuests = editTextGuests.text.toString().toIntOrNull() ?: 0
            val eventStyle = spinnerStyle.selectedItem.toString()
            val venueType = spinnerVenue.selectedItem.toString()

            val venueBudget = etVenueBudget.text.toString().toIntOrNull() ?: 0
            val foodBudget = etFoodBudget.text.toString().toIntOrNull() ?: 0
            val decorBudget = etDecorBudget.text.toString().toIntOrNull() ?: 0
            val attireBudget = etAttireBudget.text.toString().toIntOrNull() ?: 0
            val photographyBudget = etPhotographyBudget.text.toString().toIntOrNull() ?: 0
            val entertainmentBudget = etEntertainmentBudget.text.toString().toIntOrNull() ?: 0
            val miscBudget = etMiscBudget.text.toString().toIntOrNull() ?: 0
            val currency = tvCurrencyCode.text.toString()

            val perGuestAmount = if (numGuests > 0) (foodBudget.toFloat() / numGuests).roundToInt() else 0

            val budgetSummary = """
                Event Budget Summary:

                Event Type: $selectedEventType
                Event Style: $eventStyle
                Venue Type: $venueType
                Number of Guests: $numGuests

                Budget Breakdown:
                - Venue: $currency $venueBudget
                - Food & Drinks: $currency $foodBudget
                - Decorations: $currency $decorBudget
                - Attire: $currency $attireBudget
                - Photography: $currency $photographyBudget
                - Entertainment: $currency $entertainmentBudget
                - Miscellaneous: $currency $miscBudget

                Total Budget: $currency $totalBudget
                Per Guest (Food): $currency $perGuestAmount
            """.trimIndent()

            Toast.makeText(this, "Budget calculated successfully!", Toast.LENGTH_SHORT).show()
            displayBudgetSummary(budgetSummary)
        }
    }


    private fun displayBudgetSummary(summary: String) {
        val dialogView = layoutInflater.inflate(R.layout.activity_result, null)
        val tvResultSummary = dialogView.findViewById<TextView>(R.id.tvResultSummary)
        tvResultSummary.text = summary

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setNeutralButton("Share") { _, _ ->
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, summary)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Share Budget Summary"))
            }

        dialogBuilder.show()
    }

}


