package id.co.bankmandiri.common.extension

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.View.*

/**
 * @author hendrawd on 26 Jan 2019
 */
fun View.visible() {
    visibility = VISIBLE
}

fun View.gone() {
    visibility = GONE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun Context.startActivity(cls: Class<*>) {
    startActivity(Intent(this, cls))
}

infix fun View.goTo(cls: Class<*>) {
    setOnClickListener { context.startActivity(cls) }
}