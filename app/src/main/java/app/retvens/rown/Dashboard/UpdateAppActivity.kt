package app.retvens.rown.Dashboard

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityUpdateAppBinding

class UpdateAppActivity : AppCompatActivity() {
    lateinit var binding : ActivityUpdateAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val updateLink = intent.getStringExtra("updateLink")
        binding.update.setOnClickListener {
            val uri : Uri = Uri.parse("$updateLink")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}