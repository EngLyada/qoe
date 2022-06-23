package com.example.qoe.ui.gallery;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.qoe.R;
import com.example.qoe.databinding.FragmentGalleryBinding;
import com.example.qoe.ui.home.HomeFragment;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    WifiManager wifiManager;
    WifiInfo connection;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
         TextView g=(TextView)root.findViewById(R.id.text_gallery);

        wifiManager=(WifiManager)getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connection=wifiManager.getConnectionInfo();
        g.setText("SSID :"+connection.getSSID()+"\n"+"\n"+"MAC ADDRESS(User Machine) :"+connection.getMacAddress()+"\n"+"\n"+"Network IP :"+connection.getIpAddress()+
                "\n"+"\n"+"Frequency :"+connection.getFrequency()+"\n"+"\n"+"Network Bandwidth :"+connection.getLinkSpeed()+ "Mbps");
        // g.setText(HomeFragment.display);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}