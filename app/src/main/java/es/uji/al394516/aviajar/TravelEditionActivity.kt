package es.uji.al394516.aviajar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.iterator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.uji.al394516.aviajar.classes.Person
import es.uji.al394516.aviajar.classes.Travel
import es.uji.al394516.aviajar.dialogs.AddPersonDialog
import es.uji.al394516.aviajar.dialogs.IDialogsFunctions

class TravelEditionActivity : AppCompatActivity(), ITravelEdition, IDialogsFunctions {

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

        //eventos
        anadirPersona.setOnClickListener {
            createAddPersonDialog("Añadir persona")
        }

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
        nameText.setText(currentTravel!!.name)
        placeText.setText(currentTravel!!.place)

        fillPersonasScroll()
        
        //TODO("Lo que pone debajo")
        //fillGastosScroll()

        //set precioTotalText

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

    override fun createAddPersonDialog(title:String, personLayout:View?) {
        val apDialog: AddPersonDialog = AddPersonDialog(title, personLayout)
        apDialog.show(supportFragmentManager,"addperson")
    }

    override fun createAlertDialog(title: String, text: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
        dialog.setMessage(text)
        dialog.setPositiveButton("OK") { dialog, which ->dialog.dismiss()}
        dialog.show()
    }

    override fun <T> createConfirmationDialog(title: String, text: String, parameter: T?,function: ((personLayout: T?) -> Unit)?) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
        dialog.setMessage(text)
        dialog.setPositiveButton("OK") { dialog, which ->
            if(function != null && parameter != null){
                function(parameter)
            }
            dialog.dismiss()
        }

        dialog.setNegativeButton("Cancel") { dialog, which ->dialog.dismiss()}
        dialog.show()
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

    /**
     * Function that fill the personasScroll with the "Personas" that go to the travel
     */
    private fun fillPersonasScroll(){
        for (person in currentTravel!!.people){
            onAccept(person.name, null, true)
        }
    }

    override fun onAccept(text: String, personLayout:View?, internalUse: Boolean) {
        //Insert a new person on the scroll view
        val inflater = LayoutInflater.from(this)

        val linearLayout = personasScroll.findViewById<LinearLayout>(R.id.linearLayPersona)
        if(personLayout == null) {
            val customLayout: View = inflater.inflate(R.layout.person_scrollview_layout, linearLayout, false)
            customLayout.findViewById<TextView>(R.id.personName).text = text

            //Edit person
            customLayout.findViewById<FloatingActionButton>(R.id.editPerson).setOnClickListener({
                createAddPersonDialog("Editar persona",customLayout)
            })

            //Delete person
            customLayout.findViewById<FloatingActionButton>(R.id.deletePerson).setOnClickListener({
                createConfirmationDialog("Borrar persona", "¿Estas seguro de borrar a esta persona?",customLayout,linearLayout::removeView)
            })

            linearLayout.addView(customLayout)

            //todo generar id de la persona y obtener id del viaje
            presenter.addNewPerson(Person(0,text,0), personLayout.)

            if (!internalUse){
                //Shows dialog to warn the user that a new person its added
                createAlertDialog("Persona añadida","Persona añadida y gastos reiniciados")
            }
        }else{
            personLayout.findViewById<TextView>(R.id.personName).text = text
            //todo generar id de la persona y obtener id del viaje
            //presenter.editPerson(Person(0,text,0), personLayout.)
            //Shows dialog to warn the user that a person its edited
            createAlertDialog("Persona editada","Persona editada con éxito")
        }



        //todo HAcer que inserte la persona en una lista para el model
        //todo hacer que edite la persona en la lista
    }
}