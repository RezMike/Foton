package io.github.rezmike.foton.utils

import android.text.TextUtils
import android.util.Patterns


fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPasswordValid(): Boolean {
    return !TextUtils.isEmpty(this) && this.length >= 8
}