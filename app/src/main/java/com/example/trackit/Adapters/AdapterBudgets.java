package com.example.trackit.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackit.Model.Budget;
import com.example.trackit.R;

import java.util.ArrayList;

public class AdapterBudgets extends RecyclerView.Adapter<AdapterBudgets.ViewHolder> {
    ArrayList<Budget> budgetVos;
    private Context context;

    public AdapterBudgets(ArrayList<Budget> budgetVos, Context context) {
        this.budgetVos =budgetVos;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterBudgets.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBudgets.ViewHolder holder, int position) {
        holder.title.setText(budgetVos.get(position).getName());
        holder.amount.setText(budgetVos.get(position).getType());
        String quantity = String.valueOf(budgetVos.get(position).getQuantity());
        int percentatge = (int) (budgetVos.get(position).getQuantity()/budgetVos.get(position).getObjective());

        holder.bar.setProgress(percentatge, true);
        holder.amount.setText(quantity);

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));
    }

    @Override
    public int getItemCount() {
        return budgetVos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, amount;
        CardView cardView;
        ProgressBar bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTransaction);
            amount = itemView.findViewById(R.id.amount);
            cardView = itemView.findViewById(R.id.card_image_budget);
            bar = itemView.findViewById(R.id.progressBar);
        }
    }

}
