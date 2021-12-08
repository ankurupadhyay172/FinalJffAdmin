package com.ankurupadhyay.finaljffadmin.LocalDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertCart(CartTable cartTable);

    @Query("select * from CartTable where id=:id")
    public int isCart(String id);


    @Query("select id from carttable where id=:id")
    public String getCartId(String id);

    @Query("select * from CartTable")
    public List<CartTable> cartList();

    @Query("update CartTable set quantity=:quantity where id=:id")
    public void updateCartQuantity(int quantity,String id);

    @Query("update CartTable set price=:price where id=:id")
    public int updateCartPrice(int price,String id);

    @Query("delete from CartTable where id=:id")
    public void deleteItem(String id);


    @Query("delete from CartTable")
    public void clearCart();

    @Query("select quantity from CartTable where id=:id")
    public int getQuantity(String id);



    @Query("update CartTable set extra=:extra ,isAc=:isAc where id=:id")
    public int updateExtra(int extra,boolean isAc,String id);




    //------------------------------handle the fav



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertFav(FavTable favTable);


    @Query("select id from favtable where id=:id")
    public String getFavId(String id);

    @Query("select * from favtable")
    public List<FavTable> favList();

    @Query("update favtable set isfav=:status where id=:id")
    public void updateFav(boolean status,String id);

    @Query("delete from favtable where id=:id")
    public void deleteFav(String id);




    //--------------------------------- for manage Address

    @Insert
    public void InsertAddress(AddressTable addressTable);


    @Query("select * from AddressTable")
    public List<AddressTable> getAddressData();

    @Query("delete from AddressTable where id=:uid")
    public void deleteAddress(int uid);

    @Query("select * from AddressTable where id=:uid")
    public AddressTable getSingleAddress(int uid);


    @Query("update AddressTable set name=:name,mobile_no=:mobile,address=:address,address_type=:type where id=:uid")
    public void updateAddress(String name,String mobile,String address,String type,int uid);


    //--------------------------- for MyOrders



    @Query("select * from MyOrders")
    public List<MyOrders> ordersList();


    @Query("select * from myorders where id=:uid")
    public MyOrders getSingleOrder(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertMyOrder(MyOrders myOrders);

    @Query("delete from MyOrders")
    public void clearOrder();

    @Query("delete from MyOrders where id=:uid")
    public void deleteSignlerder(String uid);


    @Query("update MyOrders set cart_list=:cart_list where id=:uid")
    public void updateCart(String uid,String cart_list);




    //--------------------------------------Pending cart


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertPendingCart(PendingCart cartTable);

    @Query("select * from PendingCart where id=:id")
    public int isPendingCart(String id);


    @Query("select id from PendingCart where id=:id")
    public String getPendingCartId(String id);

    @Query("select * from PendingCart")
    public List<PendingCart> pendingcartList();

    @Query("update PendingCart set quantity=:quantity where id=:id")
    public void updatePendingCartQuantity(int quantity,String id);

    @Query("update PendingCart set price=:price where id=:id")
    public int updatePendingCartPrice(int price,String id);

    @Query("delete from PendingCart where id=:id")
    public void deletePendingItem(String id);


    @Query("delete from PendingCart")
    public void clearPendingCart();

    @Query("select quantity from PendingCart where id=:id")
    public int getPendingQuantity(String id);



    //---------------------------------SearchItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertSearchItem(NewMealModel mealModel);

    @Query("select exists(select * from NewMealModel where id=:id)")
    public boolean isRowIsExist(String id);

    @Query("select * from NewMealModel")
    public List<NewMealModel> getSearchData();


}
