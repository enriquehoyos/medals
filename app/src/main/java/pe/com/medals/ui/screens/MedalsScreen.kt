package pe.com.medals.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.com.medals.R
import pe.com.medals.domain.model.UiMedal
import pe.com.medals.ui.theme.White
import pe.com.medals.ui.theme.hexToColor
import pe.com.medals.utils.ConfettiOverlay
import pe.com.medals.utils.mapIconNameToRes
import pe.com.medals.viewmodel.ProfileViewModel

/**
 * Created by Quique on 10/16/2025.
 */
@Composable
fun MedalsScreen(
    vm: ProfileViewModel,
    onBack: () -> Unit
) {
    val all by vm.medals.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Pausa/reanuda
    LifecycleResumeEffect(Unit) {
        vm.resumeEngine()
        onPauseOrDispose { vm.pauseEngine() }
    }

    // Confeti SOLO cuando hay level-up
    var showConfetti by remember { mutableStateOf(false) }
    var confettiKey by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        vm.levelUp.collect {
            confettiKey++
            showConfetti = true
            delay(1800)
            showConfetti = false
        }
    }

    LaunchedEffect(Unit) {
        vm.pointsGained.collect { inc ->
            Toast.makeText(context, "Ha ganado $inc puntos", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        vm.resetEvent.collect {
            Toast.makeText(context, "Se han reiniciado los niveles", Toast.LENGTH_SHORT).show()
        }
    }

    // Reset con 5 taps
    var taps by remember { mutableStateOf(0) }
    var lastTap by remember { mutableStateOf(0L) }
    val tapWindow = 1200L
    fun onHeaderTap() {
        val now = System.currentTimeMillis()
        taps = if (now - lastTap <= tapWindow) taps + 1 else 1
        lastTap = now
        if (taps >= 5) {
            scope.launch { vm.resetAll() }
            taps = 0
        }
    }

    Scaffold { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (all.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val maxLevel = all.size
                val trackProg = all.first().prog
                val level = trackProg.level.coerceIn(1, maxLevel)
                val points = trackProg.points
                val stageIndex = (level - 1).coerceIn(0, maxLevel - 1)
                val stageDef = all[stageIndex].def
                val completed = (level >= maxLevel && points == 0)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(mapIconNameToRes(stageDef.icon)),
                            contentDescription = stageDef.name,
                            modifier = Modifier.size(96.dp)
                        )
                        Spacer(Modifier.height(12.dp))

                        Text(stageDef.name, style = MaterialTheme.typography.titleLarge)
                        Text(stageDef.description, style = MaterialTheme.typography.bodyMedium)

                        Text(
                            text = stageDef.category,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(16.dp))

                        if (completed) {
                            Text(
                                text = "¡Ha completado todos los niveles!",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Text("Nivel $level/$maxLevel")
                            LinearProgressIndicator(
                                progress = (points / 100f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                color = hexToColor(stageDef.progressColor),
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            Text("$points/100", style = MaterialTheme.typography.labelSmall)
                        }

                        Spacer(Modifier.height(20.dp))

                        OutlinedButton(onClick = onBack) {
                            Text("Volver")
                        }
                    }
                }


            }

            if (showConfetti) {
                ConfettiOverlay(key = confettiKey)
            }
        }
    }
}
