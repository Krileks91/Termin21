package com.example.termin21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.net.IpSecManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvText;
    private Switch swSmer;
    private Button bStart;

    private boolean smer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvText = findViewById(R.id.tvText);
        swSmer = findViewById(R.id.swSmer);
        bStart = findViewById(R.id.bStart);

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startThread();
                startAsyncTask();
            }
        });
        swSmer.setText(smer ? "Pozitivan" : "Negativan");
        swSmer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                smer = !smer;
                swSmer.setText(smer ? "Pozitivan" : "Negativan");
            }
        });
    }
    private void startAsyncTask(){
        MojAsyncTask mojAsyncTask = new MojAsyncTask();
        mojAsyncTask.execute(10);
    }

    private void startThread() {
        bStart.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sekunde = 10;
                while (sekunde > 0) {
                    sekunde = smer ? sekunde + 1 : sekunde - 1;
                    uprateTextViwAsync(sekunde + "");
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e ){
                        e.printStackTrace();
                    }
                }
                uprateTextViwAsync("BOOM");
                bStart.post(new Runnable() {
                    @Override
                    public void run() {
                        bStart.setEnabled(true);
                    }
                });
                bStart.setEnabled(true);
            }
        }).start();
    }

    private void uprateTextViwAsync(final String text) {
        tvText.post(new Runnable() {
            @Override
            public void run() {
                tvText.setText(text);
            }
        });
    }

    private class MojAsyncTask extends AsyncTask<Integer, Integer, Void> {

        public MojAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            bStart.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            bStart.setEnabled(true);
            uprateTextViwAsync("BOOM");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            tvText.setText(values[0] + "");
        }

        @Override
        protected Void doInBackground(Integer... integers) {

            int sekunde = integers[0];

            do {
                sekunde = smer? sekunde + 1 : sekunde - 1;
                publishProgress(sekunde);
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e ){
                    e.printStackTrace();
                }
            }while(sekunde > 0);

            return null;
        }
    }
}