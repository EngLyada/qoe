package com.example.qoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qoe.ui.home.HomeFragment;

import java.util.HashMap;
import java.util.Map;

public class RateActivity extends AppCompatActivity {
EditText comment,sname;
    RadioButton e,vg,g,f,p;
    public static String str="";
    EditText cmt;
    String save_url="https://lysmultd.com/qoe/save_rate.php";
    RadioGroup rdGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        cmt=(EditText)findViewById(R.id.comment);
        e=(RadioButton)findViewById(R.id.e);
        vg=(RadioButton)findViewById(R.id.vg);
        g=(RadioButton)findViewById(R.id.g);

        p=(RadioButton)findViewById(R.id.p);

        Button go=(Button) findViewById(R.id.button);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });
        rdGroup=(RadioGroup)findViewById(R.id.rdGroup);
        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(e.isChecked()) {
                    str = "Excellent";
                } else if(vg.isChecked()) {
                    str = "Very Good";
                }else if(g.isChecked()) {
                    str = "Good";

                }else if(p.isChecked()) {
                    str = "Poor";
                }

            }
        });
    }
    void popup() {

        RequestQueue requestQueue;
        ProgressDialog progressDialog;
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        // Adding click listener to button.


        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST,save_url, new Response.Listener<String>() {
            @Override public void onResponse(String ServerResponse) {
                // Hiding the progress dialog after all task complete.
                progressDialog.dismiss();
                // Showing response message coming from server.
                Toast.makeText(RateActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(RateActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Hiding the progress dialog after all task complete.
                progressDialog.dismiss();
                // Showing error message if something goes wrong.
                Toast.makeText(RateActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show(); } }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("comment",cmt.getText().toString());
                params.put("rate",str);
                params.put("machine",HomeFragment.mac);
                params.put("faculty",HomeFragment.str);
                params.put("app",HomeFragment.apptype);
                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                startActivity(new Intent(this, MainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}