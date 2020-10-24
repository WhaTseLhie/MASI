package com.example.asus.masi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.asus.masi.about.About;
import com.example.asus.masi.cart.PaymentActivity;
import com.example.asus.masi.masi.add.Product;
import com.example.asus.masi.order.Order;
import com.example.asus.masi.order.ProductOrder;
import com.example.asus.masi.rating.Rating;
import com.example.asus.masi.wallet.Wallet;

import java.util.ArrayList;
import java.util.Locale;

public class AppDatabase extends SQLiteOpenHelper {

    static String DATABASE ="db_app";
    static String TBL_CUSTOMER = "tbl_customer";
    static String TBL_ACCOUNT = "tbl_account";
    static String TBL_PRODUCT = "tbl_product";
    static String TBL_ORDER = "tbl_order";
    static String TBL_PRODUCT_ORDER = "tbl_product_order";
    static String TBL_RATING = "tbl_rating";
    static String TBL_ABOUT = "tbl_about";
    static String TBL_WALLET = "tbl_wallet";

    static String TR_ACTIVATE = "tr_activate";

    static String SP_GET_ALL_CUSTOMER = "sp_get_all_customer";
    static String SP_GET_ALL_PRODUCT = "sp_get_all_product";
    static String SP_GET_ALL_ORDER = "sp_get_all_order";
    static String SP_GET_ALL_RATING = "sp_get_all_rating";

    public AppDatabase(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlUser = "CREATE TABlE " +TBL_CUSTOMER+ "(custid integer primary key autoincrement, profilepic varchar(50), username varchar(50), password varchar(50), fname varchar(150), lname varchar(150), gender varchar(15), email varchar(50), contact varchar (11), address varchar(100), totalbal decimal(9, 2), status varchar(10))";
        db.execSQL(sqlUser);
        String sqlAccount = "CREATE TABlE " +TBL_ACCOUNT+ "(accountid integer primary key autoincrement, custid integer, profilepic varchar(50), username varchar(50), password varchar(50), fname varchar(150), lname varchar(150), gender varchar(15), email varchar(50), contact varchar (11), address varchar(100), totalbal decimal(9, 2), status varchar(10))";
        db.execSQL(sqlAccount);
        String sqlProduct = "CREATE TABlE " +TBL_PRODUCT+ "(productid integer primary key autoincrement, productpic varchar(50), productbrand varchar(50), productname varchar(50), productcategory varchar(20), productcolor varchar(10), productprice decimal(5,2), productqty integer, productrating float)";
        db.execSQL(sqlProduct);
        String sqlRating= "CREATE TABlE " +TBL_RATING+ "(id integer primary key autoincrement, productid integer, customerid integer, customerpic varchar(50), customername varchar(100), rating float, comment varchar(100), date varchar(15))";
        db.execSQL(sqlRating);
        String sqlAbout = "CREATE TABlE " +TBL_ABOUT+ "(aboutid integer primary key autoincrement, appabout varchar(400), apphours varchar(30), appaddress varchar(100))";
        db.execSQL(sqlAbout);
        String sqlOrder = "CREATE TABlE " +TBL_ORDER+ "(orderid integer primary key autoincrement, customerid integer, orderdate varchar(15), ordertotal decimal(9, 2), orderstatus varchar(15))";
        db.execSQL(sqlOrder);
        String sqlProductOrder = "CREATE TABlE " +TBL_PRODUCT_ORDER+ "(poid integer primary key autoincrement, orderid integer, productid integer, poqty integer, pototalprice decimal(9, 2))";
        db.execSQL(sqlProductOrder);
        String sqlWallet = "CREATE TABlE " +TBL_WALLET+ "(walletid integer primary key autoincrement, custid integer, date varchar(20), amount decimal(9, 2))";
        db.execSQL(sqlWallet);

        String tr_activate = "CREATE TRIGGER "+TR_ACTIVATE+" AFTER INSERT ON " +TBL_CUSTOMER+ " FOR EACH ROW "
                + "BEGIN "
                + "UPDATE "+TBL_CUSTOMER+" SET status=\"ACTIVATED\"; "
                + "END";
        db.execSQL(tr_activate);
    }

    //***********************************************************************************************************
    // ABOUT
    //************************************************************************************************************
    public void addAbout(String appAbout, String appServiceHours, String appAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("appabout", appAbout);
        cv.put("apphours", appServiceHours);
        cv.put("appaddress", appAddress);
        db.insert(TBL_ABOUT, null, cv);

        db.close();
    }

    public void editAbout(int aboutId, String appAbout, String appServiceHours, String appAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("appabout", appAbout);
        cv.put("apphours", appServiceHours);
        cv.put("appaddress", appAddress);
        db.update(TBL_ABOUT, cv, "aboutid=" +aboutId, null);

        db.close();
    }

