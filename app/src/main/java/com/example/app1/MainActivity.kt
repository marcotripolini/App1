package com.example.app1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.app1.db.DBHelper
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inizializza la view
        // spiega all'activity quale è
        // l'xml da caricare...
        setContentView(R.layout.activity_main)

        val button =  findViewById<Button>(R.id.button)


        val preferenze_registrazione = applicationContext.getSharedPreferences("registered_user", 0)
        val editor_registrazione = preferenze_registrazione.edit()
        editor_registrazione.putString("nome", "Marco");
        editor_registrazione.putString("cognome", "Tripolini");
        editor_registrazione.putString("email", "marco.tripolini@gmail.com");
        editor_registrazione.putString("telefono", "3206156477");
        editor_registrazione.commit();
        // val preferenze = applicationContext.getSharedPreferences("paperino_preferenze", 0)  // 0 - for private mode

        // 2) approccio con getPreferences
        // privato: solo questa Activity può accedere alle preferenze

        val preferenze_carrello = applicationContext.getSharedPreferences("ultimo_carrello", 0)
        val editor_preferenze_carrello = preferenze_carrello.edit()
        editor_preferenze_carrello.putString("data_carrello", "gg-mm-aa");
        editor_preferenze_carrello.putString("elenco_prodotti", "object");
        editor_preferenze_carrello.putString("totale_carrello", "prezzo");
        editor_preferenze_carrello.putString("codici_sconto", "3206156477");
        editor_preferenze_carrello.commit();
        val preferenze = applicationContext.getSharedPreferences("paperino_preferenze", 0)  // 0 - for private mode


        // Editor serve per scrivere nelle preferenze                                                                                // 1 - anche altre applicazioni
        val editor = preferenze.edit()
        editor.putString("nome", "Paolino");
        editor.putString("cognome", "Paperino");
        editor.putString("email", "paolino.paperino@paperopoli.quack");
        editor.commit();

        val preferenze2 = this.getPreferences(0) // 0 - for private mode

        // non esiste push e pop,
        // per aggiungere un'activity allo stack usi StartActivity()
        // per rimuovere un'activity dallo stack usi finish()

        // Database Firebase:
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")

        val myRef1 = database.getReference("message1")
        myRef.setValue("Hello, World1!")

        // parliamo con il database
        // ottengo l'istanza
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        // voglio accedere alla collection prodotti

        val docRef: DocumentReference = db.collection("prodotti").document("1")

        // Leggi i dati del documento

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Accedi ai campi del documento
                    val titolo = document.getString("titolo")
                    val descrizione = document.getString("descrizione")
                    val prezzo = document.getLong("prezzo")
                    // Fai qualcosa con i dati ottenuti
                    if (titolo != null && descrizione != null) {
                        // Usa i dati (ad esempio, visualizzali o elaborali)
                        println("Titolo: $titolo, Descrizione: $descrizione")
                    } else {
                        // I dati non sono presenti o sono nulli
                        println("I dati non sono presenti o sono nulli.")
                    }
                } else {
                    println("Il documento non esiste.")
                }
            }
            .addOnFailureListener { exception ->
                println("Errore nel recupero del documento: $exception")
            }

        println (docRef)

        /*
        val prodotto = hashMapOf(
            "titolo" to "Fontina",
            "descrizione" to "Formaggio valdostano",
            "prezzo" to "95",
        )

        db.collection("prodotti").document("6")
            .set(prodotto)
            .addOnSuccessListener { Log.d("Scrivi", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Scrivi", "Error writing document", e) }
        */

        db.collection("prodotti")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Leggi", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Leggi", "Error getting documents: ", exception)
            }

        db.collection("prodotti")
            .whereEqualTo("titolo", "Brie")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("Leggi", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Leggi", "Error getting documents: ", exception)
            }
        /*
        button.setOnClickListener(){
            Toast.makeText(applicationContext,"Ciao io sono un messaggio toast",Toast.LENGTH_SHORT).show()

            val toast = Toast.makeText(applicationContext, "Hello world", Toast.LENGTH_SHORT)
            toast.show()

            val myToast = Toast.makeText(applicationContext,"Messaggio toast con gravity",Toast.LENGTH_SHORT)
            myToast.setGravity(Gravity.LEFT,200,200)
            myToast.show()
        }
        */
    }

    override fun onStart() {
        super.onStart()
        val toast  = Toast.makeText(applicationContext, "onStart Called", Toast.LENGTH_LONG).show()
    }

    override fun onRestart() {
        super.onRestart()
        // It will show a message on the screen
        // then onRestart is invoked
        val toast = Toast.makeText(applicationContext, "onRestart Called", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        // It will show a message on the screen
        // then onResume is invoked
        val toast = Toast.makeText(applicationContext, "onResume Called", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        // It will show a message on the screen
        // then onPause is invoked
        val toast = Toast.makeText(applicationContext, "onPause Called", Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        // It will show a message on the screen
        // then onStop is invoked
        val toast = Toast.makeText(applicationContext, "onStop Called", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // It will show a message on the screen
        // then onDestroy is invoked
        val toast = Toast.makeText(applicationContext, "onDestroy Called", Toast.LENGTH_LONG).show()
    }

    /*
    fun onFranco(view: View?) {
        println("Hello world")
    }
    */

    fun onButtonRead (view: View?) {
        val pref = applicationContext.getSharedPreferences("paperino_preferenze", 0) // 0 - for private mode
        val editor = pref.edit()
        val email = pref.getString("email", null)
        println(email)

        // leggiamo solo l'indirizzo email
        // che abbiamo salvato dentro
        // paperino_preferenze
        // val db = DBHelper(this, null)

        // below is the variable for cursor
        // we have called method to get
        // all names from our database
        // and add to name text view
        // val cursor = db.getName()

        // moving the cursor to first position and
        // appending value in the text view
        // cursor!!.moveToFirst()
        // Name.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
        // Age.append(cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")

        // moving our cursor to next
        // position and appending values
        // while(cursor.moveToNext()){
        //     (cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
        //    (cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
        // }
        // at last we close our cursor
        // cursor.close()
    }

    fun onButtonClick(view: View?) {
        /*
        val db = DBHelper(this, null)
        val name = "Mario"
        val age = "38"
        db.addName(name, age)
        db.addUserRecord("Stefano", "38", "Via Roma 1", "Italia", "3206156477")
        db.addUserRecord("Franco", "29", "Via Maiella 2", "Italia", "3206156477")
        db.addUserRecord("Roberto", "48", "Via delle Galline 38", "Italia", "3206156477")
        */

        val db = DBHelper(this, null)
        val cursor = db.getAllUsers()
        cursor!!.moveToFirst()
        do {
            println(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_NAME_COL)))
            println(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_AGE_COL)))
            println(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_ADDRESS_COL)))
            println(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_COUNTRY_COL)))
            println(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_PHONE_COL)))
        } while (cursor.moveToNext())

        // print(cursor.getString(1))
        // print(cursor.getString(2))

        cursor.close()

        val cursor2 = db.getUserById("19")
        cursor2!!.moveToFirst()
        do {
            println(cursor2.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_NAME_COL)))
            println(cursor2.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_PHONE_COL)))
        } while (cursor2.moveToNext())

        cursor2.close()

        }

    }

