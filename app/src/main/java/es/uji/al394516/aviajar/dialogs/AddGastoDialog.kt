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
                    if (!model.existeGastoInAuxList(gastoName.text.toString())){
                        if (gastoPrecio.text.toString() != ""){
                            var difference: Double = model.checkGastosDialogSum(gastoPrecio.text.toString().toDouble(), personaGastoRelationAux)
                            if (difference == 0.0){
                                gastoListener.onOkExpense(gastoName.text.toString(), gastoPrecio.text.toString().toDouble(), personaGastoRelationAux, gastoLayout)
                                dismiss()
                            }
                            else{
                                if (difference > 0.0){
                                    createAlertDialog("FALTA DINERO", "Te faltan $difference€ por asignar")
                                }
                                else{
                                    difference = difference.absoluteValue
                                    createAlertDialog("SOBRA DINERO", "Te sobran $difference€ al asignar")
                                }
                            }
                        }
                        else{
                            createAlertDialog("NO HAY PRECIO", "No has asignado precio a este gasto")
                            gastoPrecio.setHintTextColor(Color.RED)
                        }
                    }
                    else{
                        createAlertDialog("NOMBRE REPETIDO", "Ya existe un gasto con este nombre, prueba otro distinto")
                    }
                }
                else{
                    createAlertDialog("NO HAY NOMBRE", "No has puesto nombre al gasto")
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
}