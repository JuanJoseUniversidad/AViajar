package es.uji.al394516.aviajar.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import es.uji.al394516.aviajar.PresenterTR
import es.uji.al394516.aviajar.R
import es.uji.al394516.aviajar.classes.Travel

class TravelsAdapter (val travelsList: List<Travel>, val presenterTR:PresenterTR): RecyclerView.Adapter<TravelsAdapter.ViewHolder>() {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.travel_item, parent, false)
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
    override fun onBindViewHolder(holder: TravelsAdapter.ViewHolder, position: Int) {
        with(travelsList[position]){
            holder.titleText.text = name
            holder.textPlace.text = place
            holder.textPeople.text = people.size.toString()
            //lo siento, se nos olvido poner un parametro y ahora es cambiar demasiado codigo
            holder.textTotalPrice.text = holder.preciosTotales[position].toString()
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return travelsList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var titleText: TextView;
        lateinit var textPlace: TextView;
        lateinit var textPeople: TextView;
        lateinit var textTotalPrice:TextView

        var preciosTotales : MutableList<Double> = mutableListOf()

        init{
            titleText = itemView.findViewById(R.id.TitleTravel);
            textPlace = itemView.findViewById(R.id.textPlace);
            textPeople = itemView.findViewById(R.id.textPeople);
            textTotalPrice = itemView.findViewById(R.id.textPrice)

            for(t in travelsList){
                var total: Double = 0.0
                for (expense in t.expenses){
                    total += expense.price
                }
                preciosTotales.add(total)
            }

            itemView.setOnClickListener{
                presenterTR.launchActivity(travelsList[getLayoutPosition()]);//Gets the travel details
            }
        }
    }

}