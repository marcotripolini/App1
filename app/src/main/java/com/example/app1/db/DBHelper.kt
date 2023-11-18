package com.example.app1.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query

    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given

        val query = (
                "CREATE TABLE " +
                USERS_TABLE_NAME + " ("  +
                USERS_ID_COL + " INTEGER PRIMARY KEY, " +
                USERS_NAME_COL + " TEXT, " +
                USERS_AGE_COL + " TEXT, " +
                USERS_ADDRESS_COL + " TEXT, " +
                USERS_COUNTRY_COL + " TEXT, " +
                USERS_PHONE_COL + " TEXT ) " )
        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
        var query2 = "CREATE TABLE COUNTRY (ID INTEGER PRIMARY KEY, NAME TEXT)"
        db.execSQL(query2)
        val query3 = "CREATE TABLE products_table ( id INTEGER PRIMARY KEY, name TEXT, price TEXT )"
        db.execSQL(query3)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS COUNTRY")
        db.execSQL("DROP TABLE IF EXISTS products_table")
        onCreate(db)
    }

    fun addName(name : String, age : String ){

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(USERS_NAME_COL, name)
        values.put(USERS_AGE_COL, age)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(USERS_TABLE_NAME, null, values)

        // at last we are
        // closing our database
        db.close()
    }

    // below method is to get
    // all data from our database
    fun getName(): Cursor? {
        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase
        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME, null)
    }

    companion object{
        // below is variable for database name
        private val DATABASE_NAME = "USERS"
        // below is the variable for database version
        private val DATABASE_VERSION = 8

        val USERS_TABLE_NAME = "users_table"
        val USERS_ID_COL = "id"
        val USERS_NAME_COL = "name"
        val USERS_AGE_COL = "age"
        val USERS_ADDRESS_COL = "address"
        val USERS_COUNTRY_COL = "country"
        val USERS_PHONE_COL = "phone"

        val PROD_TABLE_NAME = "products_table"
        val PROD_ID_COL = "id"
        val PROD_NAME_COL = "name"
        val PROD_PRICE_COL = "price"


        // ho un database
        // questo database ha un nome : USERS

        // un database contiene una o più tabelle.
        // una tabella ha un nome : users_table
        // ora aggiungo la tabella products_table

    }
}
