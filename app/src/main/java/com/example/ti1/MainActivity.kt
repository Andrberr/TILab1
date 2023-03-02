package com.example.ti1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.util.*


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
            var sourceText = getStringInput(sourceTextView.text.toString(), true)
            val strKey = keyView.text.toString()
            if (strKey.isNotEmpty()) {
                val key = strKey.toInt()
                if (checkForMutuallySimple(key)) {
                    cypherTextView.text =
                        decimationMethod(sourceText, key, true)
                } else Toast.makeText(this, "k и n не взаимно простые!!!", Toast.LENGTH_LONG).show()
            }

            val keyV = getStringInput(keyViewV.text.toString(), false)
            if (keyV.isNotEmpty()) {
                sourceText = getStringInput(sourceTextViewV.text.toString(), true)
                cypherTextViewV.text = getCypherTextV(sourceText, keyV)
            }
        }

        findViewById<Button>(R.id.decryptionButton).setOnClickListener {
            var encryptedText = getStringInput(encryptedTextView.text.toString(), true)
            val strKey = keyView.text.toString()
            if (strKey.isNotEmpty()) {
                val key = strKey.toInt()
                if (checkForMutuallySimple(key)) {
                    decryptionTextView.text = decimationMethod(
                        encryptedText,
                        key,
                        false
                    )
                } else Toast.makeText(this, "k и n не взаимно простые!!!", Toast.LENGTH_LONG).show()
            }

            val keyV = getStringInput(keyViewV.text.toString(), false)
            if (keyV.isNotEmpty()) {
                encryptedText = getStringInput(encryptedTextViewV.text.toString(), true)
                decryptionTextViewV.text = getDecryptionTextV(encryptedText, keyV)
            }
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

    private fun getStringInput(text: String, isSpace: Boolean): String {
        var input = ""
        for (i in text.indices) {
            if (alphabet.indexOf(text[i]) != -1 || (text[i] in 'А'..'Я') || text[i] == 'Ё') {
                input += text[i].lowercase()
            }
            if (isSpace && text[i] == ' ') input += text[i]
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


    private fun getMultiplicativeInverse(key: Int): Int {
        var i = 1
        while ((key * i) % alphabet.size != 1) {
            i++
        }
        return i
    }

    private fun decimationMethod(input: String, key: Int, isEncryption: Boolean): String? {
        val result = StringBuilder()
        val newAlphabet = StringBuilder()

        //создание таблицы подстановки
        if (isEncryption) {
            for (i in alphabet.indices) {
                newAlphabet.append(alphabet[(i * key % alphabet.size)])
            }
        } else {
            val multiplicationInverse = getMultiplicativeInverse(key)
            for (i in alphabet.indices) {
                newAlphabet.append(alphabet[(multiplicationInverse * i % alphabet.size)])
            }
        }
        println(newAlphabet)
        //преобразование исходного текста в соответствии с таблицей подстановки
        for (ch in input) {
            if (alphabet.indexOf(ch) == -1) result.append(ch)
            else result.append(newAlphabet[alphabet.indexOf(ch)])
        }
        return result.toString()
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
        var i=0
        var k = 0
        while (i < sourceText.length) {
            if (alphabet.indexOf(sourceText[i]) != -1) {
                table[0][i] = sourceText[i]
                table[1][i] = key[k % key.length]
                k++
            }
            i++
        }

        var result = ""
        for (i in sourceText.indices) {
            result += if (alphabet.indexOf(sourceText[i]) != -1) matrix[alphabet.indexOf(table[1][i])][alphabet.indexOf(
                table[0][i]
            )]
            else sourceText[i]
        }
        return result
    }

    private fun getDecryptionTextV(cypherText: String, key: String): String {
        var result = ""
        var k=0
        for (i in cypherText.indices) {
            if (alphabet.indexOf(cypherText[i]) == -1) result += cypherText[i]
            else {
                val ind = alphabet.indexOf(key[k % key.length])
                k++
                for (j in alphabet.indices) {
                    if (matrix[ind][j] == cypherText[i]) {
                        result += alphabet[j]
                    }
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