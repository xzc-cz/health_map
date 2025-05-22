package com.example.healthmap.ui.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.healthmap.R
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

enum class InputType {
    TEXT, PASSWORD, NUMBER, DATE, TIME
}

@Composable
fun Input(
    value: Any,
    label: String = "",
    type: InputType = InputType.TEXT,
    disabled: Boolean = false,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    modifier: Modifier,
    onValueChange: (Any) -> Unit,
) {
    when (type) {
        InputType.TEXT -> TextInput(value.toString(), label, type, disabled, true, placeholder, leadingIcon, modifier) { onValueChange(it) }
        InputType.PASSWORD -> TextInput(value.toString(), label, type, disabled, true, placeholder, leadingIcon, modifier) { onValueChange(it) }
        InputType.NUMBER -> NumberInput(value as Double, label, disabled, leadingIcon, modifier) { onValueChange(it) }
        InputType.DATE -> DateInput(value as LocalDate, label, disabled, modifier) { onValueChange(it) }
        InputType.TIME -> TimeInput(value as LocalTime, label, disabled, modifier) { onValueChange(it) }
    }
}

@Composable
private fun TextInput(
    value: String,
    label: String,
    type: InputType,
    disabled: Boolean,
    enableInput: Boolean,
    placeholder: String,
    leadingIcon: ImageVector?,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
) {
    var inputType by remember { mutableStateOf(type) }
    var trueValue by remember { mutableStateOf("") }
    var displayValue by remember { mutableStateOf("") }

    Column {
        if (label.isNotEmpty()) {
            Text(text = label, modifier = Modifier.padding(start = 12.dp, bottom = 2.dp))
        }
        OutlinedTextField(
            modifier = modifier.padding(vertical = 2.dp).fillMaxWidth(),
            enabled = !disabled,
            value = displayValue,
            onValueChange = {
                if (enableInput) {
                    if (trueValue.length < it.length) {
                        trueValue += it.substring(trueValue.length)
                    } else {
                        trueValue = trueValue.substring(0, it.length)
                    }
                    onValueChange(trueValue)
                    displayValue = if (inputType == InputType.TEXT) it else "*".repeat(it.length)
                } else {
                    onValueChange("")
                    displayValue = ""
                }
            },
            leadingIcon = {
                if (leadingIcon != null) Icon(imageVector = leadingIcon, contentDescription = null)
            },
            trailingIcon = {
                if (value.isNotEmpty() && type == InputType.PASSWORD) {
                    when (inputType) {
                        InputType.TEXT -> Icon(
                            modifier = Modifier.clickable {
                                inputType = InputType.PASSWORD
                                displayValue = "*".repeat(value.length)
                            },
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_visibility_off),
                            contentDescription = null
                        )
                        InputType.PASSWORD -> Icon(
                            modifier = Modifier.clickable {
                                inputType = InputType.TEXT
                                displayValue = value
                            },
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_visibility),
                            contentDescription = null
                        )
                        else -> {}
                    }
                }
            },
            placeholder = { Text(text = placeholder) },
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun NumberInput(
    number: Double = 0.0,
    label: String = "",
    disabled: Boolean = false,
    leadingIcon: ImageVector? = ImageVector.vectorResource(id = R.drawable.ic_outline_pin),
    modifier: Modifier,
    onValueChanged: (Double) -> Unit
) {
    val formatter = DecimalFormatter(DecimalFormatSymbols(Locale.US))
    var displayValue by remember { mutableStateOf("") }

    Column {
        if (label.isNotEmpty()) {
            Text(text = label, modifier = Modifier.padding(start = 12.dp, bottom = 2.dp))
        }
        OutlinedTextField(
            modifier = modifier.padding(vertical = 2.dp).fillMaxWidth(),
            enabled = !disabled,
            value = displayValue,
            onValueChange = {
                displayValue = it // 保留用户输入的完整内容
                val cleaned = formatter.cleanup(it) // 清洗只用于转换
                val parsed = cleaned.toDoubleOrNull()
                if (parsed != null) {
                    onValueChanged(parsed)
                }
            },
            leadingIcon = {
                if (leadingIcon != null) Icon(imageVector = leadingIcon, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            placeholder = { Text(text = number.toString()) },
            visualTransformation = DecimalInputVisualTransformation(formatter)
        )
    }
}

@Composable
private fun DateInput(
    selectedDate: LocalDate,
    label: String,
    disabled: Boolean,
    modifier: Modifier,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        set(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)
    }
    val displayValue = remember(selectedDate) { toDate(selectedDate) }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(text = label, modifier = Modifier.padding(start = 12.dp, bottom = 2.dp))
        }
        OutlinedTextField(
            value = displayValue,
            onValueChange = {},
            enabled = !disabled,
            readOnly = true,
            leadingIcon = { Icon(Icons.Outlined.DateRange, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !disabled) {
                    DatePickerDialog(
                        context,
                        { _, y, m, d -> onDateSelected(LocalDate.of(y, m + 1, d)) },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun TimeInput(
    selectedTime: LocalTime,
    label: String,
    disabled: Boolean,
    modifier: Modifier,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    val displayValue = remember(selectedTime) { toTime(selectedTime) }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(text = label, modifier = Modifier.padding(start = 12.dp, bottom = 2.dp))
        }
        OutlinedTextField(
            value = displayValue,
            onValueChange = {},
            enabled = !disabled,
            readOnly = true,
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_schedule),
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !disabled) {
                    val now = LocalTime.now()
                    TimePickerDialog(
                        context,
                        { _, h, m -> onTimeSelected(LocalTime.of(h, m)) },
                        selectedTime.hour,
                        selectedTime.minute,
                        true
                    ).show()
                },
            shape = RoundedCornerShape(8.dp)
        )
    }
}


private class DecimalFormatter(symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()) {
    private val thousandsSeparator = symbols.groupingSeparator
    private val decimalSeparator = symbols.decimalSeparator
    private val minusSign = symbols.minusSign

    fun cleanup(input: String): String {
        val sb = StringBuilder()
        var hasDecimal = false
        var hasMinus = false

        for (ch in input) {
            if (ch.isDigit()) {
                sb.append(ch)
            } else if (ch == decimalSeparator && !hasDecimal) {
                sb.append(ch)
                hasDecimal = true
            } else if ((ch == minusSign || ch == '-') && !hasMinus && sb.isEmpty()) {
                sb.append(ch)
                hasMinus = true
            }
        }

        return sb.toString()
    }


    fun formatForVisual(input: String): String {
        val parts = input.split(decimalSeparator)
        val intPart = parts[0].reversed().chunked(3).joinToString(thousandsSeparator.toString()).reversed()
        return if (parts.size == 2) "$intPart$decimalSeparator${parts[1]}" else intPart
    }
}

private class DecimalInputVisualTransformation(private val formatter: DecimalFormatter) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val formatted = formatter.formatForVisual(raw)
        return TransformedText(AnnotatedString(formatted), object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = formatted.length
            override fun transformedToOriginal(offset: Int) = raw.length
        })
    }
}

private fun toDate(date: LocalDate): String = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
private fun toTime(time: LocalTime): String = time.format(DateTimeFormatter.ofPattern("HH:mm"))
