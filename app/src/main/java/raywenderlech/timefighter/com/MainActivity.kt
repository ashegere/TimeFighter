package raywenderlech.timefighter.com

import android.nfc.Tag
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    internal lateinit var tapMeButton: Button
    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal var score = 0
    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal var initialCountDown: Long = 20000
    internal var countDownInterval: Long = 1000
    internal var TAG = MainActivity::class.java.simpleName
    internal var timeLeftOnTimer: Long = 20000


    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate called. Score is $score")
        tapMeButton = findViewById<Button>(R.id.tap_me)
        gameScoreTextView = findViewById<TextView>(R.id.game_score_text_view)
        timeLeftTextView = findViewById < TextView>(R.id.time_left_text_view)

        if(savedInstanceState != null){
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        }
        else {
            resetGame()
        }

        tapMeButton.setOnClickListener {view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this,R.anim.bounce)
            view.startAnimation(bounceAnimation)
            incrementScore()
        }

    }

    private fun restoreGame(){
        gameScoreTextView.text = getString(R.string.your_score,score.toString())
        val restoredTime = timeLeftOnTimer / 1000
        timeLeftTextView.text = getString(R.string.time_left,restoredTime.toString())

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                var timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.time_left,timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }

        }
        countDownTimer.start()
        gameStarted = true

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer")

    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy called.")
    }


    private fun resetGame(){
        score = 0
        gameScoreTextView.text = getString(R.string.your_score,score.toString())
        val initialTimeLeft =  initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.time_left,initialTimeLeft.toString())

        countDownTimer = object: CountDownTimer(initialCountDown,countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.time_left, timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false

    }

    private fun startGame(){

        if(!gameStarted){
            countDownTimer.start()
        }
        gameStarted = true

    }

    private fun endGame(){

        Toast.makeText(this,getString(R.string.game_score_message,score.toString()),Toast.LENGTH_SHORT).show()
        resetGame()
    }

    private fun incrementScore(){

        startGame()
        score = score + 1
        var newScore = getString(R.string.your_score,score.toString())
        gameScoreTextView.text = newScore
        val blinkAnimation = AnimationUtils.loadAnimation(this,R.anim.blink)
        gameScoreTextView.startAnimation(blinkAnimation)

        }
    }

