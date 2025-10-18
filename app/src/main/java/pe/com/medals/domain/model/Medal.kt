package pe.com.medals.domain.model

/**
 * Created by Quique on 10/16/2025.
 */
data class Medal(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val category: String,
    val rarity: String,
    val backgroundColor: String,
    val progressColor: String,
    val maxLevel: Int,
    val reward: String,
    val unlockedAt: String,
    val animationType: String

)
