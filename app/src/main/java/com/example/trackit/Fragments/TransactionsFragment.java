package com.example.trackit.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private TextView expense, income, textTransactions;
    private PieChart mPieChart;
    private CardView card;
    SimpleDateFormat sdf;
    Integer mesActual;
    MaterialButton gener, febrer, marc, abril, maig, juny, juliol, agost, setembre, octubre, novembre, desembre;

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

        recyclerViewTransaction = vista.findViewById(R.id.recentRecyclerViewTransactions);
        recyclerViewTransaction.setLayoutManager(new LinearLayoutManager(getContext()));
        expense = vista.findViewById(R.id.expense);
        income = vista.findViewById(R.id.ingressos);
        mPieChart = (PieChart) vista.findViewById(R.id.piechart);
        card = vista.findViewById(R.id.card_icon_transaction);
        textTransactions = vista.findViewById(R.id.transactionsInFragment);
        iniciateButtons(vista);

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

                try{
                    fillUpList();
                } catch (ParseException e){
                    e.printStackTrace();
                }

                AdapterTransactions adapter = new AdapterTransactions(transactionVos);
                recyclerViewTransaction.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        monthListeners();
        provideActualMonth();

        return vista;
    }

    private void iniciateButtons(View vista){
        gener = vista.findViewById(R.id.gener);
        febrer = vista.findViewById(R.id.febrer);
        marc = vista.findViewById(R.id.marc);
        abril = vista.findViewById(R.id.abril);
        maig = vista.findViewById(R.id.maig);
        juny = vista.findViewById(R.id.juny);
        juliol = vista.findViewById(R.id.juliol);
        agost = vista.findViewById(R.id.agost);
        setembre = vista.findViewById(R.id.setembre);
        octubre = vista.findViewById(R.id.octubre);
        novembre = vista.findViewById(R.id.novembre);
        desembre = vista.findViewById(R.id.desembre);
    }

    private void monthListeners() {
        gener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(gener);
            }
        });

        febrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(febrer);
            }
        });

        marc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(marc);
            }
        });

        abril.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(abril);
            }
        });

        maig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(maig);
            }
        });

        juny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(juny);
            }
        });

        juliol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(juliol);
            }
        });

        agost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(agost);
            }
        });

        setembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(setembre);
            }
        });

        octubre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(octubre);
            }
        });

        novembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(novembre);
            }
        });

        desembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonth(desembre);
            }
        });
    }

    private void provideActualMonth(){
        Integer month = new Date().getMonth();

        if(month == 0){
            setMonth(gener);
        } else if(month == 1){
            setMonth(febrer);
        } else if(month == 2){
            setMonth(marc);
        } else if(month == 3){
            setMonth(abril);
        } else if(month == 4){
            setMonth(maig);
        } else if(month == 5){
            setMonth(juny);
        } else if(month == 6){
            setMonth(juliol);
        } else if(month == 7){
            setMonth(agost);
        } else if(month == 8){
            setMonth(setembre);
        } else if(month == 9){
            setMonth(octubre);
        } else if(month == 10){
            setMonth(novembre);
        } else if(month == 11){
            setMonth(desembre);
        }

    }

    private void setMonth(MaterialButton button) {
        String month = button.getText().toString();

        if(month.equals("Gener")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 0;
        } else if(month.equals("Febrer")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 1;
        } else if(month.equals("Març")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 2;
        } else if(month.equals("Abril")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 3;
        } else if(month.equals("Maig")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 4;
        } else if(month.equals("Juny")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 5;
        } else if(month.equals("Juliol")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 6;
        } else if(month.equals("Agost")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 7;
        } else if(month.equals("Setembre")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(octubre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 8;
        } else if(month.equals("Octubre")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(novembre);
            removeBackground(desembre);
            mesActual = 9;
        } else if(month.equals("Novembre")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(desembre);
            mesActual = 10;
        } else if(month.equals("Desembre")){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainBlue));
            button.setStrokeColorResource(R.color.mainBlue);
            removeBackground(gener);
            removeBackground(febrer);
            removeBackground(marc);
            removeBackground(abril);
            removeBackground(maig);
            removeBackground(juny);
            removeBackground(juliol);
            removeBackground(agost);
            removeBackground(setembre);
            removeBackground(octubre);
            removeBackground(novembre);
            mesActual = 11;
        }

        transactionVos = new ArrayList<>();
        try{
            fillUpList();
        } catch (ParseException e){
            e.printStackTrace();
        }

        AdapterTransactions adapter = new AdapterTransactions(transactionVos);
        recyclerViewTransaction.setAdapter(adapter);
    }

    public void fillUpList() throws ParseException{
        ArrayList<Transaction> transactions = userInfo.getTransactions();
        Transaction transaction;
        Double despeses = .0;
        Double ingressos = .0;
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        Integer monthTransaction;

        if(transactions != null){
            for(int i = 0; i < transactions.size(); i++){
                transaction = transactions.get(transactions.size() - i - 1);
                monthTransaction = sdf.parse(transaction.getDate()).getMonth();

                if(monthTransaction.equals(mesActual)){
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
            if(!transactionVos.isEmpty()){
                textTransactions.setText("Les teves transaccions");
                despeses = Math.abs(despeses);
                mPieChart.clearChart();
                mPieChart.addPieSlice(new PieModel("Ingressos", ingressos.floatValue(), Color.parseColor("#00A86B")));
                mPieChart.addPieSlice(new PieModel("Despeses", despeses.floatValue(), Color.parseColor("#FD3C4A")));

                mPieChart.setDrawValueInPie(false);
                mPieChart.startAnimation();
            } else{
                mPieChart.clearChart();
                mPieChart.startAnimation();
                textTransactions.setText("No hi ha transaccions");
            }
        } else{
            textTransactions.setText("No hi ha transaccions");
        }

        expense.setText(despeses.toString());
        income.setText(ingressos.toString());
    }

    private void removeBackground(MaterialButton button){
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
        button.setStrokeColorResource(R.color.secondary);
    }
}