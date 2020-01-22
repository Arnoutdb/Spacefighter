package nl.ajdb.spacefighter.player

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import nl.ajdb.spacefighter.R
import java.util.*

class Friend(context: Context, private val maxX: Float, private val maxY: Float) {

    //getters
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.friend)
    var x: Float = 0f
        private set
    var y: Float = 0f
        private set
    private var speed = 1
    private val minX: Float = 0f
    private val minY: Float = 0f

    private var detectCollision: Rect = Rect(x.toInt(), y.toInt(), bitmap.width, bitmap.height)

    init {
        val generator = Random()
        speed = generator.nextInt(6) + 10
        x = maxX
        y = ((0 + bitmap.height .. maxY.toInt()).random()) - bitmap.height * 1f
    }

    fun update(playerSpeed: Int) {
        x -= playerSpeed
        x -= speed
        if (x < minX - bitmap.width) {
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

    fun getDetectCollision(): Rect {
        return detectCollision
    }

}