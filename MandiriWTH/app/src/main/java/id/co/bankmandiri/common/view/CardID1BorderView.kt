package id.co.bankmandiri.common.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.SizeUtils

/**
 * ISO 7810 (ID-1)
 * 85.60 mm x 53.98 mm
 * 8.56 cm x 5.398 cm
 * Width to height ratio = 1.585772508336421
 * Height to width ratio = 0.630607476635514
 * Usually for:
 * - ATM card,
 * - Indonesian ID card (KTP/Kartu Tanda Penduduk),
 * - Student card,
 * - Library card, etc
 *
 * @author hendrawd on 02 Apr 2019
 */
class CardID1BorderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        @JvmField
        val widthToHeightRatio = 1.585772508336421
        @JvmField
        val heightToWidthRatio = 0.630607476635514
    }

    init {
        createBorder(Color.parseColor("#FFFFFF"))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // Timber.d("widthMeasureSpec = " + widthMeasureSpec);
        // Timber.d("heightMeasureSpec = " + heightMeasureSpec);
        // Timber.d("getMeasuredWidth = " + getMeasuredWidth());
        // Timber.d("getMeasuredHeight = " + getMeasuredHeight());
        val newHeight = measuredWidth * heightToWidthRatio
        // Timber.d("new height = " + newHeight);
        // Timber.d("int new height = " + (int) newHeight);
        setMeasuredDimension(measuredWidth, newHeight.toInt())
    }

    private fun createBorder(borderColor: Int) {
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            val cornerRadius = SizeUtils.dp2px(8f).toFloat()
            cornerRadii = floatArrayOf(
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius
            )
            // set background if needed
            // shape.setColor(backgroundColor);
            val strokeWidth = SizeUtils.dp2px(2f)
            setStroke(strokeWidth, borderColor)
        }
    }
}