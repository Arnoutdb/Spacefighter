package nl.ajdb.spacefighter.personas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import nl.ajdb.spacefighter.R
import android.graphics.Rect

class Player(context: Context, screenX: Int, screenY: Int) {

    // De afbeelding van de speler
    private var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.player)

    // De positie van de speler
    private var x = 75f
    private var y = 50f

    // De snelheid van de speler
    private var speed = 1

    private var boosting: Boolean = false

    private val GRAVITY = -10

    private val maxY: Int = screenY - bitmap.height
    private val minY: Int = 0

    //Limit the bounds of the ship's speed
    private val MIN_SPEED = 1
    private val MAX_SPEED = 20

    private val detectCollision: Rect = Rect(x.toInt(), y.toInt(), bitmap.width, bitmap.height)

    fun startBoosting() {
        boosting = true
    }

    fun stopBoosting() {
        boosting = false
    }

    fun update() {
        //if the ship is boosting
        if (boosting) {
            //speeding up the ship
            speed += 2
        } else {
            //slowing down if not boosting
            speed -= 5
        }
        //controlling the top speed
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED
        }
        //if the speed is less than min speed
        //controlling it so that it won't stop completely
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED
        }

        //moving the ship down
        y -= (speed + GRAVITY).toFloat()

        //but controlling it also so that it won't go off the screen
        if (y < minY) {
            y = minY.toFloat()
        }
        if (y > maxY) {
            y = maxY.toFloat()
        }

        //adding top, left, bottom and right to the rect object
        detectCollision.left = x.toInt()
        detectCollision.top = y.toInt()
        detectCollision.right = x.toInt() + bitmap.width
        detectCollision.bottom = y.toInt() + bitmap.height
    }

    //one more getter for getting the rect object
    fun getDetectCollision(): Rect {
        return detectCollision
    }

    fun getBitmap(): Bitmap {
        return bitmap
    }

    fun getX(): Float {
        return x
    }

    fun getY(): Float {
        return y
    }

    fun getSpeed(): Int {
        return speed
    }
}