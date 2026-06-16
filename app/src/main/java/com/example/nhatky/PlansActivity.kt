package com.example.nhatky

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class PlansActivity : AppCompatActivity() {
    private lateinit var adapter: PlanAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plans)

        database = AppDatabase.getDatabase(this)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val rvPlans = findViewById<RecyclerView>(R.id.rvPlans)
        val edtTitle = findViewById<EditText>(R.id.edtPlanTitle)
        val edtDesc = findViewById<EditText>(R.id.edtPlanDesc)
        val btnAdd = findViewById<Button>(R.id.btnAddPlan)

        btnBack.setOnClickListener { finish() }

        adapter = PlanAdapter(emptyList(), 
            onUpdate = { plan ->
                lifecycleScope.launch {
                    database.planDao().updatePlan(plan)
                }
            },
            onDelete = { plan ->
                lifecycleScope.launch {
                    database.planDao().deletePlan(plan)
                    loadPlans()
                }
            }
        )

        rvPlans.layoutManager = LinearLayoutManager(this)
        rvPlans.adapter = adapter

        btnAdd.setOnClickListener {
            val title = edtTitle.text.toString()
            val desc = edtDesc.text.toString()

            if (title.isNotEmpty()) {
                lifecycleScope.launch {
                    database.planDao().insertPlan(PlanEntry(title = title, description = desc))
                    runOnUiThread {
                        edtTitle.text.clear()
                        edtDesc.text.clear()
                        Toast.makeText(this@PlansActivity, "Đã thêm kế hoạch!", Toast.LENGTH_SHORT).show()
                    }
                    loadPlans()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập tên kế hoạch", Toast.LENGTH_SHORT).show()
            }
        }

        loadPlans()
    }

    private fun loadPlans() {
        lifecycleScope.launch {
            val plans = database.planDao().getAllPlans()
            runOnUiThread {
                adapter.updateData(plans)
            }
        }
    }
}
