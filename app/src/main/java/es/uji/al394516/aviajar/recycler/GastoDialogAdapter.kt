package es.uji.al394516.aviajar.recycler

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.uji.al394516.aviajar.R
import es.uji.al394516.aviajar.classes.Person
import es.uji.al394516.aviajar.classes.Personid

class GastoDialogAdapter(val personaGastoMap: MutableMap<Personid, Double>, val personas: List<Person>): RecyclerView.Adapter<GastoDialogAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var personName: TextView
        lateinit var personGasto: EditText

        //listas auxiliares para recorrer el diccionario y modificarlo desde aquí
        lateinit var llaves: MutableList<Personid>
        lateinit var valores: MutableList<Double>

        init {
            personName = itemView.findViewById(R.id.personNamePrecio)
            personGasto = itemView.findViewById(R.id.editTextPrecioPersona)

            llaves = ArrayList<Personid>(personaGastoMap.keys)
            valores = ArrayList<Double>(personaGastoMap.values)

            //meter un evento a persongasto para modificar los valores del diccionario
            personGasto.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    val actualKey = llaves[layoutPosition]
                    if (personGasto.text.toString() == "" || personGasto.text.toString() == ".")
                        personaGastoMap[actualKey] = 0.0
                    else{
                        val precioUnitario: Double = personGasto.text.toString().toDouble()
                        personaGastoMap[actualKey] = precioUnitario
                        valores[layoutPosition] = precioUnitario
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
            })
        }
    }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.gasto_persona_prefab, parent, false)
        return ViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.personName.text = personas[position].name
        holder.personGasto.setText(holder.valores[position].toString())
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return personaGastoMap.size
    }
}