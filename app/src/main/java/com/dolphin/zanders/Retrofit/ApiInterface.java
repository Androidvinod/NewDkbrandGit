package com.dolphin.zanders.Retrofit;


import com.dolphin.zanders.Model.AboutUsModel.AboutusModel;
import com.dolphin.zanders.Model.AddToCartModel.AddToCartModel;
import com.dolphin.zanders.Model.AddToWishlit.AddToWishlist;
import com.dolphin.zanders.Model.Addresss_Modell.AddressModell;
import com.dolphin.zanders.Model.CartWishlistCountModel.CountModel;
import com.dolphin.zanders.Model.CartlistModel.Cartlist;
import com.dolphin.zanders.Model.CategoriesModel;
import com.dolphin.zanders.Model.CategoryModel.CategoryModel;
import com.dolphin.zanders.Model.Checkout_model.CheckoutTotalModel;
import com.dolphin.zanders.Model.Create_order_model.CreateorderModel;
import com.dolphin.zanders.Model.FavouriteModel.GetFavouriteslist;
import com.dolphin.zanders.Model.Forgotpassword.Forgotpassword_model;
import com.dolphin.zanders.Model.GetAddresslistModel.GetAddressModel;
import com.dolphin.zanders.Model.Home_model.HomePage;
import com.dolphin.zanders.Model.Homedk.Homedata;
import com.dolphin.zanders.Model.LoginModel.Login_Model;
import com.dolphin.zanders.Model.Manufacturerslist_model.GetManufacturelistModel;
import com.dolphin.zanders.Model.NewOrderDetailModel.NewOrderDetailModel;
import com.dolphin.zanders.Model.NewOrderModel.NewOrderModel;
import com.dolphin.zanders.Model.NewProductListModel.NewProductListModel;
import com.dolphin.zanders.Model.OrderView_model.OrderviewModel;
import com.dolphin.zanders.Model.Ordermodel.OrderModel;
import com.dolphin.zanders.Model.PaymentMethodModel.PaymentMehtodModel;
import com.dolphin.zanders.Model.PrivacyModel;
import com.dolphin.zanders.Model.ProductDetailModel.GetProductdetails;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlist;
import com.dolphin.zanders.Model.RemoveProductFomCart.RemoveCartProductModel;
import com.dolphin.zanders.Model.RemoveWishlistModel.RemoveFromWishlistModel;
import com.dolphin.zanders.Model.Shipingmethod.ShippingMethodModel;
import com.dolphin.zanders.Model.Sortlist_model.GetSortModel;
import com.dolphin.zanders.Model.SubCategoryModel.SubCategoryModel;
import com.dolphin.zanders.Model.UpdateCartQtyModel.UpdateCartQtyModel;
import com.dolphin.zanders.Model.UpdateuserModel.UpdateserinfoModel;
import com.dolphin.zanders.Model.UserInformationModel.UserinfoModel;
import com.dolphin.zanders.Model.Wishlistcheck_model.WishlistproductcheckModel;
import com.dolphin.zanders.Model.change_password.Changepassword;
import com.dolphin.zanders.Model.porduct_showcustome.ProductModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {
    //remove coponun code api
    //http://dkbraende.demoproject.info/rest/V1/carts/192029/coupons
    @DELETE()
    Call<Boolean> deletecopouncode(@Header("Authorization") String authHeader, @Url String url);


    //add copon code
    //http://dkbraende.demoproject.info/rest/V1/carts/192029/coupons/test
    //pass auth token
    @PUT()
    Call<Boolean> addCoponCode(@Header("Authorization") String authHeader, @Url String url);

    //orderdetail
    //http://dkbraende.demoproject.info/rest/V1/orders/61989
    //pass auth token
    @GET()
    Call<NewOrderDetailModel> orderDetail(@Header("Authorization") String authHeader,
                                          @Url String url);


    //http://dkbraende.demoproject.info/rest/V1/orders/?searchCriteria[filterGroups][0][filters][0][field]=customer_id&searchCriteria[filterGroups][0][filters][0][value]=12517&searchCriteria[filterGroups][0][filters][0][conditionType]=eq
    @GET()
    Call<NewOrderModel> getorderListData(@Header("Authorization") String authHeader,
                                         @Url String url);


    //search first api
    //http://dkbraende.demoproject.info/rest/V1/search?searchCriteria[requestName]=quick_search_container&searchCriteria[filter_groups][0][filters][0][field]=search_term&searchCriteria[filter_groups][0][filters][0][value]=brændetårn&fields=items[id]
    //getsearch api
    //http://dkbraende.demoproject.info/rest/all/V1/products?searchCriteria[filterGroups][0][filters][0][field]=entity_id&searchCriteria[filterGroups][0][filters][0][value]=364,227&searchCriteria[filterGroups][0][filters][0][conditionType]=fineset

    @GET()
    Call<ResponseBody> getSearchValue(@Header("Authorization") String authHeader,
                                      @Url String url);


    @GET()
    Call<ResponseBody> getSearchList(@Header("Authorization") String authHeader,
                                     @Url String url);


    //delete address api
    //http://dkbraende.demoproject.info/rest/V1/addresses/15593
    //DELETE method
    //pass auth token
    @DELETE()
    Call<Boolean> deleteaddress(@Header("Authorization") String authHeader, @Url String url);


    //country list
    //http://dkbraende.demoproject.info/rest/V1/directory/countries
    @GET("directory/countries")
    Call<ResponseBody> getCountryList(@Header("Authorization") String authHeader);


    //edit userinfo
    //        //http://dkbraende.demoproject.info/rest/V1/customers/12497/?customer[addresses][0][customer_id]=12497&customer[addresses][0][countryId]=DK&customer[addresses][0][street][0]=sdsds&customer[addresses][0][firstname]=info&customer[addresses][0][lastname]=info&customer[addresses][0][company]=dolphin_test&customer[addresses][0][telephone]=123456789&customer[addresses][0][city]=test&customer[addresses][0][postcode]=382348&customer[email]=info@gmail.com&customer[id]=12497&customer[websiteId]=1

    //creat address api
    //pass auth token
    //http://dkbraende.demoproject.info/rest/V1/customers/12497/?customer[addresses][0][customer_id]=12497&customer[addresses][0][countryId]=DK&customer[addresses][0][street][0]=sdsds&customer[addresses][0][firstname]=info&customer[addresses][0][lastname]=info&customer[addresses][0][company]=dolphin_test&customer[addresses][0][telephone]=123456789&customer[addresses][0][city]=test&customer[addresses][0][postcode]=382348&customer[email]=info@gmail.com&customer[id]=12497&customer[websiteId]=1
    @PUT()
    Call<ResponseBody> createAddreess(@Header("Authorization") String authHeader, @Url String url);


    //change password
    //http://dkbraende.demoproject.info/rest/V1/customers/me/password?currentPassword=vinod@203&newPassword=vinod@123
    //pass customer token
    //@put
    //http://dkbraende.demoproject.info/rest/V1/customers/me/password?currentPassword=Admin@123&newPassword=Admin@1234
    @PUT()
    Call<Boolean> changePassword(@Header("Authorization") String authHeader, @Url String url);


    //http://dkbraende.demoproject.info/rest//V1/customers/12497
    //getAddress
    //pass auth token
    //customerid
    @GET()
    Call<ResponseBody> getAddress(@Header("Authorization") String authHeader,
                                  @Url String url);


    //get wish list api
    //http://dkbraende.demoproject.info/rest/V1/wishlist/items
    @GET("wishlist/items")
    Call<ResponseBody> defaultgetWishlistData(@Header("Authorization") String authHeader);


    //delete api  use the delete method
    //https://app.demoproject.info/rest/V1/wishlist/delete/26
    @DELETE()
    Call<Boolean> removeitemfromWishlistt(@Header("Authorization") String authHeader,
                                          @Url String url);


    //Add to wishlist api
    //post method and customer token
    //http://dkbraende.demoproject.info/rest/V1/wishlist/add/367
    //http://dkbraende.demoproject.info/rest/V1/wishlist/add/:wishlist_item_id
    @POST()
    Call<Boolean> defaultaddtowishlist(@Header("Authorization") String authHeader, @Url String url);

    //wishlist count
    //count wishlist api
    //get method
    //http://dkbraende.demoproject.info/rest/V1/wishlist/info
    @GET("wishlist/info")
    // http://dkbraende.demoproject.info/rest/V1/wishlist/info
    Call<ResponseBody> defaultWishlistCount(@Header("Authorization") String authHeader);


    //http://dkbraende.demoproject.info/rest/V1/integration/admin/token/?username=admin&password=9yWpe6v7(OZ7
   /* @POST("integration/admin/token/?")
    Call<String> gettoken(@Query("username") String username,
                                @Query("password") String password);*/


    //get auth token
    //http://dkbraende.demoproject.info/rest/V1/store/storeConfigs
    @GET("store/storeConfigs")
    Call<ResponseBody> getcurrenycode(@Header("Authorization") String authHeader);


    @POST()
    Call<String> gettoken(@Url String url);

    //http://dkbraende.demoproject.info/rest//V1/products?searchCriteria[filterGroups][0][filters][0][field]=category_id&searchCriteria[filterGroups][0][filters][0][value]=2
    @GET("products")
    Call<ProductModel> products(@Header("Authorization") String authHeader,
                                @Query("searchCriteria[filterGroups][0][filters][0][field]") String categoryid,
                                @Query("searchCriteria[filterGroups][0][filters][0][value]") String id);

    @GET("products")
    Call<ResponseBody> getproducts(@Header("Authorization") String authHeader,
                                   @Query("searchCriteria[filterGroups][0][filters][0][field]") String categoryid,
                                   @Query("searchCriteria[filterGroups][0][filters][0][value]") String id);

    @GET("products")
    Call<NewProductListModel> getproductslist(@Header("Authorization") String authHeader,
                                              @Query("searchCriteria[filterGroups][0][filters][0][field]") String categoryid,
                                              @Query("searchCriteria[filterGroups][0][filters][0][value]") String id);


    //product detail
    //http://dkbraende.demoproject.info/rest/V1/products?searchCriteria[filterGroups][0][filters][0][field]=entity_id&searchCriteria[filterGroups][0][filters][0][condition_type]=eq&searchCriteria[filterGroups][0][filters][0][value]=130
    @GET("products")
    Call<ResponseBody> productsdetail(@Header("Authorization") String authHeader,
                                      @Query("searchCriteria[filterGroups][0][filters][0][field]") String enitityid,
                                      @Query("searchCriteria[filterGroups][0][filters][0][condition_type]") String eq,
                                      @Query("searchCriteria[filterGroups][0][filters][0][value]") String id);


    //add to cart
    //http://app.demoproject.info/rest/V1/integration/customer/token?username=test@gmail.com&password=admin@123

    //http://dkbraende.demoproject.info/rest/V1/integration/customer/token?username=vinod@dolphinwebsolution.com&password=vinod@203
    //http://dkbraende.demoproject.info/rest/V1/carts/mine/?customerId=2
    //http://dkbraende.demoproject.info/rest/V1/carts/mine/items?cartItem[quoteId]=2&cartItem[qty]=1&cartItem[sku]=CHECKED PINAFORE DRESS

    //
    //custome token
    @POST()
    Call<String> getcustomerToken(@Url String url);


    //remove from cart
    //DELETE http://dkbraende.demoproject.info/rest//V1/carts/mine/items/{itemId}
    //http://dkbraende.demoproject.info/rest//V1/carts/mine/items/162920
    @DELETE()
    Call<Boolean> removeitemfromcart(@Header("Authorization") String authHeader,
                                     @Url String url);


    //login data
    //http://dkbraende.demoproject.info/rest/V1/customers/me
    @GET("customers/me")
    Call<ResponseBody> loginn(@Header("Authorization") String authHeader);

    //getshippingmethid
    //http://dkbraende.demoproject.info/rest//V1/carts/mine/shipping-methods

    //http://dkbraende.demoproject.info/rest/V1/carts/:cartId/shipping-methods
    @GET()
    Call<ResponseBody> getshipping(@Header("Authorization") String authHeader, @Url String url);

    //get payment
    //-
    @GET()
    Call<ResponseBody> getpaymentmethod(@Header("Authorization") String authHeader,
                                        @Url String url);


    //get quote id
    @POST()
    Call<Integer> getQuoteid(@Header("Authorization") String authHeader,
                             @Url String customer_id);

    //add to cart
    @POST()
    Call<ResponseBody> getaddtocartapi(@Header("Authorization") String authHeader,
                                       @Url String url);


    //cartlist api
    //http://dkbraende.demoproject.info/rest/V1/carts/mine/items
    @GET("carts/mine/items")
    Call<ResponseBody> getcartlistapi(@Header("Authorization") String authHeader);

    //http://app.demoproject.info/rest/V1/customers/?customer[email]=test12@yahoo.com&customer[firstname]=AA&customer[lastname]=BB&password=admin@123
    //register
    @POST()
    Call<ResponseBody> register(@Header("Authorization") String authHeader,
                                @Url String url);


    //address
    //http://dkbraende.demoproject.info/rest//V1/customers/1
    @GET()
    Call<AddressModell> address(@Header("Authorization") String authHeader, @Url String url);


    //category list
    ///http://testgz.shop2.gzanders.com/mobileapi/getcategorylist.php
    //http://dkbraende.demoproject.info/rest//V1/categories
    @GET("categories")
    Call<CategoriesModel> categories(@Header("Authorization") String authHeader);

    //http://dkbraende.demoproject.info/rest/V1/customers/password?email=info@gmail.com&template=email_reset
    //forget passwprd
    @PUT()
    Call<Boolean> forgetpassword(@Header("Authorization") String authHeader, @Url String url);


    /////*************************************************************************************************************************************


    @GET("getcategorylist.php")
    Call<CategoryModel> getCategoryData();

    //category list
    ///http://testgz.shop2.gzanders.com/mobileapi/getCount.php?customer_id=34406
    @GET("getCount.php")
    Call<CountModel> getCount(@Query("customer_id") String customer_id);

    //subcategory list
    //http://testgz.shop2.gzanders.com/mobileapi/getsubcategorylist.php?categoryid=
    @GET("getsubcategorylist.php")
    Call<SubCategoryModel> getSubCategoryData(@Query("categoryid") String categoryid);

    //login
    @POST("login.php")
    @FormUrlEncoded
    Call<Login_Model> login(@Field("email") String email,
                            @Field("password") String password);

    //productlist
    // http://testgz.shop2.gzanders.com/mobileapi/getcategoryproductlist.php?customer_id=34406&category_id=595&page=2
    @GET("getcategoryproductlist.php")
    Call<GetCategoryProductlist> categoryproducts(@Query("category_id") String category_id,
                                                  @Query("customer_id") String customer_id,
                                                  @Query("page") int page);

    //closeout productlist
    //http://testgz.shop2.gzanders.com/mobileapi/closeoutProducts.php?page=2
    @GET("closeoutProducts.php")
    Call<GetCategoryProductlist> getcloseoutProduct(@Query("page") int page,
                                                    @Query("customer_id") String customer_id,
                                                    @Query("dir") String dir,
                                                    @Query("sort") String sort);

    //wishlist
    //http://testgz.shop2.gzanders.com/mobileapi/getwishlist.php?customer_id=34406&page=1
    @GET("getwishlist.php")
    Call<GetFavouriteslist> getWishlist(@Query("customer_id") String customer_id,
                                        @Query("page") int page);

    //add to cart
    //http://testgz.shop2.gzanders.com/mobileapi/addtowishlist.php?customer_id=34406&product_id=64318
    @GET("addtowishlist.php")
    Call<AddToWishlist> addtowishlist(@Query("customer_id") String customer_id,
                                      @Query("product_id") String product_id);

    //remove from wishlist
    //http://testgz.shop2.gzanders.com/mobileapi/removefromwishlist.php?customer_id=34406&product_id=1785&page=1
    @GET("removefromwishlist.php")
    Call<RemoveFromWishlistModel> removewishlist(@Query("customer_id") String customer_id,
                                                 @Query("product_id") String product_id);


    //payment method
    //http://testgz.shop2.gzanders.com/mobileapi/getpaymentmethods.php?
    @GET("getpaymentmethods.php")
    Call<PaymentMehtodModel> getPaymentMethod();


    //http://testgz.shop2.gzanders.com/mobileapi/addtocart.php?email=&product_id=54842&customer_id=41474
    //add to cart
    @GET("addtocart.php")
    Call<AddToCartModel> addToCart(@Query("email") String email,
                                   @Query("product_id") String product_id);


    //cartlist
    //http://testgz.shop2.gzanders.com/mobileapi/cartlist.php?email=zanderstestacc@gmail.com
    @GET("cartlist.php")
    Call<Cartlist> getcartlist(@Query("email") String email);


    ///remove product from cart
    //http://testgz.shop2.gzanders.com/mobileapi/removeproductincart.php?email=zanderstestacc@gmail.com&product_id=651
    @GET("removeproductincart.php")
    Call<RemoveCartProductModel> removeproductFromCart(@Query("email") String email,
                                                       @Query("product_id") String product_id);

    //update cart quantity
    //http://testgz.shop2.gzanders.com/mobileapi/updatecart.php?email=zanderstestacc@gmail.com&product_id=42173&qty=3
    @GET("updatecart.php")
    Call<UpdateCartQtyModel> updatecart(@Query("email") String email,
                                        @Query("product_id") String product_id,
                                        @Query("qty") String qty);


    //http://testgz.shop2.gzanders.com/mobileapi/getproduct.php?product_id=64318&customer_id=&related_page
    //productdetail
    //http://testgz.shop2.gzanders.com/mobileapi/getproduct.php?product_id=3965
    @POST("getproduct.php")
    @FormUrlEncoded
    Call<GetProductdetails> getproductdetails(@Field("product_id") String category_id);

    @POST("getaddresslist.php")
    @FormUrlEncoded
    Call<GetAddressModel> getaddressapi(@Field("customer_id") String category_id);

    //homepage
    //http://testgz.shop2.gzanders.com/mobileapi/homepage.php?
    @GET("homepage.php")
    Call<HomePage> gethomepage();

    @GET("homepage.php")
    Call<ResponseBody> getSubmanu();

    //manufacturerlist
    //http://testgz.shop2.gzanders.com/mobileapi/getManufacturelist.php?page=1
    @GET("getManufacturelist.php")
    Call<GetManufacturelistModel> getmanufacturerlistapi();

    ///search
    //http://testgz.shop2.gzanders.com/mobileapi/getsearchresult.php?q=HENRY%20U.S.%20SURVIVAL%20AR-7%20.22LR%2016.125%22%20BLACK%20SYNTHETIC
    @GET("getsearchresult.php")
    Call<GetCategoryProductlist> getsearchresult(@Query("customer_id") String customer_id
            , @Query("q") String q,
                                                 @Query("page") int page);

    //change password
    //http://testgz.shop2.gzanders.com/mobileapi/changePassword.php?customer_id=34406&password=1234567&confirm_password=1234567&current_password=123456
    @GET("changePassword.php")
    Call<Changepassword> changepassword(@Query("customer_id") String customer_id,
                                        @Query("current_password") String current_password,
                                        @Query("password") String password,
                                        @Query("confirm_password") String confirm_password);

    //forgot password
    //http://testgz.shop2.gzanders.com/mobileapi/forgotpassword.php?email=developertest2017@gmail.com
    @GET("forgotpassword.php")
    Call<Forgotpassword_model> forgotpassword(@Query("email") String email);

    //offerlist
    //http://testgz.shop2.gzanders.com/mobileapi/offerlist.php?page=305
    @GET("offerlist.php")
    Call<GetCategoryProductlist> getOfferList(@Query("customer_id") String customer_id, @Query("page") int page);

    //shipping method
    //http://testgz.shop2.gzanders.com/mobileapi/getshippingmethods.php?quote_id=972101
    @GET("getshippingmethods.php")
    Call<ShippingMethodModel> getShippingMethod(@Query("quote_id") String quote_id);

    //payment method
    //http://testgz.shop2.gzanders.com/mobileapi/getpaymentmethods.php?quote_id=972101
    @GET("getpaymentmethods.php")
    Call<PaymentMehtodModel> getPaymentMethod(@Query("quote_id") String quote_id);


    // http://testgz.shop2.gzanders.com/mobileapi/getuserinfo.php?customer_id=34406
    @GET("getuserinfo.php")
    Call<UserinfoModel> getuserinfo(@Query("customer_id") String customer_id);

    //http://testgz.shop2.gzanders.com/mobileapi/updateuserprofile.php?customer_id=34406&firstname=ZANDERS%20TEST%20ACCOUNT%20Mobiles&email=zanderstestacc@gmail.co
    @GET("updateuserprofile.php")
    Call<UpdateserinfoModel> updateusrinfo(@Query("customer_id") String customer_id,
                                           @Query("firstname") String firstname,
                                           @Query("email") String email);

    //http://testgz.shop2.gzanders.com/mobileapi/updateuserprofile.php?customer_id=34406&firstname=ZANDERS%20TEST%20ACCOUNT%20Mobiles&email=zanderstestacc@gmail.co
    @GET("cmspage.php")
    Call<AboutusModel> aboutus(@Query("identifier") String identifier);

    //http://testgz.shop2.gzanders.com/mobileapi/getCustomProducts.php?pro=popular_products&page=1
    @GET("getCustomProducts.php")
    Call<GetCategoryProductlist> getCustomProducts(@Query("pro") String pro,
                                                   @Query("customer_id") String customer_id,
                                                   @Query("page") int page);

    //orderlist
    //http://testgz.shop2.gzanders.com/mobileapi/orderlist.php?customer_id=34406&page=1
    @GET("orderlist.php")
    Call<OrderModel> getOrderlist(@Query("customer_id") String customer_id,
                                  @Query("page") int page);

    //checkout api
    //  http://testgz.shop2.gzanders.com/mobileapi/quoteData.php?email=zanderstestacc@gmail.com&customer_billing_address_id=41045&customer_shipping_address_id=43839&shipping_method=zandersship_U1&payment_method=zpay
    @GET("quoteData.php")
    Call<CheckoutTotalModel> getCheckouttotal(@Query("email") String email,
                                              @Query("customer_billing_address_id") String customer_billing_address_id,
                                              @Query("customer_shipping_address_id") String customer_shipping_address_id,
                                              @Query("shipping_method") String shipping_method,
                                              @Query("quote_id") String quote_id,
                                              @Query("payment_method") String payment_method);


    //createorder api
    // http://testgz.shop2.gzanders.com/mobileapi/createorder.php?email=zanderstestacc@gmail.com&customer_billing_address_id=41045&customer_shipping_address_id=43839&shipping_method=zandersship_U1&payment_method=zpay&payment_po_number=232132131&payment_process_date=2020-01-02&payment_comments=Test%20comment&payment_notes=COD%20Certified%20Funds&payment_terms=COD%20CERT%20FUNDS
    @GET("createorder.php")
    Call<CreateorderModel> createorder(@Query("email") String email,
                                       @Query("customer_billing_address_id") String customer_billing_address_id,
                                       @Query("customer_shipping_address_id") String customer_shipping_address_id,
                                       @Query("shipping_method") String shipping_method,
                                       @Query("payment_method") String payment_method,
                                       @Query("payment_po_number") String payment_po_number,
                                       @Query("payment_process_date") String payment_process_date,
                                       @Query("payment_comments") String payment_comments,
                                       @Query("payment_notes") String payment_notes,
                                       @Query("payment_terms") String payment_terms);


    //createorder api
    // http://testgz.shop2.gzanders.com/mobileapi/createorder.php?email=npatel@coreshopsolutions.com&customer_billing_address_id=52356&customer_shipping_address_id=52356&shipping_method=zandersship_PU&payment_method=zcard&cc_number=4766410001371111&cc_exp_year=2022&cc_exp_month=5&cc_cid=134&payment_process_date=2020/01/13&payment_po_number=1
    @GET("createorder.php")
    Call<CreateorderModel> createorder_zcard(@Query("email") String email,
                                             @Query("customer_billing_address_id") String customer_billing_address_id,
                                             @Query("customer_shipping_address_id") String customer_shipping_address_id,
                                             @Query("shipping_method") String shipping_method,
                                             @Query("payment_method") String payment_method,
                                             @Query("cc_number") String cc_number,
                                             @Query("cc_exp_year") String cc_exp_year,
                                             @Query("cc_exp_month") String cc_exp_month,
                                             @Query("cc_cid") String cc_cid,
                                             @Query("payment_process_date") String payment_process_date,
                                             @Query("payment_po_number") String payment_po_number);

    //http://testgz.shop2.gzanders.com/mobileapi/orderview.php?increment_id=779380&email=zanderstestacc@gmail.com
    @GET("orderview.php")
    Call<OrderviewModel> getvieworder(@Query("increment_id") String increment_id,
                                      @Query("email") String email);

    @GET("sortcloseout.php")
    Call<GetSortModel> getsort();


    //manufacturer product
    //https://shop2.gzanders.com/mobileapi/getmanufacturerProducts.php?customer_id=34406&manufacturer=682&page=1&sort=&dir=asc
    @GET("getmanufacturerProducts.php")
    Call<GetCategoryProductlist> manufacturerproducts_withlogin(@Query("manufacturer") String category_id,
                                                                @Query("customer_id") String customer_id,
                                                                @Query("page") int page,
                                                                @Query("dir") String dir,
                                                                @Query("sort") String sort);


    @GET("getmanufacturerProducts.php")
    Call<GetCategoryProductlist> manufaturerproducts_withoutlogin(@Query("manufacturer") String category_id,
                                                                  @Query("page") int page,
                                                                  @Query("dir") String dir,
                                                                  @Query("sort") String sort);

    //productlist
    // http://testgz.shop2.gzanders.com/mobileapi/getcategoryproductlist.php?customer_id=34406&category_id=595&page=2
    @GET("getcategoryproductlist.php")
    Call<GetCategoryProductlist> categoryproducts_withlogin(@Query("category_id") String category_id,
                                                            @Query("customer_id") String customer_id,
                                                            @Query("page") int page,
                                                            @Query("dir") String dir,
                                                            @Query("sort") String sort);


    @GET("getcategoryproductlist.php")
    Call<GetCategoryProductlist> categoryproducts_withoutlogin(@Query("category_id") String category_id,
                                                               @Query("page") int page,
                                                               @Query("dir") String dir,
                                                               @Query("sort") String sort);

    //get filter api
    //http://testgz.shop2.gzanders.com/mobileapi/getcategoryproductlist.php?
    // category_id=595&customer_id=34406&page=1&manufacturer=1164&sort=name
    @GET
    Call<GetCategoryProductlist> call_filter_categoryproducts(@Url String url);

    //http://testgz.shop2.gzanders.com/mobileapi/getCustomProducts.php?pro=popular_products&page=1
    @GET("getCustomProducts.php")
    Call<GetCategoryProductlist> getCustomProducts_withlogin(@Query("pro") String pro,
                                                             @Query("customer_id") String customer_id,
                                                             @Query("page") int page);

    //http://testgz.shop2.gzanders.com/mobileapi/getCustomProducts.php?pro=popular_products&page=1
    @GET("getCustomProducts.php")
    Call<GetCategoryProductlist> getCustomProducts_withoutlogin(@Query("pro") String pro,
                                                                @Query("page") int page);

    //offerlist
    //http://testgz.shop2.gzanders.com/mobileapi/offerlist.php?page=305
    @GET("offerlist.php")
    Call<GetCategoryProductlist> getOfferList_withlogin(@Query("customer_id") String customer_id,
                                                        @Query("sort") String sort,
                                                        @Query("dir") String dir,
                                                        @Query("page") int page);

    //offerlist
    //http://testgz.shop2.gzanders.com/mobileapi/offerlist.php?page=305
    @GET("offerlist.php")
    Call<GetCategoryProductlist> getOfferList_withoutlogin(@Query("page") int page,
                                                           @Query("sort") String sort,
                                                           @Query("dir") String dir);

    ///search
    //http://testgz.shop2.gzanders.com/mobileapi/getsearchresult.php?q=HENRY%20U.S.%20SURVIVAL%20AR-7%20.22LR%2016.125%22%20BLACK%20SYNTHETIC
    @GET("getsearchresult.php")
    Call<GetCategoryProductlist> getsearchresult_withlogin(@Query("customer_id") String customer_id,
                                                           @Query("q") String q,
                                                           @Query("sort") String sort,
                                                           @Query("dir") String dir,
                                                           @Query("page") int page);

    //http://testgz.shop2.gzanders.com/mobileapi/getsearchresult.php?q=HENRY%20U.S.%20SURVIVAL%20AR-7%20.22LR%2016.125%22%20BLACK%20SYNTHETIC
    @GET("getsearchresult.php")
    Call<GetCategoryProductlist> getsearchresult_withoutlogin(@Query("q") String q,
                                                              @Query("sort") String sort,
                                                              @Query("dir") String dir,
                                                              @Query("page") int page);

    //http://testgz.shop2.gzanders.com/mobileapi/wishlistproduct.php?product_id=56823&customer_id=41492
    @GET("wishlistproduct.php")
    Call<WishlistproductcheckModel> getwishlistcheck(@Query("product_id") String product_id,
                                                     @Query("customer_id") String customer_id);


    @GET("offerfilterlist.php")
    Call<GetSortModel> getoffersort();

    @GET("filterlist.php")
    Call<GetSortModel> getProductlistsort(@Query("category_id") String category_id);

    @GET("filterlistSearch.php")
    Call<GetSortModel> getSearchlistsort(@Query("q") String q,
                                         @Query("customer_id") String customer_id,
                                         @Query("sort") String sort,
                                         @Query("page") String page);


    //country list
    //http://dkbraende.demoproject.info/rest/V1/carts/mine/payment-information
    @GET("carts/mine/payment-information")
    Call<ResponseBody> getpricedata(@Header("Authorization") String authHeader);

    //http://dkbraende.demoproject.info/rest/V1/carts/:cartId/items
    //update cart
    @POST()
    Call<ResponseBody> udatecarttt(@Header("Authorization") String authHeader,
                                   @Url String url);

    //create order
    //http://dkbraende.demoproject.info/rest//V1/orders/create?entity[base_currency_code]=USD&entity[base_discount_amount]=0&entity[base_grand_total]=25.98&entity[base_shipping_amount]=0&entity[base_subtotal]=25.98&entity[base_tax_amount]=0&entity[customer_email]=test@gmail.com&entity[customer_firstname]=Test&entity[customer_group_id]=1&entity[customer_id]=2&entity[customer_is_guest]=0&entity[customer_lastname]=Test&entity[customer_note_notify]=1&entity[discount_amount]=0&entity[email_sent]=1&entity[grand_total]=25.98&entity[is_virtual]=0&entity[order_currency_code]=USD&entity[shipping_amount]=0&entity[shipping_description]=Shipping %26 Handling&entity[state]=new&entity[status]=pending&entity[store_currency_code]=usd&entity[store_id]=1&entity[store_name]=Main Website\nMain Website Store\n&entity[subtotal]=25.98&entity[subtotal_incl_tax]=25.98&entity[tax_amount]=0&entity[total_item_count]=2&entity[total_qty_ordered]=2&entity[items][0][base_discount_amount]=0&entity[items][0][base_original_price]=12.99&entity[items][0][base_price]=12.99&entity[items][0][base_price_incl_tax]=12.99&entity[items][0][base_row_invoiced]=25.98&entity[items][0][base_row_total]=25.98&entity[items][0][base_tax_amount]=0&entity[items][0][base_tax_invoiced]=0&entity[items][0][discount_amount]=0&entity[items][0][discount_percent]=0&entity[items][0][free_shipping]=0&entity[items][0][is_virtual]=0&entity[items][0][name]=Firestarter 30 stk&entity[items][0][original_price]=80&entity[items][0][price]=12.99&entity[items][0][price_incl_tax]=12.99&entity[items][0][product_id]=12.99&entity[items][0][product_type]=simple&entity[items][0][qty_ordered]=2&entity[items][0][row_total]=25.98&entity[items][0][row_total_incl_tax]=25.98&entity[items][0][sku]=CHECKED PINAFORE dress&entity[items][0][store_id]=1&entity[payment][method]=cashondelivery&entity[billing_address][address_type]=billing&entity[billing_address][city]=ahmedabad&entity[billing_address][country_id]=in&entity[billing_address][email]=test@gmail.com&entity[billing_address][firstname]=test&entity[billing_address][lastname]=test&entity[billing_address][postcode]=1222&entity[billing_address][region]=gujarat&entity[billing_address][region_code]=gj&entity[billing_address][region_id]=544&entity[billing_address][street][0]=ahm&entity[billing_address][street][1]=new&entity[billing_address][telephone]=1111&entity[extension_attributes][shipping_assignments][0][shipping][address][address_type]=shipping&entity[extension_attributes][shipping_assignments][0][shipping][address][city]=ahmedabad&entity[extension_attributes][shipping_assignments][0][shipping][address][country_id]=in&entity[extension_attributes][shipping_assignments][0][shipping][address][email]=test&entity[extension_attributes][shipping_assignments][0][shipping][address][firstname]=Test&entity[extension_attributes][shipping_assignments][0][shipping][address][lastname]=Test&entity[extension_attributes][shipping_assignments][0][shipping][address][postcode]=1234&entity[extension_attributes][shipping_assignments][0][shipping][address][region]=Gujarat&entity[extension_attributes][shipping_assignments][0][shipping][address][region_code]=GJ&entity[extension_attributes][shipping_assignments][0][shipping][address][region_id]=544&entity[extension_attributes][shipping_assignments][0][shipping][address][street][0]=AHm&entity[extension_attributes][shipping_assignments][0][shipping][address][street][1]=New&entity[extension_attributes][shipping_assignments][0][shipping][address][telephone]=111&entity[items][1][name]=½ palle Dkbrænde træpiller svenske 6mm 450kg 10kg poser
    //pass auth token
    @PUT()
    Call<ResponseBody> createorder(@Header("Authorization") String authHeader, @Url String url);

    @PUT("orders/create?")
    Call<ResponseBody> callcreateorder(@Header("Authorization") String authHeader, @Body String url);


    //store group
    //http://dkbraende.demoproject.info/rest/V1/store/storeGroups
    @GET("store/storeGroups")
    Call<ResponseBody> getstorename(@Header("Authorization") String authHeader);

    //http://dkbraende.demoproject.info/customapi/AppHomePage.php?store=1
    @GET()
    Call<Homedata> gethomedata(@Url String url);

    //store group
    //http://dkbraende.demoproject.info/rest/V1/cmsPage/:pageId
    @GET("cmsPage/13")
    Call<PrivacyModel> getprivacy();

    //store group
    //http://dkbraende.demoproject.info/rest/V1/cmsPage/:pageId
    @GET("cmsPage/3")
    Call<PrivacyModel> getaboutus();

    //store group
    //http://dkbraende.demoproject.info/rest/V1/cmsPage/:pageId
    @GET("cmsPage/4")
    Call<PrivacyModel> gettrems();


}
