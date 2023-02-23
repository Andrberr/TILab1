package com.example.ti1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.*


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

    private val matrix = getMatrix()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sourceTextView = findViewById<EditText>(R.id.sourceText)
        val encryptedTextView = findViewById<EditText>(R.id.encryptedText)
        val keyView = findViewById<EditText>(R.id.key)
        val cypherTextView = findViewById<TextView>(R.id.cypherText)
        val decryptionTextView = findViewById<TextView>(R.id.decryptionText)

        val sourceTextViewV = findViewById<EditText>(R.id.sourceTextV)
        val encryptedTextViewV = findViewById<EditText>(R.id.encryptedTextV)
        val keyViewV = findViewById<EditText>(R.id.keyV)
        val cypherTextViewV = findViewById<TextView>(R.id.cypherTextV)
        val decryptionTextViewV = findViewById<TextView>(R.id.decryptionTextV)

        findViewById<Button>(R.id.cypherButton).setOnClickListener {
            var sourceText = getStringInput(sourceTextView.text.toString())
            val strKey = keyView.text.toString()
            var isEmpty = false
            if (strKey.isNotEmpty()) {
                val key = strKey.toInt()
                if (checkForMutuallySimple(key)) {
                    cypherTextView.text = getCypherText(sourceText, key)
                } else Toast.makeText(this, "k и n не взаимно простые!!!", Toast.LENGTH_LONG).show()
            } else isEmpty = true

            val keyV = getStringInput(keyViewV.text.toString())
            if (keyV.isNotEmpty()) {
                sourceText = getStringInput(sourceTextViewV.text.toString())
                cypherTextViewV.text = getCypherTextV(sourceText, keyV)
            } else isEmpty = true
            if (isEmpty) Toast.makeText(this, "Поле с ключом пустое!!!", Toast.LENGTH_LONG).show()
        }

        findViewById<Button>(R.id.decryptionButton).setOnClickListener {
            var encryptedText = getStringInput(encryptedTextView.text.toString())
            val strKey = keyView.text.toString()
            var isEmpty = false
            if (strKey.isNotEmpty()) {
                val key = strKey.toInt()
                if (checkForMutuallySimple(key)) {
                    decryptionTextView.text = getDecryptionText(encryptedText, key)
                } else Toast.makeText(this, "k и n не взаимно простые!!!", Toast.LENGTH_LONG).show()
            } else isEmpty = true

            val keyV = getStringInput(keyViewV.text.toString())
            if (keyV.isNotEmpty()) {
                encryptedText = getStringInput(encryptedTextViewV.text.toString())
                decryptionTextViewV.text = getDecryptionTextV(encryptedText, keyV)
            } else isEmpty = true

            if (isEmpty) Toast.makeText(this, "Поле с ключом пустое!!!", Toast.LENGTH_LONG).show()
        }

        findViewById<ImageButton>(R.id.sourceDown).setOnClickListener {
            readFromFile("source.txt", sourceTextView)
        }

        findViewById<ImageButton>(R.id.shirfDown).setOnClickListener {
            readFromFile("shifr.txt", encryptedTextView)
        }

        findViewById<ImageButton>(R.id.sourceDownV).setOnClickListener {
            readFromFile("sourceV.txt", sourceTextViewV)
        }

        findViewById<ImageButton>(R.id.shifrDownV).setOnClickListener {
            readFromFile("shifrV.txt", encryptedTextViewV)
        }

        findViewById<ImageButton>(R.id.sourceUp).setOnClickListener {
            File(filesDir, "shifr.txt").writeText(cypherTextView.text.toString())
            File(filesDir, "shifrV.txt").writeText(cypherTextViewV.text.toString())
        }

        findViewById<ImageButton>(R.id.shifrUp).setOnClickListener {
            File(filesDir, "source.txt").writeText(decryptionTextView.text.toString())
            File(filesDir, "sourceV.txt").writeText(decryptionTextViewV.text.toString())
        }
    }

    private fun getStringInput(text: String): String {
        var input = ""
        for (i in text.indices) {
            if (alphabet.indexOf(text[i]) != -1 || (text[i] in 'А'..'Я') || text[i] == 'Ё') {
                input += text[i].lowercase()
            }
        }
        return input
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

    private fun getDecryptionText(cypherText: String, key: Int): String {
        var result = ""
        for (element in cypherText) {
            result += alphabet[(alphabet.indexOf(element) / key) % n]
        }
        return result
    }

    private fun getMatrix(): Array<Array<Char>> {
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
        return matrix
    }

    private fun getCypherTextV(sourceText: String, key: String): String {
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

    private fun getDecryptionTextV(cypherText: String, key: String): String {
        var result = ""
        for (i in cypherText.indices) {
            val ind = alphabet.indexOf(key[i % key.length])
            for (j in alphabet.indices) {
                if (matrix[ind][j] == cypherText[i]) {
                    result += alphabet[j]
                }
            }
        }
        return result
    }

    private fun readFromFile(file: String, view: EditText) {
        val myInputStream: InputStream
        val output: String
        try {
            myInputStream = assets.open(file)
            val size: Int = myInputStream.available()
            val buffer = ByteArray(size)
            myInputStream.read(buffer)
            output = String(buffer)
            view.setText(output)
        } catch (e: IOException) {
            Toast.makeText(this, "Файл не найден!!!", Toast.LENGTH_LONG).show()
        }
    }
}