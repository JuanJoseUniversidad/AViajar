package es.uji.al394516.aviajar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.iterator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.uji.al394516.aviajar.classes.Travel

class TravelEditionActivity : AppCompatActivity(), ITravelEdition {

    //variables a recibir de otras activities
    private var editMode : Boolean = false
    private var currentTravel: Travel? = null

    //referencias del layout
    private lateinit var nameText: EditText
    private lateinit var placeText: EditText

    private lateinit var anadirPersona: Button
    private lateinit var personasScroll: ScrollView
    private lateinit var anadirGasto: Button
    private lateinit var gastosScroll: ScrollView

    private lateinit var precioTotalText: TextView

    private lateinit var anadirViaje: FloatingActionButton
    private lateinit var editViaje: FloatingActionButton
    private lateinit var deleteViaje: FloatingActionButton

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
        currentTravel = intent.extras!!.get("CurrentTravel") as Travel?

        //referenciar los objetos del layout
        nameText = findViewById(R.id.nameEditText)
        placeText = findViewById(R.id.placeEditText)

        anadirPersona = findViewById(R.id.anadirPersonaButton)
        personasScroll = findViewById(R.id.personasScrollView)
        anadirGasto = findViewById(R.id.anadirGastoButton)
        gastosScroll = findViewById(R.id.gastosScrollView)

        precioTotalText = findViewById(R.id.precioTotalTextView)

        anadirViaje = findViewById(R.id.anadirViajeActionButton)
        editViaje = findViewById(R.id.editTravelActionButton)
        deleteViaje = findViewById(R.id.deleteTravelActionButton)

        //presenter
        presenter = PresenterTE(this, Model(applicationContext))

        //funciones iniciales
        presenter.travelExists(currentTravel)
    }

    //region ITravelEdition
    /**
     * This function fills all the parameters with the currentTravel data
     */
    override fun fillLayout(){

    }

    /**
     * This function enables/disables the editTexts and shows/hides the editButtons
     * So the user cannot modify the travel data
     * @param canEdit
     */
    override fun canUserModifyTravel(canEdit: Boolean){
        nameText.isEnabled = canEdit
        placeText.isEnabled = canEdit

        if (canEdit) {
            anadirPersona.visibility = View.VISIBLE
            anadirGasto.visibility = View.VISIBLE
            anadirViaje.visibility = View.VISIBLE

            modifyScrollsButtons(View.VISIBLE)

            editViaje.visibility = View.INVISIBLE
            deleteViaje.visibility = View.INVISIBLE
        }
        else{
            anadirPersona.visibility = View.INVISIBLE
            anadirGasto.visibility = View.INVISIBLE
            anadirViaje.visibility = View.INVISIBLE

            modifyScrollsButtons(View.INVISIBLE)

            editViaje.visibility = View.VISIBLE
            deleteViaje.visibility = View.VISIBLE
        }
    }
    //endregion

    /**
     * Function that iterates through the scrolls to show/hide the buttons that it has
     * @param invisible is an internal Int
     */
    private fun modifyScrollsButtons(invisible: Int) {
        //personsScroll
        val linearLayoutPersonas: LinearLayout = personasScroll.getChildAt(0) as LinearLayout
        for (card in linearLayoutPersonas){
            val cardLL: LinearLayout = (card as CardView).getChildAt(0) as LinearLayout
            cardLL.getChildAt(1).visibility = invisible
            cardLL.getChildAt(2).visibility = invisible
        }

        //gastosScroll
        val linearLayoutGastos: LinearLayout = gastosScroll.getChildAt(0) as LinearLayout
        for (card in linearLayoutGastos){
            val cardLL: LinearLayout = (card as CardView).getChildAt(0) as LinearLayout
            cardLL.getChildAt(1).visibility = invisible
            cardLL.getChildAt(2).visibility = invisible
        }
    }
}