package com.example.trackit.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Model.Transaction;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    ValueLineSeries serie;
    ValueLineChart mCubicValueLineChart;
    SimpleDateFormat sdf;
    ArrayList Transactions;

    UserInfo userInfo;

    View vista;

    TextView balance;
    TextView QuantityDespeses;
    TextView QuantityIngressos;
    Spinner tendencia;

    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for our object class
    com.example.trackit.Model.UserInfo UserInfo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        vista = inflater.inflate(R.layout.fragment_home, container, false);
        balance = vista.findViewById(R.id.balance);
        QuantityDespeses = vista.findViewById(R.id.QuantityDespeses);
        QuantityIngressos = vista.findViewById(R.id.QuantityIngressos);

        setSpinner();

        firebaseDatabase = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");

        // Getting text from our edittext fields.
        SharedPreferences preferences = getActivity().getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferences.getString(AuthActivity.USER, null);
        email = email.replace('.', ',');

        userInfo = com.example.trackit.Model.UserInfo.getInstance();

        // Below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("users/" + email);

        // Attach a listener to read the data at our posts reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(com.example.trackit.Model.UserInfo.class);
                com.example.trackit.Model.UserInfo.setUniqueInstance(userInfo);
                try {
                    updateFragment();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        tendencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    updateFragment();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        return vista;
    }

    public void updateFragment() throws ParseException {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        Transactions = userInfo.getTransactions();
        balance.setText(Double.toString(userInfo.getQuantity()) + '€');
        QuantityIngressos.setText(Double.toString(userInfo.getMoneySaved()) + '€');
        QuantityDespeses.setText(Double.toString(userInfo.getMoneyWasted()) + '€');

        mCubicValueLineChart = (ValueLineChart) vista.findViewById(R.id.cubiclinechart);
        mCubicValueLineChart.setEmptyDataText("");

        serie = new ValueLineSeries();
        serie.setColor(0xFF56B7F1);

        String seleccio = tendencia.getSelectedItem().toString();

        if(seleccio.equals("últim any")){
            chartYear();
        } else if(seleccio.equals("Últims 6 mesos")){
            chartHalfYear();
        }
    }

    private void chartYear() throws ParseException {
        Iterator<Transaction> iter = null;
        Transaction actual;
        Integer monthActual;
        if (Transactions != null) {
            iter = Transactions.iterator();
        }
        ArrayList<String> Months = new ArrayList<>();

        Months.add("Jan");
        Months.add("Feb");
        Months.add("Mar");
        Months.add("Apr");
        Months.add("May");
        Months.add("Jun");
        Months.add("Jul");
        Months.add("Aug");
        Months.add("Sep");
        Months.add("Oct");
        Months.add("Nov");
        Months.add("Dec");

        ArrayList<Float> TransMonth = new ArrayList<>();
        for (int y = 0; y < 12; y++) {
            TransMonth.add((float) 0);
        }

        double NumAnterior;
        String lastMonth = "null";
        if (Transactions != null) {
            while (iter.hasNext()) {
                actual = iter.next();
                String da = actual.getDate();
                monthActual = sdf.parse(actual.getDate()).getMonth();
                NumAnterior = TransMonth.get(monthActual) + actual.getQuantity();
                TransMonth.set(monthActual, (float) NumAnterior);
            }
        }

        Date actualDate = new Date();
        int actualMonth = actualDate.getMonth();

        for (int i = 0; i < 12; i++) {
            if (actualMonth < 0) {
                actualMonth = 11;
            } else {
                actualMonth--;
            }
        }

        for (int j = 0; j < 12; j++) {
            if (actualMonth == 12) {
                actualMonth = 0;
            }
            String s = Months.get(0);
            serie.addPoint(new ValueLinePoint(Months.get(actualMonth), TransMonth.get(actualMonth)));
            actualMonth++;
        }

        mCubicValueLineChart.clearChart();
        mCubicValueLineChart.addSeries(serie);
        mCubicValueLineChart.startAnimation();
    }

    private void chartHalfYear() throws ParseException {
        Iterator<Transaction> iter = null;
        Transaction actual;
        Integer monthActual;
        if (Transactions != null) {
            iter = Transactions.iterator();
        }
        ArrayList<String> Months = new ArrayList<>();

        Months.add("Jan");
        Months.add("Feb");
        Months.add("Mar");
        Months.add("Apr");
        Months.add("May");
        Months.add("Jun");
        Months.add("Jul");
        Months.add("Aug");
        Months.add("Sep");
        Months.add("Oct");
        Months.add("Nov");
        Months.add("Dec");

        ArrayList<Float> TransMonth = new ArrayList<>();
        for (int y = 0; y < 12; y++) {
            TransMonth.add((float) 0);
        }

        double NumAnterior;
        String lastMonth = "null";
        if (Transactions != null) {
            while (iter.hasNext()) {
                actual = iter.next();
                String da = actual.getDate();
                monthActual = sdf.parse(actual.getDate()).getMonth();
                NumAnterior = TransMonth.get(monthActual) + actual.getQuantity();
                TransMonth.set(monthActual, (float) NumAnterior);
            }
        }

        Date actualDate = new Date();
        int actualMonth = actualDate.getMonth();

        for (int i = 0; i <= 6; i++) {
            if (actualMonth < 0) {
                actualMonth = 11;
            } else {
                actualMonth--;
            }
        }

        for (int j = 0; j <= 6; j++) {
            if (actualMonth == 12) {
                actualMonth = 0;
            }
            String s = Months.get(0);
            serie.addPoint(new ValueLinePoint(Months.get(actualMonth), TransMonth.get(actualMonth)));
            actualMonth++;
        }

        mCubicValueLineChart.clearChart();
        mCubicValueLineChart.addSeries(serie);
        mCubicValueLineChart.startAnimation();
    }

    private void chartMonth() {

    }

    private void chartWeek() {

    }

    private void setSpinner() {
        tendencia = vista.findViewById(R.id.spinnerTendencia);

        String arrayName = "tendencia";
        int arrayName_ID = getResources().getIdentifier(arrayName, "array", getContext().getPackageName());
        String[] categories = getResources().getStringArray(arrayName_ID);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Desactivem el primer item
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tendencia.setAdapter(spinnerAdapter);
    }
}