package virgil.dailycost.page;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import virgil.dailycost.R;
import virgil.dailycost.models.MonthDay;

// NOT IN USE

public class DailyCost extends AppCompatActivity {

    private int DAY;
    private View mButton_back;
    private String FILENAME;
    final private String SEPERATOR = "___";

    private EditText mEditText_subject1;
    private EditText mEditText_subject2;
    private EditText mEditText_subject3;
    private EditText mEditText_subject4;
    private EditText mEditText_subject5;
    private EditText mEditText_subject6;
    private EditText mEditText_subject7;
    private EditText mEditText_subject8;
    private EditText mEditText_subject9;
    private EditText mEditText_subject10;
    private EditText mEditText_spend1;
    private EditText mEditText_spend2;
    private EditText mEditText_spend3;
    private EditText mEditText_spend4;
    private EditText mEditText_spend5;
    private EditText mEditText_spend6;
    private EditText mEditText_spend7;
    private EditText mEditText_spend8;
    private EditText mEditText_spend9;
    private EditText mEditText_spend10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_cost);


        mEditText_subject1 = (EditText) findViewById(R.id.daily_cost_edittext_subject1);
        mEditText_subject2 = (EditText) findViewById(R.id.daily_cost_edittext_subject2);
        mEditText_subject3 = (EditText) findViewById(R.id.daily_cost_edittext_subject3);
        mEditText_subject4 = (EditText) findViewById(R.id.daily_cost_edittext_subject4);
        mEditText_subject5 = (EditText) findViewById(R.id.daily_cost_edittext_subject5);
        mEditText_subject6 = (EditText) findViewById(R.id.daily_cost_edittext_subject6);
        mEditText_subject7 = (EditText) findViewById(R.id.daily_cost_edittext_subject7);
        mEditText_subject8 = (EditText) findViewById(R.id.daily_cost_edittext_subject8);
        mEditText_subject9 = (EditText) findViewById(R.id.daily_cost_edittext_subject9);
        mEditText_subject10 = (EditText) findViewById(R.id.daily_cost_edittext_subject10);
        mEditText_spend1 = (EditText) findViewById(R.id.daily_cost_edittext_spend1);
        mEditText_spend2 = (EditText) findViewById(R.id.daily_cost_edittext_spend2);
        mEditText_spend3 = (EditText) findViewById(R.id.daily_cost_edittext_spend3);
        mEditText_spend4 = (EditText) findViewById(R.id.daily_cost_edittext_spend4);
        mEditText_spend5 = (EditText) findViewById(R.id.daily_cost_edittext_spend5);
        mEditText_spend6 = (EditText) findViewById(R.id.daily_cost_edittext_spend6);
        mEditText_spend7 = (EditText) findViewById(R.id.daily_cost_edittext_spend7);
        mEditText_spend8 = (EditText) findViewById(R.id.daily_cost_edittext_spend8);
        mEditText_spend9 = (EditText) findViewById(R.id.daily_cost_edittext_spend9);
        mEditText_spend10 = (EditText) findViewById(R.id.daily_cost_edittext_spend10);


        if (getIntent() !=null && getIntent().hasExtra(Intent.EXTRA_TEXT)){
            FILENAME = getIntent().getStringExtra(Intent.EXTRA_TEXT).toString();
            DAY = Integer.valueOf(getIntent().getStringExtra(Intent.EXTRA_TITLE).toString());
        }
        String[] fileName = FILENAME.split("_");
        setTitle(DAY + "-" + fileName[1] + "-" + fileName[2].substring(0,4));
        mButton_back = (View) findViewById(R.id.daily_cost_button_back);
        mButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFile();
                String[] partOfFileName = FILENAME.split("_");
                String fileName = FILENAME;
                Intent intent = new Intent(DailyCost.this,DayList.class);
                intent.putExtra(Intent.EXTRA_TITLE,partOfFileName[1]);
                intent.putExtra(Intent.EXTRA_TEXT,fileName);
                startActivity(intent);
                finish();
            }
        });

        readFile();


    }

    private void readFile(){
        try {
            InputStream inputStream = openFileInput(FILENAME);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String newLine = null;
                while ((newLine = bufferedReader.readLine()) != null ) {
                    String[] part = newLine.split(SEPERATOR);
                    if(Integer.valueOf(part[0]) == DAY){
                        if(!part[1].equals("000")) {mEditText_subject1.setText(part[1]);}
                        if(!part[2].equals("000")) {mEditText_spend1.setText(part[2]);}
                        if(!part[3].equals("000")) {mEditText_subject2.setText(part[3]);}
                        if(!part[4].equals("000")) {mEditText_spend2.setText(part[4]);}
                        if(!part[5].equals("000")) {mEditText_subject3.setText(part[5]);}
                        if(!part[6].equals("000")) {mEditText_spend3.setText(part[6]);}
                        if(!part[7].equals("000")) {mEditText_subject4.setText(part[7]);}
                        if(!part[8].equals("000")) {mEditText_spend4.setText(part[8]);}
                        if(!part[9].equals("000")) {mEditText_subject5.setText(part[9]);}
                        if(!part[10].equals("000")) {mEditText_spend5.setText(part[10]);}
                        if(!part[11].equals("000")) {mEditText_subject6.setText(part[11]);}
                        if(!part[12].equals("000")) {mEditText_spend6.setText(part[12]);}
                        if(!part[13].equals("000")) {mEditText_subject7.setText(part[13]);}
                        if(!part[14].equals("000")) {mEditText_spend7.setText(part[14]);}
                        if(!part[15].equals("000")) {mEditText_subject8.setText(part[15]);}
                        if(!part[16].equals("000")) {mEditText_spend8.setText(part[16]);}
                        if(!part[17].equals("000")) {mEditText_subject9.setText(part[17]);}
                        if(!part[18].equals("000")) {mEditText_spend9.setText(part[18]);}
                        if(!part[19].equals("000")) {mEditText_subject10.setText(part[19]);}
                        if(!part[20].equals("000")) {mEditText_spend10.setText(part[20]);}

                    }
                }
            }
            inputStream.close();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void writeFile(){

        StringBuilder sb = new StringBuilder();

        try {
            InputStream inputStream = openFileInput(FILENAME);
            Integer numOfLine = 0;
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String newLine = null;
                while ((newLine = bufferedReader.readLine()) != null ) {
                    String[] part = newLine.split(SEPERATOR);
                    if(Integer.valueOf(part[0]) == DAY){
                        //DoNothing
                        numOfLine = numOfLine +1;
                    }else {
                        sb.append(newLine);
                        sb.append("\n");
                        numOfLine = numOfLine +1;
                    }
                }
            }
            inputStream.close();


            FileOutputStream fileOutputStream =
                    openFileOutput(FILENAME, Context.MODE_PRIVATE);



            sb.append(DAY);
            sb.append(SEPERATOR);

            if(!mEditText_subject1.getText().toString().equals("")){sb.append(mEditText_subject1.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend1.getText().toString().equals("")){sb.append(mEditText_spend1.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_subject2.getText().toString().equals("")){sb.append(mEditText_subject2.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend2.getText().toString().equals("")){sb.append(mEditText_spend2.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_subject3.getText().toString().equals("")){sb.append(mEditText_subject3.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend3.getText().toString().equals("")){sb.append(mEditText_spend3.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_subject4.getText().toString().equals("")){sb.append(mEditText_subject4.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend4.getText().toString().equals("")){sb.append(mEditText_spend4.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_subject5.getText().toString().equals("")){sb.append(mEditText_subject5.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend5.getText().toString().equals("")){sb.append(mEditText_spend5.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_subject6.getText().toString().equals("")){sb.append(mEditText_subject6.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend6.getText().toString().equals("")){sb.append(mEditText_spend6.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_subject7.getText().toString().equals("")){sb.append(mEditText_subject7.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend7.getText().toString().equals("")){sb.append(mEditText_spend7.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_subject8.getText().toString().equals("")){sb.append(mEditText_subject8.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend8.getText().toString().equals("")){sb.append(mEditText_spend8.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_subject9.getText().toString().equals("")){sb.append(mEditText_subject9.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend9.getText().toString().equals("")){sb.append(mEditText_spend9.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_subject10.getText().toString().equals("")){sb.append(mEditText_subject10.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);
            if(!mEditText_spend10.getText().toString().equals("")){sb.append(mEditText_spend10.getText().toString());}else{sb.append("000");}
            sb.append(SEPERATOR);

            double dayCost = 0;
            for(int i= 1; i<=10;i++){
                String[] partOfnewLine = sb.toString().split(SEPERATOR);
                dayCost = dayCost + Double.valueOf(partOfnewLine[2*i + (numOfLine-1)*22]);
            }

            sb.append(dayCost);
            sb.append(SEPERATOR);

            fileOutputStream.write(sb.toString().getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}
