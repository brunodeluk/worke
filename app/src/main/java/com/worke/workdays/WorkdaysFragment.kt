package com.worke.workdays

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.worke.R
import com.worke.adapters.WorkdayAdapter
import com.worke.data.Workday
import com.worke.data.source.DefaultWorkdayRepository

class WorkdaysFragment : Fragment() {

    private lateinit var workdaysRecyclerView: RecyclerView
    private lateinit var viewAdapter: WorkdayAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var repository: DefaultWorkdayRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = DefaultWorkdayRepository.getRepository(activity?.application!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workdays, container, false)

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = WorkdayAdapter()

        this.workdaysRecyclerView = view.findViewById<RecyclerView>(R.id.workdays_rv).apply {
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        repository.observeWorkdays()
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    viewAdapter.setData(snapshot.documents.map { it.toObject(Workday::class.java) })
                }
            }

        return view.rootView
    }



}
