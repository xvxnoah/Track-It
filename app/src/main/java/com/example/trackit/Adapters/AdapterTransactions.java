package com.example.trackit.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.HomePage;
import com.example.trackit.Model.Transaction;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.example.trackit.Transactions.EditTransaction;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class AdapterTransactions extends RecyclerView.Adapter<AdapterTransactions.ViewHolder> implements Serializable {
    ArrayList<Transaction> transactionVos;
    private Context context;
    private DatabaseReference ref;

    public AdapterTransactions(ArrayList<Transaction> transactionVos, Context context) {
        this.transactionVos = transactionVos;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterTransactions.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTransactions.ViewHolder holder, int position) {
        holder.title.setText(transactionVos.get(position).getName());
        holder.category.setText(transactionVos.get(position).getType());
        String quantity = String.valueOf(transactionVos.get(position).getQuantity());

        if(transactionVos.get(position).getQuantity() < 0){
            holder.amount.setText(quantity);
            holder.amount.setTextColor(Color.RED);
        } else{
            holder.amount.setText(quantity);
        }
        holder.date.setText(transactionVos.get(position).getDate());

        String type = transactionVos.get(position).getType();
        if(type.equals("Alimentació")){
            holder.picCategory.setImageResource(R.drawable.ic_baseline_fastfood_24);
        } else if(type.equals("Compres")){
            holder.picCategory.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
        } else if(type.equals("Transport")){
            holder.picCategory.setImageResource(R.drawable.ic_baseline_directions_transit_24);
        } else if(type.equals("Salut/Higiene")){
            holder.picCategory.setImageResource(R.drawable.person);
        } else if(type.equals("Educació")){
            holder.picCategory.setImageResource(R.drawable.ic_baseline_auto_stories_24);
        } else if(type.equals("Altres despeses")){
            holder.picCategory.setImageResource(R.drawable.transaction);
        } else if(type.equals("Nòmina")){
            holder.picCategory.setImageResource(R.drawable.ic_baseline_attach_money_24);
        } else if(type.equals("Criptomonedes")){
            holder.picCategory.setImageResource(R.drawable.ic_currency_btc);
        } else if(type.equals("Accions")){
            holder.picCategory.setImageResource(R.drawable.ic_cash_100);
        } else if(type.equals("Altres ingressos")){
            holder.picCategory.setImageResource(R.drawable.transaction);
        }

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));

        clickTransaction(holder, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return transactionVos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, category, amount, date;
        AppCompatImageView picCategory;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTransaction);
            category = itemView.findViewById(R.id.amount);
            amount = itemView.findViewById(R.id.money);
            date = itemView.findViewById(R.id.date);
            picCategory = itemView.findViewById(R.id.card_icon_transaction);
            cardView = itemView.findViewById(R.id.transaction_card);
        }
    }

    private void clickTransaction(AdapterTransactions.ViewHolder holder, int position){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfo(holder);
            }
        });
    }

    private void openInfo(AdapterTransactions.ViewHolder holder){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomePage.getInstance(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(HomePage.getInstance()).inflate(R.layout.bottom_sheet_layout, (LinearLayout)holder.itemView.findViewById(R.id.bottomSheetContainer));

        ImageView roundedImageView = bottomSheetView.findViewById(R.id.imageSheet);
        TextView title = bottomSheetView.findViewById(R.id.nameTransaction);
        TextView date = bottomSheetView.findViewById(R.id.dateTransaction);
        TextView money = bottomSheetView.findViewById(R.id.moneySheet);

        title.setText(holder.title.getText());
        date.setText(holder.date.getText());

        if(Double.valueOf(holder.amount.getText().toString()) < 0){
            money.setTextColor(Color.RED);
        } else{
            money.setTextColor(Color.GREEN);
        }

        money.setText(holder.amount.getText().toString() + '€');
        roundedImageView.setImageResource(selectImage(holder));

        bottomSheetView.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setTitle("Vols eliminar la transacció?").setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Transaction t = transactionVos.get(holder.getAdapterPosition());
                        UserInfo user = UserInfo.getInstance();

                        if(user.getInstance().deleteTransaction(t)){
                            Toast.makeText(context, "Transacció eliminada!", Toast.LENGTH_SHORT).show();
                            transactionVos.remove(holder.getAdapterPosition());
                            updateFirebase();
                        } else{
                            Toast.makeText(context, "Error a l'eliminar!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transaction t = transactionVos.get(holder.getAdapterPosition());

                Gson gson = new Gson();
                String transactionDataObject = gson.toJson(t);

                Intent i = new Intent(context, EditTransaction.class);
                i.putExtra("Transaction", transactionDataObject);
                context.startActivity(i);

                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private int selectImage(AdapterTransactions.ViewHolder holder){
        String type = transactionVos.get(holder.getLayoutPosition()).getType();
        if(type.equals("Alimentació")){
            return R.drawable.ic_baseline_fastfood_24;
        } else if(type.equals("Compres")){
            return R.drawable.ic_baseline_shopping_cart_24;
        } else if(type.equals("Transport")){
            return R.drawable.ic_baseline_directions_transit_24;
        } else if(type.equals("Salut/Higiene")){
            return R.drawable.person;
        } else if(type.equals("Educació")){
            return R.drawable.ic_baseline_auto_stories_24;
        } else if(type.equals("Altres despeses")){
            return R.drawable.transaction;
        } else if(type.equals("Nòmina")){
            return R.drawable.ic_baseline_attach_money_24;
        } else if(type.equals("Criptomonedes")){
            return R.drawable.ic_currency_btc;
        } else if(type.equals("Accions")){
            return R.drawable.ic_cash_100;
        } else if(type.equals("Altres ingressos")){
            return R.drawable.transaction;
        }
        return 0;
    }

    private void updateFirebase(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");
        SharedPreferences preferences = context.getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferences.getString(AuthActivity.USER, null);
        email = email.replace('.', ',');

        ref = database.getReference("users/"+email);
        ref.setValue(UserInfo.getInstance());
    }
}
