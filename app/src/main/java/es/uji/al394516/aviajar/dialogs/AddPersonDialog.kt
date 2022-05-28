package es.uji.al394516.aviajar.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import es.uji.al394516.aviajar.R
import java.lang.ClassCastException

class AddPersonDialog(val title:String, val personLayout:View? = null) : DialogFragment() {
    private lateinit var personListener: IDialogsFunctions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        personListener = try {
            context as IDialogsFunctions
        } catch (e: ClassCastException) {
            throw ClassCastException("$context does not listen to add person")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null)
            throw IllegalStateException("Activity cannot be null")

        val aux = requireActivity()     //como no es null la activity pues esto tiene valor seguro

        var text: EditText
        val view = aux.layoutInflater.inflate(R.layout.edittext_dialog, null)
        with(view){
            text = findViewById(R.id.editTextPersonName)

            val cancel = findViewById<Button>(R.id.Cancel)
            cancel.setOnClickListener({
                dismiss()
            })

            val add = findViewById<Button>(R.id.Add)
            add.setOnClickListener({
                if(text.text.toString() != ""){
                    personListener.onAccept(text.text.toString(),personLayout, false)
                    dismiss()
                }else{
                    text.setHintTextColor(Color.RED)
                }
            })
        }

        return AlertDialog.Builder(aux).run {
            setTitle(title)
            setView(view)

            create()
        }
    }

}