package com.example.healthmap.ui.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.healthmap.R
import com.mapbox.maps.extension.style.expressions.dsl.generated.number
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

enum class InputType {
    TEXT,
    PASSWORD,
    NUMBER,
    DATE,
    TIME
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
        InputType.TEXT -> TextInput(
            value = value.toString(),
            label = label,
            type = InputType.TEXT,
            disabled = disabled,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            modifier = modifier
        ) { onValueChange(it) }

        InputType.PASSWORD -> TextInput(
            value = value.toString(),
            label = label,
            type = InputType.PASSWORD,
            disabled = disabled,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            modifier = modifier
        ) { onValueChange(it) }

        InputType.NUMBER -> NumberInput(
            number = value as Double,
            label = label,
            disabled = disabled,
            leadingIcon = leadingIcon,
            modifier = modifier
        ) { onValueChange(it) }

        InputType.DATE -> DateInput(
            selectedDate = value as LocalDate,
            label = label,
            disabled = disabled,
            modifier = modifier
        ) { onValueChange(it) }

        InputType.TIME -> TimeInput(
            selectedTime = value as LocalTime,
            label = label,
            disabled = disabled,
            modifier = modifier
        ) { onValueChange(it) }
    }
}

@Composable
private fun TextInput(
    value: String = "",
    label: String = "",
    type: InputType = InputType.TEXT,
    disabled: Boolean = false,
    enableInput: Boolean = true,
    placeholder: String = "",
    leadingIcon: ImageVector?,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
) {
    var inputType by remember { mutableStateOf(type) }
    var trueValue by remember { mutableStateOf("") }
    var displayValue by remember { mutableStateOf("") }

    Column {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 2.dp)
            )
        }
        OutlinedTextField(
            modifier = modifier
                .padding(10.dp, 2.dp)
                .fillMaxWidth(),
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
                    displayValue = if (inputType == InputType.TEXT) {
                        it
                    } else {
                        "*".repeat(it.length)
                    }
                } else {
                    onValueChange("")
                    displayValue = ""
                }
            },
            leadingIcon = {
                if (leadingIcon != null)
                    Icon(imageVector = leadingIcon, contentDescription = null)
            },
            trailingIcon = {
                if (value.isNotEmpty()) { // When the input value is  ot empty
                    // Show password button
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

                            InputType.NUMBER -> {}
                            InputType.DATE -> {}
                            InputType.TIME -> {}
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

@Composable
private fun NumberInput(
    number: Double = 0.0,
    label: String = "",
    disabled: Boolean = false,
    leadingIcon: ImageVector? = ImageVector.vectorResource(
        id = R.drawable.ic_outline_pin
    ),
    modifier: Modifier,
    onValueChanged: (Double) -> Unit
) {
    val formatter = DecimalFormatter(symbols = DecimalFormatSymbols(Locale.US))
    var trueValue by remember { mutableDoubleStateOf(0.0) }
    var displayValue by remember { mutableStateOf("") }

    Column {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 2.dp)
            )
        }
        OutlinedTextField(
            modifier = modifier
                .padding(10.dp, 2.dp)
                .fillMaxWidth(),
            enabled = !disabled,
            value = displayValue,
            onValueChange = {
                trueValue = if (it.length == 1 && it == "-") {
                    displayValue = it
                    0.0
                } else {
                    displayValue = formatter.cleanup(it)
                    when (displayValue.isNotEmpty()) {
                        true -> if (decimalPointAtLast(displayValue)) {
                            displayValue.replace(".", "").toDouble()
                        } else {
                            displayValue.toDouble()
                        }

                        false -> 0.0
                    }
                }
                onValueChanged(trueValue)
            },
            leadingIcon = {
                if (leadingIcon != null)
                    Icon(
                        imageVector = leadingIcon, contentDescription = null
                    )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            placeholder = {
                Text(text = number.toString())
            },
            visualTransformation = DecimalInputVisualTransformation(formatter)
        )
    }
}

