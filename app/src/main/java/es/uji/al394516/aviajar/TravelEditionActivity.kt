package es.uji.al394516.aviajar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import es.uji.al394516.aviajar.classes.Travel

class TravelEditionActivity : AppCompatActivity(), ITravelEdition {

    //variables a recibir de otras activities
    private var editMode : Boolean = false
    private var currentTravel: Travel? = null

    //referencias del layout
    private lateinit var nameText: EditText
    private lateinit var placeText: EditText

    private lateinit var personasScroll: ScrollView
    private lateinit var gastosScroll: ScrollView

    private lateinit var precioTotalText: TextView

    //presenter
    private lateinit var presenter: PresenterTE

    /**
     * Function called when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_edition)

        //obtener datos que nos pasan las activities
        editMode = intent.getBooleanExtra("EditMode", false)
        currentTravel = intent.extras!!.get("CurrentTravel") as Travel

        //referenciar los objetos del layout
        nameText = findViewById(R.id.nameEditText)
        placeText = findViewById(R.id.placeEditText)

        personasScroll = findViewById(R.id.personasScrollView)
        gastosScroll = findViewById(R.id.gastosScrollView)

        precioTotalText = findViewById(R.id.precioTotalTextView)

        //presenter
        presenter = PresenterTE(this, Model(applicationContext))
    }
}