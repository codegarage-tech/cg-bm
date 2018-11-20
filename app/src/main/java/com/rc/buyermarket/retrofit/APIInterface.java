package com.rc.buyermarket.retrofit;



import com.rc.buyermarket.model.AddProperty;
import com.rc.buyermarket.model.BuyerLogin;
import com.rc.buyermarket.model.Country;
import com.rc.buyermarket.model.Exterior;
import com.rc.buyermarket.model.ParamsAddProperty;
import com.rc.buyermarket.model.ParamsBuyerLogin;
import com.rc.buyermarket.model.ParamsBuyerSignUp;
import com.rc.buyermarket.model.ParamsSellerLogin;
import com.rc.buyermarket.model.ParamsSellerSearchBuyer;
import com.rc.buyermarket.model.ParamsSellerSignUp;
import com.rc.buyermarket.model.ParamsUpdateSellerProfile;
import com.rc.buyermarket.model.PropertyDelete;
import com.rc.buyermarket.model.PropertyType;
import com.rc.buyermarket.model.SellerLogin;
import com.rc.buyermarket.model.SellerSearchBuyer;
import com.rc.buyermarket.model.Styles;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public interface APIInterface {

    @POST("buyer_login/signin")
    Call<APIResponse<List<BuyerLogin>>> doBuyerLogin(@Body ParamsBuyerLogin paramsBuyerLogin);

    @POST("buyer_login/signup")
    Call<APIResponse<List<BuyerLogin>>> doBuyerSignUp(@Body ParamsBuyerSignUp paramsBuyerSignUp);

    @POST("seller/signin")
    Call<APIResponse<List<BuyerLogin>>> doSellerLogin(@Body ParamsSellerLogin paramsSellerLogin);

    @POST("seller/signup")
    Call<APIResponse<List<SellerLogin>>> doSellerSignUp(@Body ParamsSellerSignUp paramsSellerSignUp);

    @POST("seller/signup")
    Call<APIResponse<List<SellerLogin>>> doSellerProfileUpdate(@Body ParamsUpdateSellerProfile paramsUpdateSellerProfile);

    @POST("properties/add")
    Call<APIResponse<List<AddProperty>>> doAddPropety(@Body ParamsAddProperty paramsAddProperty);

    @POST("properties/search")
    Call<APIResponse<List<SellerSearchBuyer>>> doSellerSearchProperty(@Body ParamsSellerSearchBuyer paramsSellerSearchBuyer);

    @GET("lists/details/exterior")
    Call<APIResponse<List<Exterior>>> doGetListExterior();

    @GET("lists/details/styles")
    Call<APIResponse<List<Styles>>> doGetListStyles();

    @GET("lists/details/property_type")
    Call<APIResponse<List<PropertyType>>> doGetListPropertyType();

    @GET("lists/states")
    Call<APIResponse<List<Country>>> doGetListCountryWithState();

    @GET("properties/lists/{id}")
    Call<APIResponse<List<AddProperty>>> doGetListProperty(@Path("id") String buyer_id);


    @GET("properties/deleteProperty/{id}")
    Call<APIResponse<PropertyDelete>> doGetDeleteListProperty(@Path("id") String property_id);

}
