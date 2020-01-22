package nl.ajdb.spacefighter.player

import android.graphics.Bitmap
import android.content.Context
import android.graphics.BitmapFactory
import nl.ajdb.spacefighter.R


class Boom(context: Context) {

    //bitmap object
    //getters
    var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.boom)

    //coordinate variables
    //setters for x and y to make it visible at the place of collision
    var x: Float = 0f
    var y: Float = 0f

    init {
        //setting the coordinate outside the screen
        //so that it won't shown up in the screen
        //it will be only visible for a fraction of second
        //after collission
        x = -400f
        y = -400f
    }

}