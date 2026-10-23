package com.enrollment.system.Components

import android.graphics.Color.GREEN
import android.graphics.Color.RED
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val Green = Color(0xFF37EE37)
val Red = Color(0xFFEC2323)

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
