package com.example.trackit.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Model.Budget;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterBudgets extends RecyclerView.Adapter<AdapterBudgets.ViewHolder> {
    ArrayList<Budget> budgetVos;
    private Context context;
    private DatabaseReference ref;

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
        int g = (int) budgetVos.get(position).getQuantity();
        int r = (int) budgetVos.get(position).getObjective();
        double percentatge = (budgetVos.get(position).getQuantity()/budgetVos.get(position).getObjective())*100;
        int percentatge2 = (int) percentatge;
        holder.bar.setProgress(percentatge2, true);
        holder.bar.setProgressTintList(ColorStateList.valueOf(budgetVos.get(position).getColor()));
        holder.amount.setText(quantity);

        holder.cardView.setBackgroundColor(budgetVos.get(position).getColor());
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));

        clickBudget(holder);
    }

    private void clickBudget(ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfo(holder);
            }
        });
    }

    private void openInfo(ViewHolder holder) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_info_budget);

        bottomSheetDialog.show();

        TextView nom = bottomSheetDialog.findViewById(R.id.nameBudget);
        TextView actual = bottomSheetDialog.findViewById(R.id.ammountActual);

        nom.setText(holder.title.getText());

        actual.setText(holder.amount.getText().toString() + '€');

        bottomSheetDialog.findViewById(R.id.deleteButtonBudget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setTitle("Vols eliminar el pressupost?").setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Budget b = budgetVos.get(holder.getAdapterPosition());
                        UserInfo user = UserInfo.getInstance();

                        if(user.deleteBudget(b.getName())){
                            Toast.makeText(context, "Pressupost eliminat!", Toast.LENGTH_SHORT).show();
                            budgetVos.remove(holder.getAdapterPosition());
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
    }

    private void updateFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");
        SharedPreferences preferences = context.getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferences.getString(AuthActivity.USER, null);
        email = email.replace('.', ',');

        ref = database.getReference("users/"+email);
        ref.setValue(UserInfo.getInstance());
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
