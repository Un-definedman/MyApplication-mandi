package com.harshkethwas.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.RequestBuilder.put
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import org.json.JSONObject
import java.util.Properties

class registration2 : AppCompatActivity() {

    private lateinit var btn: Button


    var fullname:String = ""
    var email:String=""
    var city:String=""
    var contact:String="1234567891"

    lateinit var txtFullName:EditText
    lateinit var txtEmail:EditText
    lateinit var txtCity:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2)

        btn = findViewById(R.id.RBtn)
        txtFullName = findViewById(R.id.fullName)
        txtEmail = findViewById(R.id.emailId)
        txtCity = findViewById(R.id.city)

        btn.setOnClickListener {
            if(validateEntries(txtFullName,txtEmail,txtCity))
            {
                executeMySQLQuery()
            }
            else
            {
                Toast.makeText(this,"Invalid inputs for fullname, email or city",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun validateEntries(txtFullName:EditText,txtEmail:EditText,txtCity:EditText):Boolean
    {
        var valid:Boolean=true

        if(txtFullName.text.toString().isEmpty() || txtCity.text.toString().isEmpty() || txtEmail.text.toString().isEmpty())
        {
            valid = false
        }
        else
        {
            fullname=txtFullName.text.toString().uppercase()
            email = txtEmail.text.toString()
            city = txtCity.text.toString()

        }
        return valid
    }
    private fun executeMySQLQuery() {
        val url = "http://192.168.216.196/mandi/insert_data.php"  // Replace with your actual server URL

        val params = listOf(
            "contact" to contact,
            "fullname" to fullname,
            "email" to email,
            "city" to city
        )

        Fuel.post(url, params).response { request, response, result ->
            when (result) {
                is Result.Success -> {
                    val data = String(response.data)
                    val json = JSONObject(data)

                    val success = json.optString("success", "")
                    val message = json.optString("message", "")

                    if (success == "1") {
                        // Successful insertion, handle as needed
                        Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                        startActivity((Intent(this, HomeFragment::class.java)))
                    } else {
                        // Handle error
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Failure -> {
                    // Handle failure
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}