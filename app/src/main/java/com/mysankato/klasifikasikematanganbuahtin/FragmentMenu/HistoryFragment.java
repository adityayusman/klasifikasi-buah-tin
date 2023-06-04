package com.mysankato.klasifikasikematanganbuahtin.FragmentMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mysankato.klasifikasikematanganbuahtin.DataModel.HasilKlasifikasiDao;
import com.mysankato.klasifikasikematanganbuahtin.DataModel.RoomDB;
import com.mysankato.klasifikasikematanganbuahtin.HasilKlasifikasiRecyclerView;
import com.mysankato.klasifikasikematanganbuahtin.R;

public class HistoryFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    ImageButton deleteBtn;
    HasilKlasifikasiDao hasilKlasifikasiDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_history, container, false);
        deleteBtn = view.findViewById(R.id.deleteBtn);

        recyclerView = view.findViewById(R.id.recyclerview);

        hasilKlasifikasiDao = RoomDB.getInstance(this.getContext()).hasilKlasifikasiDao();

        HasilKlasifikasiRecyclerView adapter = new HasilKlasifikasiRecyclerView(this.getContext(), hasilKlasifikasiDao.getAll());

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}