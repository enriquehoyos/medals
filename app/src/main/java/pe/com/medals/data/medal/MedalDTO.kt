package pe.com.medals.data.medal

import kotlinx.serialization.Serializable
import pe.com.medals.domain.model.Medal

/**
 * Created by Quique on 10/16/2025.
 */

@Serializable
data class MedalDTO(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val category: String,
    val rarity: String,
    val backgroundColor: String,
    val progressColor: String,
    val level: Int? = null,
    val points: Int? = null,
    val maxLevel: Int,
    val reward: String,
    val unlockedAt: String,
    val nextLevelGoal: String? = null,
    val isLocked: Boolean? = null,
    val animationType: String
) {
    fun toDomain(): Medal = Medal(
        id, name, description, icon, category, rarity,
        backgroundColor, progressColor, maxLevel, reward, unlockedAt, animationType
    )
}