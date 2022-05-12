package com.example.trackit.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    LineGraphSeries<DataPoint> series;

    UserInfo userInfo;

    View vista;

    TextView balance;
    TextView QuantityDespeses;
    TextView QuantityIngressos;

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

        firebaseDatabase = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");

        // Getting text from our edittext fields.
        SharedPreferences preferences = getActivity().getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferences.getString(AuthActivity.USER, null);
        email = email.replace('.', ',');

        userInfo = com.example.trackit.Model.UserInfo.getInstance();

        // Below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("users/"+email);

        // Attach a listener to read the data at our posts reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //userInfo = com.example.trackit.Model.UserInfo.getInstance();
                userInfo = dataSnapshot.getValue(com.example.trackit.Model.UserInfo.class);
                updateFragment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return vista;
    }

    public void updateFragment(){
        ArrayList Transactions = userInfo.getTransactions();
        balance.setText(Double.toString(userInfo.getQuantity()) + '€');
        QuantityIngressos.setText(Double.toString(userInfo.getMoneySaved()) + '€');
        QuantityDespeses.setText(Double.toString(userInfo.getMoneyWasted()) + '€');

        ValueLineChart mCubicValueLineChart = (ValueLineChart) vista.findViewById(R.id.cubiclinechart);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        series.addPoint(new ValueLinePoint("Jan", 2.4f));
        series.addPoint(new ValueLinePoint("Feb", 3.4f));
        series.addPoint(new ValueLinePoint("Mar", .4f));
        series.addPoint(new ValueLinePoint("Apr", 1.2f));
        series.addPoint(new ValueLinePoint("Mai", 2.6f));
        series.addPoint(new ValueLinePoint("Jun", 1.0f));
        series.addPoint(new ValueLinePoint("Jul", 3.5f));
        series.addPoint(new ValueLinePoint("Aug", 2.4f));
        series.addPoint(new ValueLinePoint("Sep", 2.4f));
        series.addPoint(new ValueLinePoint("Oct", 3.4f));
        series.addPoint(new ValueLinePoint("Nov", 15.4f));
        series.addPoint(new ValueLinePoint("Dec", 1.3f));

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();
    }
}