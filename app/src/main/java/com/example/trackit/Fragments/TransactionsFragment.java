package com.example.trackit.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trackit.Adapters.AdapterTransactions;
import com.example.trackit.R;
import com.example.trackit.ViewModel.TransactionVo;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerViewTransaction;
    ArrayList<TransactionVo> transactionVos;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionsFragment newInstance(String param1, String param2) {
        TransactionsFragment fragment = new TransactionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_transactions, container, false);

        transactionVos = new ArrayList<>();
        recyclerViewTransaction = vista.findViewById(R.id.transactionRecyclerView);
        recyclerViewTransaction.setLayoutManager(new LinearLayoutManager(getContext()));

        //TextView tv = vista.findViewById(R.id.ingressos);
        //tv.setText("50");

        fillUpList();

        AdapterTransactions adapter = new AdapterTransactions(transactionVos);
        recyclerViewTransaction.setAdapter(adapter);

        return vista;
    }

    private void fillUpList() {
        transactionVos.add(new TransactionVo("Accions", "Ingrés", 50.0, "28/04/2022", R.drawable.ic_baseline_fastfood_24));

        transactionVos.add(new TransactionVo("Robatori", "Compres", 150.0, "28/04/2022", R.drawable.ic_baseline_shopping_cart_24));

        transactionVos.add(new TransactionVo("Accions", "Ingrés", 50.0, "28/04/2022", R.drawable.ic_baseline_fastfood_24));

        transactionVos.add(new TransactionVo("Robatori", "Compres", 150.0, "28/04/2022", R.drawable.ic_baseline_shopping_cart_24));

        transactionVos.add(new TransactionVo("Accions", "Ingrés", 50.0, "28/04/2022", R.drawable.ic_baseline_fastfood_24));

        transactionVos.add(new TransactionVo("Robatori", "Compres", 150.0, "28/04/2022", R.drawable.ic_baseline_shopping_cart_24));

        transactionVos.add(new TransactionVo("Accions", "Ingrés", 50.0, "28/04/2022", R.drawable.ic_baseline_fastfood_24));

        transactionVos.add(new TransactionVo("Robatori", "Compres", 150.0, "28/04/2022", R.drawable.ic_baseline_shopping_cart_24));
    }
}