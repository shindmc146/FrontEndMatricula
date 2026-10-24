package com.enrollment.system.Components

import android.app.AlertDialog
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import retrofit2.Response

val Green = Color(0xFF37EE37)
val Red = Color(0xFFBA0E0E)

@Composable
fun Select(title: String, text: String, options: List<String>, whenClick: (Int) -> Unit) {

    var open by remember { mutableStateOf(false) }
    Text(text = title, modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))

    Box{
        OutlinedButton(onClick = {open = true}, modifier = Modifier.fillMaxWidth()) {
            Text(text);
        }

        DropdownMenu(expanded = open, onDismissRequest = { open = false}) {
            for (position in options.indices){
                DropdownMenuItem(
                    text = {Text(text = options[position])},
                    onClick = {whenClick(position)
                        open = false})
            }
        }
    }
}


@Composable
fun ConfirmDialog(message: String, confirm: () -> Unit, cancel: () -> Unit)
{
    AlertDialog(onDismissRequest = cancel,
        title = { Text(text = "¿Está seguro?")},
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = confirm) {
                Text(text = "Sí, borrar", color = Red)
            }
        },
        dismissButton = {
            TextButton(onClick = cancel) {
                Text(text = "Cancelar")
            }
        }


        )
}


@Composable
fun Alert(text: String){
    if (text != "") {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}


fun errorReader(response : Response<Unit>): String{
    val body = response.errorBody();
    if (body == null){
        return "Error " + response.code();
    }

    return body.string().trim();

}

