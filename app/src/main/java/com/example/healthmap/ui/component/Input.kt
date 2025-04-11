package com.example.healthmap.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.healthmap.R

enum class InputType {
    TEXT,
    PASSWORD
}

@Composable
fun Input(
    value: String = "",
    label: String = "",
    type: InputType = InputType.TEXT,
    placeholder: String = "",
    leadingIcon: ImageVector,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
) {
    var inputType by remember { mutableStateOf(type) }
    var trueValue by remember { mutableStateOf("") }
    var displayValue by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth(1f)) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 2.dp)
            )
        }
        OutlinedTextField(
            modifier = modifier.padding(10.dp, 2.dp),
            value = displayValue,
            onValueChange = {
                if (trueValue.length < it.length) {
                    trueValue += it.substring(trueValue.length)
                } else {
                    trueValue = trueValue.substring(0, it.length)
                }
                onValueChange(trueValue)
                displayValue = if (inputType == InputType.TEXT) {
                    it
                } else {
                    "*".repeat(it.length)
                }
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon, contentDescription = null)
            },
            trailingIcon = {
                if (value.isNotEmpty()) { // When the input value is  ot empty
                    // Show password button
                    Row(modifier = Modifier.padding(24.dp, 0.dp)) {
                        if (type == InputType.PASSWORD) { // When the input type is password
                            when (inputType) {
                                InputType.TEXT -> Icon(
                                    modifier = Modifier.clickable(
                                        onClick = {
                                            inputType = InputType.PASSWORD
                                            displayValue = "*".repeat(value.length)
                                        }
                                    ),
                                    imageVector = ImageVector.vectorResource(
                                        id = R.drawable.ic_outline_visibility_off
                                    ),
                                    contentDescription = null
                                )
                                InputType.PASSWORD -> Icon(
                                    modifier = Modifier.clickable(
                                        onClick = {
                                            inputType = InputType.TEXT
                                            displayValue = value
                                        }
                                    ),
                                    imageVector = ImageVector.vectorResource(
                                        id = R.drawable.ic_outline_visibility
                                    ),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            },
            placeholder = {
                Text(text = placeholder)
            },
            shape = RoundedCornerShape(8.dp)
        )
    }
}