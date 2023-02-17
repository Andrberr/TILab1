package com.example.ti1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val n = 33
    private val alphabet = arrayOf(
        'а',
        'б',
        'в',
        'г',
        'д',
        'е',
        'ё',
        'ж',
        'з',
        'и',
        'й',
        'к',
        'л',
        'м',
        'н',
        'о',
        'п',
        'р',
        'с',
        'т',
        'у',
        'ф',
        'х',
        'ц',
        'ч',
        'ш',
        'щ',
        'ъ',
        'ы',
        'ь',
        'э',
        'ю',
        'я'
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sourceTextView = findViewById<EditText>(R.id.sourceText)
        val keyView = findViewById<EditText>(R.id.key)
        val cypherTextView = findViewById<TextView>(R.id.cypherText)

        val sourceTextViewV = findViewById<EditText>(R.id.sourceTextV)
        val keyViewV = findViewById<EditText>(R.id.keyV)
        val cypherTextViewV = findViewById<TextView>(R.id.cypherTextV)


        findViewById<Button>(R.id.cypherButton).setOnClickListener {
            var sourceText = sourceTextView.text.toString()
            val key = keyView.text.toString().toInt()

            if (checkForMutuallySimple(key)) {
                cypherTextView.text = getCypherText(sourceText, key)
            } else Toast.makeText(this, "k и n не взаимно простые!!!", Toast.LENGTH_LONG).show()

            sourceText = sourceTextViewV.text.toString()
            val keyV = keyViewV.text.toString()
            cypherTextViewV.text = getCypherTextV(sourceText, keyV)
        }
    }

    private fun checkForMutuallySimple(k: Int): Boolean {
        val max = k.coerceAtLeast(n)
        var result = true
        for (i: Int in 2..max) {
            if (k % i == 0 && n % i == 0) {
                result = false
                break
            }
        }
        return result
    }

    private fun getCypherText(sourceText: String, key: Int): String {
        //Ek(i) = (i*k) mod n
        var result = ""
        for (element in sourceText) {
            result += alphabet[(alphabet.indexOf(element) * key) % n]
        }
        return result
    }

    private fun getCypherTextV(sourceText: String, key: String): String {
        val matrix = Array(n) { Array(n) { ' ' } }
        for (i: Int in 0 until n) {
            var j = i
            var k = 0
            while (j < n) {
                matrix[i][k] = alphabet[j]
                j++
                k++
            }

            j = 0
            while (k < n) {
                matrix[i][k] = alphabet[j]
                j++
                k++
            }
        }

        val table = Array(2) { Array(sourceText.length) { ' ' } }
        for (i in sourceText.indices) {
            table[0][i] = sourceText[i]
            table[1][i] = key[i % key.length]
        }

        var result = ""
        for (i in sourceText.indices) {
            result += matrix[alphabet.indexOf(table[1][i])][alphabet.indexOf(table[0][i])]
        }
        return result
    }
}