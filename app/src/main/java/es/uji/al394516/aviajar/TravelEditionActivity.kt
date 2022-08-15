package es.uji.al394516.aviajar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
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
    private var travelName: String=""
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

    private lateinit var eeImage: ImageView

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

        eeImage = findViewById(R.id.ee_imageView)

        //mirar si hay un viaje o no
        if(currentTravel != null){
            travelId = currentTravel!!.id
            travelName = currentTravel!!.name
            placeName = currentTravel!!.place
        }

        //eventos
        editViaje.setOnClickListener{
            toEditMode()
        }

        anadirPersona.setOnClickListener {
            createAddPersonDialog(resources.getString(R.string.anadirPersona_str))
        }

        anadirGasto.setOnClickListener{
            createAddGastoDialog(resources.getString(R.string.anadirGasto_str), null, null)
        }

        anadirViaje.setOnClickListener{
            travelName = nameText.text.toString()
            placeName = placeText.text.toString()
            presenter.checkEE(travelName, placeName)
            presenter.insertTravel(travelId,travelName,placeName);
        }

        deleteViaje.setOnClickListener{
            presenter.showDeleteTravel()
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

        //reescalar los scrollview a moviles pequeños
        val displayMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(displayMetrics)
        }

        if (displayMetrics.heightPixels <= 480){
            personasScroll.layoutParams.height = 50
            gastosScroll.layoutParams.height = 50
        }

        //presenter
        presenter = PresenterTE(this, Model(applicationContext))

        //funciones iniciales
        presenter.travelExists(currentTravel,editMode)
    }

    //region ITravelEdition
    override var precioTotal: String
        get() = precioTotalText.text.toString()
        set(value) {
            precioTotalText.text = value
        }

    override var eeImageVisible: Boolean
        get() = eeImage.isVisible
        set(value) {
            eeImage.isVisible = value
        }

    override fun setTravel(travel: Travel) {
        currentTravel = travel
    }

    override fun getTravel(): Travel? {
        return currentTravel
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
        runOnUiThread {
            val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
            toast.show()
        }
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

        fillGastosScroll()

        //set precioTotalText
        presenter.setPrecioTotal()
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
        runOnUiThread {
            val apDialog: AddPersonDialog = AddPersonDialog(title, personLayout)
            apDialog.show(supportFragmentManager, "addperson")
        }
    }

    override fun createAddGastoDialog(title: String, gastoLayout: View?, gasto: Expense?) {
        if (presenter.model.getAuxPeople().size < 1){
            createAlertDialog(resources.getString(R.string.faltaGente_str), resources.getString(R.string.errorFaltaGente))
            return
        }
        val agDialog: AddGastoDialog = AddGastoDialog(title, gastoLayout, presenter.model, gasto)
        agDialog.show(supportFragmentManager, "addgasto")
    }

    override fun createAlertDialog(title: String, text: String, function: (() -> Unit)?, cancelButton:Boolean) {
        runOnUiThread {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle(title)
            dialog.setMessage(text)
            dialog.setPositiveButton(resources.getString(R.string.ok_str)) { dialog, which ->
                if(function != null){
                    function()
                }
                dialog.dismiss()}

            if(cancelButton == true){
                dialog.setNegativeButton(resources.getString(R.string.cancel_str)) { dialog, which ->
                    dialog.dismiss()}
            }

            dialog.show()
        }
    }


    override fun <T> createConfirmationDialog(title: String, text: String, parameter: List<T>?,function: ((personLayout: List<T>?) -> Unit)?) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
        dialog.setMessage(text)
        dialog.setPositiveButton(resources.getString(R.string.ok_str)) { dialog, which ->
            if(function != null && parameter != null){
                function(parameter)
            }
            dialog.dismiss()
        }

        dialog.setNegativeButton(resources.getString(R.string.cancel_str)) { dialog, which ->dialog.dismiss()}
        dialog.show()
    }

    override fun setEEImage(image: Int) {
        eeImage.setImageResource(image)
    }

    override fun getResourceString(R_id: Int): String {
        return resources.getString(R_id)
    }
    //endregion

    /**
     * Function that iterates through the scrolls to show/hide the buttons that it has
     * @param invisible is an internal Int
     */
    private fun modifyScrollsButtons(invisible: Int) {
        //personsScroll
        val linearLayoutPersonas: LinearLayout = personasScroll.getChildAt(0) as LinearLayout
        for (view in linearLayoutPersonas){
            val card: CardView = (view as ConstraintLayout).getChildAt(0) as CardView
            val cardLL: LinearLayout = card.getChildAt(0) as LinearLayout
            cardLL.getChildAt(1).visibility = invisible
            cardLL.getChildAt(2).visibility = invisible
        }

        //gastosScroll
        val linearLayoutGastos: LinearLayout = gastosScroll.getChildAt(0) as LinearLayout
        for (view in linearLayoutGastos){
            val card: CardView = (view as ConstraintLayout).getChildAt(0) as CardView
            val cardLL: LinearLayout = card.getChildAt(0) as LinearLayout
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
        presenter.setPeople(currentTravel!!.people.toMutableList())
    }

    /**
     * Function that fill the gastosScroll with the expenses of the travel
     */
    private fun fillGastosScroll(){
        for (gasto in currentTravel!!.expenses){
            onOkExpense(gasto.name, gasto.price, gasto.person_money, null, true)
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
                createAddPersonDialog(resources.getString(R.string.editarPersona_str),customLayout)
            })

            //Delete person
            customLayout.findViewById<FloatingActionButton>(R.id.deletePerson).setOnClickListener({
                createConfirmationDialog(resources.getString(R.string.borrarPersona_str), resources.getString(R.string.confirmacionBorrarPersona_str), listOf(linearLayout,customLayout), ::removePerson)
            })

            linearLayout.addView(customLayout)

            //UUID.randomUUID().toString().hashCode(): generates and unique id of 128 and hash it to convert it to an int of 32bits
            //this method avoids collisions.
            //If use UUID.randomUUID().toInt() can produce collisions
            if(internalUse == false)
                presenter.addNewPerson(Person(UUID.randomUUID().toString().hashCode(),text,travelId))
            presenter.debugPersonList()

            presenter.resetAllExpensesPerPerson()//Resets the values of the map for each expense

            if (!internalUse){
                //Shows dialog to warn the user that a new person its added
                createAlertDialog(resources.getString(R.string.personaAnyadida_str),resources.getString(R.string.confirmacionPersonaAnyadida_str))
            }
        }else{
            personLayout.findViewById<TextView>(R.id.personName).text = text
            val index = linearLayout.indexOfChild(personLayout)
            presenter.editPerson(Person(presenter.getPerson(index).id,text,travelId), index)
            presenter.debugPersonList()
            //Shows dialog to warn the user that a person its edited
            createAlertDialog(resources.getString(R.string.personaEditada_str),resources.getString(R.string.confirmacionPersonaEditada_str))
        }
    }

    override fun onOkExpense(name: String, totalPrice: Double, person_expense: MutableMap<Personid, Double>, gastoLayout: View?, internalUse: Boolean) {
        //crear gasto y añadir a la lista de gastos
        val newExpense = Expense(UUID.randomUUID().toString().hashCode(),name, travelId, totalPrice, person_expense)

        //actualizar scroll
        val inflater = LayoutInflater.from(this)

        val linearLayout = gastosScroll.findViewById<LinearLayout>(R.id.linearLayGasto)
        if(gastoLayout == null) {
            val customLayout: View = inflater.inflate(R.layout.person_scrollview_layout, linearLayout, false)
            customLayout.findViewById<TextView>(R.id.personName).text = name

            //Edit gasto
            customLayout.findViewById<FloatingActionButton>(R.id.editPerson).setOnClickListener({
                createAddGastoDialog(resources.getString(R.string.editarGasto_str),customLayout, newExpense)
            })

            //Delete gasto
            customLayout.findViewById<FloatingActionButton>(R.id.deletePerson).setOnClickListener({
                createConfirmationDialog("Borrar gasto", "¿Estas seguro de borrar este gasto?", listOf(linearLayout, customLayout), ::removeGasto)
            })

            presenter.addNewExpense(newExpense)
            linearLayout.addView(customLayout)

        }else{
            gastoLayout.findViewById<TextView>(R.id.personName).text = name

            //Edit gasto, machacar el setOnClikListener
            val editButton = gastoLayout.findViewById<FloatingActionButton>(R.id.editPerson)
            editButton.setOnClickListener(null)
            editButton.setOnClickListener({
                createAddGastoDialog("Editar gasto",gastoLayout, newExpense)
            })

            val index = linearLayout.indexOfChild(gastoLayout)
            presenter.editGasto(newExpense, index)
        }

        //actualizar precioTotal
        presenter.setPrecioTotal()

        //mostrar Toast
        if (!internalUse){
            var message: String
            if (gastoLayout == null){
                message = "Gasto añadido"
            }else{
                message = "Gasto editado"
            }
            val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun toEditMode(){
        val intent = Intent(this@TravelEditionActivity, TravelEditionActivity::class.java)
        val trDetail: Travel? = currentTravel;
        intent.putExtra("EditMode", true)
        intent.putExtra("CurrentTravel", trDetail)
        startActivity(intent)
    }

    override fun toMainActivity() {
        val intent = Intent(this@TravelEditionActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //this will lead us close the app if we BACK_BUTOTN and we are on the main activity
        startActivity(intent)
    }

    override fun deleteTravel(){
        presenter.deleteTravel(currentTravel)

    }
    //endregion

    private fun <T> removePerson(parameters: List<T>?){
        val linearLayout:LinearLayout = parameters?.get(0) as LinearLayout
        val personLayout: View = parameters?.get(1) as View

        val personaBorrada: Boolean = presenter.deletePerson(linearLayout.indexOfChild(personLayout))
        presenter.debugPersonList()

        //si no se ha borrado persona es porque es la ultima persona
        if (!personaBorrada){
            createAlertDialog("ÚLTIMA PERSONA", "No puedes hacer un viaje sin gente")
            return
        }
        linearLayout.removeView(personLayout)

        presenter.resetAllExpensesPerPerson()//Resets the values of the map for each expense
        createAlertDialog("Persona borrada", "Persona borrada y gastos reiniciados");
    }

    private fun <T> removeGasto(parameters: List<T>?){
        val linearLayout: LinearLayout = parameters?.get(0) as LinearLayout
        val gastoLayout: View = parameters?.get(1) as View

        presenter.deleteGasto(gastoLayout, linearLayout)
        presenter.setPrecioTotal()

        val toast = Toast.makeText(applicationContext, "Gasto borrado", Toast.LENGTH_LONG)
        toast.show()
    }

}