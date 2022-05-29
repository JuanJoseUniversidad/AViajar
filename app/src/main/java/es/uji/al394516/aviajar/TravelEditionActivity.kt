package es.uji.al394516.aviajar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.iterator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.uji.al394516.aviajar.classes.Person
import es.uji.al394516.aviajar.classes.Expense
import es.uji.al394516.aviajar.classes.Personid
import es.uji.al394516.aviajar.classes.Travel
import es.uji.al394516.aviajar.dialogs.AddGastoDialog
import es.uji.al394516.aviajar.dialogs.AddPersonDialog
import es.uji.al394516.aviajar.dialogs.IDialogsFunctions
import java.util.*

class TravelEditionActivity : AppCompatActivity(), ITravelEdition, IDialogsFunctions {

    //variables a recibir de otras activities
    private var editMode : Boolean = false
    private var currentTravel: Travel? = null

    //datos miembro local
    //TODO("modificar si currentTravel existe")
    private var travelId:Int = 0;

    private var placeName:String = ""

    //referencias del layout
    private lateinit var nameText: EditText
    private lateinit var placeText: AutoCompleteTextView

    private lateinit var anadirPersona: Button
    private lateinit var personasScroll: ScrollView
    private lateinit var anadirGasto: Button
    private lateinit var gastosScroll: ScrollView

    private lateinit var precioTotalText: TextView

    private lateinit var anadirViaje: FloatingActionButton
    private lateinit var editViaje: FloatingActionButton
    private lateinit var deleteViaje: FloatingActionButton

    private lateinit var progressBarNetwork:ProgressBar

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

        //
        if(editMode == true && currentTravel == null){
            //UUID.randomUUID().toString().hashCode(): generates and unique id of 128 and hash it to convert it to an int of 32bits
            //this method avoids collisions.
            //If use UUID.randomUUID().toInt() can produce collisions
            travelId = UUID.randomUUID().toString().hashCode();
        }

        //referenciar los objetos del layout
        nameText = findViewById(R.id.nameEditText)
        placeText = findViewById(R.id.placeAutoText)

        anadirPersona = findViewById(R.id.anadirPersonaButton)
        personasScroll = findViewById(R.id.personasScrollView)
        anadirGasto = findViewById(R.id.anadirGastoButton)
        gastosScroll = findViewById(R.id.gastosScrollView)

        precioTotalText = findViewById(R.id.precioTotalTextView)

        anadirViaje = findViewById(R.id.anadirViajeActionButton)
        editViaje = findViewById(R.id.editTravelActionButton)
        deleteViaje = findViewById(R.id.deleteTravelActionButton)

        progressBarNetwork = findViewById(R.id.progressBarNetwork)

        //eventos
        anadirPersona.setOnClickListener {
            createAddPersonDialog("Añadir persona")
        }

        anadirGasto.setOnClickListener{
            createAddGastoDialog("Añadir gasto", null, null)
        }

