package com.org.chiranz.aitem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;

import org.jsoup.nodes.Document;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import process.Dataset;
import process.Model;
import process.Process;

public class MainActivity extends AppCompatActivity {

    private Button search_button;
    private String reg_type;
    private String initials,
            family_name,
            other_name,
            reg_no,
            nic_no,
            part_of_address;
    Process process;
    ArrayList<Model> data = new ArrayList<>();
    Intent intent;
    ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        search_button = (Button) findViewById(R.id.search_button);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view) {

        EditText initials = (EditText) findViewById(R.id.initials);
        EditText family_name = (EditText) findViewById(R.id.family_name);
        EditText other_name = (EditText) findViewById(R.id.other_name);
        EditText reg_no = (EditText) findViewById(R.id.reg_no);
        EditText nic_no = (EditText) findViewById(R.id.nic_no);
        EditText part_of_address = (EditText) findViewById(R.id.part_of_address);
        Spinner reg_type = (Spinner) findViewById(R.id.reg_type);


        this.initials = initials.getText().toString();
        this.family_name = family_name.getText().toString();
        this.other_name = other_name.getText().toString();
        this.reg_no = reg_no.getText().toString();
        this.nic_no = nic_no.getText().toString();
        this.part_of_address = part_of_address.getText().toString();
        this.reg_type = reg_type.getSelectedItem().toString();
        this.intent = new Intent(this, ResultActivity.class);
        new Description().execute();
    }

    private class Description extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            nDialog = new ProgressDialog(MainActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Find Doctors");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                process = new Process();
                String url = process.createUrl(reg_type,
                        initials,
                        family_name,
                        other_name,
                        reg_no,
                        nic_no,
                        part_of_address);
                Dataset.setUrl(url);

                Document homedoc = process.getDocument(url);
                data = process.getPageData(homedoc);
                Dataset.setModels(data);
                Dataset.setDoc(homedoc);
                Dataset.setNumOfDoc(process.getNumberOfDoctors(homedoc));
                Dataset.setIsPrevOk(process.getHasPrev(homedoc));
                Dataset.setIsNextOk(process.getHasNext(homedoc));

                FileOutputStream fos = openFileOutput("out", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(Dataset.getModels());
                os.close();
                fos.close();
            } catch (Exception e) {
                try {
                    FileInputStream fis = openFileInput("out");

                    ObjectInputStream is = new ObjectInputStream(fis);
                    ArrayList<Model> simpleClass = (ArrayList<Model>) is.readObject();
                    is.close();
                    fis.close();
                    ArrayList<Model> offlinedata = (simpleClass);
                    Dataset.setModels(offlinedata);
                    Dataset.setIsNextOk(false);
                } catch (Exception ex) {


                }
//                e.printStackTrace();
            }

            System.out.println(data.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            nDialog.dismiss();
            startActivity(intent);
        }
    }

}
