package es.uji.al394516.aviajar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.uji.al394516.aviajar.classes.Travel

class TravelEditionActivity : AppCompatActivity() {

    private var editMode : Boolean = false
    private var currentTravel: Travel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_edition)

        //obtener datos que nos pasan las activities
        editMode = intent.getBooleanExtra("EditMode", false)
        currentTravel = intent.extras!!.get("CurrentTravel") as Travel
    }
}