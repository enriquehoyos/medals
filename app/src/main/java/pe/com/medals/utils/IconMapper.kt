package pe.com.medals.utils

import androidx.annotation.DrawableRes
import pe.com.medals.R

/**
 * Created by Quique on 10/16/2025.
 */

@DrawableRes
fun mapIconNameToRes(name: String): Int = when (name) {
    "medal_novato.png" -> R.drawable.medal_novato
    "medal_cazafijas.png" -> R.drawable.medal_cazafijas
    "medal_capo.png" -> R.drawable.medal_capo
    "medal_constante.png" -> R.drawable.medal_constante
    "medal_leyenda.png" -> R.drawable.medal_leyenda
    "medal_estratega.png" -> R.drawable.medal_estratega
    "medal_album.png" -> R.drawable.medal_album
    "medal_mision.png" -> R.drawable.medal_mision
    "medal_racha.png" -> R.drawable.medal_racha
    "medal_rey.png" -> R.drawable.medal_rey
    else -> R.drawable.medal_default
}