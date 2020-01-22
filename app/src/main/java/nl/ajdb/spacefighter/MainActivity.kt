package nl.ajdb.spacefighter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Luister naar een klik op de play knop
        buttonPlay.setOnClickListener {
            startActivity(GameActivity.intent(this@MainActivity))
        }

        buttonScore.setOnClickListener {
            startActivity(HighScoreActivity.intent(this@MainActivity))
        }
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
