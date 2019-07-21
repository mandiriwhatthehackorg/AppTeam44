package id.co.bankmandiri.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.bankmandiri.R
import id.co.bankmandiri.common.extension.gone
import id.co.bankmandiri.common.extension.hyperlinkStyle
import id.co.bankmandiri.common.extension.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_pop_up.view.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivMascot.setOnClickListener { startActivity<ChatActivity>() }
        ivLogin.setOnClickListener { showLayoutPopUp() }
        layoutPopUp.tvForgetCode.hyperlinkStyle()
        layoutPopUp.setOnClickListener { layoutPopUp.gone() }
    }

    private fun showLayoutPopUp() {
        layoutPopUp.visible()
    }
}
