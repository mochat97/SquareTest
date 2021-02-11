package com.mshaw.squaretest.util.extensions

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

fun AlertDialog.Builder.withCancelButton(title: String = "Cancel", listener: ((dialog: DialogInterface, which: Int) -> Unit)? = null): AlertDialog.Builder {
    return setNegativeButton(title, listener)
}