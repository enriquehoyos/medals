package pe.com.medals.data.progress

import kotlinx.serialization.Serializable

/**
 * Created by Quique on 10/16/2025.
 */
@Serializable
data class MedalProgressDTO(val id: String, val level: Int, val points: Int, val isLocked: Boolean)
