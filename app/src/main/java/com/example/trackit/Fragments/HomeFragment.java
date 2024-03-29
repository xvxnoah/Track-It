package com.example.trackit.Fragments;

import static java.lang.Math.abs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Adapters.AdapterTransactions;
import com.example.trackit.Model.Transaction;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.News.NewsPage;
import com.example.trackit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    FloatingActionButton fab_news;

    ValueLineSeries serieIncome;
    ValueLineSeries serieExpenses;

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
    DatabaseReference databaseReference;

    RecyclerView recyclerViewTransaction;
    TextView recent;
    private ArrayList<Transaction> transactionVos;

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

        fab_news = vista.findViewById(R.id.fab_news);

        recyclerViewTransaction = vista.findViewById(R.id.recentRecyclerViewHome);
        recyclerViewTransaction.setLayoutManager(new LinearLayoutManager(getContext()));

        recent = vista.findViewById(R.id.Recents);
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
                    transactionVos = new ArrayList<>();
                    fillUpList();
                    AdapterTransactions adapter = new AdapterTransactions(transactionVos, getContext());
                    recyclerViewTransaction.setAdapter(adapter);
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

        fab_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewsPage.class));
            }
        });
        return vista;
    }

    public void updateFragment() throws ParseException {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        Transactions = userInfo.getTransactions();

        StorageReference mImageStorage = FirebaseStorage.getInstance("gs://track-it-86761.appspot.com").getReference();
        if(userInfo.getImageStr() != null){
            StorageReference ref = mImageStorage.child("images/" + userInfo.getImageStr());

            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();
                        String imageUrl = downUri.toString();
                        new HomeFragment.DownloadImageTask((ImageView) vista.findViewById(R.id.profile_pic)).execute(imageUrl);
                    }else{
                        Toast.makeText(vista.getContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        balance.setText(Double.toString(userInfo.getQuantity()) + '€');

        if(userInfo.getMoneySaved() >= 1000 && userInfo.getMoneySaved() < 10000){
            QuantityIngressos.setTextSize(22);
        } else if(userInfo.getMoneySaved() >= 10000){
            QuantityIngressos.setTextSize(18);
        }

        QuantityIngressos.setText(Double.toString(userInfo.getMoneySaved()) + '€');

        if(userInfo.getMoneyWasted() >= 1000 && userInfo.getMoneyWasted() < 10000){
            QuantityDespeses.setTextSize(22);
        } else if(userInfo.getMoneyWasted() >= 10000){
            QuantityDespeses.setTextSize(18);
        }
        QuantityDespeses.setText(Double.toString(userInfo.getMoneyWasted()) + '€');

        mCubicValueLineChart = (ValueLineChart) vista.findViewById(R.id.cubiclinechart);
        mCubicValueLineChart.setEmptyDataText("");

        serieIncome = new ValueLineSeries();
        serieIncome.setColor(Color.GREEN);

        serieExpenses = new ValueLineSeries();
        serieExpenses.setColor(Color.RED);

        serieExpenses = new ValueLineSeries();
        serieExpenses.setColor(Color.RED);

        String seleccio = tendencia.getSelectedItem().toString();

        if(seleccio.equals("Últim any") || seleccio.equals("Selecciona")){
            tendencia.setSelection(1);
            chartYear();
        } else if(seleccio.equals("Últims 6 mesos")){
            chartHalfYear();
        } else if(seleccio.equals("Darrer mes")){
            chartMonth();
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

        ArrayList<Float> incomeMonth = new ArrayList<>();
        ArrayList<Float> expenseMonth = new ArrayList<>();

        for (int y = 0; y < 12; y++) {
            incomeMonth.add((float) 0);
            expenseMonth.add((float) 0);
        }

        double NumAnterior;
        String lastMonth = "null";
        if (Transactions != null) {
            while (iter.hasNext()) {
                actual = iter.next();
                String da = actual.getDate();
                monthActual = sdf.parse(actual.getDate()).getMonth();
                if(actual.getType().equals("Nòmina") || actual.getType().equals("Criptomonedes") || actual.getType().equals("Accions") || actual.getType().equals("Altres ingressos")){
                    NumAnterior = incomeMonth.get(monthActual) + actual.getQuantity();
                    incomeMonth.set(monthActual, (float) NumAnterior);
                }else{
                    NumAnterior = expenseMonth.get(monthActual) + abs(actual.getQuantity());
                    expenseMonth.set(monthActual, (float) NumAnterior);
                }
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
            serieIncome.addPoint(new ValueLinePoint(Months.get(actualMonth), incomeMonth.get(actualMonth)));
            serieExpenses.addPoint(new ValueLinePoint(Months.get(actualMonth), expenseMonth.get(actualMonth)));
            actualMonth++;
        }

        mCubicValueLineChart.clearChart();
        mCubicValueLineChart.addSeries(serieIncome);
        mCubicValueLineChart.addSeries((serieExpenses));
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

        ArrayList<Float> incomeMonth = new ArrayList<>();
        ArrayList<Float> expenseMonth = new ArrayList<>();

        for (int y = 0; y < 12; y++) {
            incomeMonth.add((float) 0);
            expenseMonth.add((float) 0);
        }

        double NumAnterior;
        String lastMonth = "null";
        if (Transactions != null) {
            while (iter.hasNext()) {
                actual = iter.next();
                String da = actual.getDate();
                monthActual = sdf.parse(actual.getDate()).getMonth();
                if(actual.getType().equals("Nòmina") || actual.getType().equals("Criptomonedes") || actual.getType().equals("Accions") || actual.getType().equals("Altres ingressos")){
                    NumAnterior = incomeMonth.get(monthActual) + actual.getQuantity();
                    incomeMonth.set(monthActual, (float) NumAnterior);
                }else{
                    NumAnterior = expenseMonth.get(monthActual) + abs(actual.getQuantity());
                    expenseMonth.set(monthActual, (float) NumAnterior);
                }
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
            serieIncome.addPoint(new ValueLinePoint(Months.get(actualMonth), incomeMonth.get(actualMonth)));
            serieExpenses.addPoint(new ValueLinePoint(Months.get(actualMonth), expenseMonth.get(actualMonth)));

            actualMonth++;
        }

        mCubicValueLineChart.clearChart();
        mCubicValueLineChart.addSeries(serieIncome);
        mCubicValueLineChart.addSeries(serieExpenses);
        mCubicValueLineChart.startAnimation();
    }

    private void chartMonth() throws ParseException {
        Iterator<Transaction> iter = null;
        Transaction actual;
        Integer dayActual;
        Integer monthActual;
        if (Transactions != null) {
            iter = Transactions.iterator();
        }

        ArrayList<Float> incomeDay = new ArrayList<>();
        ArrayList<Float> expenseDay = new ArrayList<>();

        Date now = new Date();
        int month = now.getMonth();
        int day = now.getDay();
        int days;

        if(month == 0 || month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10){
            days = 31;
        }else if(month == 2){
            days = 28;
        }else{
            days = 30;
        }

        for (int y = 0; y < days; y++) {
            incomeDay.add((float) 0);
            expenseDay.add((float) 0);
        }

        double NumAnterior;
        if (Transactions != null) {
            while (iter.hasNext()) {
                actual = iter.next();
                String da = actual.getDate();
                monthActual = sdf.parse(actual.getDate()).getMonth();
                if(da.substring(1,2).equals("/")){
                    dayActual = Integer.parseInt(da.substring(0,1)) - 1;
                }else{
                    dayActual = Integer.parseInt(da.substring(0,2)) - 1;
                }

                if(monthActual == month || (dayActual < days && monthActual == month - 1)) {
                    if (actual.getType().equals("Nòmina") || actual.getType().equals("Criptomonedes") || actual.getType().equals("Accions") || actual.getType().equals("Altres ingressos")) {
                        NumAnterior = incomeDay.get(dayActual) + actual.getQuantity();
                        incomeDay.set(dayActual, (float) NumAnterior);
                    } else {
                        NumAnterior = expenseDay.get(dayActual) + abs(actual.getQuantity());
                        expenseDay.set(dayActual, (float) NumAnterior);
                    }
                }
            }
        }

        Date actualDate = new Date();
        int actualDay = actualDate.getDay();

        for (int i = 0; i < days; i++) {
            if (actualDay < 0) {
                actualDay = days - 1;
            } else {
                actualDay--;
            }
        }

        for (int j = 0; j < days; j++) {
            if (actualDay == days) {
                actualDay = 0;
            }
            serieIncome.addPoint(new ValueLinePoint(Integer.toString(actualDay + 1), incomeDay.get(actualDay)));
            serieExpenses.addPoint(new ValueLinePoint(Integer.toString(actualDay + 1), expenseDay.get(actualDay)));
            actualDay++;
        }

        mCubicValueLineChart.clearChart();
        mCubicValueLineChart.addSeries(serieIncome);
        mCubicValueLineChart.addSeries((serieExpenses));
        mCubicValueLineChart.startAnimation();
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

    public void fillUpList() {
        ArrayList<Transaction> transactions = userInfo.getTransactions();
        Transaction transaction;

        if(transactions != null){
            recent.setText("Transaccions recents");
            for(int i = 0; i < transactions.size() && i < 10; i++){
                transaction = transactions.get(transactions.size() - i - 1);
                String type = transaction.getType();

                if(type.equals("Alimentació")){
                    transaction.setPic(R.drawable.ic_baseline_fastfood_24);
                } else if(type.equals("Compres")){
                    transaction.setPic(R.drawable.ic_baseline_shopping_cart_24);
                } else if(type.equals("Transport")){
                    transaction.setPic(R.drawable.ic_baseline_directions_transit_24);
                } else if(type.equals("Salut/Higiene")){
                    transaction.setPic(R.drawable.person);
                } else if(type.equals("Educació")){
                    transaction.setPic(R.drawable.ic_baseline_auto_stories_24);
                } else if(type.equals("Altres despeses")){
                    transaction.setPic(R.drawable.transaction);
                } else if(type.equals("Nòmina")){
                    transaction.setPic(R.drawable.ic_baseline_attach_money_24);
                } else if(type.equals("Criptomonedes")){
                    transaction.setPic(R.drawable.ic_currency_btc);
                } else if(type.equals("Accions")){
                    transaction.setPic(R.drawable.ic_cash_100);
                } else if(type.equals("Altres ingressos")){
                    transaction.setPic(R.drawable.transaction);
                }
                transactionVos.add(transaction);
            }
        }else{
            recent.setText("No hi ha transaccions");
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}