package com.example.qoe.ui.home;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Magnifier;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qoe.R;
import com.example.qoe.RateActivity;
import com.example.qoe.databinding.FragmentHomeBinding;
import com.example.qoe.ui.gallery.GalleryFragment;
import com.example.qoe.ui.slideshow.SlideshowFragment;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
     public static  String apptype="";
    public static  String display="";
    public static  String time="5";
    public static String str="";
    public static String mac="";
    public static String nip="";
    public static String freq="";
    public static String bw="";
    public static String status="";
    public static String pck="";
    String save_url="https://lysmultd.com/qoe/save_wifi.php";
    final int intervalTime = 10000; // 10 sec
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    PopupWindow popupWindow;
    LinearLayout linearLayout1;
    WifiManager wifiManager;
    WifiInfo connection;
    Button zoom, team;
    RadioButton ict, pha, agric,vet,cedat,edu, eas;
    RadioGroup rdGroup;
    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView image = (ImageView)root.findViewById(R.id.imageView3);

        Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        image.startAnimation(animation1);

        wifiManager=(WifiManager)getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connection=wifiManager.getConnectionInfo();
        display="SSID :"+connection.getSSID()+"\n"+"\n"+"MAC ADDRESS(User Machine) :"+connection.getMacAddress()+"\n"+"\n"+"Network IP :"+connection.getIpAddress()+
                "\n"+"\n"+"Frequency :"+connection.getFrequency()+"\n"+"\n"+"Network Bandwidth :"+connection.getLinkSpeed()+ "Mbps";
       mac=connection.getMacAddress();
       nip=String.valueOf(connection.getIpAddress());
       freq=String.valueOf(connection.getFrequency());
       bw=String.valueOf(connection.getLinkSpeed());
       if(connection.getLinkSpeed()<20){
           status="Poor";
       }else if(connection.getLinkSpeed()>20 && connection.getLinkSpeed()< 60){
           status="Average";
       }else if(connection.getLinkSpeed()>60 && connection.getLinkSpeed()< 120){
           status="Good";
       }else if(connection.getLinkSpeed()>120 ){
           status="Very Good";
       }
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // inflate the layout of the popup window
                final Dialog popupView = new Dialog(getContext());
                popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                popupView.setContentView(R.layout.popuplayout);
                params.copyFrom(popupView.getWindow().getAttributes());
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.CENTER;
                popupView.getWindow().setAttributes(params);

                popupView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupView.show();

                zoom=(Button)popupView.findViewById(R.id.zoom);
                team=(Button)popupView.findViewById(R.id.team);
                ict=(RadioButton)popupView.findViewById(R.id.ict);
                pha=(RadioButton)popupView.findViewById(R.id.pha);
                agric=(RadioButton)popupView.findViewById(R.id.agric);
                vet=(RadioButton)popupView.findViewById(R.id.vet);
                cedat=(RadioButton)popupView.findViewById(R.id.cedat);
                edu=(RadioButton)popupView.findViewById(R.id.edu);
                eas=(RadioButton)popupView.findViewById(R.id.eslis);
                rdGroup=(RadioGroup)popupView.findViewById(R.id.rdGroup);
                rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(ict.isChecked()) {
                            str = "Computing And Information Science";
                        } else if(pha.isChecked()) {
                            str = "Pharmacy";
                        }else if(agric.isChecked()) {
                            str = "Agriculture";
                        }else if(vet.isChecked()) {
                            str = "Vetinary Medicine";
                        }else if(cedat.isChecked()) {
                            str = "CEDAT";
                        }else if(edu.isChecked()) {
                            str = "Education";
                        }else if(eas.isChecked()) {
                            str = "East African School of Library And Information Science";
                        }

                    }
                });
                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
               // popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                zoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


                       // if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getExtraInfo().equals("MAKAIR")) {
                            WifiManager wifimgr=(WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE) ;
                            WifiInfo winfo=wifimgr.getConnectionInfo();
                            String name =winfo.getSSID();
                            Toast.makeText(getContext(),"Network Name:"+name,Toast.LENGTH_LONG).show();
                            apptype="Zoom";
                            pck="us.zoom.videomeetings";
                            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("us.zoom.videomeetings");
                            //startActivity(intent);
                            if (intent != null) {
                                if(str!=""){
                                startActivity(intent);}
                            } else {
                                if(str!="") {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=us.zoom.videomeetings")));
                                }
                            }
                            notification();

                            ActionStartsHere();