@Composable
private fun DateInput(
    selectedDate: LocalDate,
    label: String = "",
    disabled: Boolean = false,
    modifier: Modifier,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        set(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)
    }
    var displayValue by remember { mutableStateOf("") }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        TextInput(
            value = displayValue,
            label = label,
            disabled = disabled,
            enableInput = false,
            placeholder = toDate(selectedDate),
            leadingIcon = Icons.Outlined.DateRange,
            modifier = modifier.align(Alignment.Center)
        ) { displayValue = it }
        if (!disabled) {
            TextInput(
                label = if (label.isNotEmpty()) " " else "",
                disabled = true,
                leadingIcon = null,
                modifier = modifier
                    .align(Alignment.Center)
                    .clickable( // no ripple clickable
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
            ) { }
        }
    }
}

@Composable
private fun TimeInput(
    selectedTime: LocalTime,
    label: String = "",
    disabled: Boolean = false,
    modifier: Modifier,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    val currentDate = LocalDate.now()
    val calendar = Calendar.getInstance().apply {
        set(
            currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth,
            selectedTime.hour, selectedTime.minute, selectedTime.second
        )
    }
    var displayValue by remember { mutableStateOf("") }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        TextInput(
            value = displayValue,
            label = label,
            disabled = disabled,
            enableInput = false,
            placeholder = toTime(selectedTime),
            leadingIcon = ImageVector.vectorResource(
                id = R.drawable.ic_outline_schedule
            ),
            modifier = modifier.align(Alignment.Center)
        ) { displayValue = it }
        if (!disabled) {
            TextInput(
                label = if (label.isNotEmpty()) " " else "",
                disabled = true,
                leadingIcon = null,
                modifier = modifier
                    .align(Alignment.Center)
                    .clickable( // no ripple clickable
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                onTimeSelected(LocalTime.of(hourOfDay, minute))
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    }
            ) { }
        }
    }
}

private class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {

    private val thousandsSeparator = symbols.groupingSeparator
    private val decimalSeparator = symbols.decimalSeparator
    private val minusSign = symbols.minusSign

    fun isNumeric(s: String): Boolean {
        return try {
            s.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun cleanup(input: String): String {
        if (!isNumeric(input))return ""
        if (input.matches("0+".toRegex())) return "0"


        val sb = StringBuilder()

        var hasDecimalSep = false
        var hasMinusSign = false

        for (char in input) {
            if (char.isDigit()) {
                sb.append(char)
                continue
            }
            if (char == decimalSeparator && !hasDecimalSep && sb.isNotEmpty()) {
                sb.append(char)
                hasDecimalSep = true
            }
            if ((char == minusSign || char == '-') && !hasMinusSign && sb.isEmpty()) {
                sb.append(char)
                hasMinusSign = true
            }
        }

        return sb.toString()
    }

    fun formatForVisual(input: String): String {

        val split = input.split(decimalSeparator)

        val intPart = split[0]
            .reversed()
            .chunked(3)
            .joinToString(separator = thousandsSeparator.toString())
            .reversed()

        val fractionPart = split.getOrNull(1)

        return if (fractionPart == null) intPart else intPart + decimalSeparator + fractionPart
    }
}

private class DecimalInputVisualTransformation(
    private val decimalFormatter: DecimalFormatter
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {

        val inputText = text.text
        val formattedNumber = decimalFormatter.formatForVisual(inputText)

        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        val offsetMapping = FixedCursorOffsetMapping(
            contentLength = inputText.length,
            formattedContentLength = formattedNumber.length
        )

        return TransformedText(newText, offsetMapping)
    }
}

private class FixedCursorOffsetMapping(
    private val contentLength: Int,
    private val formattedContentLength: Int,
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = formattedContentLength
    override fun transformedToOriginal(offset: Int): Int = contentLength
}

private fun decimalPointAtLast(text: String): Boolean {
    var count = 0
    for (ch in text) {
        if (ch == '.') {
            count++
        }

        if (count > 1) {
            break
        }
    }

    return text.isNotEmpty() &&
            text.last() == '.' &&
            count == 1
}

private fun toDate(localDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDate.format(formatter)
}

private fun toTime(localTime: LocalTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return localTime.format(formatter)
}