package es.uji.al394516.aviajar

import android.content.Context
import es.uji.al394516.aviajar.classes.Expense
import es.uji.al394516.aviajar.classes.Person
import es.uji.al394516.aviajar.classes.Travel
import es.uji.al394516.aviajar.database.ExpenseEntity
import es.uji.al394516.aviajar.database.PersonEntity
import es.uji.al394516.aviajar.database.TravelEntity

class PresenterTR(val view: ITravelResults, val model: Model) {

    /**
     * Constructor
     */
    init {
        connect()
    }

    /**
     * Gets the list of cocktails and ingredients from the model
     */
    fun connect(){

        model.getAllTravels({
            view.fillRecyclerView(it)
            view.hideLoadingBar()
        },{
            view.showMessage(it.toString())
            view.hideLoadingBar()
        })
    }

    /**
     * Launch the details activity
     */
    fun launchActivity(travel: Travel) {
        view.startActivity(travel);
    }
}