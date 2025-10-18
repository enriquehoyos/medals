package pe.com.medals.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import java.util.Random

/**
 * Created by Quique on 10/16/2025.
 */


@Composable
fun ConfettiOverlay(key: Int) {
    val particles = remember(key) { List(60) { Particle.random() } }
    val anim = rememberInfiniteTransition(label = "confetti")
    val t by anim.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "t"
    )
    Canvas(Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        particles.forEach { p ->
            val x = p.x * w
            val y = (p.y + (t * p.speedY)) % 1f * h
            drawCircle(color = p.color, radius = p.size, center = Offset(x, y))
        }
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val speedY: Float,
    val size: Float,
    val color: androidx.compose.ui.graphics.Color
) {
    companion object {
        fun random(): Particle {
            val r = Random(System.nanoTime())
            return Particle(
                x = r.nextFloat(),
                y = r.nextFloat(),
                speedY = 0.7f + r.nextFloat(),
                size = 6f + r.nextFloat() * 14f,
                color = Color(r.nextFloat(), r.nextFloat(), r.nextFloat())
            )
        }
    }
}