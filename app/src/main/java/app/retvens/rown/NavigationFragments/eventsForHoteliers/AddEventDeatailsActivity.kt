package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import app.retvens.rown.R

class AddEventDeatailsActivity : AppCompatActivity() {

    lateinit var next : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event_deatails)

        next = findViewById(R.id.next_Event)
        next.setOnClickListener {
            startActivity(Intent(this, EvenDetailsActivity::class.java))
        }

    }
}