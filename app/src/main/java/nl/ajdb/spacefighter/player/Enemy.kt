package nl.ajdb.spacefighter.player

import android.graphics.Bitmap
import android.content.Context
import android.graphics.BitmapFactory
import java.util.*
import android.graphics.Rect
import nl.ajdb.spacefighter.R

class Enemy(
    context: Context, //min and max coordinates to keep the enemy inside the screen
    private val screenX: Float, private val screenY: Float
) {

    //bitmap for the enemy
    //we have already pasted the bitmap in the drawable folder
    //getters
    private val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)

    //x and y coordinates
    private var x: Float = 0f
        private set
    private var y: Float = 0f
        private set

    //enemy speed
    private var speed = 1
        private set
    //min and max coordinates to keep the enemy inside the screen
    private val maxX: Float = screenX
    private val minX: Float = 0f

    private val maxY: Float = screenY
    private val minY: Float = 0f + bitmap.height

    //creating a rect object
    private val detectCollision: Rect = Rect(x.toInt(), y.toInt(), bitmap.width, bitmap.height)

    init {

        //generating a random coordinate to add enemy
        val generator = Random()
        speed = generator.nextInt(6) + 10
        x = maxX
        y = ((0 + bitmap.height .. maxY.toInt()).random()) - bitmap.height * 1f

    }

    fun update(playerSpeed: Int) {
        //decreasing x coordinate so that enemy will move right to left
        x -= playerSpeed
        x -= speed
        //if the enemy reaches the left edge
        if (x < minX - bitmap.width) {
            //adding the enemy again to the right edge
            val generator = Random()
            speed = generator.nextInt(10) + 10
            x = maxX
            y = ((0 + bitmap.height .. maxY.toInt()).random()) - bitmap.height * 1f
        }

        //Adding the top, left, bottom and right to the rect object
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

    fun setX(x: Float) {
        this.x = x
    }

    fun setY(y: Float) {
        this.y = y
    }

    fun getSpeed(): Int {
        return speed
    }

}