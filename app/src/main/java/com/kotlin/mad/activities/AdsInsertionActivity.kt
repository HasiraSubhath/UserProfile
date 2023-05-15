package com.kotlin.mad.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.kotlin.mad.models.AdsModel
import com.kotlin.mad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdsInsertionActivity : AppCompatActivity() {



    private lateinit var etPTitle: EditText
    private lateinit var etPDesc: EditText
    private lateinit var etPMdate: EditText
    private lateinit var etPEdate: EditText
    private lateinit var etPPrice: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads_insertion)

        etPTitle = findViewById(R.id.etPTitle)
        etPDesc = findViewById(R.id.etPDesc)
        etPMdate = findViewById(R.id.etPMdate)
        etPEdate = findViewById(R.id.etPEdate)
        etPPrice = findViewById(R.id.etPPrice)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("AdsDB")

        btnSaveData.setOnClickListener {
            saveAdsData()
        }

    }

    private fun saveAdsData() {

        //Geting Values
        val pTitle = etPTitle.text.toString()
        val pDesc = etPDesc.text.toString()
        val pMdate = etPMdate.text.toString()
        val pEdate = etPEdate.text.toString()
        val pPrice = etPPrice.text.toString()

        //validation
        if (pTitle.isEmpty() || pDesc.isEmpty() || pMdate.isEmpty() || pEdate.isEmpty() || pPrice.isEmpty()) {

            if (pTitle.isEmpty()) {
                etPTitle.error = "Please enter Title"
            }
            if (pDesc.isEmpty()) {
                etPDesc.error = "Please Description"
            }
            if (pMdate.isEmpty()) {
                etPMdate.error = "Please Enter Manufacture Date"
            }
            if (pEdate.isEmpty()) {
                etPEdate.error = "Please Enter Expire year month"
            }
            if (pEdate.isEmpty()) {
                etPPrice.error = "Please Enter Price"
            }
            Toast.makeText(this, "please check Some areas are not filled", Toast.LENGTH_LONG).show()
        } else {

            //genrate unique ID
            val pId = dbRef.push().key!!

            val payment = AdsModel(pId, pTitle, pDesc, pMdate, pEdate, pPrice)

            dbRef.child(pId).setValue(payment)
                .addOnCompleteListener {
                    Toast.makeText(this, "Details insert successfully", Toast.LENGTH_SHORT).show()

                    //clear data after insert
                    etPTitle.text.clear()
                    etPDesc.text.clear()
                    etPMdate.text.clear()
                    etPEdate.text.clear()
                    etPPrice.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}