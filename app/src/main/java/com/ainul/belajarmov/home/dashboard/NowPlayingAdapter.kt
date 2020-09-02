package com.ainul.belajarmov.home.dashboard

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ainul.belajarmov.model.Film

class NowPlayingAdapter(private var data: List<Film>,
                        private var listener:(Film) -> Unit)
    : RecyclerView.Adapter<NowPlayingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NowPlayingAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: NowPlayingAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}
