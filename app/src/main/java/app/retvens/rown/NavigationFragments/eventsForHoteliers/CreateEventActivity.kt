package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetEventCategory
import app.retvens.rown.databinding.ActivityCreateEventBinding

class CreateEventActivity : AppCompatActivity(), BottomSheetEventCategory.OnBottomEcClickListener {
    lateinit var binding : ActivityCreateEventBinding

    var category_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileBackBtn.setOnClickListener { onBackPressed() }

        val location = intent.getStringExtra("location")
        val venue = intent.getStringExtra("venue")

        binding.eventCategory.setOnClickListener {
            val bottomSheet = BottomSheetEventCategory()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetEventCategory.EC_TAG)}
            bottomSheet.setOnECclickListener(this)
        }
        binding.nextEventCreate.setOnClickListener {
            if (binding.eventNameEdit.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter Title", Toast.LENGTH_SHORT).show()
            } else if (binding.eventDescriptionEdit.text.toString().isEmpty() || binding.eventCategory.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Description", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(applicationContext, AddEventDeatailsActivity::class.java)
                intent.putExtra("location", location)
                intent.putExtra("venue", venue)
                intent.putExtra("category_id", category_id)
                intent.putExtra("title", binding.eventNameEdit.text.toString())
                intent.putExtra("description", binding.eventDescriptionEdit.text.toString())
                intent.putExtra("eventCategory", binding.eventCategory.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }
    override fun bottomEcClick(eventC: String, NumericCodeFrBo: String) {
        binding.eventCategory.setText(eventC)
        category_id = NumericCodeFrBo
    }
}