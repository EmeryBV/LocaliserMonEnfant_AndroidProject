
package com.example.localisermonenfant_enfant.ServerAPI;

import android.content.Context;
import android.os.Debug;
import android.provider.CallLog;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.*;
import com.android.volley.toolbox.*;
import com.example.localisermonenfant_enfant.activity.Contacts.CallLog.CallLogActivity;
import com.sendbird.android.shadow.com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Connection {
    public static String CommandURL = "https://www.lme.romimap.com/commands.php";

    public static class Contact {
        int id;
        String name;
        String num;

        public Contact() {
        }

        public Contact(int id, String name, String num) {
            this.id = id;
            this.name = name;
            this.num = num;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

    public static class Child {
        int id;
        String name;

        public Child() {
        }

        public Child(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
    }


    public static class SMS {
        int id;
        Child child;
        Contact contact;
        String text;
        String date;
        boolean sended;

        public SMS() {
        }

        public SMS(int id, Child child, Contact contact, String text, String date, boolean sended) {
            this.id = id;
            this.child = child;
            this.contact = contact;
            this.text = text;
            this.date = date;
            this.sended = sended;
        }

        public int getID() {return id;}
        public Child getChild() { return child; }
        public Contact getContact() {return contact;}
        public String getText() {return text;}
        public String getDate() {return date;}
        public boolean isSended() {return sended;}
    }

    public class CallData {
        int id;
        public int getId () {return id;}
        Contact contact;
        public Contact getContact () {return contact;}
        Child child;
        public Child getChild () {return child;}
        String date;
        public String getDate () {return date;}
        int type;
        public int getType () {return type;}
        String duration;
        public String getDuration () {return duration;}
    }

    public class Media {
        int id;
        public int getId () {return id;}
        Long date;
        public Long getDate () {return date;}
        String name;
        public String getName () {return name;}
        String link;
        public String getLink () {return link;}
    }

    public enum ConnectionType {Child, Parent};

    String sid = null;
    String user = null;
    String pass = null;

    ConnectionType connectionType = null;
    public ConnectionType GetConnectionType() { return connectionType; }

    public interface ConnectionCallback {
        public void Success();
        public void Error();
    }
    public Connection (String user, String pass, Context context, final ConnectionCallback connectionCallback) {
        try {
            this.user = user;
            this.pass = pass;

            JSONObject params = new JSONObject();
            params.put("type", "Connect");
            params.put("login", user);
            params.put("passwd", pass);


            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {

                    try {
                        sid = response.getString("sid");
                        if (response.getString("role").equals("child"))
                            connectionType = ConnectionType.Child;
                        else
                            connectionType = ConnectionType.Parent;

                        connectionCallback.Success();
                    } catch (JSONException e) {
                        connectionCallback.Error();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    connectionCallback.Error();
                }
            });
        } catch (Exception e) {
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
            params.put("type", "ChildList");

            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        ArrayList<Child> children = new ArrayList<Child>();
                        JSONArray jsonArray = response.getJSONArray("children");
                        Log.e("Children JSON Array", jsonArray.toString());
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
            params.put("type", "ContactList");
            params.put("ChildId", child.id);

            Post(context, CommandURL, params, new VolleyCallback() {
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
            params.put("type", "SMS");
            params.put("ChildId", child.id);
            params.put("ContactId", contact.id);

            Post(context, CommandURL, params, new VolleyCallback() {
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
                            s.id = jo.getInt("id");
                            s.sended = jo.getString("type_value").equals("sender");
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

    public interface GetCallsCallback {
        public void Success(ArrayList<CallData> callDataList);
        public void Error();
    }
    public void GetCallData (Context context, final Child child, final Contact contact, final GetCallsCallback getCallsCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "Calls");
            params.put("ChildId", child.id);
            params.put("ContactId", contact.id);

            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        ArrayList<CallData> calls = new ArrayList<CallData>();
                        JSONArray jsonArray = response.getJSONArray("Calls");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CallData cd = new CallData();
                            JSONObject jo = jsonArray.getJSONObject(i);
                            cd.id = jo.getInt("id");
                            cd.contact = contact;
                            cd.child = child;
                            cd.date = jo.getString("date_time");
                            cd.duration = jo.getString("duration");
                            String type = jo.getString("type_value");
                            switch (type) {
                                case "out":
                                    cd.type = CallLog.Calls.OUTGOING_TYPE;
                                    break;
                                case "in":
                                    cd.type = CallLog.Calls.INCOMING_TYPE;
                                    break;
                                case "miss":
                                    cd.type = CallLog.Calls.MISSED_TYPE;
                                    break;
                            }
                            calls.add(cd);
                        }

                        getCallsCallback.Success(calls);
                    } catch (Exception e) {
                        getCallsCallback.Error();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    getCallsCallback.Error();
                }
            });
        } catch (JSONException e) {
            getCallsCallback.Error();
        }
    }

    public interface SendSMSCallback {
        void Success();
        void Error();
    }
    public void SendSMS (Context context, ArrayList<SMS> SMSs, final SendSMSCallback sendSMSCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "AddSMS");
            JSONArray array = new JSONArray();
            for (SMS sms : SMSs) {
                JSONObject c = new JSONObject();
                c.put("contact_name", sms.contact.name);
                c.put("contact_num", sms.contact.num);
                c.put("date_time", sms.date);
                if (sms.sended)
                    c.put("type_value", "sender");
                else
                    c.put("type_value", "reciever");
                c.put("text_value", sms.text);
                array.put(c);
            }
            params.put("SMS", array);
            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    sendSMSCallback.Success();
                }

                @Override
                public void OnError(VolleyError error) {
                    sendSMSCallback.Error();
                }
            });
        } catch (JSONException e) {
            sendSMSCallback.Error();
        }
    }

    public interface GetGPSCallback {
        public void OnSuccess(double lat, double lon);
        public void OnError();
    }
    public void GetGPS (Context context, Child child, final GetGPSCallback getGPSCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "GetGPS");
            params.put("ChildId", child.id);
            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        if (response.getBoolean("return")) {
                            getGPSCallback.OnSuccess(response.getDouble("lat"), response.getDouble("lon"));
                        } else {
                            getGPSCallback.OnError();
                        }
                    } catch (JSONException e) {
                        getGPSCallback.OnError();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    getGPSCallback.OnError();
                }
            });
        } catch (JSONException e) {
            getGPSCallback.OnError();
        }
    }

    public interface SetGPSCallback {
        public void OnSuccess();
        public void OnError();
    }
    public void SetGPS (Context context, double lon, double lat, final SetGPSCallback setGPSCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "SetGPS");
            params.put("lat", lat);
            params.put("lon", lon);
            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                   setGPSCallback.OnSuccess();
                }

                @Override
                public void OnError(VolleyError error) {
                    setGPSCallback.OnError();
                }
            });
        } catch (JSONException e) {
            setGPSCallback.OnError();
        }
    }

    public interface GetSMSLastDateCallback {
        public void OnSuccess(Long date);
        public void OnError();
    }
    public void GetSMSLastDateCallback (Context context, final GetSMSLastDateCallback getSMSLastDateCallback){
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "SMSLastDate");
            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        getSMSLastDateCallback.OnSuccess(response.getLong("date"));
                    } catch (JSONException e) {
                        getSMSLastDateCallback.OnError();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    getSMSLastDateCallback.OnError();
                }
            });
        } catch (JSONException e) {
            getSMSLastDateCallback.OnError();
        }
    }

    public class Parent {
        int id;
        public int getId () {return id;}
        String name;
        public String getName () {return name;}
        String email;
        public String getEmail () {return email;}
    }
    public interface GetParentCallback {
        public void OnSuccess (Parent parent);
        public void OnError ();
    }
    public void GetParent (Context context, final GetParentCallback getParentCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "GetParent");
            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        if (response.getBoolean("parent")) {
                            Parent p = new Parent();
                            p.id = response.getInt("id");
                            p.email = response.getString("email");
                            p.name = response.getString("name");
                            getParentCallback.OnSuccess(p);
                        } else {
                            getParentCallback.OnSuccess(null);
                        }
                    } catch (JSONException e) {
                        getParentCallback.OnError();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    getParentCallback.OnError();
                }
            });
        } catch (JSONException e) {
            getParentCallback.OnError();
        }
    }

    public interface SetParentCallback {
        public void OnSuccess ();
        public void OnError ();
    }
    public void SetParent (Context context, String user, String pass, final SetParentCallback setParentCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "SetParent");
            params.put("user", user);
            params.put("pass", pass);
            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    setParentCallback.OnSuccess();
                }

                @Override
                public void OnError(VolleyError error) {
                    setParentCallback.OnError();
                }
            });
        } catch (JSONException e) {
            setParentCallback.OnError();
        }
    }

    public interface SendImagesCallback {
        public void OnSuccess();
        public void OnError();
    }
    public void SendImages (Context context, ArrayList<String> fileList, final SendImagesCallback sendImagesCallback) {
        try {
            JSONArray dates = new JSONArray();
            for (String path: fileList) {
                File file = new File(path);
                dates.put(file.lastModified());
            }

            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "SendImages");
            params.put("dates", dates);
            MultiPartPost(context, CommandURL, fileList, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    sendImagesCallback.OnSuccess();
                }

                @Override
                public void OnError(VolleyError error) {
                    sendImagesCallback.OnError();
                }
            });
        } catch (JSONException e) {
            sendImagesCallback.OnError();
        }
    }

    public interface GetImagesCallback {
        public void OnSuccess (ArrayList<Media> imageList);
        public void OnError ();
    }
    public void GetImages (Context context, Child child, final GetImagesCallback getImagesCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "GetImages");
            params.put("IdChild", child.id);
            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        ArrayList<Media> imageList = new ArrayList<Media>();

                        JSONArray imageArray = response.getJSONArray("images");
                        for (int i = 0; i < imageArray.length(); i++) {
                            Media image = new Media();
                            JSONObject jo = (JSONObject)imageArray.get(i);
                            image.id = jo.getInt("id");
                            image.date = jo.getLong("date_time");
                            image.name = jo.getString("name");
                            image.link = "https://www.lme.romimap.com/" + jo.getString("link");
                            imageList.add(image);
                        }

                        getImagesCallback.OnSuccess(imageList);
                    } catch (JSONException e) {
                        getImagesCallback.OnError();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    getImagesCallback.OnError();
                }
            });
        } catch (JSONException e) {
            getImagesCallback.OnError();
        }
    }

    public interface SendVideosCallback {
        public void OnSuccess();
        public void OnError();
    }
    public void SendVideos (Context context, ArrayList<String> fileList, final SendVideosCallback sendVideosCallback) {
        try {
            JSONArray dates = new JSONArray();
            for (String path: fileList) {
                File file = new File(path);
                dates.put(file.lastModified());
            }

            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "SendVideos");
            params.put("dates", dates);
            MultiPartPost(context, CommandURL, fileList, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    sendVideosCallback.OnSuccess();
                }

                @Override
                public void OnError(VolleyError error) {
                    sendVideosCallback.OnError();
                }
            });
        } catch (JSONException e) {
            sendVideosCallback.OnError();
        }
    }

    public interface GetVideosCallback {
        public void OnSuccess (ArrayList<Media> videoList);
        public void OnError ();
    }
    public void GetVideos (Context context, Child child, final GetVideosCallback getVideosCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("sid", sid);
            params.put("type", "GetVideos");
            params.put("IdChild", child.id);
            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        ArrayList<Media> videoList = new ArrayList<Media>();

                        JSONArray videoArray = response.getJSONArray("videos");
                        for (int i = 0; i < videoArray.length(); i++) {
                            Media image = new Media();
                            JSONObject jo = (JSONObject)videoArray.get(i);
                            image.id = jo.getInt("id");
                            image.date = jo.getLong("date_time");
                            image.name = jo.getString("name");
                            image.link = "https://www.lme.romimap.com/" + jo.getString("link");
                            videoList.add(image);
                        }

                        getVideosCallback.OnSuccess(videoList);
                    } catch (JSONException e) {
                        getVideosCallback.OnError();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    getVideosCallback.OnError();
                }
            });
        } catch (JSONException e) {
            getVideosCallback.OnError();
        }
    }

    public interface SignUpCallback {
        public void OnSuccess();
        public void OnError();
    }
    public static void SignUp (Context context, String email, String username, String pass, boolean isChild, final SignUpCallback signUpCallback) {
        try {
            JSONObject params = new JSONObject();
            params.put("type", "SignUp");
            params.put("email", email);
            params.put("username", username);
            params.put("pass", pass);
            params.put("isChild", isChild);

            Post(context, CommandURL, params, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        if (response.getBoolean("return")) {
                            signUpCallback.OnSuccess();
                        } else {
                            signUpCallback.OnError();
                        }
                    } catch (JSONException e) {
                        signUpCallback.OnError();
                    }
                }

                @Override
                public void OnError(VolleyError error) {
                    signUpCallback.OnError();
                }
            });
        } catch (JSONException e) {
            signUpCallback.OnError();
        }
    }

    interface VolleyCallback {
        public void OnSuccess(JSONObject response);
        public void OnError(VolleyError error);
    }
    private static void Post (Context context, String url, final JSONObject params, final VolleyCallback volleyCallback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.e("POST : " + url, params.toString());

        StringRequest sr = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("POST : Success !", response);
                    try {
                        volleyCallback.OnSuccess(new JSONObject(response));
                    } catch (JSONException e) {
                        volleyCallback.OnSuccess(null);
                        Log.e("POST : ", "Error parsing response");
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("POST : Error...", error.getMessage());
                    volleyCallback.OnError(error);
                }
            })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> p = new HashMap<String, String>();
                p.put("json",params.toString());
                return p;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    private static void MultiPartPost (Context context, String url, ArrayList<String> fileList, JSONObject params, final VolleyCallback volleyCallback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.e("MP-POST : " + url, params.toString() + " " + fileList.size() + " files");

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, CommandURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("MP-POST : Success !", response);
                try {
                    volleyCallback.OnSuccess(new JSONObject(response));
                } catch (JSONException e) {
                    volleyCallback.OnSuccess(null);
                    Log.e("MP-POST : ", "Error parsing response");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MP-POST : Error...", error.getMessage());
                volleyCallback.OnError(error);
            }
        });
        int i = 0;
        for (String path: fileList) {
            request.addFile(i + "", path);
            i++;
        }

        request.addStringParam("json", params.toString());

        queue.add(request);
    }
}
