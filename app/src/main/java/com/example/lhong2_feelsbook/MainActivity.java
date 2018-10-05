package com.example.lhong2_feelsbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//main class, initialize the first activity
public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "record.txt";

    private TextView numLove;
    private TextView numJoy;
    private TextView numSuprise;
    private TextView numAnger;
    private TextView numSadness;
    private TextView numFear;
    private EditText Comment;
    private Button BLove;
    private Button BJoy;
    private Button BSuprise;
    private Button BAnger;
    private Button BSadness;
    private Button BFear;
    private Button History;
    ArrayList<String> alist = new ArrayList<String>();

    int line;
    int counterLove = 0;
    int counterJoy = 0;
    int counterSuprise = 0;
    int counterAnger = 0;
    int counterSadness = 0;
    int counterFear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Comment = (EditText) findViewById(R.id.comment);
        BLove = (Button) findViewById(R.id.love);
        BJoy = (Button) findViewById(R.id.joy);
        BSuprise = (Button) findViewById(R.id.suprise);
        BAnger = (Button) findViewById(R.id.anger);
        BSadness = (Button) findViewById(R.id.sadness);
        BFear = (Button) findViewById(R.id.fear);
        numLove = (TextView) findViewById(R.id.countLove);
        numJoy = (TextView) findViewById(R.id.countJoy);
        numSuprise = (TextView) findViewById(R.id.countSuprise);
        numAnger = (TextView) findViewById(R.id.countAnger);
        numSadness = (TextView) findViewById(R.id.countSadness);
        numFear = (TextView) findViewById(R.id.countFear);
        History = (Button) findViewById(R.id.history);

        History.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openHistory();
            }
        });

        //initilize counters
        alist  = load(FILE_NAME);
        counterLove = 0;
        counterJoy = 0;
        counterSuprise = 0;
        counterAnger = 0;
        counterSadness = 0;
        counterFear = 0;

        for (int i=0; i<alist.size();i++) {
            if (alist.get(i).contains("|Love|")) {
                counterLove++;
            }
            else if (alist.get(i).contains("|Joy|")) {
                counterJoy++;
            }
            else if (alist.get(i).contains("|Suprise|")) {
                counterSuprise++;
            }
            else if (alist.get(i).contains("|Anger|")) {
                counterAnger++;
            }
            else if (alist.get(i).contains("|Sadness|")) {
                counterSadness++;
            }
            else if (alist.get(i).contains("|Fear|")) {
                counterFear++;
            }
        }
        numLove.setText(Integer.toString(counterLove));
        numJoy.setText(Integer.toString(counterJoy));
        numSuprise.setText(Integer.toString(counterSuprise));
        numAnger.setText(Integer.toString(counterAnger));
        numSadness.setText(Integer.toString(counterSadness));
        numFear.setText(Integer.toString(counterFear));

        //update counters when click buttons
        BLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(view, "Love");
                loveCounter(view);
            }
        });

        BJoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(view,"Joy");
                joyCounter(view);
            }
        });

        BSuprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(view,"Suprise");
                supriseCounter(view);
            }
        });

        BAnger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(view,"Anger");
                angerCounter(view);
            }
        });

        BSadness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(view,"Sadness");
                sadnessCounter(view);
            }
        });

        BFear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(view,"Fear");
                fearCounter(view);
            }
        });
    }

    //jump to the second activity
    public void openHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    //for saving comments
    public void saveText(View view, String emotion) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.CANADA);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String text = Comment.getText().append(" |"+emotion+"| "+" At "+simpleDateFormat.format(new Date())+"]").toString();
        text = "["+text+'\n';
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_APPEND);
            fos.write(text.getBytes());
            fos.close();
            Comment.getText().clear();
            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //load file, return an arrayList with each line as an item
    private ArrayList<String> load(String input) {
        File file = new File(input);
        StringBuilder text = new StringBuilder();
        ArrayList<String> array = new ArrayList<String>();
        FileInputStream fis = null;
        try {

            fis = getApplicationContext().openFileInput(input);

            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;

            while ((line=br.readLine())!=null) {
                array.add(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    //counter for each emotion
    public void loveCounter (View view) {
        counterLove++;
        numLove.setText(Integer.toString(counterLove));
    }

    public void joyCounter (View view) {
        counterJoy++;
        numJoy.setText(Integer.toString(counterJoy));
    }

    public void supriseCounter (View view) {
        counterSuprise++;
        numSuprise.setText(Integer.toString(counterSuprise));
    }

    public void angerCounter (View view) {
        counterAnger++;
        numAnger.setText(Integer.toString(counterAnger));
    }

    public void sadnessCounter (View view) {
        counterSadness++;
        numSadness.setText(Integer.toString(counterSadness));
    }

    public void fearCounter (View view) {
        counterFear++;
        numFear.setText(Integer.toString(counterFear));
    }


}
