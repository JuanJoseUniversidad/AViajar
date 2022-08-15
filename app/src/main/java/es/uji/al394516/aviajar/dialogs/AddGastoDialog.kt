package es.uji.al394516.aviajar.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.uji.al394516.aviajar.Model
import es.uji.al394516.aviajar.R
import es.uji.al394516.aviajar.classes.Expense
import es.uji.al394516.aviajar.classes.Personid
import es.uji.al394516.aviajar.recycler.GastoDialogAdapter
import java.lang.ClassCastException
import kotlin.math.absoluteValue

class AddGastoDialog(val title: String, val gastoLayout: View? = null, val model: Model, val gasto: Expense?) : DialogFragment() {
    private lateinit var gastoListener: IDialogsFunctions

    private fun createAlertDialog(title: String, message: String){
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton("OK"){dialog, which -> dialog.dismiss()}
        dialog.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        gastoListener = try {
            context as IDialogsFunctions
        } catch (e: ClassCastException) {
            throw ClassCastException("$context does not listen to add gasto")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null)
            throw IllegalStateException("Activity cannot be null")

        val aux = requireActivity()     //como no es null la activity pues esto tiene valor seguro

        val view = aux.layoutInflater.inflate(R.layout.editgasto_dialog, null)
        var personaGastoRelationAux: MutableMap<Personid, Double> = mutableMapOf()
        val personListAux = model.getAuxPeople()

        //si estamos editando uno
        if (gasto != null){
            personaGastoRelationAux = gasto.person_money
        }
        //si estamos creando uno
        else{
            //crear map con valores por default a 0.0
            for (person in personListAux){
                personaGastoRelationAux.put(person.id, 0.0)
            }
        }

        with(view){
            val gastoName: EditText = findViewById(R.id.nombreGasto)
            val gastoPrecio: EditText = findViewById(R.id.precioGasto)
            val gastoPersonaRecyclerView: RecyclerView = findViewById(R.id.recyclerGasto)

            //mirar si estamos editando uno -> rellenar los datos
            if (gasto != null){
                gastoName.setText(gasto.name)
                gastoPrecio.setText(gasto.price.toString())
            }

            //llenar recyclerView
            gastoPersonaRecyclerView.layoutManager = LinearLayoutManager(aux)
            val adapter = GastoDialogAdapter(personaGastoRelationAux, personListAux)
            gastoPersonaRecyclerView.adapter = adapter

            //cancel button
            val cancelButton = findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener({
                dismiss()
            })

            //ok button
            val addButton = findViewById<Button>(R.id.addButton)
            addButton.setOnClickListener{
                if (gastoName.text.toString() != ""){
                    //estamos creando gasto -> comprobar nombre único
                    if (gastoLayout == null){
                        if (!model.existeGastoInAuxList(gastoName.text.toString())){
                            ComprobacionFinal(gastoName, gastoPrecio, personaGastoRelationAux)
                        }
                        else{
                            createAlertDialog(model.getResourcesString(R.string.nombreRepetido_str), model.getResourcesString(R.string.errorRepetido_str))
                        }
                    }
                    //estamos editando gasto, da igual repetir nombre si es el de este viaje
                    else{
                        //existe el nombre pero es el que estamos editando
                        if (model.existeGastoInAuxList(gastoName.text.toString()) && gastoName.text.toString() == gasto!!.name){
                            ComprobacionFinal(gastoName, gastoPrecio, personaGastoRelationAux)
                        }
                        //si existe en la bdd y no es el que estamos editando
                        else if (model.existeGastoInAuxList(gastoName.text.toString())){
                            createAlertDialog(model.getResourcesString(R.string.nombreRepetido_str), model.getResourcesString(R.string.errorRepetidoVariacion_str))
                        }
                        //nombre nuevo -> machacar
                        else{
                            ComprobacionFinal(gastoName, gastoPrecio, personaGastoRelationAux)
                        }
                    }
                }
                else{
                    createAlertDialog(model.getResourcesString(R.string.noHayNombre_str), model.getResourcesString(R.string.errorNoHayNombre_str))
                    gastoName.setHintTextColor(Color.RED)
                }
            }
        }

        return AlertDialog.Builder(aux).run {
            setTitle(title)
            setView(view)

            create()
        }
    }

    private fun ComprobacionFinal(gastoName: EditText, gastoPrecio: EditText, personaGastoRelationAux: MutableMap<Personid, Double>) {
        if (gastoPrecio.text.toString() != ""){
            var difference: Double = model.checkGastosDialogSum(gastoPrecio.text.toString().toDouble(), personaGastoRelationAux)
            if (difference == 0.0){
                gastoListener.onOkExpense(gastoName.text.toString(), gastoPrecio.text.toString().toDouble(), personaGastoRelationAux, gastoLayout, false)
                dismiss()
            }
            else{
                if (difference > 0.0){
                    createAlertDialog(model.getResourcesString(R.string.faltaDinero_str), model.getResourcesString(R.string.errorFaltaDinero_str) + " " + difference)
                }
                else{
                    difference = difference.absoluteValue
                    createAlertDialog(model.getResourcesString(R.string.sobraDinero_str), model.getResourcesString(R.string.errorSobraDinero_str) + " " + difference)
                }
            }
        }
        else{
            createAlertDialog(model.getResourcesString(R.string.noHayPrecio_str), model.getResourcesString(R.string.errorNoHayPrecio_str))
            gastoPrecio.setHintTextColor(Color.RED)
        }
    }
}