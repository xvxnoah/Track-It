package com.example.trackit.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public AdapterTransactions(ArrayList<Transaction> transactionVos) {
        this.transactionVos = transactionVos;
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
        holder.amount.setText(quantity);
        holder.date.setText(transactionVos.get(position).getDate());
        holder.picCategory.setImageResource(transactionVos.get(position).getPic());

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
