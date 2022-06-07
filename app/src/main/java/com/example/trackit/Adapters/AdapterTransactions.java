package com.example.trackit.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackit.Model.Transaction;
import com.example.trackit.R;
import com.example.trackit.ViewModel.TransactionVo;

import java.util.ArrayList;

public class AdapterTransactions extends RecyclerView.Adapter<AdapterTransactions.ViewHolder> {
    ArrayList<Transaction> transactionVos;
    private Context context;

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
        holder.picCategory.setImageResource(transactionVos.get(position).getPic());

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));
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
            category = itemView.findViewById(R.id.category);
            amount = itemView.findViewById(R.id.money);
            date = itemView.findViewById(R.id.date);
            picCategory = itemView.findViewById(R.id.card_icon_transaction);
            cardView = itemView.findViewById(R.id.transaction_card);
        }
    }

}
