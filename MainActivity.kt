package com.example.simplecalculator

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var firstNumber = 0.0
    private var pendingOperation: String? = null
    private var newNumber = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener { button ->
                writeNumber((button as Button).text.toString())
            }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { selectOperation("+") }
        findViewById<Button>(R.id.btnSub).setOnClickListener { selectOperation("-") }
        findViewById<Button>(R.id.btnMul).setOnClickListener { selectOperation("*") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { selectOperation("/") }

        findViewById<Button>(R.id.btnEq).setOnClickListener { calculate() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clear() }

        findViewById<Button>(R.id.btnSin).setOnClickListener { trig("sin") }
        findViewById<Button>(R.id.btnCos).setOnClickListener { trig("cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener { trig("tan") }
    }

    private fun writeNumber(number: String) {
        if (newNumber || display.text.toString() == "0") {
            display.text = number
            newNumber = false
        } else {
            display.append(number)
        }
    }

    private fun selectOperation(operation: String) {
        // If there is already an operation, execute it first.
        // This makes the calculator work from left to right.
        if (pendingOperation != null && !newNumber) {
            calculate()
        }
        firstNumber = display.text.toString().toDouble()
        pendingOperation = operation
        newNumber = true
    }

    private fun calculate() {
        val operation = pendingOperation ?: return
        val secondNumber = display.text.toString().toDouble()

        val result = when (operation) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> {
                if (secondNumber == 0.0) {
                    display.text = "Error"
                    pendingOperation = null
                    newNumber = true
                    return
                }
                firstNumber / secondNumber
            }
            else -> secondNumber
        }

        showResult(result)
        pendingOperation = null
        newNumber = true
    }

    private fun trig(type: String) {
        val value = display.text.toString().toDoubleOrNull() ?: return
        val degreesSelected = findViewById<RadioButton>(R.id.degrees).isChecked
        val angle = if (degreesSelected) Math.toRadians(value) else value

        val result = when (type) {
            "sin" -> sin(angle)
            "cos" -> cos(angle)
            "tan" -> tan(angle)
            else -> value
        }

        showResult(result)
        newNumber = true
    }

    private fun clear() {
        display.text = "0"
        firstNumber = 0.0
        pendingOperation = null
        newNumber = true
    }

    private fun showResult(value: Double) {
        display.text = if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }
}
