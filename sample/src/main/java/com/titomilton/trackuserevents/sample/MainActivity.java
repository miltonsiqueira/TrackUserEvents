package com.titomilton.trackuserevents.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.titomilton.trackuserevents.CallbackResponse;
import com.titomilton.trackuserevents.InvalidEventRequestException;
import com.titomilton.trackuserevents.NetworkConnectionNotFoundException;
import com.titomilton.trackuserevents.TrackUserEvents;

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
                EditText editTextEventName = (EditText) findViewById(R.id.editTextEventName);
                TrackUserEvents trackUserEvents = new TrackUserEvents(editTextApiKey.getText().toString(), context);
                final TextView textViewResult = (TextView) findViewById(R.id.textResult);
                textViewResult.setText("Sending");

                String eventName = editTextEventName.getText().toString();
                try {
                    trackUserEvents.newEvent(eventName)
                            .addParameter("Param1", 1)
                            .addParameter("Param2", "test")
                            .addParameter("Param3", 18.39)
                            .addParameter("Param4", true)
                            .send(new CallbackResponse() {
                                @Override
                                public void onResponse(int responseCode, String responseBody, String requestBody, String exceptionResponseBody ) {
                                    String text = "code:" + responseCode +
                                            System.getProperty ("line.separator") +
                                            "Response body:" + responseBody +
                                            System.getProperty ("line.separator") +
                                            "Request body:" + requestBody +
                                            System.getProperty ("line.separator") +
                                            "Exception response body:" + exceptionResponseBody;
                                    textViewResult.setText(text);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    String text = "onFailure" + t.getMessage();
                                    textViewResult.setText(text);
                                }
                            });

                } catch (InvalidEventRequestException e) {
                    textViewResult.setText(e.getMessage());
                } catch (NetworkConnectionNotFoundException e) {
                    textViewResult.setText(e.getMessage());
                } catch (Exception e) {
                    textViewResult.setText(e.getMessage());
                }

            }
        });
    }

}
