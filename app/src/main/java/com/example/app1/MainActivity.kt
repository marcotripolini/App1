package com.example.app1

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import android.content.BroadcastReceiver
import android.content.Intent

var enqueueId: Long = 0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

        // Crea una richiesta di download

        val request = DownloadManager.Request(Uri.parse("https://www.improvity.it/wp-content/uploads/2021/04/cropped-cropped-improvity_logo-1-1-768x328.png"))
        request.setTitle("Image Download")
        request.setDescription("Downloading image...")

        // Configura la directory di destinazione e il nome del file
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloaded_image.jpg")

        // Ottieni il servizio di DownloadManager dal sistema
        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // Invia la richiesta di download e ottieni l'ID dell'enqueued download
        enqueueId = downloadManager.enqueue(request)

        // Registra un BroadcastReceiver per ricevere una notifica quando il download è completato
        this.registerReceiver(DownloadReceiver(), IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        val button =  findViewById<Button>(R.id.button)
        val nomefile = "pippo.png"
        // scaricare un file

        Toast.makeText(this, "Download in corso...", Toast.LENGTH_SHORT).show()
        val preferenze_registrazione = applicationContext.getSharedPreferences("registered_user", 0)
        val editor_registrazione = preferenze_registrazione.edit()

        editor_registrazione.putString("nome", "Marco")
        editor_registrazione.putString("cognome", "Tripolini")
        editor_registrazione.putString("email", "marco.tripolini@gmail.com")
        editor_registrazione.putString("telefono", "3206156477")
        editor_registrazione.commit()
        // val preferenze = applicationContext.getSharedPreferences("paperino_preferenze", 0)  // 0 - for private mode

        // 2) approccio con getPreferences
        // privato: solo questa Activity può accedere alle preferenze

        val preferenze_carrello = applicationContext.getSharedPreferences("ultimo_carrello", 0)
        val editor_preferenze_carrello = preferenze_carrello.edit()
        editor_preferenze_carrello.putString("data_carrello", "gg-mm-aa")
        editor_preferenze_carrello.putString("elenco_prodotti", "object")
        editor_preferenze_carrello.putString("totale_carrello", "prezzo")
        editor_preferenze_carrello.putString("codici_sconto", "3206156477")
        editor_preferenze_carrello.commit()
        val preferenze = applicationContext.getSharedPreferences("paperino_preferenze", 0)  // 0 - for private mode


        // Editor serve per scrivere nelle preferenze
        // 1 - anche altre applicazioni
        val editor = preferenze.edit()
        editor.putString("nome", "Paolino")
        editor.putString("cognome", "Paperino")
        editor.putString("email", "paolino.paperino@paperopoli.quack")
        editor.commit()

        val preferenze2 = this.getPreferences(0) // 0 - for private mode

        // Database Firebase:
        // Write a message to the database
        // ottengo il riferimento al database
        val database = Firebase.database

        // val myRef = database.getReference("message")
        // myRef.setValue("Hello, World!")

        // val myRef1 = database.getReference("message1")
        // myRef.setValue("Hello, World1!")

        // parliamo con il database
        // ottengo l'istanza
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        // voglio accedere alla collection prodotti

        val docRef: DocumentReference = db.collection("prodotti").document("1")
        // ************************************************
        // asincrono
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

        db.collection("prodotti").document("7")
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

    fun onButtonRead (view: View?) {
        val pref = applicationContext.getSharedPreferences("paperino_preferenze", 0) // 0 - for private mode
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

        cursor.close()

        val cursor2 = db.getUserById("19")
        cursor2!!.moveToFirst()
        do {
            println(cursor2.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_NAME_COL)))
            println(cursor2.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_PHONE_COL)))
        } while (cursor2.moveToNext())
        cursor2.close()
        }

    private class DownloadReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Verifica che l'azione sia di completamento del download
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                // Verifica se il download è quello che abbiamo iniziato
                if (downloadId == enqueueId) {
                    // L'immagine è stata scaricata con successo, puoi ora gestire il file salvato nella cache
                    // Ad esempio, puoi leggere il file e convertirlo in un Bitmap
                    // Oppure puoi utilizzare il file direttamente nell'applicazione
                }
            }
        }
    }
}
