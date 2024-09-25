package com.ndregt.project.ngopigo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_COFFEE = "extra_coffee"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.tv_label_description).text = getString(R.string.description)
        findViewById<TextView>(R.id.tv_label_ingredients).text = getString(R.string.ingredients)
        findViewById<TextView>(R.id.tv_label_tools).text = getString(R.string.tools)
        findViewById<TextView>(R.id.tv_label_serving_steps).text = getString(R.string.serving_steps)

        loadCoffeeData()
    }

    private fun loadCoffeeData() {
        lifecycleScope.launch(Dispatchers.Default) {
            val coffee = intent.getParcelableExtra<Coffee>(EXTRA_COFFEE)
            coffee?.let {
                withContext(Dispatchers.Main) {
                    displayCoffeeDetails(it)
                    setupShareButton(it)
                }
            }
        }
    }

    private fun displayCoffeeDetails(coffee: Coffee) {
        findViewById<ImageView>(R.id.img_detail_photo).setImageResource(coffee.photo)
        findViewById<TextView>(R.id.tv_detail_name).text = coffee.name
        findViewById<TextView>(R.id.tv_detail_description).text = coffee.description
        findViewById<TextView>(R.id.tv_detail_category).text = coffee.category
        findViewById<TextView>(R.id.tv_detail_ingredients).text = coffee.ingredients
        findViewById<TextView>(R.id.tv_detail_tools).text = coffee.tools
        findViewById<TextView>(R.id.tv_detail_serving_steps).text = coffee.servingSteps

        setCategoryStyle(coffee.category)
    }

    private fun setCategoryStyle(category: String) {
        val tvCategory = findViewById<TextView>(R.id.tv_detail_category)
        val icCategory = findViewById<ImageView>(R.id.ic_detail_category)

        when (category) {
            getString(R.string.hot_category) -> {
                tvCategory.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
                icCategory.setImageResource(R.drawable.ic_hot)
                icCategory.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_orange_light))
            }
            getString(R.string.cold_category) -> {
                tvCategory.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))
                icCategory.setImageResource(R.drawable.ic_cold)
                icCategory.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_blue_light))
            }
        }
    }

    private fun setupShareButton(coffee: Coffee) {
        findViewById<ImageButton>(R.id.action_share).setOnClickListener {
            shareContent(coffee)
        }
    }

    private fun shareContent(coffee: Coffee) {
        val shareText = getString(
            R.string.share_text_header,
            coffee.name,
            coffee.category,
            coffee.description,
            coffee.ingredients,
            coffee.tools,
            coffee.servingSteps
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = getString(R.string.share_type)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject, coffee.name))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)))
    }
}