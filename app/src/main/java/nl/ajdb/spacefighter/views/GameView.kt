package nl.ajdb.spacefighter.views

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.*
import nl.ajdb.spacefighter.MainActivity
import nl.ajdb.spacefighter.R
import nl.ajdb.spacefighter.personas.Boom
import nl.ajdb.spacefighter.personas.Enemy
import nl.ajdb.spacefighter.personas.Friend
import nl.ajdb.spacefighter.personas.Player
import kotlin.coroutines.CoroutineContext


class GameView(context: Context, val screenX: Int, val screenY: Int) : SurfaceView(context), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    // Dit is een waarde die bij houdt of het spel bezig is.
    private var playing: Boolean = false

    private var player: Player = Player(context, screenX, screenY)
    private val friend: Friend = Friend(context, screenX.toFloat(), screenY.toFloat())

    private lateinit var gameThread: Job

    //Adding an stars list
    private val stars = ArrayList<Star>()

    private var paint: Paint = Paint()
    private lateinit var canvas: Canvas
    private var surfaceHolder: SurfaceHolder = holder

    //Adding 3 enemies you may increase the size
    private val enemyCount = 3

    //Adding enemies object array
    private var enemies = Enemy(context, screenX.toFloat(), screenY.toFloat())//= mutableListOf<Enemy>()

    private val boom: Boom = Boom(context)

    //to count the number of Misses
    var countMisses: Int = 0

    //indicator that the enemy has just entered the game screen
    var flag: Boolean = false

    //an indicator if the game is Over
    private var isGameOver: Boolean = false

    //the score holder
    var score: Int = 0

    //the high Scores Holder
    var highScore = IntArray(4)

    //Shared Prefernces to store the High Scores
    var sharedPreferences: SharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE)

    var gameOnsound: MediaPlayer = MediaPlayer.create(context, R.raw.gameon)
    val killedEnemysound: MediaPlayer = MediaPlayer.create(context, R.raw.killedenemy)
    val gameOversound: MediaPlayer = MediaPlayer.create(context, R.raw.gameover)

    init {

        //adding 100 stars you may increase the number
        val starNums = 100
        for (i in 0 until starNums) {
            val s = Star(screenX, screenY)
            stars.add(s)
        }

        gameOnsound.start()

        //initializing the array high scores with the previous values
        highScore[0] = sharedPreferences.getInt("score1",0)
        highScore[1] = sharedPreferences.getInt("score2",0)
        highScore[2] = sharedPreferences.getInt("score3",0)
        highScore[3] = sharedPreferences.getInt("score4",0)

    }

    private fun createGameThread(): Job {
        return launch {
            while (playing) {
                //to update the frame
                update()

                //to draw the frame
                draw()

                //to control
                delay(17)
                control()
            }
        }
    }

    private fun update() {

        //incrementing score as time passes
        score++

        // updating player position
        player.update()

        //setting boom outside the screen
        boom.x = -400f
        boom.y = -400f

        //Updating the stars with player speed
        for (s in stars) {
            s.update(player.getSpeed())
        }

        if(enemies.getX().toInt() == screenX){
            flag = true
        }

        enemies.update(player.getSpeed())

        //if collision occurs with player
        if (Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())) {
            //displaying boom at that location
            boom.x = enemies.getX()
            boom.y = enemies.getY()

            //will play a sound at the collision between player and the enemy
            killedEnemysound.start()

            enemies.setX(-400f)
        }// the condition where player misses the enemy
        else{
            //if the enemy has just entered
            if(flag){
                //if player's x coordinate is more than the enemies's x coordinate.i.e. enemy has just passed across the player
                if(player.getDetectCollision().exactCenterX() >= enemies.getDetectCollision().exactCenterX()+380){
                    //increment countMisses
                    countMisses++

                    //setting the flag false so that the else part is executed only when new enemy enters the screen
                    flag = false;
                    //if no of Misses is equal to 3, then game is over.
                    if(countMisses==3){
                        //setting playing false to stop the game.
                        playing = false
                        isGameOver = true

                        gameOnsound.stop()
                        gameOversound.start()

                        var num = 0
                        var tempScore = 0
                        var newScoreIsSet = false
                        for (i in highScore) {
                            if(i < tempScore) {
                                val t = i
                                highScore[num] = tempScore
                                tempScore = t
                            }
                            if(i < score && !newScoreIsSet){
                                tempScore = i
                                highScore[num] = score
                                newScoreIsSet = true
                            }

                            num++
                        }

                        //storing the scores through shared Preferences
                        val e = sharedPreferences.edit()
                        var ii = 0
                        for(i in highScore){

                            var j = ii+1
                            e.putInt("score$j", i)
                            ii++
                        }
                        e.apply()
                    }
                }
            }
        }

        friend.update(player.getSpeed())
        //checking for a collision between player and a friend
        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision())){

            //displaying the boom at the collision
            boom.x = friend.x
            boom.y = friend.y
            //setting playing false to stop the game
            playing = false
            //setting the isGameOver true as the game is over
            isGameOver = true

            gameOnsound.stop()
            gameOversound.start()

            //Assigning the scores to the highscore integer array
            var num = 0
            var tempScore = 0
            var newScoreIsSet = false
            for (i in highScore) {
                if(i < tempScore) {
                    val t = i
                    highScore[num] = tempScore
                    tempScore = t
                }
                if(i < score && !newScoreIsSet){
                    tempScore = i
                    highScore[num] = score
                    newScoreIsSet = true
                }

                num++
            }

            //storing the scores through shared Preferences
            val e = sharedPreferences.edit()
            var ii = 0
            for(i in highScore){

                var j = ii+1
                e.putInt("score$j", i)
                ii++
            }
            e.apply()
        }
    }

    private fun draw() {
        surfaceHolder.let {
            if(it.surface.isValid) {
                //locking the canvas
                canvas = surfaceHolder.lockCanvas()
                //drawing a background color for canvas
                canvas.drawColor(Color.BLACK)

                //setting the paint color to white to draw the stars
                paint.color = Color.WHITE

                //drawing all stars
                for (s in stars) {
                    paint.strokeWidth = s.starWidth
                    canvas.drawPoint(s.x.toFloat(), s.y.toFloat(), paint)
                }

                //drawing the score on the game screen
                paint.textSize = 30f
                canvas.drawText("Score: $score",100f,50f,paint)

                //drawing the score on the game screen
                paint.textSize = 30f
                val lives = 3 - countMisses
                canvas.drawText("lives: $lives",300f,50f,paint)

                //Drawing the player
                canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint)

                canvas.drawBitmap(
                    enemies.getBitmap(),
                    enemies.getX(),
                    enemies.getY(),
                    paint
                )

                //drawing boom image
                canvas.drawBitmap(
                    boom.bitmap,
                    boom.x,
                    boom.y,
                    paint
                )

                //drawing friends image
                canvas.drawBitmap(
                    friend.bitmap,
                    friend.x,
                    friend.y,
                    paint
                )

                //draw game Over when the game is over
                if (isGameOver) {
                    paint.textSize = 150f
                    paint.textAlign = Paint.Align.CENTER

                    val yPos = (canvas.height / 2 - (paint.descent() + paint.ascent()) / 2).toInt()
                    canvas.drawText(
                        "Game Over",
                        (canvas.width / 2).toFloat(),
                        yPos.toFloat(),
                        paint
                    )
                }

                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }

    private fun control() {
    }

    suspend fun pause() {
        // Wanneer het spel is gepauzeerd
        // Zet de 'playing' waarde op false, zodat het spel weet dat hij niet meer draait.
        playing = false

        //stopping the thread
        gameThread.join()
    }

    fun resume() {
        //when the game is resumed
        //starting the thread again
        playing = true
        gameThread = createGameThread()
        gameThread.start()
    }

    fun stopMusic() {
        gameOnsound.stop()
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> {
                player.stopBoosting()
            }
            MotionEvent.ACTION_DOWN -> {
                player.startBoosting()
            }
        }

        //if the game's over, tappin on game Over screen sends you to MainActivity
        if (isGameOver) {
            if (motionEvent.action === MotionEvent.ACTION_DOWN) {
                context.startActivity(MainActivity.intent(context))
            }
        }
        return true
    }
}