    public ArrayList<About> getAllAbout() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_ABOUT, null, null, null, null, null, "aboutid");
        ArrayList<About> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int aboutId = c.getInt(c.getColumnIndex("aboutid"));
            String appAbout = c.getString(c.getColumnIndex("appabout"));
            String appServiceHours = c.getString(c.getColumnIndex("apphours"));
            String appAddress = c.getString(c.getColumnIndex("appaddress"));

            list.add(new About(aboutId, appAbout, appServiceHours, appAddress));
        }

        c.close();
        db.close();
        return list;
    }

    //***********************************************************************************************************
    // CUSTOMER
    //************************************************************************************************************
    public long addCustomer(String profilepic, String username, String password , String fname, String lname, String gender, String email, String contact, String address, double totalBal, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues cv = new ContentValues();

        cv.put("profilepic", profilepic);
        cv.put("username", username);
        cv.put("password", password);
        cv.put("fname", fname);
        cv.put("lname", lname);
        cv.put("gender", gender);
        cv.put("email", email);
        cv.put("contact", contact);
        cv.put("address", address);
        cv.put("totalbal", totalBal);
        cv.put("status", status);
        result = db.insert(TBL_CUSTOMER, null, cv);

        db.close();
        return result;
    }

    public ArrayList<Customer> findCustomer(int custId){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Customer> list = new ArrayList<>();
        Cursor c = db.query(TBL_CUSTOMER,null,null,null,null,null,"custid");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int custid = c.getInt(c.getColumnIndex("custid"));

            if(custId == custid) {
                String profilepic = c.getString(c.getColumnIndex("profilepic"));
                String username = c.getString(c.getColumnIndex("username"));
                String password = c.getString(c.getColumnIndex("password"));
                String fname = c.getString(c.getColumnIndex("fname"));
                String lname = c.getString(c.getColumnIndex("lname"));
                String gender = c.getString(c.getColumnIndex("gender"));
                String email = c.getString(c.getColumnIndex("email"));
                String contact = c.getString(c.getColumnIndex("contact"));
                String address = c.getString(c.getColumnIndex("address"));
                double totalBal = c.getDouble(c.getColumnIndex("totalbal"));
                String status = c.getString(c.getColumnIndex("status"));

                list.add(new Customer(Uri.parse(profilepic), username, password, fname, lname, gender, email, contact, address, totalBal, status));
                break;
            }
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Customer> getAllCustomer(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Customer> list = new ArrayList<>();
        Cursor c = db.query(TBL_CUSTOMER,null,null,null,null,null,"username");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String profilepic = c.getString(c.getColumnIndex("profilepic"));
            String username = c.getString(c.getColumnIndex("username"));
            String password = c.getString(c.getColumnIndex("password"));
            String fname = c.getString(c.getColumnIndex("fname"));
            String lname = c.getString(c.getColumnIndex("lname"));
            String gender = c.getString(c.getColumnIndex("gender"));
            String email = c.getString(c.getColumnIndex("email"));
            String contact = c.getString(c.getColumnIndex("contact"));
            String address = c.getString(c.getColumnIndex("address"));
            double totalBal = c.getDouble(c.getColumnIndex("totalbal"));
            String status = c.getString(c.getColumnIndex("status"));

            list.add(new Customer(Uri.parse(profilepic), username, password , fname, lname, gender, email, contact, address, totalBal, status));
        }

        c.close();
        db.close();
        return list;
    }

    public void editCustomerAccount(int custId, String profilepic, String username, String password, String fname, String lname, String gender, String email, String contact, String address, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("profilepic", profilepic);
        cv.put("username", username);
        cv.put("password", password);
        cv.put("fname", fname);
        cv.put("lname", lname);
        cv.put("gender", gender);
        cv.put("email", email);
        cv.put("contact", contact);
        cv.put("address", address);
        cv.put("status", status);
        db.update(TBL_CUSTOMER, cv, "custid=" +custId, null);
        db.update(TBL_ACCOUNT, cv, "custid=" +custId, null);

        db.close();
    }

    public void editCustomerAccountBal(int custId, double totalBal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("totalbal", totalBal);
        db.update(TBL_CUSTOMER, cv, "custid=" +custId, null);
        db.update(TBL_ACCOUNT, cv, "custid=" +custId, null);

        db.close();
    }

    public void deactivateCustomerAccount(int custId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("status", status);
        db.update(TBL_CUSTOMER, cv, "custid=" +custId, null);
        db.update(TBL_ACCOUNT, cv, "custid=" +custId, null);

        db.close();
    }

    //***********************************************************************************************************
    // ACCOUNT
    //************************************************************************************************************
    public long addAccount(int custId, String profilepic, String username, String password , String fname, String lname, String gender, String email, String contact, String address, double totalBal, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues cv = new ContentValues();

        cv.put("custid", custId);
        cv.put("profilepic", profilepic);
        cv.put("username", username);
        cv.put("password", password);
        cv.put("fname", fname);
        cv.put("lname", lname);
        cv.put("gender", gender);
        cv.put("email", email);
        cv.put("contact", contact);
        cv.put("address", address);
        cv.put("totalbal", totalBal);
        cv.put("status", status);
        result = db.insert(TBL_ACCOUNT, null, cv);

        db.close();
        return result;
    }

    public ArrayList<Account> getAccountInfo(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Account> list = new ArrayList<>();
        Cursor c = db.query(TBL_ACCOUNT,null,null,null,null,null,"username");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int custid = c.getInt(c.getColumnIndex("custid"));
            String profilepic = c.getString(c.getColumnIndex("profilepic"));
            String username = c.getString(c.getColumnIndex("username"));
            String password = c.getString(c.getColumnIndex("password"));
            String fname = c.getString(c.getColumnIndex("fname"));
            String lname = c.getString(c.getColumnIndex("lname"));
            String gender = c.getString(c.getColumnIndex("gender"));
            String email = c.getString(c.getColumnIndex("email"));
            String contact = c.getString(c.getColumnIndex("contact"));
            String address = c.getString(c.getColumnIndex("address"));
            double totalBal = c.getDouble(c.getColumnIndex("totalbal"));
            String status = c.getString(c.getColumnIndex("status"));

            list.add(new Account(custid, Uri.parse(profilepic), username, password , fname, lname, gender, email, contact, address, totalBal, status));
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Account> findCustomerAccount(int custId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String profilepic, username, password, fname, lname, gender, email, contact, address, status;
        ArrayList<Account> list = new ArrayList<>();
        Cursor c = db.query(TBL_CUSTOMER,null,null,null,null,null,"custid");

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int custid = c.getInt(c.getColumnIndex("custid"));

            if(custid == custId) {
                profilepic = c.getString(c.getColumnIndex("profilepic"));
                username = c.getString(c.getColumnIndex("username"));
                password = c.getString(c.getColumnIndex("password"));
                fname = c.getString(c.getColumnIndex("fname"));
                lname = c.getString(c.getColumnIndex("lname"));
                gender = c.getString(c.getColumnIndex("gender"));
                email = c.getString(c.getColumnIndex("email"));
                contact = c.getString(c.getColumnIndex("contact"));
                address = c.getString(c.getColumnIndex("address"));
                double totalBal = c.getDouble(c.getColumnIndex("totalbal"));
                status = c.getString(c.getColumnIndex("status"));

                list.add(new Account(custid, Uri.parse(profilepic), username, password, fname, lname, gender, email, contact, address, totalBal, status));
                break;
            }
        }

        db.close();
        c.close();
        return list;
    }

    public ArrayList<Account> findAccount(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String profilepic, username, password, fname, lname, gender, email, contact, address, status;
        ArrayList<Account> list = new ArrayList<>();
        Cursor c = db.query(TBL_CUSTOMER,null,null,null,null,null,"username");

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            username = c.getString(c.getColumnIndex("username"));

            if(userName.equals(username)) {
                int custid = c.getInt(c.getColumnIndex("custid"));
                profilepic = c.getString(c.getColumnIndex("profilepic"));
                username = c.getString(c.getColumnIndex("username"));
                password = c.getString(c.getColumnIndex("password"));
                fname = c.getString(c.getColumnIndex("fname"));
                lname = c.getString(c.getColumnIndex("lname"));
                gender = c.getString(c.getColumnIndex("gender"));
                email = c.getString(c.getColumnIndex("email"));
                contact = c.getString(c.getColumnIndex("contact"));
                address = c.getString(c.getColumnIndex("address"));
                double totalBal = c.getDouble(c.getColumnIndex("totalbal"));
                status = c.getString(c.getColumnIndex("status"));

                list.add(new Account(custid, Uri.parse(profilepic), username, password , fname, lname, gender, email, contact, address, totalBal, status));
                break;
            }
        }

        db.close();
        c.close();
        return list;
    }

    //************************************************************************************************************
    // PRODUCT
    //************************************************************************************************************
    public long addProduct(String productPic, String productBrand, String productName, String productCategory, String productColor, double productPrice, int productQty, float productRating) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues cv = new ContentValues();

        cv.put("productpic", productPic);
        cv.put("productbrand", productBrand);
        cv.put("productname", productName);
        cv.put("productcategory", productCategory);
        cv.put("productcolor", productColor);
        cv.put("productprice", productPrice);
        cv.put("productqty", productQty);
        cv.put("productrating", productRating);
        result = db.insert(TBL_PRODUCT, null, cv);

        db.close();
        return result;
    }

    public void editProduct(int productId, String productPic, String productBrand, String productName, String productCategory, String productColor, double productPrice, int productQty, float productRating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("productpic", productPic);
        cv.put("productbrand", productBrand);
        cv.put("productname", productName);
        cv.put("productcategory", productCategory);
        cv.put("productcolor", productColor);
        cv.put("productprice", productPrice);
        cv.put("productqty", productQty);
        cv.put("productrating", productRating);
        db.update(TBL_PRODUCT, cv, "productid=" +productId, null);

        db.close();
    }

    public void editProductQty(int productId, int productQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("productqty", productQty);
        db.update(TBL_PRODUCT, cv, "productid=" +productId, null);

        db.close();
    }

    public void editProductRating(int productId, float productRating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("productrating", productRating);
        db.update(TBL_PRODUCT, cv, "productid=" +productId, null);

        db.close();
    }

    public ArrayList<Product> findProduct(int productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Product> list = new ArrayList<>();
        int productId;
        Cursor c = db.query(TBL_PRODUCT,null,null,null,null,null,"productid");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            productId = c.getInt(c.getColumnIndex("productid"));

            if(productId == productid) {
                String productPic = c.getString(c.getColumnIndex("productpic"));
                String productBrand = c.getString(c.getColumnIndex("productbrand"));
                String productName = c.getString(c.getColumnIndex("productname"));
                String productCategory = c.getString(c.getColumnIndex("productcategory"));
                String productColor = c.getString(c.getColumnIndex("productcolor"));
                double productPrice = c.getDouble(c.getColumnIndex("productprice"));
                int productQty = c.getInt(c.getColumnIndex("productqty"));
                float productRating = c.getFloat(c.getColumnIndex("productrating"));

                list.add(new Product(productId, Uri.parse(productPic), productBrand, productName, productCategory, productColor, productPrice, productQty, productRating));
                break;
            }
        }

        c.close();
        db.close();
        return list;
    }

    public String findErrorProduct(int productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        int productId;
        Cursor c = db.query(TBL_PRODUCT,null,null,null,null,null,"productid");
        String errMessage = "";

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            productId = c.getInt(c.getColumnIndex("productid"));

            if(productId == productid) {
                String productName = c.getString(c.getColumnIndex("productname"));
                int productQty = c.getInt(c.getColumnIndex("productqty"));

                errMessage = String.format(Locale.getDefault(), "Product Id: %d\tProduct Name: %s\nProduct Qty: %d", productId, productName, productQty);
                break;
            }
        }

        c.close();
        db.close();
        return errMessage;
    }

    public ArrayList<Product> getAllProductCategory(String productcategory) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Product> list = new ArrayList<>();
        Cursor c = db.query(TBL_PRODUCT,null,null,null,null,null,"productname");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String productCategory = c.getString(c.getColumnIndex("productcategory"));

            if(productCategory.toLowerCase().equals(productcategory.toLowerCase())) {
                int productQty = c.getInt(c.getColumnIndex("productqty"));

                if(productQty != 0) {
                    int productId = c.getInt(c.getColumnIndex("productid"));
                    String productPic = c.getString(c.getColumnIndex("productpic"));
                    String productBrand = c.getString(c.getColumnIndex("productbrand"));
                    String productName = c.getString(c.getColumnIndex("productname"));
                    String productColor = c.getString(c.getColumnIndex("productcolor"));
                    double productPrice = c.getDouble(c.getColumnIndex("productprice"));
                    float productRating = c.getFloat(c.getColumnIndex("productrating"));

                    list.add(new Product(productId, Uri.parse(productPic), productBrand, productName, productCategory, productColor, productPrice, productQty, productRating));
                }
            }
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Product> getAllProductColor(String productcolor) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Product> list = new ArrayList<>();
        Cursor c = db.query(TBL_PRODUCT,null,null,null,null,null,"productname");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String productColor = c.getString(c.getColumnIndex("productcolor"));

            if(productColor.toLowerCase().equals(productcolor.toLowerCase())) {
                int productQty = c.getInt(c.getColumnIndex("productqty"));

                if(productQty != 0) {
                    int productId = c.getInt(c.getColumnIndex("productid"));
                    String productPic = c.getString(c.getColumnIndex("productpic"));
                    String productBrand = c.getString(c.getColumnIndex("productbrand"));
                    String productName = c.getString(c.getColumnIndex("productname"));
                    String productCategory = c.getString(c.getColumnIndex("productcategory"));
                    double productPrice = c.getDouble(c.getColumnIndex("productprice"));
                    float productRating = c.getFloat(c.getColumnIndex("productrating"));

                    list.add(new Product(productId, Uri.parse(productPic), productBrand, productName, productCategory, productColor, productPrice, productQty, productRating));
                }
            }
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Product> getAllProductCategoryColor(String productcategory, String productcolor) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Product> list = new ArrayList<>();
        Cursor c = db.query(TBL_PRODUCT,null,null,null,null,null,"productname");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String productCategory = c.getString(c.getColumnIndex("productcategory"));
            String productColor = c.getString(c.getColumnIndex("productcolor"));

            if(productCategory.toLowerCase().equals(productcategory.toLowerCase()) && productColor.toLowerCase().equals(productcolor.toLowerCase())) {
                int productQty = c.getInt(c.getColumnIndex("productqty"));

                if(productQty != 0) {
                    int productId = c.getInt(c.getColumnIndex("productid"));
                    String productPic = c.getString(c.getColumnIndex("productpic"));
                    String productBrand = c.getString(c.getColumnIndex("productbrand"));
                    String productName = c.getString(c.getColumnIndex("productname"));
                    double productPrice = c.getDouble(c.getColumnIndex("productprice"));
                    float productRating = c.getFloat(c.getColumnIndex("productrating"));

                    list.add(new Product(productId, Uri.parse(productPic), productBrand, productName, productCategory, productColor, productPrice, productQty, productRating));
                }
            }
        }

        c.close();
        db.close();
        return list;
    }

    public int getProductQty(int productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Product> list = new ArrayList<>();
        Cursor c = db.query(TBL_PRODUCT,null,null,null,null,null,"productname");
        list.clear();
        int productQty = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int productId = c.getInt(c.getColumnIndex("productid"));

            if(productId == productid) {
                productQty =  c.getInt(c.getColumnIndex("productqty"));
                break;
            }
        }

        c.close();
        db.close();
        return productQty;
    }

    public ArrayList<Product> getAllProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Product> list = new ArrayList<>();
        Cursor c = db.query(TBL_PRODUCT,null,null,null,null,null,"productname");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int productQty = c.getInt(c.getColumnIndex("productqty"));

            if(productQty != 0) {
                int productId = c.getInt(c.getColumnIndex("productid"));
                String productPic = c.getString(c.getColumnIndex("productpic"));
                String productBrand = c.getString(c.getColumnIndex("productbrand"));
                String productName = c.getString(c.getColumnIndex("productname"));
                String productCategory = c.getString(c.getColumnIndex("productcategory"));
                String productColor = c.getString(c.getColumnIndex("productcolor"));
                double productPrice = c.getDouble(c.getColumnIndex("productprice"));
                float productRating = c.getFloat(c.getColumnIndex("productrating"));

                list.add(new Product(productId, Uri.parse(productPic), productBrand, productName, productCategory, productColor, productPrice, productQty, productRating));
            }
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Product> getAllProductAdmin() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Product> list = new ArrayList<>();
        Cursor c = db.query(TBL_PRODUCT,null,null,null,null,null,"productname");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int productId = c.getInt(c.getColumnIndex("productid"));
            String productPic = c.getString(c.getColumnIndex("productpic"));
            String productBrand = c.getString(c.getColumnIndex("productbrand"));
            String productName = c.getString(c.getColumnIndex("productname"));
            String productCategory = c.getString(c.getColumnIndex("productcategory"));
            String productColor = c.getString(c.getColumnIndex("productcolor"));
            double productPrice = c.getDouble(c.getColumnIndex("productprice"));
            int productQty = c.getInt(c.getColumnIndex("productqty"));
            float productRating = c.getFloat(c.getColumnIndex("productrating"));

            list.add(new Product(productId, Uri.parse(productPic), productBrand, productName, productCategory, productColor, productPrice, productQty, productRating));
        }

        c.close();
        db.close();
        return list;
    }

    //***********************************************************************************************************
    // ORDER
    //************************************************************************************************************
    public long addOrder(int customerId, String orderDate, double orderTotal, String orderStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long res = 0;

        cv.put("customerid", customerId);
        cv.put("orderdate", orderDate);
        cv.put("ordertotal", orderTotal);
        cv.put("orderstatus", orderStatus);
        res = db.insert(TBL_ORDER, null, cv);

        db.close();
        return res;
    }

    public void editOrder(int orderId, int customerId, double orderTotal, String orderStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("customerid", customerId);
        cv.put("orderstatus", orderStatus);
        cv.put("ordertotal", orderTotal);
        db.update(TBL_ORDER, cv, "orderid=" +orderId, null);

        db.close();
    }

    public ArrayList<Order> getAllOrderId(int orderid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_ORDER, null, null, null, null, null, "customerid");
        ArrayList<Order> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int orderId = c.getInt(c.getColumnIndex("orderid"));

            if(orderId == orderid) {
                int customerId = c.getInt(c.getColumnIndex("customerid"));
                String orderDate = c.getString(c.getColumnIndex("orderdate"));
                String orderStatus = c.getString(c.getColumnIndex("orderstatus"));
                double orderTotal = c.getDouble(c.getColumnIndex("ordertotal"));

                list.add(new Order(orderId, customerId, orderDate, orderTotal, orderStatus));
            }
        }

        c.close();
        db.close();
        return list;
    }

    /*public void editOrderProductQty(int orderid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_ORDER, null, null, null, null, null, "customerid");
        //ArrayList<Order> list = new ArrayList<>();
        //list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int orderId = c.getInt(c.getColumnIndex("orderid"));

            if(orderId == orderid) {
                int customerId = c.getInt(c.getColumnIndex("customerid"));
                String orderDate = c.getString(c.getColumnIndex("orderdate"));
                String orderStatus = c.getString(c.getColumnIndex("orderstatus"));
                double orderTotal = c.getDouble(c.getColumnIndex("ordertotal"));

                //list.add(new Order(orderId, customerId, orderDate, orderTotal, orderStatus));
            }
        }

        c.close();
        db.close();
        //return list;
    }*/

    public ArrayList<Order> findOrderId(int orderid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_ORDER, null, null, null, null, null, "orderid");
        ArrayList<Order> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int orderId = c.getInt(c.getColumnIndex("orderid"));

            if(orderId == orderid) {
                int customerId = c.getInt(c.getColumnIndex("customerid"));
                String orderDate = c.getString(c.getColumnIndex("orderdate"));
                String orderStatus = c.getString(c.getColumnIndex("orderstatus"));
                double orderTotal = c.getDouble(c.getColumnIndex("ordertotal"));

                list.add(new Order(orderId, customerId, orderDate, orderTotal, orderStatus));
                break;
            }
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Order> getAllCurrentOrderCustomer(int customerid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_ORDER, null, null, null, null, null, "customerid");
        ArrayList<Order> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int customerId = c.getInt(c.getColumnIndex("customerid"));
            String orderStatus = c.getString(c.getColumnIndex("orderstatus"));

            if(customerId == customerid && orderStatus.equals("PENDING")) {
                int orderId = c.getInt(c.getColumnIndex("orderid"));
                String orderDate = c.getString(c.getColumnIndex("orderdate"));
                double orderTotal = c.getDouble(c.getColumnIndex("ordertotal"));

                list.add(new Order(orderId, customerId, orderDate, orderTotal, orderStatus));
            }
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Order> getAllOrderCustomer(int customerid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_ORDER, null, null, null, null, null, "orderstatus desc");
        ArrayList<Order> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int customerId = c.getInt(c.getColumnIndex("customerid"));

            if(customerId == customerid) {
                int orderId = c.getInt(c.getColumnIndex("orderid"));
                String orderDate = c.getString(c.getColumnIndex("orderdate"));
                String orderStatus = c.getString(c.getColumnIndex("orderstatus"));
                double orderTotal = c.getDouble(c.getColumnIndex("ordertotal"));

                list.add(new Order(orderId, customerId, orderDate, orderTotal, orderStatus));
            }
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Order> getAllOrder() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_ORDER, null, null, null, null, null, "orderid");
        ArrayList<Order> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String orderStatus = c.getString(c.getColumnIndex("orderstatus"));

            if(orderStatus.equals("PAID")) {
                int orderId = c.getInt(c.getColumnIndex("orderid"));
                int customerId = c.getInt(c.getColumnIndex("customerid"));
                String orderDate = c.getString(c.getColumnIndex("orderdate"));
                double orderTotal = c.getDouble(c.getColumnIndex("ordertotal"));

                list.add(new Order(orderId, customerId, orderDate, orderTotal, orderStatus));
            }
        }

        c.close();
        db.close();
        return list;
    }

    public double getSales() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_ORDER, null, null, null, null, null, "ordertotal");
        double totalSales = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String orderStatus = c.getString(c.getColumnIndex("orderstatus"));

            if(orderStatus.equals("PAID")) {
                double orderTotal = c.getDouble(c.getColumnIndex("ordertotal"));

                totalSales += orderTotal;
            }
        }

        c.close();
        db.close();
        return totalSales;
    }

    public void deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_ORDER, "orderid=" +orderId, null);
        db.close();
    }

    //***********************************************************************************************************
    // PRODUCT ORDER
    //************************************************************************************************************
    public long addProductOrder(int orderId, int productId, int productQty, double productTotalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long res = 0;

        cv.put("orderid", orderId);
        cv.put("productid", productId);
        cv.put("poqty", productQty);
        cv.put("pototalprice", productTotalPrice);
        res = db.insert(TBL_PRODUCT_ORDER, null, cv);

        db.close();
        return res;
    }

    public void editProductOrderPID(int orderId, int productId, int productQty, double productTotalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("orderid", orderId);
        cv.put("productid", productId);
        cv.put("poqty", productQty);
        cv.put("pototalprice", productTotalPrice);
        db.update(TBL_PRODUCT_ORDER, cv, "productid=" +productId+ " AND orderid=" +orderId, null);

        db.close();
    }

    public ArrayList<ProductOrder> findProductOrderId(int orderid, int productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_PRODUCT_ORDER, null, null, null, null, null, "orderid");
        ArrayList<ProductOrder> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int orderId = c.getInt(c.getColumnIndex("orderid"));
            int productId = c.getInt(c.getColumnIndex("productid"));

            if(orderId == orderid && productId == productid) {
                int productQty = c.getInt(c.getColumnIndex("poqty"));
                double productTotalPrice = c.getDouble(c.getColumnIndex("pototalprice"));

                list.add(new ProductOrder(orderId, productId, productQty, productTotalPrice));
                break;
            }
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<ProductOrder> getAllProductOrderId(int orderid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_PRODUCT_ORDER, null, null, null, null, null, "orderid");
        ArrayList<ProductOrder> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int orderId = c.getInt(c.getColumnIndex("orderid"));

            if(orderId == orderid) {
                int productId = c.getInt(c.getColumnIndex("productid"));
                int productQty = c.getInt(c.getColumnIndex("poqty"));
                double productTotalPrice = c.getDouble(c.getColumnIndex("pototalprice"));

                list.add(new ProductOrder(orderId, productId, productQty, productTotalPrice));
            }
        }

        c.close();
        db.close();
        return list;
    }

    public String editOrderProductQty(int orderid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_PRODUCT_ORDER, null, null, null, null, null, "orderid");
        ContentValues cv = new ContentValues();
        String isError = "f";

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int orderId = c.getInt(c.getColumnIndex("orderid"));

            if(orderId == orderid) {
                int productId = c.getInt(c.getColumnIndex("productid"));
                int pOQty = c.getInt(c.getColumnIndex("poqty"));

                int productQty = getProductQty(productId);
                if(productQty >= pOQty) {
                    Log.d("DB: ", productQty+ ">" +pOQty);
                } else {
                    if(isError.equals("f")) {
                        isError = "" +productId;
                    } else {
                        isError = "," +productId;
                    }
                }
            }
        }

        c.close();
        db.close();
        return isError;
    }

    public void editOrderProductQuantity(int orderid) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor c = db.query(TBL_PRODUCT_ORDER, null, null, null, null, null, "orderid");
        Cursor c2 = db2.query(TBL_PRODUCT, null, null, null, null, null, "productid");
        ContentValues cv = new ContentValues();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int orderId = c.getInt(c.getColumnIndex("orderid"));

            if(orderId == orderid) {
                c2 = db2.query(TBL_PRODUCT, null, null, null, null, null, "productid");
                int productId = c.getInt(c.getColumnIndex("productid"));
                int pOQty = c.getInt(c.getColumnIndex("poqty"));

                for(c2.moveToFirst(); !c2.isAfterLast(); c2.moveToNext()) {
                    int productQty = c2.getInt(c2.getColumnIndex("productqty"));

                    if (productQty >= pOQty) {
                        int remQty = productQty - pOQty;
                        cv.put("productqty", remQty);
                        db2.update(TBL_PRODUCT, cv, "productid=" + productId, null);
                        Log.d("DB: ", productQty + ">" + pOQty);
                        Log.d("remQty: ", "" +remQty);

                        break;
                    }
                }
            }
        }

        c.close();
        c2.close();
        db.close();
        db2.close();
    }

    public double getProductOrderTotal(int orderid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_PRODUCT_ORDER, null, null, null, null, null, "orderid");
        double total = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int orderId = c.getInt(c.getColumnIndex("orderid"));

            if(orderId == orderid) {
                double productTotalPrice = c.getDouble(c.getColumnIndex("pototalprice"));

                total += productTotalPrice;
            }
        }

        c.close();
        db.close();
        return total;
    }

    public void deleteProductOrder(int orderId, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_PRODUCT_ORDER, "orderid=" +orderId+ " AND productid=" +productId, null);
        db.close();
    }

    //************************************************************************************************************
    // RATING
    //************************************************************************************************************
    public long addRating(int productId, int customerId, String customerPic, String customerName, float rating, String comment, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues cv = new ContentValues();

        cv.put("productid", productId);
        cv.put("customerid", customerId);
        cv.put("customerpic", customerPic);
        cv.put("customername", customerName);
        cv.put("rating", rating);
        cv.put("comment", comment);
        cv.put("date", date);
        result = db.insert(TBL_RATING, null, cv);

        db.close();
        return result;
    }

    public void editRating(int productId, int customerId, float rating, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("rating", rating);
        cv.put("comment", comment);
        db.update(TBL_RATING, cv, "productid=" +productId+ " AND customerid=" +customerId, null);

        db.close();
    }

    public ArrayList<Rating> findProductRating(int productid, int customerid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Rating> list = new ArrayList<>();
        int productId, customerId;
        Cursor c = db.query(TBL_RATING,null,null,null,null,null,"productid");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            productId = c.getInt(c.getColumnIndex("productid"));
            customerId = c.getInt(c.getColumnIndex("customerid"));

            if(productId == productid && customerId == customerid) {
                int ratingId = c.getInt(c.getColumnIndex("id"));
                String customerPic = c.getString(c.getColumnIndex("customerpic"));
                String customerName = c.getString(c.getColumnIndex("customername"));
                float rating = c.getFloat(c.getColumnIndex("rating"));
                String comment = c.getString(c.getColumnIndex("comment"));
                String date = c.getString(c.getColumnIndex("date"));

                list.add(new Rating(ratingId, productId, customerId, Uri.parse(customerPic), customerName, rating, comment, date));
                break;
            }
        }

        c.close();
        db.close();
        return list;
    }

    public float getProductRating(int productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        float total = 0;
        int size = 0;
        int productId;
        Cursor c = db.query(TBL_RATING,null,null,null,null,null,"productid");

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            productId = c.getInt(c.getColumnIndex("productid"));

            if(productId == productid) {
                float productRating = c.getFloat(c.getColumnIndex("rating"));

                total += productRating;
                size++;
            }
        }

        float averageRating;
        if(size != 0) {
            averageRating = total / size;
        } else {
            averageRating = total;
        }

        editProductRating(productid, averageRating);

        c.close();
        db.close();
        return averageRating;
    }

    public ArrayList<Rating> getAllProductRating(int productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Rating> list = new ArrayList<>();
        int productId;
        Cursor c = db.query(TBL_RATING,null,null,null,null,null,"productid");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            productId = c.getInt(c.getColumnIndex("productid"));

            if(productId == productid) {
                int ratingId = c.getInt(c.getColumnIndex("id"));
                int customerId = c.getInt(c.getColumnIndex("customerid"));
                String customerPic = c.getString(c.getColumnIndex("customerpic"));
                String customerName = c.getString(c.getColumnIndex("customername"));
                float rating = c.getFloat(c.getColumnIndex("rating"));
                String comment = c.getString(c.getColumnIndex("comment"));
                String date = c.getString(c.getColumnIndex("date"));

                list.add(new Rating(ratingId, productId, customerId, Uri.parse(customerPic), customerName, rating, comment, date));
            }
        }

        c.close();
        db.close();
        return list;
    }

    //***********************************************************************************************************
    // WALLLET
    //************************************************************************************************************
    public long addWallet(int custId, String date, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long id = 0;

        cv.put("custid", custId);
        cv.put("date", date);
        cv.put("amount", amount);
        id = db.insert(TBL_WALLET, null, cv);

        db.close();
        return id;
    }

    public ArrayList<Wallet> getWalletCustomer(int custId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_WALLET, null, null, null, null, null, "custid");
        ArrayList<Wallet> list = new ArrayList<>();
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int custid = c.getInt(c.getColumnIndex("custid"));

            if(custId == custid) {
                int walletId = c.getInt(c.getColumnIndex("walletid"));
                String date = c.getString(c.getColumnIndex("date"));
                double amount = c.getDouble(c.getColumnIndex("amount"));

                list.add(new Wallet(walletId, custId, date, amount));
            }
        }

        c.close();
        db.close();
        return list;
    }

    public double getTotalWalletCustomer(int custId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_WALLET, null, null, null, null, null, "custid");
        double total = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int custid = c.getInt(c.getColumnIndex("custid"));

            if(custId == custid) {
                double amount = c.getDouble(c.getColumnIndex("amount"));

                total += amount;
            }
        }

        c.close();
        db.close();
        return total;
    }

    //************************************************************************************************************
    // DELETE
    //************************************************************************************************************
    public void deleteAllAbout() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_ABOUT, null, null);

        db.close();
    }

    public void deleteAllCustomer() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_CUSTOMER, null, null);

        db.close();
    }

    public void deleteAllAccount() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_ACCOUNT, null, null);

        db.close();
    }

    public void deleteAllProduct() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_PRODUCT, null, null);

        db.close();
    }

    public void deleteAllRating() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_RATING, null, null);

        db.close();
    }

    public void deleteAllWallet() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_WALLET, null, null);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }


    //************************************************************************************************************
    // STORED PROCEDURE
    //************************************************************************************************************
    public void spGetAllCustomer() {
        String sp_getAllCustomer = "CREATE PROCEDURE "+SP_GET_ALL_CUSTOMER
                + " AS "
                + "SELECT * FROM " +TBL_CUSTOMER;
    }

    public void spGetAllProduct() {
        String sp_getAllProduct = "CREATE PROCEDURE "+SP_GET_ALL_PRODUCT
                + " AS "
                + "SELECT * FROM " +TBL_PRODUCT;
    }

    public void spGetAllOrder() {
        String sp_getAllOrder= "CREATE PROCEDURE "+SP_GET_ALL_ORDER
                + " AS "
                + "SELECT * FROM " +TBL_ORDER;
    }

    public void spGetAllRating() {
        String sp_getAllRating= "CREATE PROCEDURE "+SP_GET_ALL_RATING
                + " AS "
                + "SELECT * FROM " +TBL_RATING;
    }
}