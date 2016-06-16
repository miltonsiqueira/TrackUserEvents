package com.titomilton.trackuserevents.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.titomilton.trackuserevents.InvalidEventRequestException;
import com.titomilton.trackuserevents.NetworkConnectionNotFoundException;
import com.titomilton.trackuserevents.TrackUserEvents;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;
        Button buttonSend = (Button) findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextApiKey = (EditText) findViewById(R.id.editTextAPIKey);
                EditText editTextEventName= (EditText) findViewById(R.id.editTextEventName);
                TrackUserEvents trackUserEvents = new TrackUserEvents(editTextApiKey.getText().toString(), context);
                final TextView textViewResult = (TextView) findViewById(R.id.textResult);
                textViewResult.setText("Sending");

                //Key/Value data
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("Param1", 1);
                data.put("Param2", "second");
                data.put("Param3", 1.987);
                data.put("Param4", true);

                String eventName = editTextEventName.getText().toString();
                try {
                    trackUserEvents.sendEvent(eventName, data, new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if(response.isSuccessful()){
                                    textViewResult.setText("onResponse: "+ response.body().string());
                                }else{
                                    textViewResult.setText("onResponse: not success "+ response.errorBody().string());
                                }

                            } catch (IOException e) {
                                textViewResult.setText("onResponse IOException: " + e.getMessage());
                            } catch (Exception e){
                                textViewResult.setText("onResponse Exception: " + e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            textViewResult.setText("onFailure" + t.getMessage());
                        }
                    });
                } catch (InvalidEventRequestException e) {
                    textViewResult.setText(e.getMessage());
                } catch (NetworkConnectionNotFoundException e) {
                    textViewResult.setText(e.getMessage());
                } catch (Exception e){
                    textViewResult.setText(e.getMessage());
                }

            }
        });
    }

}
