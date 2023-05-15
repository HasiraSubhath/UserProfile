package com.kotlin.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.mad.adapters.AdsAdapter
import com.kotlin.mad.models.AdsModel
import com.kotlin.mad.R
import com.google.firebase.database.*

class AdsFetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var paymentList: ArrayList<AdsModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads_fetching)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        paymentList = arrayListOf<AdsModel>()

        getAdsData()


    }

    private fun getAdsData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("AdsDB")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               paymentList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val paymentData = empSnap.getValue(AdsModel::class.java)
                        paymentList.add(paymentData!!)
                    }
                    val mAdapter = AdsAdapter(paymentList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : AdsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@AdsFetchingActivity, AdsDetailsActivity::class.java)

                            //put extra(passing data to another activity)
                            intent.putExtra("pId", paymentList[position].pId)
                            intent.putExtra("pTitle", paymentList[position].pTitle)
                            intent.putExtra("pDesc", paymentList[position].pDesc)
                            intent.putExtra("pMdate", paymentList[position].pMdate)
                            intent.putExtra("pEdate", paymentList[position].pEdate)
                            intent.putExtra("pPrice", paymentList[position].pPrice)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}