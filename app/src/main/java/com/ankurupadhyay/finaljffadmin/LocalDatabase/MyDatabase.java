package com.ankurupadhyay.finaljffadmin.LocalDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CartTable.class,FavTable.class,AddressTable.class,MyOrders.class,PendingCart.class,NewMealModel.class},version = 1,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {


    public abstract DAO dao();



}
