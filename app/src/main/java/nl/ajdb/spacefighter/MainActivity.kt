package nl.ajdb.spacefighter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Luister naar een klik op de play knop
        /*
            Opdracht 1: Luister naar een klik op de play knop
        */

        /*
            Opdracht 6: High score inzien
         */

    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
