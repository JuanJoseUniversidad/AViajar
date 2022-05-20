
package es.uji.al394516.aviajar.database

import android.app.people.PeopleManager
import androidx.room.*

@Dao
interface ITravelsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTravel(travel: TravelEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTravels(travels: Array<TravelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expense: ExpenseEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpenses(travels: Array<ExpenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(expense: PersonEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersons(travels: Array<PersonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonExpense(expense: PersonExpenseEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonsExpenses(travels: Array<PersonExpenseEntity>)


    @Delete
    public fun deleteTravel(vararg travel: TravelEntity)

    @Delete
    public fun deleteExpense(vararg expense: ExpenseEntity)

    @Delete
    public fun deletePerson(vararg person: PersonEntity)

    @Delete
    public fun deletePersonExpense(vararg personExpense: PersonExpenseEntity)



    @Query("SELECT * FROM TravelEntity ORDER BY name")
    fun readAllTravels():List<TravelEntity>

    @Query("SELECT * FROM PersonEntity ORDER BY name")
    fun readAllPersons():List<PersonEntity>

    @Query("SELECT * FROM ExpenseEntity ORDER BY name")
    fun readAllExpenses():List<ExpenseEntity>

    @Query("SELECT * FROM PersonExpenseEntity ORDER BY personID,expeseName")
    fun readAllPersonExpense():List<PersonExpenseEntity>

    //Todas las personas de un viaje(revisar)
    @Query("SELECT pe.* FROM PersonEntity AS pe LEFT JOIN TravelEntity AS te ON pe.travelID=te.id WHERE te.id = :travelId ORDER BY pe.name")
    fun readAllPersonFromATravel(travelId:Int):List<PersonEntity>

    //Todos los gastos de un viaje
    @Query("SELECT ee.* FROM ExpenseEntity AS ee LEFT JOIN TravelEntity AS te ON ee.tavelID=te.id WHERE te.id = :travelId ORDER BY ee.name")
    fun readAllExpensesFromATravel(travelId:Int):List<ExpenseEntity>

    //Las parte dividida de un gasto entre las personas
    @Query("SELECT * FROM PersonExpenseEntity WHERE personID = :personID AND LOWER(expeseName) LIKE LOWER(:expenseName)")
    fun readAllExpensePerPerson(personID:Int, expenseName:String):List<PersonExpenseEntity>

}