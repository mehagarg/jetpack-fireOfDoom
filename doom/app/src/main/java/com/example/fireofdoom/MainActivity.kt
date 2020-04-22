package com.example.fireofdoom

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.Recompose
import androidx.compose.state
import androidx.ui.core.DrawScope
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Canvas
import androidx.ui.geometry.Rect
import androidx.ui.graphics.Color
import androidx.ui.graphics.Paint
import androidx.ui.graphics.PaintingStyle
import androidx.ui.graphics.withSave
import androidx.ui.layout.fillMaxSize
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoomCompose()
        }
    }
}

//@Model
//data class DoomState(var offset: Float = 300f + (Random.nextFloat()*100))
//
//
//@Composable
//fun DoomCompose(
//    state: DoomState = DoomState()
//) {
//    DoomCompose(state = state)
//
//    val handler = Handler()
//
//    val propagate: Runnable = object : Runnable {
//        override fun run() {
//            handler.postDelayed(this, 500)
//            state.offset = 300f + (Random.nextFloat() * 100)
//        }
//    }
//    propagate.run()
//}
//
//@Composable
//fun DoomCanvas(
//    state: DoomState,
//    measurements: (Int, Int) -> Unit
//) {
//    var measured = false
//    Canvas(modifier = Modifier.fillMaxSize()) {
////        if(!measured){
////            measured = true
////            measurements(
////                size.width.value.toInt(),
////                size.height.value.toInt()
////            )
////        }
//        withSave { drawRect(state.offset) }
//    }
//}
//
//private fun DrawScope.drawRect(offset: Float) {
//    drawRect(
//        rect = Rect(
//            left = 0f + offset,
//            top = 0f + offset,
//            right = size.width.value - offset,
//            bottom = size.height.value - offset
//        ),
//        paint = Paint().apply {
//            style = PaintingStyle.fill
//            color = Color.Red
//        }
//    )
//}


/**
 * https://adambennett.dev/2020/04/adventures-in-compose-the-doom-fire-effect/
 *
 */
