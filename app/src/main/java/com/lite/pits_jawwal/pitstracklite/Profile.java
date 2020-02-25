package com.lite.pits_jawwal.pitstracklite;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {
    View view;
    Button edit, ok;
    EditText name, password, phone, email, time, mobile, address;
    private Switch language, sound_noti;
    CircleImageView c1, c2, c3, c4, c5, c6, c7, c8, c9;
    ArrayAdapter<String> dataAdapter;
    ArrayList<String> info = new ArrayList<>();
    private Spinner spinner;
    public int edit_cancel = 0;
    ImageView profile_img;
    private String pass, name_;
    private LinearLayout page_profile;
    public Profile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        name =  view.findViewById(R.id.ed_msg);
        name.setOnFocusChangeListener(this);
        password =  view.findViewById(R.id.ed_password);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setOnFocusChangeListener(this);
        password.setOnClickListener(this);
        password.setOnTouchListener(this);
        phone =  view.findViewById(R.id.ed_phone);
        phone.setOnFocusChangeListener(this);
        email =  view.findViewById(R.id.ed_email);
        email.setOnFocusChangeListener(this);
        language =  view.findViewById(R.id.language);
        language.setOnTouchListener(this);
        sound_noti =  view.findViewById(R.id.sound_noti);
        sound_noti.setOnTouchListener(this);
        mobile =  view.findViewById(R.id.ed_mobile);
        mobile.setOnFocusChangeListener(this);
        address =  view.findViewById(R.id.ed_address);
        address.setOnFocusChangeListener(this);
        ok =  view.findViewById(R.id.but_ok);
        ok.setOnClickListener(this);
        ok.setVisibility(View.GONE);
        page_profile =  view.findViewById(R.id.LinearLayoutstart);
        page_profile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard();
                return false;
            }
        });
        profile_img =  view.findViewById(R.id.profile_image);
        profile_img.setOnClickListener(this);
        edit =  view.findViewById(R.id.but_cancel_edit);
        c1 =  view.findViewById(R.id.c1);
        c2 =  view.findViewById(R.id.c2);
        c3 =  view.findViewById(R.id.c3);
        c4 =  view.findViewById(R.id.c4);
        c5 =  view.findViewById(R.id.c5);
        c6 =  view.findViewById(R.id.c6);
        c7 =  view.findViewById(R.id.c7);
        c8 =  view.findViewById(R.id.c8);
        c9 =  view.findViewById(R.id.c9);

        edit.setOnClickListener(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        name_ = prefs.getString(Preferences.USER_NAME, "");
        pass = prefs.getString(Preferences.PASSWORD, "");
        spinner =  view.findViewById(R.id.spinner);
        dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_gmt);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.add("(GMT)");
        dataAdapter.add("(GMT +1:00)");
        dataAdapter.add("(GMT +2:00)");
        dataAdapter.add("(GMT +3:00)");
        dataAdapter.add("(GMT +4:00)");
        dataAdapter.add("(GMT +5:00)");
        dataAdapter.add("(GMT +6:00)");
        dataAdapter.add("(GMT +7:00)");
        dataAdapter.add("(GMT +8:00)");
        spinner.setAdapter(dataAdapter);
        spinner.setOnTouchListener(this);
        spinner.setEnabled(false);
        if (Utils.isOnline(getContext())) {
            user_information user = new user_information(name_, pass, 0);
            user.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            ALL.show_custom_msg(getContext(), getActivity(), getResources().getString(R.string.str12));
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.but_cancel_edit) {
            if (edit_cancel == 0) {
                dialog();
            } else {
                edit_cancel = 0;
                edit.setText(getString(R.string.edit));
                name.setEnabled(false);
                password.setEnabled(false);
                phone.setEnabled(false);
                email.setEnabled(false);
                language.setEnabled(false);
                mobile.setEnabled(false);
                address.setEnabled(false);
                spinner.setEnabled(false);
                ok.setVisibility(View.GONE);
                ok.setSoundEffectsEnabled(true);
                change();
                if (!info.equals("img")) {
                    information(info);
                }
            }
        }
        if (view.getId() == R.id.but_ok) {
            ArrayList<String> info = new ArrayList<>();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String name2 = prefs.getString(Preferences.USER_NAME, "");
            String pass = prefs.getString(Preferences.PASSWORD, "");
            info.add(name.getText().toString());
            info.add(password.getText().toString());
            info.add(phone.getText().toString());
            info.add(mobile.getText().toString());
            info.add(email.getText().toString());
            info.add(address.getText().toString());
            String lan;
            String noti_s = "1";
            if (sound_noti.isChecked()) {
                noti_s = "0";
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Preferences.SOUND, noti_s);
            editor.apply();

            if (language.isChecked()) {
                lan = "0";
            } else {
                lan = "1";
            }
            info.add(lan);
            String time_gmt = GMT_Value(String.valueOf(spinner.getSelectedItem().toString()));
            info.add(time_gmt);
            if (Utils.isOnline(getContext())) {
                update_information user = new update_information(name2, pass, info);
                user.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                ALL.show_custom_msg(getContext(), getActivity(), getResources().getString(R.string.str12));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void dialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getResources().getString(R.string.str_pass1));
        alert.setMessage(getResources().getString(R.string.str_pass2));
        final EditText input = new EditText(getContext());
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alert.setView(input);
        alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String srt = input.getEditableText().toString().trim();
                if (srt.equals(pass)) {
                    edite();
                } else {
                    dialog();
                    edite();
                    ALL.show_custom_msg(getContext(), getActivity(), getResources().getString(R.string.str13));
                }
            }
        });
        alert.setNegativeButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void edite() {
        password.setInputType(InputType.TYPE_CLASS_TEXT);
        edit_cancel = 1;
        name.setFocusable(true);
        edit.setText(getString(R.string.CANCEL));
        name.setEnabled(true);
        password.setEnabled(true);
        phone.setEnabled(true);
        email.setEnabled(true);
        language.setEnabled(true);
        sound_noti.setEnabled(true);
        mobile.setEnabled(true);
        address.setEnabled(true);
        spinner.setEnabled(true);
        ok.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        change();
        if (view.getId() == R.id.language) {
            c7.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        } else if (view.getId() == R.id.spinner) {
            c8.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        } else if (view.getId() == R.id.sound_noti) {
            c9.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        }
        return false;
    }

    public void change() {
        c1.setColorFilter(0xff526852, PorterDuff.Mode.SRC);
        c2.setColorFilter(0xff526852, PorterDuff.Mode.SRC);
        c3.setColorFilter(0xff526852, PorterDuff.Mode.SRC);
        c4.setColorFilter(0xff526852, PorterDuff.Mode.SRC);
        c5.setColorFilter(0xff526852, PorterDuff.Mode.SRC);
        c6.setColorFilter(0xff526852, PorterDuff.Mode.SRC);
        c7.setColorFilter(0xff526852, PorterDuff.Mode.SRC);
        c8.setColorFilter(0xff526852, PorterDuff.Mode.SRC);
        c9.setColorFilter(0xff526852, PorterDuff.Mode.SRC);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        change();
        if (view.getId() == R.id.ed_msg) {
            c1.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        }
        if (view.getId() == R.id.ed_password) {

            c2.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        }
        if (view.getId() == R.id.ed_phone) {
            c3.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        }
        if (view.getId() == R.id.ed_mobile) {
            c4.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        }
        if (view.getId() == R.id.ed_email) {
            c5.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        }
        if (view.getId() == R.id.ed_address) {
            c6.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        }
        if (view.getId() == R.id.language) {
            c7.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        }
        if (view.getId() == R.id.sound_noti) {
            c9.setColorFilter(0xff8ABB1C, PorterDuff.Mode.SRC);
        }
    }

    private class user_information extends AsyncTask<Object, Object, ArrayList<String>> {
        private String _userName;
        private String _password;
        LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);

        public user_information(String userName, String password, int edit) {
            _userName = userName;
            _password = password;
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(Object... data) {
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url + "/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "profile"));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        if (resString.startsWith("ï»¿")) {
                            resString = resString.substring(3);
                        }
                        JSONObject jsonObject = new JSONObject(resString);
                        JSONArray v = jsonObject.getJSONArray("profile");
                        for (int i = 0; i < v.length(); i++) {
                            JSONObject jsonObject2 = v.getJSONObject(i);
                            info.add(jsonObject2.optString("fullName"));
                            info.add(jsonObject2.optString("address"));
                            info.add(jsonObject2.optString("phone"));
                            info.add(jsonObject2.optString("mobile"));
                            info.add(jsonObject2.optString("email"));
                            info.add(jsonObject2.optString("language"));
                            info.add(jsonObject2.optString("gmt"));
                            info.add(_password);
                        }
                        return info;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                return null;
            } catch (ClientProtocolException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                return null;
            }catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {
                try {
                    information(result);
                } catch (Exception e) {
                    return;
                }
            }
            linlaHeaderProgress.setVisibility(View.GONE);
        }
    }

    private void information(ArrayList<String> result) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        if (result.size() != 0) {
            editor.putString(Preferences.FULL_NAME, result.get(0));
            editor.putString(Preferences.ADDREES, result.get(1));
            editor.putString(Preferences.PHONE, result.get(2));
            editor.putString(Preferences.MOBILE, result.get(3));
            editor.putString(Preferences.EMAIL, result.get(4));
            editor.putString(Preferences.language, result.get(5));
            editor.putString(Preferences.GMT, result.get(6));
        }
        editor.apply();
        show_information();
    }

    public void show_information() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ((EditText) view.findViewById(R.id.ed_msg)).setText(prefs.getString(Preferences.FULL_NAME, ""));
        ((EditText) view.findViewById(R.id.ed_address)).setText(prefs.getString(Preferences.ADDREES, ""));
        ((EditText) view.findViewById(R.id.ed_phone)).setText(prefs.getString(Preferences.PHONE, ""));
        ((EditText) view.findViewById(R.id.ed_mobile)).setText(prefs.getString(Preferences.MOBILE, ""));
        ((EditText) view.findViewById(R.id.ed_email)).setText(prefs.getString(Preferences.EMAIL, ""));
        boolean lan;
        String language = prefs.getString(Preferences.language, "");
        lan = language.equals("0");
        ((Switch) view.findViewById(R.id.language)).setChecked(lan);

        String sound = prefs.getString(Preferences.SOUND, "");
        lan = sound.equals("0");
        ((Switch) view.findViewById(R.id.sound_noti)).setChecked(lan);

        String gmt_time = prefs.getString(Preferences.GMT, "");
        String time = "1";
        switch (gmt_time) {
            case "1":
                time = "(GMT)";
                break;
            case "3600":
                time = "(GMT +1:00)";
                break;
            case "7200":
                time = "(GMT +2:00)";
                break;
            case "10800":
                time = "(GMT +3:00)";
                break;
            case "14400":
                time = "(GMT +4:00)";
                break;
            case "18000":
                time = "(GMT +5:00)";
                break;
            case "21600":
                time = "(GMT +6:00)";
                break;
            case "25200":
                time = "(GMT +7:00)";
                break;
            case "28800":
                time = "(GMT +8:00)";
                break;
        }
        int spinnerPosition = dataAdapter.getPosition(time);
        ((Spinner) view.findViewById(R.id.spinner)).setSelection(spinnerPosition);
        ((EditText) view.findViewById(R.id.ed_password)).setText(prefs.getString(Preferences.PASSWORD, ""));
    }

    public String GMT_Value(String gmt) {
        String time = "1";
        switch (gmt) {
            case "(GMT)":
                time = "1";
                break;
            case "(GMT +1:00)":
                time = "3600";
                break;
            case "(GMT +2:00)":
                time = "7200";
                break;
            case "(GMT +3:00)":
                time = "10800";
                break;
            case "(GMT +4:00)":
                time = "14400";
                break;
            case "(GMT +5:00)":
                time = "18000";
                break;
            case "(GMT +6:00)":
                time = "21600";
                break;
            case "(GMT +7:00)":
                time = "25200";
                break;
            case "(GMT +8:00)":
                time = "28800";
                break;
        }
        return time;
    }

    class update_information extends AsyncTask<Object, Object, String> {
        private String _userName;
        private String _password;
        ArrayList<String> _info;
        LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);

        public update_information(String userName, String password, ArrayList<String> info) {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            _userName = userName;
            _password = password;
            _info = info;
            hideKeyboard();
        }

        @Override
        protected String doInBackground(Object... data) {
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url + "/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(11);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "profileupdate"));
                nameValuePairs.add(new BasicNameValuePair("fullName", _info.get(0)));
                nameValuePairs.add(new BasicNameValuePair("password", _info.get(1)));
                nameValuePairs.add(new BasicNameValuePair("phone", _info.get(2)));
                nameValuePairs.add(new BasicNameValuePair("mobile", _info.get(3)));
                nameValuePairs.add(new BasicNameValuePair("email", _info.get(4)));
                nameValuePairs.add(new BasicNameValuePair("address", _info.get(5)));
                nameValuePairs.add(new BasicNameValuePair("language", _info.get(6)));
                nameValuePairs.add(new BasicNameValuePair("gmt", _info.get(7)));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        if (resString.contains("1")) {
                            SharedPreferences.Editor edite = prefs.edit();
                            edite.putString(Preferences.PASSWORD, _info.get(1));
                            edite.apply();
                            String lang1 = prefs.getString(Preferences.language1, "");
                            String lang2 = _info.get(6);
                            if (!lang2.equals(lang1)) {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(Preferences.language1, lang2);
                                editor.apply();
                                return "2";
                            }
                        }
                        return resString;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                return null;
            } catch (ClientProtocolException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            linlaHeaderProgress.setVisibility(View.GONE);
            if (result != null) {
                if (result.equals("2")) {
                    PitsTrack main = PitsTrack.getInstance();
                    main.selectItemFromDrawer(0, "");
                    main.finish();
                    main.startActivity(new Intent(main, MainActivity.class));
                } else {
                    Profile info = new Profile();
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction().replace(R.id.fragmaent, info).commit();
                    }
                }
            }
        }
    }

    public void hideKeyboard() {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (in != null) {
            in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
