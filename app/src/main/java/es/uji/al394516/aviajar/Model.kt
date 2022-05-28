package es.uji.al394516.aviajar

import android.content.Context
import com.android.volley.Response
import com.android.volley.VolleyError
import es.uji.al394516.aviajar.classes.Expense
import es.uji.al394516.aviajar.classes.Person
import es.uji.al394516.aviajar.classes.Travel
import es.uji.al394516.aviajar.database.TravelDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.math.exp

class Model(context: Context) {
    //Database
    private val database = TravelDatabase.getInstance(context)

    private var personList:MutableList<Person> = mutableListOf()

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
                        var expense_price_per_person: MutableMap<Int, Double> = mutableMapOf()
                        //Gets the corresponding part of the espense per person (MEJORAR ESTO POR FAVOR!!!!!!)
                        for(person in peopleTravel) {
                            val expense_person = withContext(Dispatchers.IO) {
                                database.dao.readAllExpensePerPerson(person.id, expense.name)
                            }

                            if(expense_person != null){
                                expense_price_per_person.put(expense_person.personID,expense_person.price.toDouble())
                            }

                        }
                        expenses.add(Expense(expense.name,expense.tavelID,expense.price,expense_price_per_person))
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
     * Add person to the list
     */
    fun addPersonList(person :Person){
        personList.add(person)
    }

    /**
     * Edit person
     */
    fun editPersonList(person:Person, index:Int){
        personList[index] = person
    }
}