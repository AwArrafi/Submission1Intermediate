package com.example.submission1intermediate.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText(context: Context, attrs: AttributeSet?) : TextInputLayout(context, attrs) {
    init {
        val editText = EditText(context)
        editText.inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length ?: 0 < 8) {
                    error = "Password harus minimal 8 karakter"
                } else {
                    error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        addView(editText)
    }
}