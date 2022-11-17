package com.example.duan1_pro1121.activity.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.duan1_pro1121.MyApplication;
import com.example.duan1_pro1121.R;
import com.example.duan1_pro1121.adapter.admin.AdapterForm;
import com.example.duan1_pro1121.database.MyDatabase;
import com.example.duan1_pro1121.model.Manager;
import com.example.duan1_pro1121.model.ManagerCategory;
import com.example.duan1_pro1121.model.PithCategory;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FormActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        createData();

        AdapterForm adapter = new AdapterForm(this);
        viewPager2 = findViewById(R.id.viewpager_form);
        viewPager2.setAdapter(adapter);
        tabLayout = findViewById(R.id.tablayout_form);

        if(MyApplication.CURRENT_TYPE == MyApplication.TYPE_USER) {
            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                if (position == 0) tab.setText("Log In");
                else if (position == 1) tab.setText("Sign Up");
            }).attach();
        }else{
            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                if (position == 0) tab.setText("Log In");
            }).attach();
        }
    }

    public void createData(){
        createAdminDataIfNotExists();
        createPitchCategoryDataIfNotExists();
    }

    public void createAdminDataIfNotExists(){
        if(MyDatabase.getInstance(this).managerCategoryDAO().getAll().size()==0){
            ManagerCategory category = new ManagerCategory();
            category.setName(MyApplication.ADMIN_CATEGORY);
            MyDatabase.getInstance(this).managerCategoryDAO().insert(category);
            Manager manager1 = new Manager();
            manager1.setPhone("Admin");
            manager1.setName("ADMIN");
            manager1.setPosition(MyDatabase.getInstance(this).managerCategoryDAO().getIdAdmin(MyApplication.ADMIN_CATEGORY).get(0).getId());
            manager1.setPassword("123456");
            MyDatabase.getInstance(this).managerDAO().insert(manager1);
        }
    }

    public void createPitchCategoryDataIfNotExists(){
        if(MyDatabase.getInstance(this).pitchCategoryDAO().getAll().size()==0){
            PithCategory category = new PithCategory(MyApplication.ID_CATEGORY_PITCH_5,"Sân 5 người",80000);
            MyDatabase.getInstance(this).pitchCategoryDAO().insert(category);
            category = new PithCategory(MyApplication.ID_CATEGORY_PITCH_7,"Sân 7 người",100000);
            MyDatabase.getInstance(this).pitchCategoryDAO().insert(category);
            category = new PithCategory(MyApplication.ID_CATEGORY_PITCH_11,"Sân 11 người",140000);
            MyDatabase.getInstance(this).pitchCategoryDAO().insert(category);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}