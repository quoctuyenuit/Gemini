package com.example.a15520824.phanmemchuyensau;

/**
 * Created by MSI on 12/30/2017.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MainActivity extends Activity {


    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private TextView txtView;
    private ImageButton mbtSpeak;
    private static final String EMAIL = "email";
    private static LoginButton loginButton;
    private ArrayList<ActionModel> listData;
    private ImageButton btnSetting;

    public static final String CALCULATOR_PACKAGE = "com.android.calculator2";
    public static final String CALCULATOR_CLASS = "com.android.calculator2.Calculator";
    CallbackManager callbackManager = CallbackManager.Factory.create();

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        txtView = (TextView) findViewById( R.id.textView2 );
        mbtSpeak = (ImageButton) findViewById( R.id.imageButton );
        this.btnSetting = findViewById(R.id.btnSetting);
        checkVoiceRecognition();
        CheckPer();
        CheckGPS();
        getPlaceId();
        operateContactWrapper();

        this.showToastMessage("main onCreate");



        loginButton = (LoginButton) findViewById( R.id.login_button );
        loginButton.setReadPermissions( Arrays.asList( EMAIL ) );
        // If you are using in a fragment, call loginButton.setFragment(this);
        // Callback registration
        loginButton.registerCallback( callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        } );

        //Pass the context for DataManager
        DataManager.Instance().giveContext(this);
        this.listData = DataManager.Instance().ListData;

        this.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingIntent = new Intent(view.getContext(), SettingActivity.class);
                startActivity(settingIntent);
            }
        });
    }


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void operateContactWrapper() {
        int hasWriteContactsPermission = checkSelfPermission( Manifest.permission.CALL_PHONE );
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale( Manifest.permission.CALL_PHONE )) {
                showMessageOKCancel( "Cần cấp quyền gọi điện",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions( new String[]{Manifest.permission.CALL_PHONE},
                                        REQUEST_CODE_ASK_PERMISSIONS );
                            }
                        } );
                return;
            }
            requestPermissions( new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CODE_ASK_PERMISSIONS );
            return;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CheckPer() {
        int hasWriteContactsPermission = checkSelfPermission( Manifest.permission.CAMERA );
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale( Manifest.permission.CAMERA )) {
                showMessageOKCancel( "Cần cấp quyền máy ảnh",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions( new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CODE_ASK_PERMISSIONS );
                            }
                        } );
                return;
            }
            requestPermissions( new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS );
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CheckGPS() {
        int hasWriteContactsPermission = checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION );
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_FINE_LOCATION )) {
                showMessageOKCancel( "Cần cấp Vị trí",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_CODE_ASK_PERMISSIONS );
                            }
                        } );
                return;
            }
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS );
            return;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder( MainActivity.this )
                .setMessage( message )
                .setPositiveButton( "OK", okListener )
                .setNegativeButton( "Cancel", null )
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText( MainActivity.this, "Permision Phone is Granted", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( MainActivity.this, "Permision Phone is Denied", Toast.LENGTH_SHORT ).show();

            }
        } else {
            super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        }
    }


    public void checkVoiceRecognition() {
        Log.v( "", "checkVoiceRecognition checkVoiceRecognition" );
        // Kiem tra thiet bi cho phep nhan dang giong noi hay ko
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities( new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH ), 0 );
        if (activities.size() == 0) {
            mbtSpeak.setEnabled( false );
            Toast.makeText( this, "Voice recognizer not present", Toast.LENGTH_SHORT ).show();
        }
    }

    // Gui tap tin am thanh
    public void speak(View view) {
        Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        // xac nhan ung dung muon gui yeu cau
        intent.putExtra( RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName() );
        // goi y nhan dang nhung gi nguoi dung se noi
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH );
        // Gui yeu cau di
        startActivityForResult( intent, VOICE_RECOGNITION_REQUEST_CODE );
    }

    // Su kien nhan lai ket qua
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult( requestCode, resultCode, data );
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)
            // Truong hop co gia tri tra ve
            if (resultCode == RESULT_OK) {
                ArrayList<String> textMatchList = data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
                if (!textMatchList.isEmpty()) {
                    // kiem tra neu co chua tu khoa 'search' thi se bat dau tim kiem tren web
                    if (textMatchList.get( 0 ).contains( "search" )) {
                        String searchQuery = textMatchList.get( 0 ).replace( "search", " " );
                        Intent search = new Intent( Intent.ACTION_WEB_SEARCH );
                        search.putExtra( SearchManager.QUERY, searchQuery );
                        startActivity( search );
                    } else {
                        // Hien thi ket qua
                        txtView.setText( textMatchList.get( 0 ) );
                        onClick( textMatchList.get( 0 ) );
                    }
                }
                // Cac truong hop loi
            } else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR) {
                showToastMessage( "Audio Error" );
            } else if (resultCode == RecognizerIntent.RESULT_CLIENT_ERROR) {
                showToastMessage( "Client Error" );
            } else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR) {
                showToastMessage( "Network Error" );
            } else if (resultCode == RecognizerIntent.RESULT_NO_MATCH) {
                showToastMessage( "No Match" );
            } else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR) {
                showToastMessage( "Server Error" );
            }
        super.onActivityResult( requestCode, resultCode, data );

    }

    void showToastMessage(String message) {
        Toast.makeText( this, message, Toast.LENGTH_SHORT ).show();
    }

    private String[] getArrayKey(ArrayList<ActionModel> listData){
        String[] listResult =  new String[listData.size()];
        for (int i = 0; i< listData.size(); i++){
            listResult[i] = listData.get(i).getKey();
        }

        return listResult;
    }

    public void onClick(String txt) {
        Intent intent;
        //txt = "abc";
        String check[]=this.getArrayKey(this.listData);
        //kiem tra neu dung mo ung dung nct

        if (txt.indexOf( check[19] ) != -1) {
            if (checkYt() == true) {

                if (txt.indexOf( check[1] ) != txt.length() - check[1].length()) {
                    String search = txt.substring( txt.indexOf( check[1] ) + check[1].length()+1, txt.length() );
                    String a = "https://www.youtube.com/results?search_query=" + search.toString();
                    startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( a ) ) );
                } else {
                    PackageManager pack = this.getPackageManager();
                    Intent app = pack.getLaunchIntentForPackage( "com.google.android.youtube" );
                    startActivity( app );
                }

            } else //sai thi vao CHPlay de tim ung dung
            {
                Intent marketIntent = new Intent( Intent.ACTION_VIEW );
                marketIntent.setData( Uri.parse( "market://details?id=com.google.android.youtube" ) );
                startActivity( marketIntent );
            }
        }
        else if (txt.indexOf( check[13] ) != -1 ) {
            intent = new Intent( Intent.ACTION_MAIN );
            intent.addCategory( Intent.CATEGORY_DEFAULT );
            intent.setType( "vnd.android-dir/mms-sms" );
            startActivity( intent );
        } else if (txt.indexOf( check[14] ) != -1 ) {
            OpenCaculator();
        } else if ( txt.indexOf( check[15] ) != -1) {
            intent = new Intent( Intent.ACTION_DIAL );
            startActivity( intent );
        } else if (txt.indexOf( check[16] ) != -1) {
            intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "content://media/internal/images/media" ) );
            try {
                MainActivity.this.startActivity( intent );
            } catch (ActivityNotFoundException eee) {
                try {
                    intent = new Intent( Intent.ACTION_VIEW );
                    intent.setType( android.provider.MediaStore.Images.Media.CONTENT_TYPE );
                } catch (Exception err) {
                    Toast.makeText( MainActivity.this, "This app not supported in your device", Toast.LENGTH_LONG ).show();
                }
            }
        } else if ( txt.indexOf( check[17] ) != -1) {
            intent = new Intent( android.content.Intent.ACTION_VIEW,
                    Uri.parse( "http://maps.google.com/" ) );
            startActivity( intent );
        } else if (txt.indexOf( check[18] ) != -1 ) {

            Intent i = new Intent( AlarmClock.ACTION_SET_ALARM );

            startActivity( i );
        }
        else if (txt.indexOf( check[0] ) != -1 ) {
            String phone;

            try {
                phone = txt.substring( txt.indexOf( check[0] ) + check[0].length()+1, txt.length() );
            } catch (Exception e) {
                return;
            }

            String uri = "tel:" + phone.trim();
            intent = new Intent( Intent.ACTION_CALL );
            intent.setData( Uri.parse( uri ) );

            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling


                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity( intent );
        } else if (txt.indexOf( check[1] ) != -1 || txt.indexOf( check[2] ) != -1 ) {
            String a = "", b = "";
            if (txt.length() > 10) {

                if (txt.indexOf( "giờ" ) != -1) {
                    try {
                        a = txt.substring( txt.indexOf( "giờ" ) - 3, txt.indexOf( "giờ" ) );
                    } catch (Exception e) {
                        return;
                    }
                    a = a.trim();
                    if (txt.indexOf( "giờ" ) + 6 < txt.length())
                        try {
                            b = txt.substring( txt.indexOf( "giờ" ) + 3, txt.indexOf( "giờ" ) + 6 );
                        } catch (Exception e) {
                            return;
                        }
                    else
                        try {
                            b = txt.substring( txt.indexOf( "giờ" ) + 3, txt.length() );
                        } catch (Exception e) {
                            return;
                        }
                    b = b.trim();

                } else if (txt.indexOf( ":" ) != -1) {
                    try {
                        a = txt.substring( txt.indexOf( ":" ) - 2, txt.indexOf( ":" ) );
                    } catch (Exception e) {
                        return;
                    }
                    a = a.trim();
                    if (txt.indexOf( ":" ) + 4 < txt.length())
                        try {
                            b = txt.substring( txt.indexOf( ":" ) + 1, txt.indexOf( ":" ) + 4 );
                        } catch (Exception e) {
                            return;
                        }
                    else
                        try {
                            b = txt.substring( txt.indexOf( ":" ) + 1, txt.length() );
                        } catch (Exception e) {
                            return;
                        }
                    b = b.trim();

                } else {
                    if (txt.indexOf( check[1] ) + check[1].length()+3 > txt.length())
                        try {

                            a = txt.substring( txt.indexOf( check[1] ) + check[1].length()+1, txt.length() );
                        } catch (Exception e) {
                            return;
                        }
                    else
                        try {
                            a = txt.substring( txt.indexOf( check[1] ) + check[1].length()+1, txt.indexOf( check[1] ) + check[1].length()+3 );
                        } catch (Exception e) {
                            return;
                        }
                    a = a.trim();


                }

            }
            a = a.replaceAll( "[^0-9,-\\.]", "" );
            b = b.replaceAll( "[^0-9,-\\.]", "" );

            if (b == "")
                b = "0";
            if (txt.indexOf( "chiều" ) != -1 || txt.indexOf( "tối" ) != -1)
                if (Integer.parseInt( a ) < 12)
                    a = String.valueOf( Integer.parseInt( a ) + 12 );
            Intent i = new Intent( AlarmClock.ACTION_SET_ALARM );
            i.putExtra( AlarmClock.EXTRA_MESSAGE, "Báo thức" );
            if (a.length() != 0) {
                try {
                    i.putExtra( AlarmClock.EXTRA_HOUR, Integer.parseInt( a ) );
                } catch (Exception e) {
                    return;
                }
            }
            if (b.length() != 0) {
                try {
                    i.putExtra( AlarmClock.EXTRA_MINUTES, Integer.parseInt( b ) );
                } catch (Exception e) {
                    return;
                }
            }
            if (b.length() == 0 && a.length() != 0) {
                try {
                    i.putExtra( AlarmClock.EXTRA_MINUTES, 0 );
                } catch (Exception e) {
                    return;
                }
            }


            startActivity( i );
        } else if (txt.indexOf( check[3] ) != -1 || txt.indexOf( check[4] ) != -1) {
            String sms = "";
            String phone_number = "";
            if (txt.indexOf( "nội dung" ) != -1) {
                try {
                    sms = txt.substring( txt.indexOf( "nội dung" ) + 8, txt.length() );
                } catch (Exception e) {
                    return;
                }
            }
            if (txt.indexOf( "cho" ) != -1) {
                try {
                    phone_number = txt.substring( txt.indexOf( "cho" ) + 4, txt.indexOf( " ", txt.indexOf( "cho" ) + 4 ) );
                } catch (Exception e) {
                    return;
                }
                int temp = txt.indexOf( " ", txt.indexOf( "cho" ) + 4 );

                try {
                    while (temp != -1 && checkNumber( txt.substring( temp + 1, temp + 2 ) )) {
                        int tempx;
                        try {
                            tempx = txt.indexOf( " ", temp + 1 );
                        } catch (Exception e) {
                            tempx = txt.length();
                        }
                        if (tempx == -1)
                            tempx = txt.length();
                        try {
                            phone_number += txt.substring( temp, tempx );
                        } catch (Exception e) {
                            return;
                        }

                        temp = txt.indexOf( " ", temp + 2 );
                        if (tempx == txt.length())
                            temp = -1;

                    }
                } catch (Exception e) {
                    return;
                }

            } else if (txt.indexOf( check[4] ) != -1) {
                try {
                    sms = txt.substring( txt.indexOf( check[4] ) + check[4].length(), txt.length() );
                } catch (Exception e) {
                    return;
                }
            }


            try {
                Intent sendIntent = new Intent( Intent.ACTION_VIEW );
                sendIntent.setData( Uri.parse( "sms:" ) );
                sendIntent.putExtra( "sms_body", sms );
                if (phone_number.length() != 0)
                    sendIntent.putExtra( "address", phone_number );
                startActivity( sendIntent );
            } catch (Exception e) {
                Toast.makeText( getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG ).show();
                e.printStackTrace();
            }
        } else if ( txt.indexOf( check[5] ) != -1) {
            Intent i = new Intent( MediaStore.ACTION_VIDEO_CAPTURE );
            startActivity( i );
        } else if ( txt.indexOf( check[6] ) != -1 || txt.indexOf( check[7] ) != -1 ||  txt.indexOf( check[8] ) != -1) {
            String a = "";

            try {
                a = txt.substring( txt.indexOf( check[6] ) + check[6].length()+1, txt.length() );
            } catch (Exception e) {
                return;
            }



            if (txt.indexOf( check[7] ) != -1) {
                try {
                    a = txt.substring( txt.indexOf( check[7] ) + check[7].length()+1, txt.length() );
                } catch (Exception e) {
                    return;
                }

            } else if (txt.indexOf( check[8] ) != -1) {
                try {
                    a = txt.substring( txt.indexOf( check[8] ) + check[8].length()+1, txt.length() );
                } catch (Exception e) {
                    return;
                }
            }

            Uri gmmIntentUri = Uri.parse( "geo:0,0?q=" + a );
            Intent mapIntent = new Intent( Intent.ACTION_VIEW, gmmIntentUri );
            mapIntent.setPackage( "com.google.android.apps.maps" );
            startActivity( mapIntent );


        } else if ( txt.indexOf( check[9] ) != -1 ||  txt.indexOf( check[10] ) != -1) {
            intent = new Intent( Intent.ACTION_WEB_SEARCH );
            String sms = txt;
            if ( txt.indexOf( check[9] ) != -1) {

                try {
                    sms = txt.substring( txt.indexOf( check[9] ) + check[9].length()+1, txt.length() );
                } catch (Exception e) {
                    return;
                }
            } else if ( txt.indexOf( check[10] ) != -1) {

                try {
                    sms = txt.substring( txt.indexOf( check[10] ) + check[10].length()+1, txt.length() );
                } catch (Exception e) {
                    return;
                }
            }
            intent.putExtra( SearchManager.QUERY, sms );
            startActivity( intent );
        } else if ( txt.indexOf( check[11] ) != -1) {

            ShareDialog shareDialog = new ShareDialog( MainActivity.this );

            if (ShareDialog.canShow( ShareLinkContent.class )) {
                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                        //.setContentUrl("")

                        .setShareHashtag( new ShareHashtag.Builder()
                                .setHashtag( "#AST" )
                                .build() )
                        .build();

                shareDialog.show( shareLinkContent );
            }

        } else if (txt.indexOf( check[12] ) != -1) {
            ShareDialog shareDialog = new ShareDialog( MainActivity.this );
            try {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            if (ShareDialog.canShow( ShareLinkContent.class )) {
                if(placeID==null)
                    return;

                ShareContent content = new ShareLinkContent.Builder()
                        .setShareHashtag( new ShareHashtag.Builder().setHashtag("io").build())
                        .setPlaceId(placeID)
                        .build();
                ShareDialog dialog = new ShareDialog(MainActivity.this);
                ShareDialog.Mode mode = ShareDialog.Mode.NATIVE;
                if (dialog.canShow( content, mode)){
                    dialog.show(content,mode);
                }




            }
        }else if(!DataManager.Instance().checkKey(txt))
        {

            String a=DataManager.Instance().getActionId(txt).toString();
            if(check(a))
            {
                PackageManager pack=this.getPackageManager();
                intent=pack.getLaunchIntentForPackage(a);
                startActivity(intent);

            }
            else
            {
                intent=new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse( "market://details?id="+a ) );
                startActivity( intent );

            }
        }
        else {
            intent = new Intent( Intent.ACTION_WEB_SEARCH );
            String sms = txt;
            intent.putExtra( SearchManager.QUERY, sms );
            startActivity( intent );
        }

    }

    public String placeID = "";
    private GpsTracker gpsTracker;
    private void getPlaceId() {
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            makeJsonObjectRequest( longitude,latitude );
        }else{
            gpsTracker.showSettingsAlert();
        }


    }


    private void makeJsonObjectRequest(double longitude,double latitude) {

        AccessToken token;
        token = AccessToken.getCurrentAccessToken();

        if (token == null) {
            //Means user is not logged in
            Toast.makeText(MainActivity.this, "Bạn cần đăng nhập facebook", Toast.LENGTH_SHORT).show();
            return ;
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest( Request.Method.GET,
                "https://graph.facebook.com/search?&type=place&center="+latitude+","+longitude+"&distance=5000&limit=1&access_token="+"EAACW5Fg5N2IBAKA1wKWviZCrWEBy44oP6W0t0ECUFd4XClBENIrIEqemuUSb1szeQSMCbq7ZAeZCIRDF8TvlZAM2ZAIOcReUAPybCdBnnZCmIpr56omYZB3ZBG9pFhF3LFx7hZBza4AZAw3qADOZAdV980e8aUsCZCZBoYDFCtXWbI2dFmzGMvjPoEInJQIz1vM7LQ8oZD", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    // Parsing json object response
                    // response will be a json object

                    JSONArray arr = (JSONArray) response.getJSONArray( "data" );
                    JSONObject place=arr.getJSONObject( 0 );


                    placeID=place.getString( "id" );



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });
        RequestQueue mRequestQueue= Volley.newRequestQueue(MainActivity.this);
        // Adding request to request queue
        mRequestQueue.add(jsonObjReq);
    }


    public  void OpenCaculator(){

        ArrayList<HashMap<String,Object>> items =new ArrayList<HashMap<String,Object>>();

        final PackageManager pm = getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs) {
            if( pi.packageName.toString().toLowerCase().contains("calcul")){
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("appName", pi.applicationInfo.loadLabel(pm));
                map.put("packageName", pi.packageName);
                items.add(map);
            }
        }
        if(items.size()>=1){
            String packageName = (String) items.get(0).get("packageName");
            Intent i = pm.getLaunchIntentForPackage(packageName);
            if (i != null)
                startActivity(i);
        }
        else{
            // Application not found
        }

    }
    public boolean check( String txt)
    {
        ApplicationInfo info;
        try{
            info = getPackageManager().getApplicationInfo(txt, 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }
    public boolean checkYt()
    {
        ApplicationInfo info;
        try{
            info = getPackageManager().getApplicationInfo("com.google.android.youtube", 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }

    public static boolean checkNumber(String s) throws Exception
    {
        for(int i =0 ; i< s.length(); i++){
            if(Character.isDigit(s.charAt(i))){

            }else return false;
        }
        return true;
    }

}

