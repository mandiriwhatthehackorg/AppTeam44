package id.co.bankmandiri.common.view

import android.content.Context
import android.util.AttributeSet
import com.github.gcacace.signaturepad.views.SignaturePad
import id.co.bankmandiri.R
import org.jetbrains.anko.backgroundResource

/**
 * @author hendrawd on 19 Mar 2019
 */
class SquareBorderedSignaturePad @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SignaturePad(context, attrs) {

    init {
        backgroundResource = R.drawable.ic_border_black
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}