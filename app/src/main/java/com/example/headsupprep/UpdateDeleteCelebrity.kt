package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateDeleteCelebrity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etTaboo1: EditText
    private lateinit var etTaboo2: EditText
    private lateinit var etTaboo3: EditText

    private lateinit var btDelete: Button
    private lateinit var btUpdate: Button
    private lateinit var btBack: Button

    private val apiInterface by lazy { APIClient().getClient().create(APIInterface::class.java) }

    private var celebrityID = 0

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete_celebrity)

        celebrityID = intent.extras!!.getInt("celebrityID", 0)

        etName = findViewById(R.id.etUpdateName)
        etTaboo1 = findViewById(R.id.etUpdateTaboo1)
        etTaboo2 = findViewById(R.id.etUpdateTaboo2)
        etTaboo3 = findViewById(R.id.etUpdateTaboo3)

        btDelete = findViewById(R.id.btDelete)
        btDelete.setOnClickListener {
            deleteCelebrity()
        }
        btUpdate = findViewById(R.id.btUpdate)
        btUpdate.setOnClickListener {
            if(etName.text.isNotEmpty() && etTaboo1.text.isNotEmpty() &&
                etTaboo2.text.isNotEmpty() && etTaboo3.text.isNotEmpty()){
                updateCelebrity()
            }else{
                Toast.makeText(this, "One or more fields is empty", Toast.LENGTH_LONG).show()
            }
        }
        btBack = findViewById(R.id.btUpdateBack)
        btBack.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait")

        getCelebrity()
    }

    private fun getCelebrity(){
        progressDialog.show()

        apiInterface.getCelebrity(celebrityID).enqueue(object: Callback<Celebrity> {
            override fun onResponse(call: Call<Celebrity>, response: Response<Celebrity>) {
                progressDialog.dismiss()
                val celebrity = response.body()!!
                etName.setText(celebrity.name)
                etTaboo1.setText(celebrity.taboo1)
                etTaboo2.setText(celebrity.taboo2)
                etTaboo3.setText(celebrity.taboo3)
            }

            override fun onFailure(call: Call<Celebrity>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrity, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun updateCelebrity(){
        progressDialog.show()

        apiInterface.updateCelebrity(
            celebrityID,
            Celebrity(
                etName.text.toString(),
                etTaboo1.text.toString(),
                etTaboo2.text.toString(),
                etTaboo3.text.toString(),
                celebrityID
            )).enqueue(object: Callback<Celebrity> {
            override fun onResponse(call: Call<Celebrity>, response: Response<Celebrity>) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrity, "Celebrity Updated", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<Celebrity>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrity, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun deleteCelebrity(){
        progressDialog.show()

        apiInterface.deleteCelebrity(celebrityID).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrity, "Celebrity Deleted", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrity, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })
    }
}