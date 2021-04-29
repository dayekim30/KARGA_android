package com.ops.text1;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsProvider;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

public class MainActivity extends AppCompatActivity implements ComponentCallbacks2 {
    EditText et_id,et_stdout;
    Button btn_test,btn_save,btn_load;
    TextView txt_path;
    Intent myFileIntent;
    String filename ="";
    String filepath = "";
    String fileContent ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        et_id = findViewById(R.id.et_id);
        et_stdout = findViewById(R.id.et_stdout);
        btn_test = findViewById(R.id.btn_test);
        txt_path = findViewById(R.id.txt_path);
//        btn_save = findViewById(R.id.btn_save);
        btn_load = findViewById(R.id.btn_load);
       // tvLoad = findViewById(R.id.tvLoad);
        filename = "myFile.txt";
        filepath = "myFileDir";

        Runtime rt = Runtime.getRuntime();
        Long maxMemory = rt.maxMemory();
        Log.v("onCreate", "maxMemory:" + Long.toString(maxMemory));

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        Log.v("onCreate:","memoryClass: "+Integer.toString(memoryClass));

        System.setOut(new PrintStream(new OutputStream() {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            @Override public void write(int oneByte) throws IOException {
                outputStream.write(oneByte);
                runOnUiThread(new Runnable() {
                    public void run() {
                        et_stdout.setText(new String(outputStream.toByteArray()));
                    }
                });

            }
        }));


        if(!isExternalStorageAvailableForRW()){
            btn_save.setEnabled(false);
        }

        btn_test.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent,10);
            }
        });

//        btn_save.setOnClickListener(new View.OnClickListener(){
//
//            public void onClick(View view){
//                tvLoad.setText("");
//                fileContent = et_id.getText().toString().trim();
//                FileOutputStream fos = null;
//                try {
//                    fos = openFileOutput(filename, MODE_PRIVATE);
//                    fos.write(fileContent.getBytes());
//                    et_id.setText("saved tool "+getFilesDir()+"/"+filename);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally{
//                    if(fos != null){
//                        try {
//                            fos.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
////                if(!fileContent.equals("")) {
////                    File myExternalFile = new File(getExternalFilesDir(filepath), filename);
////                    FileOutputStream fos = null;
////                    try {
////                        fos = new FileOutputStream(myExternalFile);
////                        fos.write(fileContent.getBytes());
////                    } catch (FileNotFoundException e) {
////                        e.printStackTrace();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                    et_id.setText("");
////                    Toast.makeText(MainActivity.this, "Information saved to SD card", Toast.LENGTH_SHORT).show();
////                }else{
////                    Toast.makeText(MainActivity.this,"text cannot be empty",Toast.LENGTH_LONG).show();
////                }
//            }
//        });
        btn_load.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {



                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        String he = et_id.getText().toString();
                        String[] the = he.split(" ");

                try {

                    KARGA_ReadMapper.main(the);

                    // KARGA_ResistomeMapper.main(the);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                    }
                });
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        et_id.setText("");
//                    }
//                });

//                FileReader fr = null;
//                File myExternalFile = new File(getExternalFilesDir(filepath),"megares_full_database_v2.00.fasta");
//                String fpath = txt_path.getText().toString();
//                DocumentsProvider.
//                StringBuilder st = new StringBuilder();
//                try {
//                    fr = new FileReader(myExternalFile);
//                    BufferedReader br = new BufferedReader(fr);
//                    String line = br.readLine();
//                    int i = 0;
//                    while(line != null && i<10 ){
//                        st.append(line).append('\n');
//                        line = br.readLine();
//                        i++;
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally {
//                    String fileContents = "File contents\n"+st.toString();
//                    tvLoad.setText(fileContents);
//                }

//            String path = getExternalFilesDir(filepath).getPath();
//            tvLoad.setText(path);
//            String[] t = {path, "example.fastq"};
//                try {
//                    KARGA_ReadMapper.main(t);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }




        });
    }
    private boolean isExternalStorageAvailableForRW() {
        String extStroageState = Environment.getExternalStorageState();
        if(extStroageState.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }
    public void Uriget(String filepath){
        String path = filepath;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       switch(requestCode){
           case 10:
               if(resultCode == RESULT_OK){
                   String path = data.getData().getPath();
                   String newPath = path.substring(path.lastIndexOf("/")+1);
                   txt_path.setText(path +" and  ");
                   Uri uri = data.getData();
                   BufferedReader br;
                   FileOutputStream os;
                   try {
                       br = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
                       os = openFileOutput(newPath, Context.MODE_PRIVATE);
                       String line = null;
                       while ((line = br.readLine()) != null) {
                           os.write(line.getBytes());
                       }
                       System.out.println(getFilesDir().toString()+"and here file director");
                       os.close();
                       br.close();

                   } catch (FileNotFoundException e) {
                       e.printStackTrace();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }
               break;
       }
    }
    public void onTrimMemory(int level) {

        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:

                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }
}