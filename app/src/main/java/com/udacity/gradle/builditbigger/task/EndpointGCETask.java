package com.udacity.gradle.builditbigger.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

import andersonsv.com.br.showjokelib.ShowJokeActivity;

public class EndpointGCETask extends AsyncTask<Context, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
   // private final OnTaskCompleted taskCompleted;

   // public EndpointGCETask(OnTaskCompleted taskCompleted) {
   //     this.taskCompleted = taskCompleted;
   // }

    @Override
    protected String doInBackground(Context...params) {
        if(myApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // - 10.0.2.2 ip local
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0];

        try {
            return myApiService.jokeService().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        final Intent intent = new Intent(context, ShowJokeActivity.class);
        intent.putExtra("gce_result",result);
        //if(taskCompleted != null) {
        //    taskCompleted.onTaskCompleted(result);
        //}
        context.startActivity(intent);
    }
}