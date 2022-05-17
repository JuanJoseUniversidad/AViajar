package es.uji.al394516.aviajar

import android.content.Context
import es.uji.al394516.aviajar.classes.Expense
import es.uji.al394516.aviajar.classes.Person
import es.uji.al394516.aviajar.classes.Travel
import es.uji.al394516.aviajar.database.ExpenseEntity
import es.uji.al394516.aviajar.database.PersonEntity
import es.uji.al394516.aviajar.database.TravelEntity

class PresenterTR(context: Context, activityView: TravelResultsActivity) {
    lateinit var model:Model
    lateinit var view:TravelResultsActivity

    /**
     * Constructor
     */
    init {
        model = Model(context);
        view = activityView
//TODO("DESCOMENTAR")
   //     connect()
    }

    /**
     * Gets the list of cocktails and ingredients from the model
     */
    fun connect(){
        view.fillRecyclerView(listOf(Travel(0,"viaje1","lugar0", listOf(Person(
            PersonEntity(0,"dw",0)
        ),Person(PersonEntity(1,"dwdw",0))),
            listOf(Expense("gasto1",0,100.0f, mutableMapOf())))))
//        TODO("Llamada asyncrona, que se le pase una lista de travel el cual construya el objeto travel a raiz de la info de la bd local")
        /*if(catSearch) {
            model.getCocktailsByCategory(crit, netSearch, {
                view.fillRecyclerView(it);
                view.hideLoadingBar();
                view.showList();
            }, {
                view.showMessage(it.toString())
            })
        }else {
            model.getCocktailsByIngredient(crit, netSearch, {
                view.fillRecyclerView(it);
                view.hideLoadingBar();
                view.showList();
            }, {
                view.showMessage(it.toString())
            })
        }*/
    }

    /**
     * Launch the details activity
     */
    fun launchActivity(travel: Travel) {
        view.startActivity(travel);
    }
}