package com.ndregt.project.ngopigo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish()
        }
        findViewById<RecyclerView>(R.id.rv_about).layoutManager = LinearLayoutManager(this)

        loadData()
    }


    private fun loadData() {
        lifecycleScope.launch(Dispatchers.Default) {
            val items = listOf(
                About(R.drawable.ic_address, getString(R.string.address)) { openUrl(getString(R.string.address_url)) },
                About(R.drawable.ic_univ, getString(R.string.univ)) { openUrl(getString(R.string.univ_url)) },
                About(R.drawable.ic_phone, getString(R.string.phone_number)) { dialPhone(getString(R.string.phone_number)) },
                About(R.drawable.ic_gmail, getString(R.string.email)) { sendEmail(getString(R.string.email)) },
                About(R.drawable.ic_instagram, getString(R.string.instagram)) { openUrl(getString(R.string.instagram_url)) },
                About(R.drawable.ic_linkedin, getString(R.string.linkedin)) { openUrl(getString(R.string.linkedin_url)) },
                About(R.drawable.ic_github, getString(R.string.github)) { openUrl(getString(R.string.github_url)) }
            )

            withContext(Dispatchers.Main) {
                findViewById<RecyclerView>(R.id.rv_about).adapter = AboutAdapter(items)
                Toast.makeText(this@AboutActivity, getString(R.string.about_toast), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(getString(R.string.mailto, email))
        }
        startActivity(intent)
    }

    private fun dialPhone(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(getString(R.string.tel, phoneNumber))
        }
        startActivity(intent)
    }
}