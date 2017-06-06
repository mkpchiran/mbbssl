package com.org.chiranz.aitem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

import process.Dataset;
import process.Model;
import process.Process;

public class ResultActivity extends AppCompatActivity {
    ArrayList<Model> data = new ArrayList<>();
    TableLayout table = null;
    ProgressDialog nDialog;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setTitle(
                getSupportActionBar().getTitle().toString() + " : " + String.valueOf(Dataset.getNumOfDoc()));
        this.intent = new Intent(this, DoctorActivity.class);
        fillData();
    }

    private void fillData() {
        final ArrayList<Model> models = Dataset.getModels();
        this.table = (TableLayout) findViewById(R.id.table);
        this.table.setPadding(5, 5, 5, 5);

        Button prevButton = (Button) findViewById(R.id.prevBut);
        Button nextButton = (Button) findViewById(R.id.nextBut);

        if (!Dataset.isPrevOk()) {
            prevButton.setActivated(false);
            prevButton.setVisibility(View.INVISIBLE);
        } else {

            prevButton.setActivated(true);
            prevButton.setVisibility(View.VISIBLE);
        }

        if (!Dataset.isNextOk()) {
            nextButton.setActivated(false);
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            nextButton.setActivated(true);
            nextButton.setVisibility(View.VISIBLE);
        }
        this.table.removeAllViews();
        if (models.size() == 0) {
            TableRow row = new TableRow(this);
            TextView view = new TextView(this);
            view.setText("No result found");
            row.addView(view);
            table.addView(row);
        } else
            for (int i = 0; i < models.size(); i++) {
                int color = i % 2;
                if (color == 0) {
                    color = R.color.colorBlue;
                } else if (color == 1) {
                    color = R.color.DarkcolorBlue;
                } /*else if (color == 2) {
                    color = R.color.colorRed;
                }*/

                final Model model = models.get(i);
                String reg_No = models.get(i).getReg_No(),
                        reg_Date = models.get(i).getReg_Date(),
                        full_Name = models.get(i).getFull_Name(),
                        address = models.get(i).getAddress(),
                        qualifications = models.get(i).getQualifications();

                TableRow row = new TableRow(this);
                TableRow row1 = new TableRow(this);
                TableRow row2 = new TableRow(this);
                TableRow row3 = new TableRow(this);
                TableRow row4 = new TableRow(this);

                TextView tvRegNo = new TextView(this);
                TextView tvRegDate = new TextView(this);
                TextView tvFullName = new TextView(this);
                TextView tvAddress = new TextView(this);
                TextView tvQualifications = new TextView(this);

                row.setBackgroundResource(color);
                row1.setBackgroundResource(color);
                row2.setBackgroundResource(color);
                row3.setBackgroundResource(color);
                row4.setBackgroundResource(color);

                tvAddress.setSingleLine(false);
                tvAddress.setMaxLines(5);
                tvAddress.setMinLines(1);
                tvAddress.setLines(1);

                tvRegNo.setPadding(20, 20, 20, 20);
                tvRegDate.setPadding(20, 20, 20, 20);
                tvFullName.setPadding(20, 20, 20, 20);
                tvAddress.setPadding(20, 20, 20, 20);
                tvQualifications.setPadding(20, 20, 20, 20);

                tvRegNo.setText("Registered No   : " + reg_No);
                tvRegDate.setText("Registered Date : " + reg_Date);
                tvFullName.setText("Full Name         : " + full_Name);
                tvAddress.setText("Address           : " + address);
                tvQualifications.setText("Qualifications : " + qualifications);

                row.addView(tvRegNo);
                row1.addView(tvFullName);
                row2.addView(tvRegDate);
                row3.addView(tvAddress);
                row4.addView(tvQualifications);

//                table.addView(row);
                table.addView(row1);
//                table.addView(row2);
                table.addView(row3);
//                table.addView(row4);

                this.intent = new Intent(this, DoctorActivity.class);


                row3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        Dataset.setModel(model);
                        startActivity(intent);
                    }
                });

                row1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        Dataset.setModel(model);
                        startActivity(intent);
                    }
                });

            }


    }

    private class NextPage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            nDialog = new ProgressDialog(ResultActivity.this);
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
                Process process = new Process();
                String pageurl = process.getNextPageUrl(Dataset.getDoc());
                Document homedoc = process.getDocument(pageurl);
                data = process.getPageData(homedoc);
                Dataset.setModels(data);
                Dataset.setDoc(homedoc);
                Dataset.setUrl(pageurl);

                Dataset.setIsPrevOk(process.getHasPrev(homedoc));
                Dataset.setIsNextOk(process.getHasNext(homedoc));

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(data.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            fillData();
            nDialog.dismiss();

        }
    }

    private class PreviousPage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nDialog = new ProgressDialog(ResultActivity.this);
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
                Process process = new Process();
                String pageurl = process.getPreviousPageUrl(Dataset.getDoc());

                Document homedoc = process.getDocument(pageurl);
                data = process.getPageData(homedoc);

                Dataset.setIsPrevOk(process.getHasPrev(homedoc));
                Dataset.setIsNextOk(process.getHasNext(homedoc));

                Dataset.setModels(data);
                Dataset.setDoc(homedoc);
                Dataset.setUrl(pageurl);

            } catch (Exception e) {
                Model model = new Model();
                model.setReg_No(e.getLocalizedMessage());
                data.add(model);
            }

            System.out.println(data.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            fillData();
            nDialog.dismiss();

        }
    }

    public void loadNextPage(View view) {

        new NextPage().execute();
    }

    public void loadPreviousPage(View view) {

        new PreviousPage().execute();
    }
}
