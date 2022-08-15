package es.uji.al394516.aviajar

import android.content.Context
import android.util.Log
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import es.uji.al394516.aviajar.classes.Expense
import es.uji.al394516.aviajar.classes.Person
import es.uji.al394516.aviajar.classes.Personid
import es.uji.al394516.aviajar.classes.Travel
import es.uji.al394516.aviajar.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.io.EOFException
import java.lang.Exception
import java.math.BigDecimal
import kotlin.math.exp

class Model(val externalContext: Context) {
    //Database
    private val database = TravelDatabase.getInstance(externalContext)
    //Network
    private val network = Network.getInstance(externalContext)

    //lista person
    private var personList:MutableList<Person> = mutableListOf()

    //lista aux gastos
    private var gastosList: MutableList<Expense> = mutableListOf()

    /**
     * Gets all the travels from the local database
     */
    fun getAllTravels(listener: Response.Listener<List<Travel>>, errorListener: Response.ErrorListener){
        GlobalScope.launch(Dispatchers.Main){
            try{
                var listData: MutableList<Travel> = mutableListOf()

                //Gets all the travels
                val travelsEntities = withContext(Dispatchers.IO){
                    database.dao.readAllTravels()
                }

                for(entity in travelsEntities){
                    //Gets people of the travel
                    val peopleTravel = withContext(Dispatchers.IO){
                        database.dao.readAllPersonFromATravel(entity.id)
                    }
                    var people: MutableList<Person> = mutableListOf()
                    for(person in peopleTravel){
                        people.add(Person(person))
                    }

                    //Gets the expenses of the travel
                    val expensesTravel = withContext(Dispatchers.IO){
                        database.dao.readAllExpensesFromATravel(entity.id)
                    }
                    var expenses: MutableList<Expense> = mutableListOf()
                    for(expense in expensesTravel){
                        var expense_price_per_person: MutableMap<Personid, Double> = mutableMapOf()
                        //Gets the corresponding part of the espense per person (MEJORAR ESTO POR FAVOR!!!!!!)
                        for(person in peopleTravel) {
                            val expense_person = withContext(Dispatchers.IO) {
                                database.dao.readAllExpensePerPerson(person.id, expense.id)
                            }

                            if(expense_person != null){
                                expense_price_per_person.put(expense_person.personID,expense_person.price.toDouble())
                            }

                        }
                        expenses.add(Expense(expense.id,expense.name,expense.tavelID,expense.price,expense_price_per_person))
                    }

                    listData.add(Travel(entity.id,entity.name,entity.place,people, expenses))
                }

                if(listData.size == 0){
                    errorListener.onErrorResponse(VolleyError("Could not retrieve all the data from the database"))
                }else {
                    listener.onResponse(listData)
                }
            }
            catch (e: Exception){
                errorListener.onErrorResponse(VolleyError(e.toString()))
            }
        }
    }

    /**
     * Clear the list of persons
     */
    fun clearPersonList(){
        personList.clear()
    }

    /**
     * Clear the list of persons
     */
    fun clearExpensesList(){
        gastosList.clear()
    }

    /**
     * Add person to the list
     */
    fun addPersonList(person :Person){
        personList.add(person)
    }

    /**
     * Add [newExpense] to the auxiliary list
     */
    fun addExpenseList(newExpense: Expense) {
        gastosList.add(newExpense)
    }

    /**
     * Edit person
     */
    fun editPersonList(person:Person, index:Int){
        personList[index] = person
    }

    /**
     * Edit [newExpense] in the auxiliary list
     */
    fun editGastoList(newExpense: Expense, index: Int) {
        gastosList[index] = newExpense
    }

    /**
     * Prints person en logcat
     */
    fun debugPersons(){
        for(p in personList) {
            Log.d("personadebug", p.id.toString() +", "+p.name)
        }
    }

    /**
     * Delete person from list
     */
    fun deletePerson(index: Int){
        personList.removeAt(index)
    }

    /**
     * Delete gasto from list
     */
    fun deleteGasto(index: Int) {
        gastosList.removeAt(index)
    }

    /**
     * Get person from list
     */
    fun getPersonList(index: Int):Person{
        return personList[index]
    }
    /** Function that checks if [nombreGasto] exists in the [gastosList]
     * @param nombreGasto
     */
    fun existeGastoInAuxList(nombreGasto: String): Boolean {
        for (gasto in gastosList){
            if (gasto.name == nombreGasto)
                return true
        }
        return false
    }

