package com.ndregt.project.ngopigo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

@Suppress("DEPRECATION")
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

        val coffee = intent.getParcelableExtra<Coffee>(EXTRA_COFFEE)

        if (coffee != null) {
            val imgPhoto: ImageView = findViewById(R.id.img_detail_photo)
            val tvName: TextView = findViewById(R.id.tv_detail_name)
            val tvDescription: TextView = findViewById(R.id.tv_detail_description)
            val tvCategory: TextView = findViewById(R.id.tv_detail_category)
            val icCategory: ImageView = findViewById(R.id.ic_detail_category)
            val tvIngredients: TextView = findViewById(R.id.tv_detail_ingredients)
            val tvTools: TextView = findViewById(R.id.tv_detail_tools)
            val tvServingSteps: TextView = findViewById(R.id.tv_detail_serving_steps)

            findViewById<TextView>(R.id.tv_label_description).text = getString(R.string.description)
            findViewById<TextView>(R.id.tv_label_ingredients).text = getString(R.string.ingredients)
            findViewById<TextView>(R.id.tv_label_tools).text = getString(R.string.tools)
            findViewById<TextView>(R.id.tv_label_serving_steps).text = getString(R.string.serving_steps)

            imgPhoto.setImageResource(coffee.photo)
            tvName.text = coffee.name
            tvDescription.text = coffee.description
            tvCategory.text = coffee.category
            tvIngredients.text = coffee.ingredients
            tvTools.text = coffee.tools
            tvServingSteps.text = coffee.servingSteps

            val context = this

            when (coffee.category) {
                context.getString(R.string.hot_category) -> {
                    tvCategory.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
                    icCategory.setImageResource(R.drawable.ic_hot)
                    icCategory.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_orange_light))

                }
                context.getString(R.string.cold_category) -> {
                    tvCategory.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))
                    icCategory.setImageResource(R.drawable.ic_cold)
                    icCategory.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_blue_light))
                }
            }

            findViewById<ImageButton>(R.id.action_share).setOnClickListener {
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

                startActivity(Intent.createChooser(shareIntent,
                    getString(R.string.share_chooser_title)))
            }
        }
    }
}