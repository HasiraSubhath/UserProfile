package com.kotlin.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kotlin.mad.R
import com.kotlin.mad.models.AdsModel
import com.google.firebase.database.FirebaseDatabase

class AdsDetailsActivity : AppCompatActivity() {

    private lateinit var tvPId: TextView
    private lateinit var tvPTitle: TextView
    private lateinit var tvPDesc: TextView
    private lateinit var tvPMdate: TextView
    private lateinit var tvPEdate: TextView
    private lateinit var tvPPrice: TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("pId").toString(),
                intent.getStringExtra("pTitle").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("pId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("AdsDB").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, " data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, AdsFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }





    private fun initView() {
        tvPId = findViewById(R.id.tvPId)
        tvPTitle = findViewById(R.id.tvPTitle)
        tvPDesc = findViewById(R.id.tvPDesc)
        tvPMdate = findViewById(R.id.tvPMdate)
        tvPEdate = findViewById(R.id.tvPEdate)
        tvPPrice = findViewById(R.id.tvPPrice)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvPId.text = intent.getStringExtra("pId")
        tvPTitle.text = intent.getStringExtra("pTitle")
        tvPDesc.text = intent.getStringExtra("pDesc")
        tvPMdate.text = intent.getStringExtra("pMdate")
        tvPEdate.text = intent.getStringExtra("pEdate")
        tvPPrice.text = intent.getStringExtra("pPrice")

    }

    private fun openUpdateDialog(
        pId: String,
        pTitle: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_ads_dialog, null)

        mDialog.setView(mDialogView)

        val etPTitle = mDialogView.findViewById<EditText>(R.id.etPTitle)
        val etPDesc = mDialogView.findViewById<EditText>(R.id.etPDesc)
        val etPMdate = mDialogView.findViewById<EditText>(R.id.etPMdate)
        val etPEdate = mDialogView.findViewById<EditText>(R.id.etPEdate)
        val etPPrice = mDialogView.findViewById<EditText>(R.id.etPPrice)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etPTitle.setText(intent.getStringExtra("pTitle").toString())
        etPDesc.setText(intent.getStringExtra("pDesc").toString())
        etPMdate.setText(intent.getStringExtra("pMdate").toString())
        etPEdate.setText(intent.getStringExtra("pEdate").toString())
        etPPrice.setText(intent.getStringExtra("pPrice").toString())

        mDialog.setTitle("Updating $pTitle Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateAdsData(
                pId,
                etPTitle.text.toString(),
                etPDesc.text.toString(),
                etPMdate.text.toString(),
                etPEdate.text.toString(),
                etPPrice.text.toString()

            )

            Toast.makeText(applicationContext, " Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our text views
            tvPTitle.text = etPTitle.text.toString()
            tvPDesc.text = etPDesc.text.toString()
            tvPMdate.text = etPMdate.text.toString()
            tvPEdate.text = etPEdate.text.toString()
            tvPPrice.text = etPPrice.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateAdsData(
        id: String,
        title: String,
        desc: String,
        mdate: String,
        edate: String,
        price: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("AdsDB").child(id)
        val adsInfo = AdsModel(id, title, desc, mdate, edate, price)
        dbRef.setValue(adsInfo)
    }
}