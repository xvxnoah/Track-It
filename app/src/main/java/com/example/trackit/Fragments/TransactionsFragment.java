package com.example.trackit.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Adapters.AdapterTransactions;
import com.example.trackit.Model.Transaction;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

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
    private ArrayList<Transaction> transactionVos;
    private TextView expense, income;
    private PieChart mPieChart;
    private CardView card;

    private UserInfo userInfo;

    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database Reference for Firebase.
    DatabaseReference databaseReference;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerViewTransaction;

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
        userInfo = UserInfo.getInstance();

        recyclerViewTransaction = vista.findViewById(R.id.recentRecyclerView);
        recyclerViewTransaction.setLayoutManager(new LinearLayoutManager(getContext()));
        expense = vista.findViewById(R.id.expense);
        income = vista.findViewById(R.id.ingressos);
        mPieChart = (PieChart) vista.findViewById(R.id.piechart);
        card = vista.findViewById(R.id.card_icon_transaction);

        firebaseDatabase = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");

        // Getting text from our edittext fields.
        SharedPreferences preferences = getActivity().getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferences.getString(AuthActivity.USER, null);
        email = email.replace('.', ',');

        // Below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("users/" + email);

        // Attach a listener to read the data at our posts reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(com.example.trackit.Model.UserInfo.class);
                com.example.trackit.Model.UserInfo.setUniqueInstance(userInfo);
                transactionVos = new ArrayList<>();
                fillUpList();
                    AdapterTransactions adapter = new AdapterTransactions(transactionVos);
                    recyclerViewTransaction.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return vista;
    }

    public void fillUpList() {
        ArrayList<Transaction> transactions = userInfo.getTransactions();
        Transaction transaction;
        Double despeses = .0;
        Double ingressos = .0;

        if(transactions != null){
            for(int i = 0; i < transactions.size(); i++){
                transaction = transactions.get(transactions.size() - i - 1);
                String type = transaction.getType();

                if(type.equals("Alimentació")){
                    transaction.setPic(R.drawable.ic_baseline_fastfood_24);
                    despeses += transaction.getQuantity();
                } else if(type.equals("Compres")){
                    transaction.setPic(R.drawable.ic_baseline_shopping_cart_24);
                    despeses += transaction.getQuantity();
                } else if(type.equals("Transport")){
                    transaction.setPic(R.drawable.ic_baseline_directions_transit_24);
                    despeses += transaction.getQuantity();
                } else if(type.equals("Salut/Higiene")){
                    transaction.setPic(R.drawable.person);
                    despeses += transaction.getQuantity();
                } else if(type.equals("Educació")){
                    transaction.setPic(R.drawable.ic_baseline_auto_stories_24);
                    despeses += transaction.getQuantity();
                } else if(type.equals("Altres despeses")){
                    transaction.setPic(R.drawable.transaction);
                    despeses += transaction.getQuantity();
                } else if(type.equals("Nòmina")){
                    transaction.setPic(R.drawable.ic_baseline_attach_money_24);
                    ingressos += transaction.getQuantity();
                } else if(type.equals("Criptomonedes")){
                    transaction.setPic(R.drawable.ic_currency_btc);
                    ingressos += transaction.getQuantity();
                } else if(type.equals("Accions")){
                    transaction.setPic(R.drawable.ic_cash_100);
                    ingressos += transaction.getQuantity();
                } else if(type.equals("Altres ingressos")){
                    transaction.setPic(R.drawable.transaction);
                    ingressos += transaction.getQuantity();
                }
                transactionVos.add(transaction);
            }
        }

        despeses = Math.abs(despeses);
        mPieChart.clearChart();
        mPieChart.addPieSlice(new PieModel("Ingressos", ingressos.floatValue(), Color.parseColor("#00A86B")));
        mPieChart.addPieSlice(new PieModel("Despeses", despeses.floatValue(), Color.parseColor("#FD3C4A")));

        mPieChart.setDrawValueInPie(false);
        mPieChart.startAnimation();

        expense.setText(despeses.toString());
        income.setText(ingressos.toString());
    }
}