package com.example.obese

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.obese.ObeseApiService
import com.example.obese.ObesityResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ObeseApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.13.2.185:3000") // เปลี่ยน IP ให้ตรงกับที่เซิร์ฟเวอร์ Flask รันอยู่
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        apiService = retrofit.create(ObeseApiService::class.java)

        // Set up button click listener
        findViewById<Button>(R.id.button).setOnClickListener {
            val age = findViewById<EditText>(R.id.editTextText).text.toString()
            val gender = findViewById<EditText>(R.id.editTextText2).text.toString()
            val height = findViewById<EditText>(R.id.editTextText3).text.toString()
            val weight = findViewById<EditText>(R.id.editTextText4).text.toString()

            // Call the API
            predictObesity(age, gender, height, weight)
        }
    }

    private fun predictObesity(age: String, gender: String, height: String, weight: String) {
        // แปลงค่า gender จาก text เป็นตัวเลข
        val genderValue = when (gender.lowercase()) {
            "male" -> 0
            "female" -> 1
            else -> {
                Toast.makeText(this, "กรุณาใส่ male หรือ female เท่านั้น", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Call the API
        val call = apiService.predictObesity(age, genderValue.toString(), height, weight)
        call.enqueue(object : Callback<ObesityResponse> {
            override fun onResponse(call: Call<ObesityResponse>, response: Response<ObesityResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.ObesityCategory ?: "Unknown"
                    findViewById<TextView>(R.id.textView2).text = "ผลการทำนาย: $result"
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ObesityResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
