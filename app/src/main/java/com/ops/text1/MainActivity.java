package com.ops.text1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsProvider;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    EditText et_id;
    Button btn_test,btn_save,btn_load;
    TextView txt_path,tvLoad;
    Intent myFileIntent;
    String filename ="";
    String filepath = "";
    String fileContent ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        et_id = findViewById(R.id.et_id);
        btn_test = findViewById(R.id.btn_test);
        txt_path = findViewById(R.id.txt_path);
//        btn_save = findViewById(R.id.btn_save);
        btn_load = findViewById(R.id.btn_load);
        tvLoad = findViewById(R.id.tvLoad);
        filename = "myFile.txt";
        filepath = "myFileDir";
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
                 String he = et_id.getText().toString();
                 String[] the = he.split(" ");

                try {
                    KARGA_ReadMapper.main(the);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                et_id.setText("");
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
}