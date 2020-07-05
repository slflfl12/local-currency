package kr.ac.hansung.localcurrency.ui.map.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hansung.localcurrency.R
import kr.ac.hansung.localcurrency.databinding.ItemResultBinding
import kr.ac.hansung.localcurrency.ui.model.PlaceUIData

class ResultAdapter : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>(){

    private var items = arrayListOf<PlaceUIData>()

    var itemClickListener: ItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = DataBindingUtil.inflate<ItemResultBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_result, parent, false
        )
        val holder = ResultViewHolder(binding)
        holder.itemView.setOnClickListener {
            itemClickListener?.itemClick(items[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    fun setItems(places: List<PlaceUIData>) {
        items.clear()
        items.addAll(places)
        notifyDataSetChanged()
    }

    fun submitList(new: List<PlaceUIData>) {
        val old = items
        val diffResult:DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ResultDiffUtil(old, new)
        )
        items = ArrayList(new)
        diffResult.dispatchUpdatesTo(this)
    }

    class ResultViewHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(placeUiData: PlaceUIData) {
            binding.uiData = placeUiData
        }

    }

    interface ItemClickListener {
        fun itemClick(placeUiData: PlaceUIData)
    }

    inner class ResultDiffUtil(
        val oldItems: List<PlaceUIData>,
        val newItems: List<PlaceUIData>
    ) : DiffUtil.Callback() {

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].title == newItems[newItemPosition].title
        }

        override fun getNewListSize(): Int = newItems.size

        override fun getOldListSize(): Int = oldItems.size

    }

}