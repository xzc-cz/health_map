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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.healthmap.R
import com.example.healthmap.ui.theme.HealthMapTheme
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

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    start = HealthMapTheme.dimensions.spacingMedium,
                    bottom = HealthMapTheme.dimensions.spacingXS
                )
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = HealthMapTheme.dimensions.spacingXS),
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
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        InputType.PASSWORD -> Icon(
                            modifier = Modifier.clickable {
                                inputType = InputType.TEXT
                                displayValue = value
                            },
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_visibility),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        else -> {}
                    }
                }
            },
            placeholder = { 
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            shape = RoundedCornerShape(HealthMapTheme.dimensions.radiusLarge),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                cursorColor = MaterialTheme.colorScheme.primary
            )
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

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    start = HealthMapTheme.dimensions.spacingMedium,
                    bottom = HealthMapTheme.dimensions.spacingXS
                )
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = HealthMapTheme.dimensions.spacingXS),
            enabled = !disabled,
            value = displayValue,
            onValueChange = {
                displayValue = it
                val cleaned = formatter.cleanup(it)
                val parsed = cleaned.toDoubleOrNull()
                if (parsed != null) {
                    onValueChanged(parsed)
                }
            },
            leadingIcon = {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            placeholder = { 
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            visualTransformation = DecimalInputVisualTransformation(formatter),
            shape = RoundedCornerShape(HealthMapTheme.dimensions.radiusLarge),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                cursorColor = MaterialTheme.colorScheme.primary
            )
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
    var displayValue by remember { mutableStateOf("") }

    Box(modifier = modifier, contentAlignment = Alignment.BottomStart) {
        TextInput(
            displayValue,
            label,
            InputType.DATE,
            disabled,
            false,
            toDate(selectedDate),
            Icons.Outlined.DateRange,
            modifier.align(Alignment.Center)
        ) {
            displayValue = it
        }
        if (!disabled) {
            TextInput(
                " ",
                "",
                InputType.TEXT,
                true,
                false,
                "",
                null,
                modifier
                    .align(Alignment.Center)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        DatePickerDialog(
                            context,
                            { _, y, m, d -> onDateSelected(LocalDate.of(y, m + 1, d)) },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
            ) {}
        }
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
    val now = LocalDate.now()
    val calendar = Calendar.getInstance().apply {
        set(now.year, now.monthValue - 1, now.dayOfMonth, selectedTime.hour, selectedTime.minute)
    }
    var displayValue by remember { mutableStateOf("") }

    Box(modifier = modifier, contentAlignment = Alignment.BottomStart) {
        TextInput(displayValue, label, InputType.TIME, disabled, false, toTime(selectedTime), ImageVector.vectorResource(id = R.drawable.ic_outline_schedule), modifier.align(Alignment.Center)) {
            displayValue = it
        }
        if (!disabled) {
            TextInput(" ", "", InputType.TEXT, true, false, "", null, modifier.align(Alignment.Center).clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                TimePickerDialog(context, { _, h, m -> onTimeSelected(LocalTime.of(h, m)) },
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
            }) {}
        }
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
