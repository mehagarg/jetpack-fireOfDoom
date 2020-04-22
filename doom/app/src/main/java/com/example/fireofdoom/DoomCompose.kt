package com.example.fireofdoom

import android.location.GnssMeasurement
import android.view.Choreographer
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.remember
import androidx.ui.core.DrawScope
import androidx.ui.core.Modifier
import androidx.ui.foundation.Canvas
import androidx.ui.geometry.Rect
import androidx.ui.graphics.Paint
import androidx.ui.layout.fillMaxSize
import com.example.fireofdoom.models.*
import kotlin.math.floor
import kotlin.random.Random

/**
 * You may have noticed that we’re using a List in the @Model,
 * not the Array that we’re mutating.
 * It turns out that an Array breaks the Compose compiler;
 * this I imagine is due to the fact that an Array in a data class
 * is a tricky thing and you need to override equals and hashcode.
 * A nice little gotcha which is easily bypassed - we simply set pixels using pixelsArray.toList().
 */
@Model
data class DoomState(var pixels: List<Int> = emptyList())

@Composable
fun DoomCompose(
    state: DoomState = DoomState()
) {

    DoomCanvas(state = state){ canvas ->
        setUpFireView(canvas, state)
    }
}

@Composable
fun DoomCanvas(
    state: DoomState,
    measurements: (CanvasMeasurements) -> Unit
) {
    val paint = remember { Paint() }
    var measured = false

    Canvas(modifier = Modifier.fillMaxSize()) {

        val width = size.width.value.toInt()
        val height = size.height.value.toInt()
        val canvasState = CanvasMeasurements(width = width, height = height)

        if (!measured) {
            measured = true
            measurements(canvasState)
        }
        // Draw
        if(state.pixels.isNotEmpty()){
            renderFire(
                paint = paint,
                firePixels = state.pixels,
                heightPixels = canvasState.heightPixel,
                widthPixels = canvasState.widthPixel,
                pixelSize = canvasState.pixelSize
            )
        }
    }
}

fun DrawScope.renderFire(
    paint: Paint,
    firePixels: List<Int>,
    heightPixels: Int,
    widthPixels: Int,
    pixelSize: Int
) {
    /*
            According to Fabien’s blog post, this is the pseudocode used to spread the fire:
            function doFire() {
                for(x = 0 ; x < FIRE_WIDTH; x++) {
                    for (y = 1; y < FIRE_HEIGHT; y++) {
                        spreadFire(y * FIRE_WIDTH + x);
                    }
                }
            }

            function spreadFire(src) {
                firePixels[src - FIRE_WIDTH] = firePixels[src] - 1;
            }
    */
    for (column in 0 until widthPixels) {
        for (row in 0 until heightPixels - 1) {
            drawRect(
                rect = Rect(
                    left = (column * pixelSize).toFloat(),
                    top = (row * pixelSize).toFloat(),
                    right = ((column + 1) * pixelSize).toFloat(),
                    bottom = ((row + 1) * pixelSize).toFloat()
                ),
                paint = paint.apply {
                    val currentPixelIndex = column + (widthPixels * row)
                    val currentPixel = firePixels[currentPixelIndex]
                    color = fireColors[currentPixel]
                }
            )
        }
    }
}

fun setUpFireView(canvas: CanvasMeasurements, doomState: DoomState,   windDirection: WindDirection = WindDirection.Right) {
//    Now that we have these measurements we can actually generate the pixel Array, where we initialise every pixel to an intensity of 0, ie a black pixel.
    val arraySize = canvas.widthPixel * canvas.heightPixel
    val pixelArray = IntArray(arraySize) { 0 }
        .apply { createFireSource(this, canvas) }

    val callback = object : Choreographer.FrameCallback{
        override fun doFrame(frameTimeNanos: Long) {
            calculateFirePropagation(
                firePixels = pixelArray,
                canvasMeasurements = canvas,
                windDirection = windDirection
            )
            doomState.pixels = pixelArray.toList()

            Choreographer.getInstance().postFrameCallback(this)
        }

    }
    Choreographer.getInstance().postFrameCallback(callback)
}

private fun createFireSource(
    firePixels: IntArray,
    canvas: CanvasMeasurements
) {
    /**
     * Finally we add the fire source itself, which is a line of pixels at the bottom of the screen
     * where the intensity is the maximum - in this case 36, or fireColors.size - 1
     * to avoid having a magic number floating about:
     */
    val overFlowFireIndex = canvas.widthPixel * canvas.heightPixel

    for (column in 0 until canvas.widthPixel) {
        val pixelIndex = (overFlowFireIndex - canvas.widthPixel) + column
        firePixels[pixelIndex] = fireColors.size - 1
    }
}

private fun calculateFirePropagation(
    firePixels: IntArray,
    canvasMeasurements: CanvasMeasurements,
    windDirection: WindDirection
) {
    for (column in 0 until canvasMeasurements.widthPixel) {
        for (row in 1 until canvasMeasurements.heightPixel) {
            val currentPixelIndex = column + (canvasMeasurements.widthPixel * row)
            updateFireIntensityPerPixel(
                currentPixelIndex,
                firePixels,
                canvasMeasurements,
                windDirection
            )
        }
    }
}

private fun updateFireIntensityPerPixel(
    currentPixelIndex: Int,
    firePixels: IntArray,
    measurements: CanvasMeasurements,
    windDirection: WindDirection
) {
    val bellowPixelIndex = currentPixelIndex + measurements.widthPixel
    if (bellowPixelIndex >= measurements.widthPixel * measurements.heightPixel) return

    val offset = if (measurements.tallerThanWide) 2 else 3
    val decay = floor(Random.nextDouble() * offset).toInt()
    val bellowPixelFireIntensity = firePixels[bellowPixelIndex]
    val newFireIntensity = when {
        bellowPixelFireIntensity - decay >= 0 -> bellowPixelFireIntensity - decay
        else -> 0
    }

    val newPosition = when (windDirection) {
        WindDirection.Right -> if (currentPixelIndex - decay >= 0) currentPixelIndex - decay else currentPixelIndex
        WindDirection.Left -> if (currentPixelIndex + decay >= 0) currentPixelIndex + decay else currentPixelIndex
        WindDirection.None -> currentPixelIndex
    }

    firePixels[newPosition] = newFireIntensity
}
