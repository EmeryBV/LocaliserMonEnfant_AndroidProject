package com.example.localisermonenfant_enfant.ServerAPI;

import android.content.Context;
import android.provider.ContactsContract;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.example.localisermonenfant_enfant.activity.Contacts.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

public class Connection {

    public static String IndexUrl = "https://www.site.com/index.php";

    public class Contact {
        int id;
        String name;
        String num;
    }

    public class Child {
        int id;
        String name;
    }

    public class SMS {
        Child child;
        Contact contact;
        String text;
        String date;
        boolean sended;
    }

    public enum ConnectionType {Child, Parent};

    String sid = null;
    String user = null;
    String pass = null;
    ConnectionType connectionType = null;

    public interface ConnectionCallback {
        public void Success();
        public void Error();
    }
    public Connection (String user, String pass, ConnectionType connectionType, Context context, final ConnectionCallback connectionCallback) {
        try {
            this.user = user;
            this.pass = pass;
            this.connectionType = connectionType;

            JSONObject params = new JSONObject();
            params.put("login", user);
            params.put("passwd", pass);
            if (connectionType == ConnectionType.Child)
                params.put("role", "child");
            else
                params.put("role", "parent");

            Post(context, IndexUrl, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        sid = response.getString("sid");
                    } catch (JSONException e) {
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                }
            });

            if (sid == null) connectionCallback.Error();
            else connectionCallback.Success();
        } catch (JSONException e) {
            connectionCallback.Error();
        }
    }

    public interface GetChildrenCallback {
        void Success (ArrayList<Child> children);
        void Error ();
    }
    public void getChildren (Context context, final GetChildrenCallback childrenCallback) {
        try {

            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "ChildrenList");

            Post(context, IndexUrl, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        ArrayList<Child> children = new ArrayList<Child>();
                        JSONArray jsonArray = response.getJSONArray("children");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Child c = new Child();
                            JSONObject jo = jsonArray.getJSONObject(i);
                            c.id = jo.getInt("id");
                            c.name = jo.getString("pseudo");
                            children.add(c);
                        }

                        childrenCallback.Success(children);
                    } catch (Exception e) {
                        childrenCallback.Error();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    childrenCallback.Error();
                }
            });
        } catch (Exception e) {
            childrenCallback.Error();
        }
    }

    public interface GetContactsCallback {
        void Success (ArrayList<Contact> contacts);
        void Error ();
    }
    public void GetContacts (Context context, Child child, final GetContactsCallback getContactsCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "ChildrenList");
            params.put("ChildId", child.id);

            Post(context, IndexUrl, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        ArrayList<Contact> contacts = new ArrayList<Contact>();
                        JSONArray jsonArray = response.getJSONArray("contacts");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Contact c = new Contact();
                            JSONObject jo = jsonArray.getJSONObject(i);
                            c.id = jo.getInt("id");
                            c.name = jo.getString("nom");
                            c.num = jo.getString("numero");
                            contacts.add(c);
                        }
                        getContactsCallback.Success(contacts);
                    } catch (Exception e) {
                        getContactsCallback.Error();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    getContactsCallback.Error();
                }
            });
        } catch (Exception e) {
            getContactsCallback.Error();
        }
    }

    public interface GetSMSCallback {
        void Success (ArrayList<SMS> smsList);
        void Error ();
    }
    public void GetSMS (Context context, final Child child, final Contact contact, final GetSMSCallback getSMSCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "SMS ");
            params.put("ChildId", child.id);

            Post(context, IndexUrl, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        ArrayList<SMS> sms = new ArrayList<SMS>();
                        JSONArray jsonArray = response.getJSONArray("SMS");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            SMS s = new SMS();
                            JSONObject jo = jsonArray.getJSONObject(i);
                            s.contact = contact;
                            s.child = child;
                            s.date = jo.getString("date_time");
                            s.text = jo.getString("text_value");
                            s.sended = jo.getString("type").equals("sender");
                            sms.add(s);
                        }

                        getSMSCallback.Success(sms);
                    } catch (Exception e) {
                        getSMSCallback.Error();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    getSMSCallback.Error();
                }
            });
        } catch (Exception e) {
            getSMSCallback.Error();
        }
    }

    public interface SendContactsCallback {
        void Success();
        void Error();
    }
    public void SendContacts (Context context, ArrayList<Contact> contacts, final SendContactsCallback sendContactsCallback) {
        try {
            JSONObject params = new JSONObject();
            JSONArray array = new JSONArray();
            for (Contact contact : contacts) {
                JSONObject c = new JSONObject();
                c.put("name", contact.name);
                c.put("num", contact.num);
                array.put(c);
            }
            params.put("Contacts", array);
            Post(context, IndexUrl, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    sendContactsCallback.Success();
                }

                @Override
                public void OnError(VolleyError error) {
                    sendContactsCallback.Error();
                }
            });
        } catch (JSONException e) {
            sendContactsCallback.Error();
        }
    }

    interface VolleyCallback {
        public void OnSuccess(JSONObject response);
        public void OnError(VolleyError error);
    }
    private void Post (Context context, String url, JSONObject params, final VolleyCallback volleyCallback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
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
}
