package nl.ajdb.spacefighter.views

import java.util.*

class Star(private val maxX: Int, private val maxY: Int) {
    var x: Int = 0
        private set
    var y: Int = 0
        private set
    private var speed: Int = 0
    private val minX: Int
    private val minY: Int

    //Making the star width random so that
    //it will give a real look
    val starWidth: Float
        get() {
            val minX = 1.0f
            val maxX = 4.0f
            val rand = Random()
            return rand.nextFloat() * (maxX - minX) + minX
        }


    init {
        minX = 0
        minY = 0
        val generator = Random()
        speed = generator.nextInt(10)

        //generating a random coordinate
        //but keeping the coordinate inside the screen size
        x = generator.nextInt(maxX)
        y = generator.nextInt(maxY)
    }

    fun update(playerSpeed: Int) {
        //animating the star horizontally left side
        //by decreasing x coordinate with player speed
        x -= playerSpeed
        x -= speed
        //if the star reached the left edge of the screen
        if (x < 0) {
            //again starting the star from right edge
            //this will give a infinite scrolling background effect
            x = maxX
            val generator = Random()
            y = generator.nextInt(maxY)
            speed = generator.nextInt(15)
        }
    }
}