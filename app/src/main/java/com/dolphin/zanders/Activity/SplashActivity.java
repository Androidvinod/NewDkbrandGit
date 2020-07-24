package com.dolphin.zanders.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ImageView imageView;
    ApiInterface customeapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        customeapi = ApiClientcusome.getClient().create(ApiInterface.class);
      //  Login_preference.settoken(SplashActivity.this,"2g5jruh20e5ejlqto6ybly5x2a5551lu");
        Log.e("token24 ",""+Login_preference.gettoken(SplashActivity.this));
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        imageView=findViewById(R.id.imageView);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        imageView.startAnimation(animation1);
        if (CheckNetwork.isNetworkAvailable(SplashActivity.this)) {
            gettokenapi();

        } else {
            //    noInternetDialog(NavigationActivity.this);
            Toast.makeText(SplashActivity.this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }

    }

    private void gettokenapi() {

        Call<String> homevideos = customeapi.gettoken("http://dkbraende.demoproject.info/rest/V1/integration/admin/token/?username=admin&password=9yWpe6v7(OZ7");
        homevideos.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("response200",""+response.toString());
                Log.e("response201",""+response.body());

                if(response.code()==200)
                {
                    Login_preference.settoken(SplashActivity.this,response.body());
                    Log.e("token_63",""+Login_preference.gettoken(SplashActivity.this));
                    callCurrencycodeApi();
                }else {
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void callCurrencycodeApi() {
        Call<ResponseBody> homevideos = customeapi.getcurrenycode("Bearer " + Login_preference.gettoken(SplashActivity.this));
        homevideos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response200",""+response.toString());
                Log.e("response201",""+response.body());

                if(response.code()==200)
                {

                    try {
                        JSONArray jsonArray=new JSONArray(response.body().string());
                        Log.e("jsonArray_101","168"+jsonArray);

                        String currencyCode=jsonArray.getJSONObject(0).getString("base_currency_code");
                        Log.e("currencyCode111",""+currencyCode);
                        Login_preference.setcurrencycode(SplashActivity.this,currencyCode);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("debug_168","168");
                                Intent i =new Intent(SplashActivity.this,NavigationActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                finish();
                            }
                        },1500);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }







                }else {

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
