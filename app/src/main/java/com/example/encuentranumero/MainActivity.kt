package com.example.encuentranumero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdivinaApp()
                }
            }
        }
    }
}

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit,
    enabled: Boolean
) {
    val numbers = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "⌫")
    )

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        numbers.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(.7f),  // Ajusta el ancho según tus necesidades
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { number ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        if (number.isNotEmpty()) {
                            Surface(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .fillMaxWidth(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable(enabled = enabled) {
                                        if (number == "⌫") {
                                            onDelete()
                                        } else {
                                            onNumberClick(number)
                                        }
                                    },
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = number,
                                        fontSize = 24.sp,
                                        color = if (enabled) {
                                            MaterialTheme.colorScheme.onSurface
                                        } else {
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true,
    showSystemUi = true,
    widthDp = 360,
    heightDp = 640)


@Composable
fun AdivinaApp() {
    val context = LocalContext.current

    var numeroSecreto by remember { mutableStateOf(0) }
    var nivel by remember { mutableStateOf(1) }
    var intentos by remember { mutableStateOf(0) }
    var juegoActivo by remember { mutableStateOf(false) }
    var numeroIngresado by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mostrarDialogoNivel by remember { mutableStateOf(true) }
    var mostrarDialogoVictoria by remember { mutableStateOf(false) }

    fun iniciarNuevoJuego() {
        val maximo = 10.0.pow(nivel.toDouble()).toInt() - 1
        numeroSecreto = (1..maximo).random()
        intentos = 0
        juegoActivo = true
        numeroIngresado = ""
        mensaje = "¡Comienza a adivinar!"
    }

    fun verificarNumero() {
        val numero = numeroIngresado.toIntOrNull()
        if (numero == null) {
            mensaje = "Por favor, ingresa un número válido"
            return
        }

        intentos++
        mensaje = when {
            numero > numeroSecreto -> "El número es Menor que $numero"
            numero < numeroSecreto -> "El número es Mayor que $numero"
            else -> {
                juegoActivo = false
                mostrarDialogoVictoria = true
                "¡Felicitaciones! ¡Has adivinado el número en $intentos intentos!"
            }
        }
        numeroIngresado = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Nivel: $nivel",
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Adivina el número entre 1 y ${10.0.pow(nivel.toDouble()).toInt() - 1}",
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = mensaje,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        // Mostrar el número ingresado
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = numeroIngresado,
                fontWeight = FontWeight.ExtraBold ,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Teclado numérico personalizado
        NumericKeypad(
            onNumberClick = { digit ->
                if (juegoActivo) {
                    numeroIngresado += digit
                }
            },
            onDelete = {
                if (numeroIngresado.isNotEmpty()) {
                    numeroIngresado = numeroIngresado.dropLast(1)
                }
            },
            enabled = juegoActivo
        )

        Button(
            onClick = { verificarNumero() },
            enabled = juegoActivo && numeroIngresado.isNotEmpty()
        ) {
            Text(text = "Adivinar",
                fontSize = 18.sp,
                color = Color.Black)
        }

        Text(
            text = "Intentos: $intentos",
            fontSize = 24.sp,
            color = Color.Blue

        )

        Button(
            onClick = { mostrarDialogoNivel = true }
        ) {
            Text("Cambiar Nivel")
        }
    }
    // Diálogo para seleccionar nivel
    if (mostrarDialogoNivel) {
        AlertDialog(
            onDismissRequest = {
                if (juegoActivo) mostrarDialogoNivel = false
            },
            title = { Text("Elige el nivel") },
            text = {
                Column {
                    for (i in 1..6) {
                        TextButton(
                            onClick = {
                                nivel = i
                                mostrarDialogoNivel = false
                                iniciarNuevoJuego()
                            }
                        ) {
                            Text("Nivel $i (1-${10.0.pow(i.toDouble()).toInt() - 1})")
                        }
                    }
                }
            },
            confirmButton = {
                if (juegoActivo) {
                    TextButton(onClick = { mostrarDialogoNivel = false }) {
                        Text("Cancelar")
                    }
                }
            }
        )
    }

    // Diálogo de victoria
    if (mostrarDialogoVictoria) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("¡Felicitaciones!", color = Color.Blue) },
            text = { Text(text = "Puntaje:" +
                    " ${ 10.0.pow(nivel.toDouble()).toInt() * (100 -intentos) }" +
                    "\n \n ¿Quieres jugar otra vez?" ,
                    fontSize = 20.sp)


                   },


            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoVictoria = false
                        mostrarDialogoNivel = true
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Convierte el contexto en una Activity y llama a finish()
                        (context as? ComponentActivity)?.finish()
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}