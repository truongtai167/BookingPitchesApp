package com.example.duan1_pro1121.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1_pro1121.MyApplication;
import com.example.duan1_pro1121.R;
import com.example.duan1_pro1121.adapter.CustomerAdapter;
import com.example.duan1_pro1121.database.MyDatabase;
import com.example.duan1_pro1121.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class KhachHangFragment extends Fragment {

    private List<Customer> customerList;
    private RecyclerView recyclerView;
    private FloatingActionButton button;
    private CustomerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerList = MyDatabase.getInstance(getContext()).customerDAO().getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_khach_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_customer);
        button = view.findViewById(R.id.btn_create_dialog_add_khachhang);
        button.setOnClickListener(v->{
            createDialogAdd();
        });

        adapter = new CustomerAdapter(getContext(),customerList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this::createDialogUpdate);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
    }

    public void createDialogAdd(){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_khachhang);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn = dialog.findViewById(R.id.btn_add_khachhang);
        EditText edtPhone = dialog.findViewById(R.id.edt_phone_dialog_add_khachhang);
        EditText edtName = dialog.findViewById(R.id.edt_name_dialog_add_khachhang);
        EditText edtPass = dialog.findViewById(R.id.edt_pass_dialog_add_khachhang);
        EditText edtPass2 = dialog.findViewById(R.id.edt_pass2_dialog_add_khachhang);
        EditText edtCmnd = dialog.findViewById(R.id.edt_cmnd_dialog_add_khachhang);
        EditText edtAddress = dialog.findViewById(R.id.edt_address_dialog_add_khachhang);

        TextView tvCheckPhone = dialog.findViewById(R.id.tv_check_phone_dialog_add_khachhang);
        TextView tvCheckName = dialog.findViewById(R.id.tv_check_name_dialog_add_khachhang);
        TextView tvCheckAddress = dialog.findViewById(R.id.tv_check_address_dialog_add_khachhang);
        TextView tvCheckPass = dialog.findViewById(R.id.tv_check_pass_dialog_add_khachhang);
        TextView tvCheckPass2 = dialog.findViewById(R.id.tv_check_pass2_dialog_add_khachhang);

        btn.setOnClickListener(v->{
            String phone = edtPhone.getText().toString();
            String name = edtName.getText().toString();
            String pass = edtPass.getText().toString();
            String pass2 = edtPass2.getText().toString();
            String cmnd = edtCmnd.getText().toString();
            String address = edtAddress.getText().toString();

            if(!phone.matches(MyApplication.PHONE_REGEX)){
                tvCheckPhone.setText("* Số điện thoại không hợp lệ");
                invisible(tvCheckAddress,tvCheckName,tvCheckPass,tvCheckPhone,tvCheckPass2);
                tvCheckPhone.setVisibility(View.VISIBLE);
            }else if(!name.matches(MyApplication.NAME_REGEX)){
                invisible(tvCheckAddress,tvCheckName,tvCheckPass,tvCheckPhone,tvCheckPass2);
                tvCheckName.setVisibility(View.VISIBLE);
            }else if(!address.matches(MyApplication.ADDRESS_REGEX)){
                invisible(tvCheckAddress,tvCheckName,tvCheckPass,tvCheckPhone,tvCheckPass2);
                tvCheckAddress.setVisibility(View.VISIBLE);
            } else if(!pass.matches(MyApplication.PASS_REGEX)){
                invisible(tvCheckAddress,tvCheckName,tvCheckPass,tvCheckPhone,tvCheckPass2);
                tvCheckPass.setVisibility(View.VISIBLE);
            }else if(!pass.equals(pass2)){
                invisible(tvCheckAddress,tvCheckName,tvCheckPass,tvCheckPhone,tvCheckPass2);
                tvCheckPass2.setVisibility(View.VISIBLE);
            }else{
                Customer customer = new Customer();
                if(MyDatabase.getInstance(getContext()).customerDAO().getCustomerWithPhone(phone,-1).size()==0) {
                    customer.setPhone(phone);
                    customer.setName(name);
                    customer.setPassword(pass);
                    customer.setCmnd(cmnd);
                    customer.setAddress(address);

                    MyDatabase.getInstance(getContext()).customerDAO().insert(customer);
                    Toast.makeText(getContext(), "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
                    customerList = MyDatabase.getInstance(getContext()).customerDAO().getAll();
                    adapter.setData(customerList);

                    dialog.dismiss();
                }else{
                    tvCheckPhone.setText("* Số điện thoại đã tồn tại");
                    invisible(tvCheckAddress,tvCheckName,tvCheckPhone,tvCheckPass,tvCheckPass2);
                    tvCheckPhone.setVisibility(View.VISIBLE);
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void invisible(TextView...tvs){
        for(TextView tv : tvs){
            tv.setVisibility(View.INVISIBLE);
        }
    }

    public void createDialogUpdate(Customer c){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_customer);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtPhone = dialog.findViewById(R.id.edt_phone_dialog_update_khachhang);
        edtPhone.setText(c.getPhone());
        EditText edtCmnd = dialog.findViewById(R.id.edt_cmnd_dialog_update_khachhang);
        edtCmnd.setText(c.getCmnd());
        EditText edtName = dialog.findViewById(R.id.edt_name_dialog_update_khachhang);
        edtName.setText(c.getName());
        EditText edtAddress = dialog.findViewById(R.id.edt_address_dialog_update_khachhang);
        edtAddress.setText(c.getAddress());
        EditText edtMoney = dialog.findViewById(R.id.edt_money_dialog_update_khachhang);
        edtMoney.setText(c.getCoin()+"");
        Button btn = dialog.findViewById(R.id.btn_update_khachhang);

        TextView tvCheckPhone = dialog.findViewById(R.id.tv_check_phone_dialog_update_khachhang);
        TextView tvCheckName = dialog.findViewById(R.id.tv_check_name_dialog_update_khachhang);
        TextView tvCheckAddress = dialog.findViewById(R.id.tv_check_address_dialog_update_khachhang);
        TextView tvCheckMoney = dialog.findViewById(R.id.tv_check_money_dialog_update_khachhang);

        btn.setOnClickListener(v->{
            String phone = edtPhone.getText().toString();
            String name = edtName.getText().toString();
            String cmnd = edtCmnd.getText().toString();
            String address = edtAddress.getText().toString();
            String money = edtMoney.getText().toString();

            if(!phone.matches(MyApplication.PHONE_REGEX)){
                tvCheckPhone.setText("* Số điện thoại không hợp lệ");
                invisible(tvCheckAddress,tvCheckName,tvCheckPhone,tvCheckMoney);
                tvCheckPhone.setVisibility(View.VISIBLE);
            }else if(!name.matches(MyApplication.NAME_REGEX)){
                invisible(tvCheckAddress,tvCheckName,tvCheckPhone,tvCheckMoney);
                tvCheckName.setVisibility(View.VISIBLE);
            }else if(!address.matches(MyApplication.ADDRESS_REGEX)){
                invisible(tvCheckAddress,tvCheckName,tvCheckMoney,tvCheckPhone);
                tvCheckAddress.setVisibility(View.VISIBLE);
            } else{
                Customer customer = new Customer();
                if(MyDatabase.getInstance(getContext()).customerDAO().getCustomerWithPhone(phone,c.getId()).size()==0) {
                    customer.setId(c.getId());
                    customer.setPassword(c.getPassword());
                    customer.setImg(c.getImg());
                    customer.setPhone(phone);
                    customer.setName(name);
                    customer.setCmnd(cmnd);
                    customer.setAddress(address);
                    try {
                        customer.setCoin(Integer.parseInt(money));
                    }catch (NumberFormatException e){
                        invisible(tvCheckAddress,tvCheckName,tvCheckMoney,tvCheckPhone);
                        tvCheckMoney.setVisibility(View.VISIBLE);
                        return;
                    }

                    MyDatabase.getInstance(getContext()).customerDAO().update(customer);
                    Toast.makeText(getContext(), "Update khách hàng thành công", Toast.LENGTH_SHORT).show();
                    customerList = MyDatabase.getInstance(getContext()).customerDAO().getAll();
                    adapter.setData(customerList);

                    dialog.dismiss();
                }else{
                    tvCheckPhone.setText("* Số điện thoại đã tồn tại");
                    invisible(tvCheckAddress,tvCheckName,tvCheckPhone,tvCheckMoney);
                    tvCheckPhone.setVisibility(View.VISIBLE);
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}