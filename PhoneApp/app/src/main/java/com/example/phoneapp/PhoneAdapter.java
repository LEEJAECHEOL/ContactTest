package com.example.phoneapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.MyViewHolder> {

    private List<Phone> phones;
    private MainActivity mainActivity;

    public PhoneAdapter(MainActivity mainActivity){
        this.phones = new ArrayList<>();
        this.mainActivity = mainActivity;
    }

    public void setPhones(List<Phone> phones){
        this.phones = phones;
        notifyDataSetChanged();
    }
    public void addPhone(Phone phone){
        phones.add(phone);
        notifyDataSetChanged();
    }
    public Phone getItem(int position){
        return phones.get(position);
    }
    public  void updateItem(int position, Phone phone){
        phones.get(position).setId(phone.getId());
        phones.get(position).setName(phone.getName());
        phones.get(position).setTel(phone.getTel());
        notifyDataSetChanged();
    }
    public void deleteItem(int position){
        phones.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.phone_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setItem(phones.get(position));
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvTel;
        private ConstraintLayout phoneItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneItem = itemView.findViewById(R.id.layout_phone_item);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTel = itemView.findViewById(R.id.tv_tel);

            phoneItem.setOnClickListener(v -> {
                View dialogView = v.inflate(v.getContext(),R.layout.layout_add_phone, null);
                final TextInputEditText etName = dialogView.findViewById(R.id.et_name);
                final TextInputEditText etTel = dialogView.findViewById(R.id.et_tel);
                Phone phone = getItem(getAdapterPosition());
                etName.setText(phone.getName());
                etTel.setText(phone.getTel());

                AlertDialog.Builder dlg = new AlertDialog.Builder(mainActivity);
                dlg.setTitle("연락처 수정");
                dlg.setView(dialogView);
                dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Long id = getItem(getAdapterPosition()).getId();
                        mainActivity.updatePhone(id, new Phone(null, etName.getText().toString(), etTel.getText().toString()), getAdapterPosition());
                    }
                });
                dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Long id = getItem(getAdapterPosition()).getId();
                        mainActivity.deletePhone(id, getAdapterPosition());
                    }
                });
                dlg.show();
            });
        }
        public void setItem(Phone phone){
            tvName.setText(phone.getName());
            tvTel.setText(phone.getTel());
        }
    }

}
