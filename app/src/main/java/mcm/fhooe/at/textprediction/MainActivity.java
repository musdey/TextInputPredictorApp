package mcm.fhooe.at.textprediction;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button b1, b2 ,b3;
    String s1, s2, s3;
    EditText editText;
    HashMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(this);
        editText =(EditText)findViewById(R.id.editText2);
        editText.addTextChangedListener(this);

        final CorpusParser parser = new CorpusParser(this);

        final Handler handler = new Handler();
        Thread thread = new Thread() {
            @Override
            public void run() {
              map = parser.loadData();
            }
        };
        thread.run();
    }

    @Override
    public void onClick(View view) {
        Button b = (Button) view;
        if(b.getText().length()==0){
            return;
        }
        switch (view.getId()){
            case R.id.button1:
                editText.append(b1.getText());
                break;
            case R.id.button2:
                editText.append(b2.getText());
                break;
            case R.id.button3:
                editText.append(b3.getText());
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length()>0){

            String s = charSequence.toString();
            char last = charSequence.charAt(charSequence.length()-1);
            String[] arr = s.split("[\\W]");
            //String[] arr = s.split(" ");
            String actual = arr[arr.length-1];
            Log.i("CHAR", s);
            Log.i("LAST", last+".");
            if(last == ' '){

                Log.i("IMPORTANT","calld");
                HashMap actualmap =(HashMap) map.get(actual);
                ValueComparator bvc = new ValueComparator(actualmap);
                TreeMap<String, Integer> sm = new TreeMap<String, Integer>(bvc);


                //TODO:check somehow why the treemap is empty
                b2.setText(sm.firstKey());
                sm.remove(sm.firstKey());
                b1.setText(sm.firstKey());
                sm.remove(sm.firstKey());
                b3.setText(sm.firstKey());
                sm.remove(sm.firstKey());

            }


        }


        /*if(actual.length()>=1){
            String c =  String.valueOf(actual.charAt(0));
            ArrayList<String> filtered = new ArrayList<String>();
        }*/
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    private class ValueComparator implements Comparator<String> {
        Map<String, Integer> base;

        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(String a, String b) {
            if (base.get(a) <= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
