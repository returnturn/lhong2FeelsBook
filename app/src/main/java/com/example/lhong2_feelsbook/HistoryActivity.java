package com.example.lhong2_feelsbook;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

//class for the second activity
public class HistoryActivity extends AppCompatActivity {

    String FILE_NAME = "record.txt";
    private ListView list;
    private Button show;
    private Button edit;

    ArrayList<String> alist = new ArrayList<String>();
    String[] record_list;

    InputStream inputStreamCounter;
    BufferedReader bufferedReaderCounter;

    InputStream inputStreamLoader;
    BufferedReader bufferedReaderLoader;

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;

    @Override
    //Used code from https://www.youtube.com/watch?v=cNVAhzaLYtw here for listView and inputBox
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        ListView list = (ListView) findViewById(R.id.listv);
        //edit = (Button) findViewById(R.id.edit);

        String item;
        alist = load(FILE_NAME);
        arrayAdapter= new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,alist);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInputBox(alist.get(position),position);
            }
        });

        /*edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveText(v);
            }
        });*/


    }

    //display the inputBox
    public void showInputBox(String oldItem, final int index) {
        final Dialog dialog = new Dialog(HistoryActivity.this);
        dialog.setTitle("Input Box");
        dialog.setContentView(R.layout.input_box);
        final EditText editText=(EditText)dialog.findViewById(R.id.txtinput);
        editText.setText(oldItem);
        Button bt = (Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alist.set(index,editText.getText().toString());
                update(alist);
                arrayAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
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
                if (!(line.equals(""))) {
                    array.add(line);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update(List list) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            String string = new String();
            int ite = list.size();
            for (int i=0;i<ite;i++){
                string=string+list.get(i);
                string=string+'\n';
            }
            fos.write(string.getBytes());
            fos.close();
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
}
