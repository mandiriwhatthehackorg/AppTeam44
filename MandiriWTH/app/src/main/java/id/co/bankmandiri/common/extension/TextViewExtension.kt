package id.co.bankmandiri.common.extension

import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.URLSpan
import android.widget.TextView

/**
 * @author hendrawd on 26 Jan 2019
 */
val TextView.string: String
    get() = text.toString()

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.hyperlinkStyle() {
    setText(
        SpannableString(text).apply {
            setSpan(
                URLSpan(""),
                0,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        },
        TextView.BufferType.SPANNABLE
    )
}

fun String.setTextTo(vararg textViews: TextView) {
    for (textView in textViews) {
        textView.text = this
    }
}