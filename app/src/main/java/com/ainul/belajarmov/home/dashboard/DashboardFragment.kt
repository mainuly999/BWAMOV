package com.ainul.belajarmov.home.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ainul.belajarmov.R
import com.ainul.belajarmov.model.Film
import com.ainul.belajarmov.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    membuat sambungan ke databse
    private lateinit var preferences: Preferences
    private lateinit var mDatabase: DatabaseReference

    private var dataList = ArrayList<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//    lalu buat untuk oroses pembuatan activitynya
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")

        tv_nama.text = preferences.getValues("nama")
        if (preferences.getValues("saldo").equals("")){
            currency(preferences.getValues("saldo")!!.toDouble(),tv_saldo)
        }
        Glide.with(this)
            .load(preferences.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile)

        rv_now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
//      karena rv_comingsoon normal(vertikal) maka tidak perlu ditambah  LinearLayoutManager.VERTICAL
        rv_coming_soon.layoutManager = LinearLayoutManager(context)

        getData()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
//                kita pake context pada argument di bawah karena ini fragment
                Toast.makeText(context,""+databaseError.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                kita clear datalistnya supaya tidak ada data duplikat
                dataList.clear()
                for(getdataSnapshot in dataSnapshot.children){
                    var film = getdataSnapshot.getValue(Film::class.java)
                    dataList.add(film!!)
                }

                rv_now_playing.adapter = NowPlayingAdapter(dataList){

                }
//                rv_coming_soon.adapter = ComingSoonAdapter(dataList){
//
//                }
            }

        })
    }

    private fun currency(harga: Double, textview: TextView){
//        harga akan diubah menjadi bentuk rupiah dengan fungsi dibawah ini
        val localID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localID)
        textview.setText(format.format(harga))
    }
}