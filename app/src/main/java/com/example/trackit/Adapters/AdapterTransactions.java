package com.example.trackit.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.HomePage;
import com.example.trackit.Model.Transaction;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterTransactions extends RecyclerView.Adapter<AdapterTransactions.ViewHolder> {
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
        ImageView picCategory;
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
        roundedImageView.setImageResource(R.drawable.ic_baseline_directions_transit_24);

        bottomSheetView.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setTitle("Vols eliminar la transacció?").setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Transaction t = transactionVos.get(holder.getAdapterPosition());
                        transactionVos.remove(holder.getAdapterPosition());

                        UserInfo user = UserInfo.getInstance();
                        Double ammount = Double.valueOf(holder.amount.getText().toString());

                        if(Double.valueOf(holder.amount.getText().toString()) < 0){
                            user.setMoneyWasted(user.getMoneyWasted() + ammount);
                            user.setQuantity(user.getQuantity() + ammount);
                        } else{
                            user.getInstance().setMoneySaved(user.getInstance().getMoneySaved() - ammount);
                            user.setQuantity(user.getQuantity() - ammount);
                        }

                        user.getInstance().deleteTransaction(t);
                        updateFirebase();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
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
