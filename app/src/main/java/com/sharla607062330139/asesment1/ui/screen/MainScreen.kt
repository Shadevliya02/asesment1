@file:Suppress("DEPRECATION")

package com.sharla607062330139.asesment1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sharla607062330139.asesment1.ui.theme.Asesment1Theme
import com.sharla607062330139.asesment1.R
import com.sharla607062330139.asesment1.navigation.Screen
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    var input1 by rememberSaveable { mutableStateOf("") }
    var input1Error by rememberSaveable { mutableStateOf(false) }
    var input2 by rememberSaveable { mutableStateOf("") }
    var input2Error by rememberSaveable { mutableStateOf(false) }
    val operationOptions = listOf(
        R.drawable.tambah,
        R.drawable.kurang,
        R.drawable.kali,
        R.drawable.bagi
    )
    var operation by rememberSaveable { mutableIntStateOf(operationOptions[0]) }
    var result by rememberSaveable { mutableFloatStateOf(0f) }
    var language by rememberSaveable { mutableStateOf("ENG") }
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val currentLocale = if (language == "ENG") Locale("en") else Locale("in")
    val newConfig = configuration.apply {
        setLocale(currentLocale)
    }
    context.resources.updateConfiguration(newConfig, context.resources.displayMetrics)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = stringResource(R.string.tentang_aplikasi),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = {
                            val operationName = when (operation) {
                                R.drawable.tambah -> if (language == "ENG") "Add" else "Tambah"
                                R.drawable.kurang -> if (language == "ENG") "Subtract" else "Kurang"
                                R.drawable.kali -> if (language == "ENG") "Multiply" else "Kali"
                                R.drawable.bagi -> if (language == "ENG") "Divide" else "Bagi"
                                else -> if (language == "ENG") "Unknown" else "Tidak Dikenal"
                            }

                            val message = context.getString(
                                R.string.bagikan_template,
                                input1, input2, operationName, result
                            )

                            shareData(context, message)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = stringResource(R.string.bagikan),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Switch(
                            checked = language == "IND",
                            onCheckedChange = { isChecked ->
                                language = if (isChecked) "IND" else "ENG"
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(text = if (language == "IND") "IND" else "ENG")
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(
            modifier = Modifier.padding(innerPadding),
            input1 = input1,
            input1Error = input1Error,
            input2 = input2,
            input2Error = input2Error,
            operation = operation,
            result = result,
            operationOptions = operationOptions,
            onInput1Change = { input1 = it },
            onInput2Change = { input2 = it },
            onOperationChange = { operation = it },
            onResultChange = { result = it },
            onInput1ErrorChange = { input1Error = it },
            onInput2ErrorChange = { input2Error = it },
            onReset = {
                input1 = ""
                input2 = ""
                result = 0f
                input1Error = false
                input2Error = false
            }
        )
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier,
    input1: String,
    input1Error: Boolean,
    input2: String,
    input2Error: Boolean,
    operation: Int,
    result: Float,
    operationOptions: List<Int>,
    onInput1Change: (String) -> Unit,
    onInput2Change: (String) -> Unit,
    onOperationChange: (Int) -> Unit,
    onResultChange: (Float) -> Unit,
    onInput1ErrorChange: (Boolean) -> Unit,
    onInput2ErrorChange: (Boolean) -> Unit,
    onReset: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.calculate_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = input1,
            onValueChange = { onInput1Change(it) },
            label = { Text(text = stringResource(R.string.input_1)) },
            isError = input1Error,
            supportingText = { if (input1Error) Text(text = stringResource(R.string.input_invalid)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = input2,
            onValueChange = { onInput2Change(it) },
            label = { Text(text = stringResource(R.string.input_2)) },
            isError = input2Error,
            supportingText = { if (input2Error) Text(text = stringResource(R.string.input_invalid)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Transparent, RoundedCornerShape(4.dp))
        ) {
            operationOptions.forEach { drawable ->
                OperationOptionImage(
                    imageRes = drawable,
                    isSelected = operation == drawable,
                    modifier = Modifier
                        .clickable { onOperationChange(drawable) }
                        .weight(1f)
                        .padding(16.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Button(
                onClick = {
                    val input1ErrorLocal = (input1 == "" || input1.toFloatOrNull() == null)
                    val input2ErrorLocal = (input2 == "" || input2.toFloatOrNull() == null)

                    onInput1ErrorChange(input1ErrorLocal)
                    onInput2ErrorChange(input2ErrorLocal)

                    if (input1ErrorLocal || input2ErrorLocal) return@Button

                    onResultChange(calculateResult(input1.toFloat(), input2.toFloat(), operation))
                },
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.hitung))
            }

            Button(
                onClick = {
                    onReset()
                },
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.reset))
            }
        }

        if (result != 0f) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            val formattedResult = if (result % 1 == 0f) {
                result.toInt().toString()
            } else {
                "%.2f".format(result)
            }

            Text(
                text = context.getString(R.string.result, formattedResult),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun OperationOptionImage(
    imageRes: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun calculateResult(input1: Float, input2: Float, operation: Int): Float {
    return when (operation) {
        R.drawable.tambah -> input1 + input2
        R.drawable.kurang -> input1 - input2
        R.drawable.kali -> input1 * input2
        R.drawable.bagi -> if (input2 != 0f) input1 / input2 else Float.NaN
        else -> 0f
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asesment1Theme {
        MainScreen(rememberNavController())
    }
}
