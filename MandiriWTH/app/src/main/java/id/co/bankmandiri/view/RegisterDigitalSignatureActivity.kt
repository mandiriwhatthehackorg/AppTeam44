package id.co.bankmandiri.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import id.co.bankmandiri.R
import id.co.bankmandiri.common.extension.loadEmailAddress
import id.co.bankmandiri.common.extension.observeLoading
import id.co.bankmandiri.common.extension.tempFile
import id.co.bankmandiri.common.extension.viewModel
import id.co.bankmandiri.common.view.Alert
import id.co.bankmandiri.viewmodel.UploadDigitalSignatureViewModel
import kotlinx.android.synthetic.main.activity_register_digital_signature.*
import kotlinx.android.synthetic.main.layout_loading.*
import org.jetbrains.anko.startActivity

class RegisterDigitalSignatureActivity : BaseActivity() {

    private val vm by lazy {
        viewModel<UploadDigitalSignatureViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_digital_signature)
        "Tanda Tangan Digital".actionBarTitle()

        vm.hasOngoingRequest.observeLoading(
            this,
            loadingLayer
        )

        bClear.setOnClickListener {
            signaturePad.clear()
        }
        bSend.setOnClickListener {
            val emailAddress = loadEmailAddress()
            if (emailAddress.isNotEmpty()) {
                Alert.showToast(this, "Sending digital signature to server")
                val svgString = signaturePad.signatureSvg
                val tempFile = tempFile("digitalSignature", "digitalSignature.svg")
                // TODO writeToFile(tempFile, svgString)
                // vm.uploadDigitalSignature(emailAddress, tempFile)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        vm.uploadDigitalSignatureResponseObservable.observe(
            this,
            Observer {
                it?.apply {
                    // TODO startActivity<RegisterTermAndConditionActivity>()
                }
            }
        )
    }
}
