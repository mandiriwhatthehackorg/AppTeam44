package id.co.bankmandiri.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.bankmandiri.R
import id.co.bankmandiri.common.Util
import id.co.bankmandiri.common.api.model.Chat
import id.co.bankmandiri.common.view.Alert
import kotlinx.android.synthetic.main.activity_chat.*

/**
 * Created by hendrawd on 9/6/17.
 */
class ChatActivity : AppCompatActivity() {

    val RQ_TAKE_PHOTO = 1212
    val RQ_TAKE_VIDEO = 1213
    val RQ_TAKE_DIGITAL_SIGNATURE = 1214

    /**
     * Current time stamp.
     *
     * @return
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val layoutManager = LinearLayoutManager(this)
        rvChat.layoutManager = layoutManager
        val chatAdapter = ChatAdapter()
        rvChat.adapter = chatAdapter

        setupActionBar()

        // setupNewMediaPlayer();

        // enable action send keyboard to chat
        etComment.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                onSendChat(null)
                return@OnEditorActionListener true
            }
            false
        })

        friendChat("Hello , perkenalkan saya “mei” asisten pribadi anda apa yang bisa saya bantu?")
        createAnswerButtons(listOf("Buka Rekening Baru"))
        // TODO transition
        // val fade = Fade(Fade.OUT)
        // TransitionManager.beginDelayedTransition(overlayLayer, fade)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == RQ_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
                val ktpFilePath = data!!.getStringExtra("fileAbsolutePath")
                friendChat("“Mei” analisa sebentar ya …… :) (data disinkron dengan data dukcapil)")
                friendChat("data kamu sebagai berikut :\n" +
                        "Nama : Budi Santoso\n" +
                        "Jenis Kelamin : Laki-laki\n" +
                        "Tempat Tanggal Lahir :  Jakarta / 01 Januari 1980\n" +
                        "Alamat :  Jl. Kebun Jeruk 5 RT 6 RW 08  Jakarta barat\n")
                friendChat("Apakah data diatas sudah benar?")
                createAnswerButtons(listOf("Data KTP Benar", "Data KTP Tidak Benar"))
            }
            if (requestCode == RQ_TAKE_VIDEO && resultCode == Activity.RESULT_OK) {
                friendChat("Terima kasih kak data kakak akan diproses selambatnya 2x24 Jam. oh ya ada yang bisa Mei bantu kembali?")
                createAnswerButtons(listOf("Buka Rekening Baru"))
            }
            if (requestCode == RQ_TAKE_DIGITAL_SIGNATURE && resultCode == Activity.RESULT_OK) {
                friendChat("Sebelum berakhir boleh minta video selfi kakak?")
                createAnswerButtons(listOf("Record Video")) {
                    val intent = Intent(
                            this@ChatActivity,
                            VideoActivity::class.java
                    )
                    startActivityForResult(intent, RQ_TAKE_VIDEO)
                }
            }
        } catch (ex: Exception) {
            Alert.showToast(this, ex.toString())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createAnswerButtons(answerList: List<String>, customAction: (() -> Unit)? = null) {
        Handler(mainLooper).postDelayed({
            rvAnswer.apply {
                adapter = AnswerAdapter(answerList) {
                    if (customAction == null) {
                        meChat(answerList[it])
                    } else {
                        customAction()
                    }
                }
            }
        }, 1000L)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Mei"
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun onSendChat(view: View?) {
        val content = etComment.text.toString()
        if (content.isEmpty()) return
        etComment.setText("")
        Util.hideKeyboardFrom(this, etComment)
        meChat(content)
    }

    private fun scrollToBottom() {
        Handler().postDelayed({
            val chatAdapter = rvChat.adapter as ChatAdapter
            rvChat.smoothScrollToPosition(chatAdapter.itemCount)
        }, 1)
    }

    private fun meChat(chatContent: String) {
        val chat = Chat(
                true,
                "Hendra Wijaya Djiono",
                System.currentTimeMillis(),
                chatContent
        )
        chat.setImage("https://avatars0.githubusercontent.com/u/9481791?s=400&u=7b7aa9cc92b4dfbe760cbe5d16c2e76794990e87&v=4")
        val chatAdapter = rvChat.adapter as ChatAdapter
        chatAdapter.add(chat)
        // chatAdapter.notifyItemInserted(chatAdapter.itemCount - 1)
        chatAdapter.notifyDataSetChanged()
        scrollToBottom()

        rvAnswer.adapter = null
        checkAiReply(chatContent)
    }

    private fun checkAiReply(chatContent: String) {
        if (chatContent == "Buka Rekening Baru") {
            friendChat("Ok, Mei bicara dengan siapa?")
        } else if (chatContent == "Budi") {
            friendChat("Enaknya mei panggil apa?")
            createAnswerButtons(listOf("Bapak", "Ibu", "Kakak"))
        } else if (chatContent == "Bapak" || chatContent == "Ibu" || chatContent == "Kakak") {
            friendChat("Ok dech, boleh minta email kakak Budi")
        } else if (chatContent == "budi123@gmail.com") {
            friendChat("Sekalian no HP Kakak Budi donk! Hehe")
        } else if (chatContent == "+6283898986397") {
            friendChat("Terimakasih kak budi, mei akan mengirimkan kode dengan menggunakan SMS, silakan kakak masukan kode unik yang telah kakak terima")
        } else if (chatContent == "6358") {
            friendChat("OK dech kodenya terverifikasi, coba photo indentitas Kakak Budi dech?")
            createAnswerButtons(listOf("Photo KTP")) {
                val intent = Intent(
                        this@ChatActivity,
                        CameraActivity::class.java
                )
                startActivityForResult(intent, RQ_TAKE_PHOTO)
            }
        } else if (chatContent == "Data KTP Benar") {
            friendChat("Wah, ternyata saya pintar ya :)")
        } else if (chatContent == "Data KTP Tidak Benar") {
            friendChat("Alamat Korespondensi kakak sama dengan data diatas?")
            createAnswerButtons(listOf("Alamat Korespondensi Benar", "Alamat Korespondensi Salah"))
        } else if (chatContent == "Alamat Korespondensi Benar") {
            // friendChat("Alamat Korespondensi kakak sama dengan data diatas?")
            friendChat("Mei tidak mengerti")
        } else if (chatContent == "Alamat Korespondensi Salah") {
            friendChat("Boleh Mei Minta alamat lengkap korespondensi kakak?")
        } else if (chatContent == "Jl. Merpati Blok P No 6 Jakarta Barat") {
            friendChat("Oh ya maaf kak budi, nama ibu kandung kakak siapa?")
        } else if (chatContent == "Dona") {
            friendChat("Data Pribadi kakak yang telah kakak Masukan")
            friendChat("Nama : Budi Santoso\n" +
                    "Email : budi123@gmail.com\n" +
                    "No Hp : +6283898986397\n" +
                    "Jenis Kelamin : Laki-laki\n" +
                    "Tempat Tanggal Lahir :  Jakarta / 01 Januari 1980\n" +
                    "Alamat :  Jl. Kebun Jeruk 5 RT 6 RW 08  Jakarta barat\n" +
                    "Alamat Korespondensi : Jl Merpati Blok P No 6 Jakarta barat \n" +
                    "Nama Ibu Kandung : Dona\n")
            friendChat("Apakah data diatas sudah benar kak?")
            createAnswerButtons(listOf("Data Benar", "Data Salah"))
        } else if (chatContent == "Data Benar") {
            friendChat("Oh ya, tinggal beberapa langkah kakak Budi menyelesaikan proses pembukaan rekening, ada beberapa pertanyaan yang ingin Mei tanyakan.")
            friendChat("Tujuan pengaktifan rekening untuk apa kak Budi?")
            createAnswerButtons(listOf("Menabung", "Transaksi Bisnis", "Transaksi Pribadi", "Gaji"))
        } else if (chatContent == "Data Salah") {
            friendChat("Mei tidak mengerti")
        } else if (chatContent == "Transaksi Pribadi") {
            friendChat("Oh  ya, Mei mau tanya lagi sumber dananya kakak dari mana?")
            createAnswerButtons(listOf("Gaji", "Usaha", "Warisan", "Tabungan"))
        } else if (chatContent == "Gaji") {
            friendChat("Tinggal 2 langkah lagi sich Mei mau tanya pendapatan kamu tiap bulan berapa kak?")
            createAnswerButtons(listOf("< 1jt", "1-5 jt", "5-10 jt", "10-25 jt"))
        } else if (chatContent == "10-25 jt") {
            friendChat("Pertanyaan terakhir nich kak, Mei mau tanya pengeluaran kakak Budi perbulan berapa?")
            createAnswerButtons(listOf("< 1jt", "1-5 jt", "5-10 jt", "10-25 jt"))
        } else if (chatContent == "1-5 jt") {
            friendChat("Terima kasih kak, mei mau verifikasi data kakak")
            friendChat("Tujuan pengaktifan rekening : Transaksi Pribadi\n" +
                    "Sumber Dana : Gaji \n" +
                    "Pendapatan Perbulan : 10-25 jt\n" +
                    "Pengeluran Perbulan : 1-5 jt\n")
            friendChat("Apakah data diatas sudah benar?")
            createAnswerButtons(listOf("Ya", "Tidak"))
        } else if (chatContent == "Ya") {
            friendChat("Silakan kakak Budi mencantumkan tanda tangan digital.")
            createAnswerButtons(listOf("Tanda Tangan Digital")){
                val intent = Intent(
                        this@ChatActivity,
                        RegisterDigitalSignatureActivity::class.java
                )
                startActivityForResult(intent, RQ_TAKE_DIGITAL_SIGNATURE)
            }
        }
    }

    private fun friendChat(chatContent: String) {
        Handler(mainLooper).postDelayed({
            val chat = Chat(
                    false,
                    "Mei",
                    System.currentTimeMillis(),
                    chatContent
            )
            // chat.setImage("https://avatars0.githubusercontent.com/u/9481791?s=400&u=7b7aa9cc92b4dfbe760cbe5d16c2e76794990e87&v=4")
            chat.setImage(R.drawable.ic_mascot_bottom)
            val chatAdapter = rvChat.adapter as ChatAdapter
            chatAdapter.add(chat)
            // chatAdapter.notifyItemInserted(chatAdapter.itemCount - 1)
            chatAdapter.notifyDataSetChanged()
            scrollToBottom()
        }, 1_000L)
        // add 1 second delay to make it as if it online and thinking
    }

    // internal fun setupNewMediaPlayer() {
    //     mediaPlayer = MediaPlayer()
    //     mediaPlayer.setOnCompletionListener(MediaPlayer.OnCompletionListener { mp ->
    //         mp.release()
    //         setupNewMediaPlayer()
    //     })
    //     mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp ->
    //         mp.start()
    //         //                playButton.setEnabled(true);
    //     })
    //     mediaPlayer.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
    //         //                playButton.setEnabled(true);
    //         false
    //     })
    // }

    companion object {
        private val TAG = ChatActivity::class.java.name
    }
}
