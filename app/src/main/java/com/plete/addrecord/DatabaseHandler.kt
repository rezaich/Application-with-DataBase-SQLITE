package com.plete.addrecord

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper
    (context, Database_Name, null, Database_Version){

    companion object{
        private val Database_Version = 1
        private val Database_Name = "EmployeeDatabase"

        private val Table_Contacts = "EmployeeTable"

        private val Key_Id = "_id"
        private val Key_Name = "nama"
        private val Key_Email = "email"
        private val Key_Phone = "phone"
        private val Key_Address = "address"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val Create_Contacts_Table = ("Create Table " + Table_Contacts + "("
                + Key_Id + " Integer Primary Key, "
                + Key_Name + " Text, "
                + Key_Email + " Text, "
                + Key_Phone + " Text, "
                + Key_Address + " Text)")
        db?.execSQL(Create_Contacts_Table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists $Table_Contacts")
        onCreate(db)
    }

    fun addEmployee(emp: EMPModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Key_Name, emp.nama)
        contentValues.put(Key_Email, emp.email)
        contentValues.put(Key_Phone, emp.phone)
        contentValues.put(Key_Address, emp.address)
        //insert employee details using insert query
        val success = db.insert(Table_Contacts, null, contentValues)
        db.close()
        return success
    }

    fun viewEmployee(): ArrayList<EMPModel>{
        val empList: ArrayList<EMPModel> = ArrayList<EMPModel>()
        val selectQuery = "Select * from $Table_Contacts"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var nama: String
        var email: String
        var phone: String
        var address: String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(Key_Id))
                nama = cursor.getString(cursor.getColumnIndex(Key_Name))
                email = cursor.getString(cursor.getColumnIndex(Key_Email))
                phone = cursor.getString(cursor.getColumnIndex(Key_Phone))
                address = cursor.getString(cursor.getColumnIndex(Key_Address))
                val emp = EMPModel(id, nama, email, phone, address)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    /**
     * method unutk delete data
     */
    fun deleteEmployee(emp: EMPModel): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Key_Id, emp.id)

        val success = db.delete(Table_Contacts, Key_Id + "=" + emp.id, null)
        db.close()
        return success
    }

    /**
     * method update db employee
     */
    fun updateEmployee(emp: EMPModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Key_Name, emp.nama)
        contentValues.put(Key_Email, emp.email)
        contentValues.put(Key_Phone, emp.phone)
        contentValues.put(Key_Address, emp.address)
        //insert employee details using insert query
        val success = db.update(Table_Contacts, contentValues, Key_Id + "=" + emp.id, null)
        db.close()
        return success
    }
}