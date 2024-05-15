package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val arrowBackFromSettings = findViewById<ImageButton>(R.id.arrow_back_from_settings)

        arrowBackFromSettings.setOnClickListener {
          finish()
        }
        val btnShare = findViewById<LinearLayout>(R.id.btn_share)
        btnShare.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.urlYandexAndroid)

            packageManager?.let {
                if (shareIntent.resolveActivity(it) != null)
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_with)))
                else
                    Toast.makeText(this, R.string.nothing, Toast.LENGTH_LONG).show()
            }
        }
        val btnSendToSupport = findViewById<LinearLayout>(R.id.btn_send_to_support)
        btnSendToSupport.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.themeMessage))
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.textEmail))
            startActivity(sendIntent)
        }

        val btnUserAgreement = findViewById<LinearLayout>(R.id.contentDescription)
        btnUserAgreement.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(getString(R.string.linkUserAgreement))
            startActivity(agreementIntent)
        }

    }
}