    /**
     * Function that checks if the sum of the prices of [personaGastoRelation] are [precioTotal]
     * @param precioTotal
     * @param personaGastoRelation
     * @return The difference between [precioTotal] and [personaGastoRelation]
     */
    fun checkGastosDialogSum(precioTotal: Double, personaGastoRelation: MutableMap<Personid,Double>): Double {
        //val totalSum = personaGastoRelation.entries.sumOf { it.value }
        var precioTotalBD: BigDecimal = BigDecimal(precioTotal.toString())
        var totalSum: BigDecimal = BigDecimal("0.0")

        for (precio in personaGastoRelation.values){
            totalSum += BigDecimal(precio.toString())
        }

        return (precioTotalBD - totalSum).toDouble()
    }

    /**
     * Function that returns a copy of [personList]
     */
    fun getAuxPeople(): List<Person>{
        return personList
    }

    /**
     * Function that set a list on [personList]
     */
    fun setAuxPeople(pl:MutableList<Person>) {
        personList = pl
    }


    /**
     * Function that returns a copy of [gastosList]
     */
    fun getAuxGasto(): List<Expense>{
        return gastosList
    }

    /**
     * Function that set a list on [personList]
     */
    fun setAuxGasto(gl:MutableList<Expense>) {
        gastosList = gl
    }

    /**
     * Gets places from API if there is an error return an a list with one item "Undefined"*/
    fun getPlaces(listener: Response.Listener<List<String>>, errorListener: Response.ErrorListener) /*: Array<Ingredients>*/{
        // Launch a coroutine
        GlobalScope.launch(Dispatchers.Main) {
                // Recover categories from the net
            network.getPlaces(Response.Listener {
                // Pass the categories to the listener
                listener.onResponse(it)
            }, Response.ErrorListener {
                errorListener.onErrorResponse(it)
            })
        }
    }

     /**
      * @return The sum of the prices of [gastosList]
      */
    fun precioTotal(): Double {
        var precio = 0.0

        for (gasto in gastosList){
            precio += gasto.price
        }
        return precio
    }

    fun insertTravelDatabase(id:Int,name:String,place:String){
        GlobalScope.launch(Dispatchers.Main) {
            GlobalScope.launch {

                    //inserting the Travel
                withContext(Dispatchers.IO) {
                    database.dao.insertTravel(TravelEntity(id, name, place))
                }
                     //inserting persons
                     for (person in personList) {
                         withContext(Dispatchers.IO) {
                             database.dao.insertPerson(PersonEntity(person.id,
                                 person.name,
                                 person.travelID))
                         }

                     }

                     //insert expenses
                 for (expense in gastosList) {
                     withContext(Dispatchers.IO) {
                         database.dao.insertExpense(ExpenseEntity(expense.id, expense.name,
                             expense.tavelID,
                             expense.price))
                     }
                         for(ep in expense.person_money){
                            // withContext(Dispatchers.IO) {
                                 val p = PersonExpenseEntity(ep.key,expense.id,
                                     expense.name,
                                     ep.value.toFloat())
                                 database.dao.insertPersonExpense(p)
                            //8 }
                         }

                 }

            }
        }
    }

    /**
     * Resets all the values of the map of all expenses
     */
    fun resetMapAllExpense(){
        for (e in gastosList){
            e.person_money.clear()
            for (person in personList){
                e.person_money.put(person.id, 0.0)
            }
        }
    }

    /**
     * Delete a travel
     */
    fun deleteTravel(travel:Travel?,listener: Response.Listener<Boolean>, errorListener: Response.ErrorListener){
        GlobalScope.launch(Dispatchers.Main) {
            GlobalScope.launch {
                try {
                    //delete the Travel
                    database.dao.deleteTravel(TravelEntity(travel!!.id, travel!!.name, travel!!.place))
                    listener.onResponse(true)
                }catch (e: Exception) {
                    errorListener.onErrorResponse(VolleyError("Error delete database"))
                }
            }
        }
    }

    fun getResourcesString(R_id: Int): String{
        return externalContext.resources.getString(R_id)
    }
}