//                        } else {
//                            Toast.makeText(getContext(), "Internet Connection Is Required", Toast.LENGTH_LONG).show();
//                        }
                    }
                });
                team.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                            apptype="MS Teams";
                            pck="com.microsoft.teams";

                            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.microsoft.teams");
                            //startActivity(intent);
                            if (intent != null) {
                                if(str!=""){
                                startActivity(intent);}
                            } else {if(str!="") {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.microsoft.teams")));
                            }
                            }

                            ActionStartsHere();
                        } else {
                            Toast.makeText(getContext(), "Internet Connection Is Required", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        return root;
    }

    private void ActionStartsHere() {
       // if (Helper.isAppRunning(getContext(), pck)) {
            if(str==""){
               Toast.makeText(getActivity(),"Select School to continue",Toast.LENGTH_LONG).show();
            }else{
                againStartGPSAndSendFile();
                notification();
            }
//
//        } else {
//            notification();
//        }

    }

    private void againStartGPSAndSendFile() {
        new CountDownTimer(500000,5000) {
            @Override
            public void onTick(long millisUntilFinished) {

                popup();
                //Toast.makeText(getContext(), display, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {

                ActionStartsHere();
            }
        }.start();
    }


    private void notification() {
//        NotificationManager mNotificationManager = (NotificationManager)getActivity().getSystemService( NOTIFICATION_SERVICE ) ;
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext().getApplicationContext() , default_notification_channel_id ) ;
//        mBuilder.setContentTitle( "MAKAIR INFO" ) ;
//        mBuilder.setContentText( display ) ;
//        mBuilder.setTicker( "Attention" ) ;
//        mBuilder.setSmallIcon(R.drawable.que);
//        mBuilder.setAutoCancel( true ) ;
//        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
//            int importance = NotificationManager. IMPORTANCE_HIGH ;
//            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
//            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
//            assert mNotificationManager != null;
//            mNotificationManager.createNotificationChannel(notificationChannel) ;
//        }
//        assert mNotificationManager != null;
//        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
//        Intent resultIntent = new Intent(getActivity(), GalleryFragment.class);
//        resultIntent.setAction(Intent.ACTION_MAIN);
//        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0,
//                resultIntent, 0);
//
//        mBuilder.setContentIntent(pendingIntent);
//        mNotificationManager.notify(1, mBuilder.build());
//
//        Intent notificationIntent = new Intent(getActivity(), RateActivity.class);
//        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("us.zoom.videomeetings");
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent conPendingIntent = PendingIntent.getActivity(getContext(),0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//         mBuilder.setContentIntent(conPendingIntent );
//         startActivity(intent);



        Intent notificationIntent = new Intent(getContext() , RateActivity. class ) ;
        notificationIntent.putExtra( "NotificationMessage" , display ) ;
        notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        notificationIntent.setAction(Intent. ACTION_MAIN ) ;
        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;

        PendingIntent resultIntent = PendingIntent. getActivity (getContext(), 0 , notificationIntent , 0 ) ;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), default_notification_channel_id )
                .setSmallIcon(R.drawable.que )
                .setContentTitle( "Makair Network| Rate your experience" )
                .setContentIntent(resultIntent)
                .setStyle( new NotificationCompat.InboxStyle())
                .setContentText(display ) ;
        NotificationManager mNotificationManager = (NotificationManager)getContext().getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >=android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () ,mBuilder.build()) ;
//        ActivityManager am = (ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE);
//        am.restartPackage(pck);

    }



    private void endmeeting() {
//        ActivityManager am = (ActivityManager)getContext(). getApplicationContext().getSystemService("activity");
//        Method forceStopPackage;
//        forceStopPackage =am.getClass().getDeclaredMethod("forceStopPackage",String.class);
//        forceStopPackage.setAccessible(true);
//        forceStopPackage.invoke(am, pkg);
    }

    void popup() {

        RequestQueue requestQueue;
        ProgressDialog progressDialog;
         requestQueue = Volley.newRequestQueue(getContext());
            progressDialog = new ProgressDialog(getContext());
            // Adding click listener to button.


                    // Creating string request with post method.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,save_url, new Response.Listener<String>() {
                        @Override public void onResponse(String ServerResponse) {
                            // Hiding the progress dialog after all task complete.
                            progressDialog.dismiss();
                            // Showing response message coming from server.
                           // Toast.makeText(getActivity(), ServerResponse, Toast.LENGTH_LONG).show();
                        }
                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            // Hiding the progress dialog after all task complete.
                            progressDialog.dismiss();
                            // Showing error message if something goes wrong.
                           // Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                        } }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Creating Map String Params.
                            Map<String, String> params = new HashMap<String, String>();
                            // Adding All values to Params.
                            params.put("faculty",str);
                            params.put("status",status);
                            params.put("app",apptype);
                            params.put("user_machine",mac);
                            params.put("Nerwork_ip",nip);
                            params.put("Frequency",freq);
                            params.put("bandwidth",bw);
                            params.put("period",time);
                            return params;
                        }
                    };
                    // Creating RequestQueue.
                     requestQueue = Volley.newRequestQueue(getContext());
                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);

    }
    public static class Helper {

        public static boolean isAppRunning(final Context context, final String packageName) {
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
            if (procInfos != null)
            {
                for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                    if (processInfo.processName.equals(packageName)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
 }