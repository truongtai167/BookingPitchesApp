package com.example.duan1_pro1121.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.duan1_pro1121.dao.CustomerDAO;
import com.example.duan1_pro1121.dao.ManagerDAO;
import com.example.duan1_pro1121.dao.OrderDAO;
import com.example.duan1_pro1121.dao.OrderDetailsDAO;
import com.example.duan1_pro1121.dao.PitchCategoryDAO;
import com.example.duan1_pro1121.dao.PitchDAO;
import com.example.duan1_pro1121.dao.ServiceDAO;
import com.example.duan1_pro1121.model.Customer;
import com.example.duan1_pro1121.model.Manager;
import com.example.duan1_pro1121.model.ManagerCategory;
import com.example.duan1_pro1121.model.Order;
import com.example.duan1_pro1121.model.OrderDetails;
import com.example.duan1_pro1121.model.Pitch;
import com.example.duan1_pro1121.model.PithCategory;
import com.example.duan1_pro1121.model.ServiceBall;

@Database(entities = {Customer.class,Manager.class, Order.class, OrderDetails.class, Pitch.class, PithCategory.class, ServiceBall.class, ManagerCategory.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    private static String DB_NAME = "PITCH_MANAGER2";
    private static MyDatabase instance;

    public static synchronized MyDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),MyDatabase.class,DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract CustomerDAO customerDAO();
    public abstract ManagerDAO managerDAO();
    public abstract OrderDAO orderDAO();
    public abstract OrderDetailsDAO orderDetailsDAO();
    public abstract PitchCategoryDAO pitchCategoryDAO();
    public abstract PitchDAO pitchDao();
    public abstract ServiceDAO serviceDAO();

}