        //Set autocompleter event
        placeText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val place = p0.toString()
                var listplaces:List<String> = presenter.places
                listplaces.binarySearch { it.compareTo(place) }.let {
                    if (it >= 0)
                        //añadir a una var aux el nombre del lugar elegido
                        placeName = listplaces[it]
                        //presenter.setChosenIngredient(listplaces[it])
                }
            }
        })        

        //presenter
        presenter = PresenterTE(this, Model(applicationContext))

        //funciones iniciales
        presenter.travelExists(currentTravel)
    }

    //region ITravelEdition
    override var precioTotal: String
        get() = precioTotalText.text.toString()
        set(value) {
            precioTotalText.text = value
        }

    /**
     * Enables and disables the ProgressBar
     * @param enable Int for enable and disable the ProgressBar
     */
    override fun enableProgressBar(enable: Int) {
        progressBarNetwork.visibility = enable;
    }

    /**
     * Shows an message via Toast
     * @param message An String of the message
     */
    override fun showMessage(message: String){
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        toast.show()
    }

    /**
     * Shows the ingredients on the autocompleter
     * @param ingredients An array of objects Ingredients
     */
    override fun showPlaces(ingredients: List<String>){
        val localAdapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, ingredients.toTypedArray())
        placeText.setAdapter(localAdapter)
    }

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

    override fun createAddGastoDialog(title: String, gastoLayout: View?, gasto: Expense?) {
        if (presenter.model.getAuxPeople().size < 1){
            createAlertDialog("FALTA GENTE", "No puedes tener un gasto sin nadie que lo pague")
            return
        }
        val agDialog: AddGastoDialog = AddGastoDialog(title, gastoLayout, presenter.model, gasto)
        agDialog.show(supportFragmentManager, "addgasto")
    }

    override fun createAlertDialog(title: String, text: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
        dialog.setMessage(text)
        dialog.setPositiveButton("OK") { dialog, which ->dialog.dismiss()}
        dialog.show()
    }

    override fun <T> createConfirmationDialog(title: String, text: String, parameter: List<T>?,function: ((personLayout: List<T>?) -> Unit)?) {
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

    //region IDialogsFunctions
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
                createConfirmationDialog("Borrar persona", "¿Estas seguro de borrar a esta persona?", listOf(linearLayout,customLayout), ::removePerson)
            })

            linearLayout.addView(customLayout)

            //UUID.randomUUID().toString().hashCode(): generates and unique id of 128 and hash it to convert it to an int of 32bits
            //this method avoids collisions.
            //If use UUID.randomUUID().toInt() can produce collisions
            presenter.addNewPerson(Person(UUID.randomUUID().toString().hashCode(),text,travelId))
            presenter.debugPersonList()

            if (!internalUse){
                //Shows dialog to warn the user that a new person its added
                createAlertDialog("Persona añadida","Persona añadida y gastos reiniciados")
            }
        }else{
            personLayout.findViewById<TextView>(R.id.personName).text = text
            val index = linearLayout.indexOfChild(personLayout)
            presenter.editPerson(Person(presenter.getPerson(index).id,text,travelId), index)
            presenter.debugPersonList()
            //Shows dialog to warn the user that a person its edited
            createAlertDialog("Persona editada","Persona editada con éxito")
        }
    }

    override fun onOkExpense(name: String, totalPrice: Double, person_expense: MutableMap<Personid, Double>, gastoLayout: View?) {
        //crear gasto y añadir a la lista de gastos
        val newExpense = Expense(name, travelId, totalPrice, person_expense)
        presenter.addNewExpense(newExpense)

        //actualizar scroll
        val inflater = LayoutInflater.from(this)

        val linearLayout = gastosScroll.findViewById<LinearLayout>(R.id.linearLayGasto)
        if(gastoLayout == null) {
            val customLayout: View = inflater.inflate(R.layout.person_scrollview_layout, linearLayout, false)
            customLayout.findViewById<TextView>(R.id.personName).text = name

            //Edit gasto
            customLayout.findViewById<FloatingActionButton>(R.id.editPerson).setOnClickListener({
                createAddGastoDialog("Editar gasto",customLayout, newExpense)
            })

            //Delete gasto
            customLayout.findViewById<FloatingActionButton>(R.id.deletePerson).setOnClickListener({
                createConfirmationDialog("Borrar gasto", "¿Estas seguro de borrar este gasto?", listOf(linearLayout, customLayout), ::removeGasto)
            })

            linearLayout.addView(customLayout)

        }else{
            gastoLayout.findViewById<TextView>(R.id.personName).text = name

            val index = linearLayout.indexOfChild(gastoLayout)
            presenter.editGasto(newExpense, index)
        }

        //actualizar precioTotal
        presenter.setPrecioTotal()

        //mostrar Toast
        var message: String
        if (gastoLayout == null){
            message = "Gasto añadido"
        }else{
            message = "Gasto editado"
        }
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        toast.show()
    }
    //endregion

    private fun <T> removePerson(parameters: List<T>?){
        val linearLayout:LinearLayout = parameters?.get(0) as LinearLayout
        val personLayout: View = parameters?.get(1) as View

        presenter.deletePerson(linearLayout.indexOfChild(personLayout))
        presenter.debugPersonList()

        linearLayout.removeView(personLayout)

        //TODO("reiniciar gastos")
        createAlertDialog("Persona borrada", "Persona borrada y gastos reiniciados");
    }

    private fun <T> removeGasto(parameters: List<T>?){
        val linearLayout: LinearLayout = parameters?.get(0) as LinearLayout
        val gastoLayout: View = parameters?.get(1) as View

        presenter.deleteGasto(gastoLayout, linearLayout)
        presenter.setPrecioTotal()
    }
}