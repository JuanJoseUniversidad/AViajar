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
import es.uji.al394516.aviajar.classes.Personid
import es.uji.al394516.aviajar.recycler.GastoDialogAdapter
import java.lang.ClassCastException

class AddGastoDialog(val title: String, val gastoLayout: View? = null, val model: Model) : DialogFragment() {
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

        val view = aux.layoutInflater.inflate(R.layout.gasto_persona_prefab, null)
        val personaGastoRelationAux: MutableMap<Personid, Double> = mutableMapOf()

        //crear map con valores por default a 0.0
        val personListAux = model.getAuxPeople()
        for (person in personListAux){
            personaGastoRelationAux.put(person.id, 0.0)
        }

        with(view){
            val gastoName: EditText = findViewById(R.id.editTextName)
            val gastoPrecio: EditText = findViewById(R.id.editTextPrecio)
            val gastoPersonaRecyclerView: RecyclerView = findViewById(R.id.gastosRecycler)

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
                        if (gastoPrecio.toString() != ""){
                            val difference: Double = model.checkGastosDialogSum(gastoPrecio.toString().toDouble(), personaGastoRelationAux)
                            if (difference == 0.0){
                                gastoListener.onOkExpense(gastoName.text.toString(), gastoPrecio.text.toString().toDouble(), personaGastoRelationAux)
                                dismiss()
                            }
                            else{
                                if (difference > 0.0){
                                    createAlertDialog("FALTA DINERO", "Te faltan $difference€ por asignar")
                                }
                                else{
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