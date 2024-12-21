package com.example.w24_3175_g7_onroadsavior.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w24_3175_g7_onroadsavior.Model.BreakdownTypes;
import com.example.w24_3175_g7_onroadsavior.R;

import java.util.List;

public class AdapterBreakdownType extends RecyclerView.Adapter<AdapterBreakdownType.ViewHolder>{

    List<BreakdownTypes> adapterBreakdownTypes;

    private OnClickListener onClickListener;

    public AdapterBreakdownType(List<BreakdownTypes> adapterBreakdownTypes, OnClickListener onClickListener) {
        this.adapterBreakdownTypes = adapterBreakdownTypes;
        this.onClickListener = onClickListener;
    }

    public List<BreakdownTypes> getAdapterBreakdownTypes() {
        return adapterBreakdownTypes;
    }

    public void setAdapterBreakdownTypes(List<BreakdownTypes> adapterBreakdownTypes) {
        this.adapterBreakdownTypes = adapterBreakdownTypes;
    }

    public AdapterBreakdownType() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.breakdown_types_list, parent, false);
        ViewHolder viewHolder =new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageViewUserTypes.setImageResource(adapterBreakdownTypes.get(position).getBreakdownIcon());
        holder.textViewUserTypes.setText(adapterBreakdownTypes.get(position).getBreakdownType());

 /*       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), adapterBreakdownTypes.get(position).getBreakdownType(), Toast.LENGTH_SHORT).show();
                replaceFragment(new CurrentLocationFragment());
            }
        });*/

        holder.itemView.setOnClickListener(v -> {
            onClickListener.onClick(position);
        });

    }

    @Override
    public int getItemCount() {
        return adapterBreakdownTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewUserTypes;
        TextView textViewUserTypes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUserTypes = itemView.findViewById(R.id.imageViewBreakdownTypes);
            textViewUserTypes = itemView.findViewById(R.id.textViewBreakdownTypes);
        }
    }

    public interface OnClickListener{
        void onClick(int position);
    }
}
