package com.titomilton.trackuserevents.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.titomilton.trackuserevents.CallbackResponse;
import com.titomilton.trackuserevents.InvalidEventRequestException;
import com.titomilton.trackuserevents.NetworkConnectionNotFoundException;
import com.titomilton.trackuserevents.TrackUserEvents;
import com.titomilton.trackuserevents.persistence.EventJson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    final StringBuilder sbLog = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editTextApiKey = (EditText) findViewById(R.id.editTextAPIKey);
        final TextView textViewResult = (TextView) findViewById(R.id.textLog);

        final CallbackResponse callbackResponse = new CallbackResponse() {
            @Override
            public void onResponse(int responseCode, String responseBody, String requestBody) {
                String text = "onResponse() code:" + responseCode +
                        System.getProperty("line.separator") +
                        "Response body:" + responseBody +
                        System.getProperty("line.separator") +
                        "Request body:" + requestBody;
                addToLog(text, textViewResult);
            }

            @Override
            public void onFailedResponse(int code, String responseBody, String requestBody) {
                String text = "onFailedResponse() code:" + code +
                        System.getProperty("line.separator") +
                        "Response body:" + responseBody +
                        System.getProperty("line.separator") +
                        "Request body:" + requestBody;
                addToLog(text, textViewResult);
            }

            @Override
            public void onFailureReadingResponse(int code, String requestBody, Throwable t) {
                String text = "onFailureReadingResponse() code:" + code +
                        System.getProperty("line.separator") +
                        "Request body:" + requestBody +
                        "Exception: " + t.getMessage();
                addToLog(text, textViewResult);
            }

            @Override
            public void onFailure(Throwable t) {
                String text = "onFailure" + t.getMessage();
                addToLog(text, textViewResult);
            }
        };

        createOnClickListenerButtonSend(editTextApiKey, textViewResult, callbackResponse);

        createOnClickListenerButtonSendCachedEvents(editTextApiKey, textViewResult, callbackResponse);

        createOnClickListenerButtonCachedEvents(textViewResult);

        createOnClickListenerButtonClear(textViewResult);

        createOnClickListenerButtonSetAlarm(textViewResult);

        createOnClickListenerButtonCancelAlarm(textViewResult);


    }

    private void createOnClickListenerButtonSetAlarm(final TextView textViewResult) {
        final Button buttonSetAlarm = (Button) findViewById(R.id.button_set_alarm);
        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TrackUserEvents trackUserEvents = new TrackUserEvents(MainActivity.this);
                trackUserEvents.setAlarm();
                addToLog("Set Alarm ", textViewResult);
            }
        });
    }

    private void createOnClickListenerButtonCancelAlarm(final TextView textViewResult) {
        final Button buttonCancelAlarm = (Button) findViewById(R.id.button_cancel_alarm);
        buttonCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TrackUserEvents trackUserEvents = new TrackUserEvents(MainActivity.this);
                trackUserEvents.cancelAlarm();
                addToLog("Cancel Alarm ", textViewResult);
            }
        });
    }

    private void createOnClickListenerButtonClear(final TextView textViewResult) {
        final Button buttonSendCachedEvents = (Button) findViewById(R.id.button_clear);
        buttonSendCachedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbLog.setLength(0);
                textViewResult.setText("");
            }
        });
    }

    private void createOnClickListenerButtonSendCachedEvents(final EditText editTextApiKey, final TextView textViewResult, final CallbackResponse callbackResponse) {
        final Button buttonSendCachedEvents = (Button) findViewById(R.id.button_send_cached_events);
        buttonSendCachedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToLog("Send tracked events - ApiKey: " + editTextApiKey.getText().toString(), textViewResult);

                final TrackUserEvents trackUserEvents = new TrackUserEvents(MainActivity.this);
                trackUserEvents.sendCachedEvents();

            }
        });
    }

    private void createOnClickListenerButtonSend(final EditText editTextApiKey, final TextView textViewResult, final CallbackResponse callbackResponse) {
        final Button buttonSend = (Button) findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editTextEventName = (EditText) findViewById(R.id.editTextEventName);
                addToLog("Send - ApiKey: " + editTextApiKey.getText().toString() +
                        " EventName: " + editTextEventName.getText().toString(), textViewResult);
                try {

                    final TrackUserEvents trackUserEvents = new TrackUserEvents(editTextApiKey.getText().toString(), MainActivity.this);

                    Map<String, Object> otherParameters = new HashMap<>();
                    otherParameters.put("other1", "Test2");
                    otherParameters.put("other2", 12321);
                    otherParameters.put("other3", false);

                    String eventName = editTextEventName.getText().toString();

                    trackUserEvents.createEventByName(eventName)
                            .addParameter("Param1", 1)
                            .addParameter("Param2", "test")
                            .addParameter("Param3", 18.39)
                            .addParameter("Param4", true)
                            .addParameters(otherParameters)
                            .send(callbackResponse);

                } catch (InvalidEventRequestException | NetworkConnectionNotFoundException e) {
                    addToLog(e.getMessage(), textViewResult);
                }

            }
        });
    }


    private void createOnClickListenerButtonCachedEvents(final TextView textViewResult) {
        Button buttonCachedEvents = (Button) findViewById(R.id.button_cached_events);
        buttonCachedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackUserEvents trackUserEvents = new TrackUserEvents(MainActivity.this);
                addToLog("Cached events", textViewResult);
                List<EventJson> listEventJson = trackUserEvents.getCachedEvents();
                StringBuilder sb = new StringBuilder();

                for (EventJson eventJson : listEventJson) {
                    sb.append(eventJson);
                    sb.append(System.getProperty("line.separator"));

                }
                addToLog("List cached events " +
                        System.getProperty("line.separator") +
                        sb.toString(), textViewResult);
            }
        });
    }

    private synchronized void addToLog(String msg, TextView textViewResult) {
        msg = System.getProperty("line.separator") +
                new SimpleDateFormat("HH:mm:SS").format(new Date())
                + " " + msg;
        sbLog.insert(0, msg);

        textViewResult.setText(sbLog.toString());
    }


}
