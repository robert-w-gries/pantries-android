package org.endhungerdurham.pantries.ui.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.endhungerdurham.pantries.R
import org.endhungerdurham.pantries.ui.fragment.MapFragment

class RootMapFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.root_map_fragment, container, false)

        if (savedInstanceState == null) {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.root_map_fragment, MapFragment())
            transaction?.commit()
        }

        return view
    }
}