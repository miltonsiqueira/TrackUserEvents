package com.titomilton.trackuserevents.rest;

import android.os.AsyncTask;

import com.titomilton.trackuserevents.persistence.EventJsonDao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendEventTask extends AsyncTask<Void, Void, Object> {
    private final boolean isCache;
    private final EventJsonDao eventJsonDao;
    private final String json;
    private final String apiKey;
    private final CallbackResponse callbackResponse;
    private static final String ENCODE_UTF_8 = "UTF-8";

    public SendEventTask(final boolean isCache, final EventJsonDao eventJsonDao, final String apiKey, final String json, final CallbackResponse callbackResponse) {
        this.isCache = isCache;
        this.eventJsonDao = eventJsonDao;
        this.apiKey = apiKey;
        this.json = json;
        this.callbackResponse = callbackResponse;
    }

    @Override
    protected Object doInBackground(Void... params) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(EventsAPI.EVENTS_ENDPOINT);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");

            connection.setRequestProperty(EventsAPI.KEY_HEADER, apiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            writeOutputStream(connection.getOutputStream());

            return readInputStream(connection);

        } catch (InvalidCodeResponseException | ReadingResponseException e) {
            return e;
        } catch (IOException e) {
            if (isCache) {
                eventJsonDao.addEventJson(apiKey, json);
            }
            return e;
        } catch (Exception e) {
            return e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    @Override
    protected void onPostExecute(Object o) {
        if (o instanceof String) {
            callbackResponse.onResponse((String) o, json);
        } else if (o instanceof InvalidCodeResponseException) {
            InvalidCodeResponseException invalidCodeResponseException = (InvalidCodeResponseException) o;
            callbackResponse.onInvalidCodeResponse(invalidCodeResponseException.getResponseCode(),
                    invalidCodeResponseException.getResponseBody(), json);
        } else if (o instanceof ReadingResponseException) {
            ReadingResponseException readingResponseException = (ReadingResponseException) o;
            callbackResponse.onFailureReadingResponse(readingResponseException.getResponseCode(), json, readingResponseException.getErrorSource());

        } else if (o instanceof Exception) {
            callbackResponse.onFailure((Exception) o);
        }
    }

    private String readInputStream(HttpURLConnection connection) throws InvalidCodeResponseException, ReadingResponseException {
        int responseCode = -1;
        BufferedReader bufferedReader = null;
        try {

            responseCode = connection.getResponseCode();
            InputStream is = null;
            StringBuilder sb = new StringBuilder();
            try {
                if (responseCode >= 200 && responseCode < 400) {
                    is = connection.getInputStream();
                } else {
                    is = connection.getErrorStream();
                }
                bufferedReader = new BufferedReader(new InputStreamReader(is, ENCODE_UTF_8));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }

            if (responseCode == HttpURLConnection.HTTP_CREATED) {

                return sb.toString();

            } else {
                throw new InvalidCodeResponseException(responseCode, sb.toString());

            }

        } catch (IOException e) {
            throw new ReadingResponseException(responseCode, e);
        }

    }

    private void writeOutputStream(OutputStream outputStream) throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, ENCODE_UTF_8);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(this.json);
            bufferedWriter.flush();
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }
}
