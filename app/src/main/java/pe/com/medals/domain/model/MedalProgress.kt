package pe.com.medals.domain.model

/**
 * Created by Quique on 10/16/2025.
 */
data class MedalProgress(
    val id: String,
    val level: Int = 1,
    val points: Int = 0,
    val isLocked: Boolean = false
)
