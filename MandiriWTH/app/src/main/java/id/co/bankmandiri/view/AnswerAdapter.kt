package id.co.bankmandiri.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.bankmandiri.R
import kotlinx.android.synthetic.main.item_answer.view.*

class AnswerAdapter(private val answerList: List<String>,
                    private val itemClickListener: (position: Int) -> Unit)
    : RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_answer, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(answerList[position])
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    inner class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                itemClickListener(adapterPosition)
            }
        }

        fun bind(answer: String) {
            with(itemView) {
                tvAnswer.text = answer
            }
        }
    }
}