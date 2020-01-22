package nl.ajdb.spacefighter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import kotlinx.android.synthetic.main.activity_high_score.*

class HighScoreActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)
        sharedPreferences = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE)

        //setting the values to the textViews
        textView.text = "1."+sharedPreferences.getInt("score1",0);
        textView2.text = "2."+sharedPreferences.getInt("score2",0);
        textView3.text = "3."+sharedPreferences.getInt("score3",0);
        textView4.text = "4."+sharedPreferences.getInt("score4",0);
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, HighScoreActivity::class.java)
        }
    }
}
