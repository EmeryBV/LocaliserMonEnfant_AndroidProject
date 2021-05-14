package com.example.localisermonenfant_enfant.ServerAPI;

import android.content.Context;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONObject;

import java.io.File;

public class Connection {
    enum ConnectionType {Child, Parent};

    String sid = null;
    String user = null;
    String pass = null;
    ConnectionType connectionType = null;

    public Connection (String user, String pass, ConnectionType connectionType) {
        this.user = user;
        this.pass = pass;
        this.connectionType = connectionType;
    }

    private void Post (Context context, String url, JSONObject params, final VolleyCallback volleyCallback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.OnSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyCallback.OnError(error);
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public interface VolleyCallback {
        public void OnSuccess(JSONObject response);
        public void OnError(VolleyError error);
    }
}
