package mcm.fhooe.at.textprediction;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button b1, b2, b3;
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
        editText = (EditText) findViewById(R.id.editText2);
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
        if (b.getText().length() == 0) {
            return;
        }
        switch (view.getId()) {
            case R.id.button1:
                if(b1.getText().equals(" ")){
                }else{
                    editText.append(b1.getText() + " ");
                }
                break;
            case R.id.button2:
                if(b2.getText().equals(" ")){
                }else{
                    editText.append(b2.getText() + " ");
                }
                break;
            case R.id.button3:
                if(b3.getText().equals(" ")){
                }else{
                    editText.append(b3.getText() + " ");
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int text1, int text2, int text3) {
        if (charSequence.length() > 0) {

            String s = charSequence.toString();
            char last = charSequence.charAt(charSequence.length() - 1);
            String[] arr = s.split("[\\W]");
            String actual = arr[arr.length - 1];

            if (last == ' ') {
                HashMap actualmap = (HashMap) map.get(actual);
                String[] prediction = new String[]{" ", " ", " "};

                if (actualmap != null) {
                    HashMap sorted = sortByValues(actualmap);
                    Log.i("sorted",sorted.toString());
                    Iterator iterator = sorted.entrySet().iterator();
                    Map.Entry<String, Integer> m1;

                    String w1= " ";
                    for (int i = 0; i <= 2; i++) {
                        if (iterator.hasNext()) {
                            m1 = (Map.Entry<String, Integer>) iterator.next();
                            w1 = m1.getKey();

                            while (w1.length() <= 0 && !containsAlready(prediction, w1) && iterator.hasNext()) {
                                if(iterator.hasNext()){
                                    m1 = (Map.Entry<String, Integer>) iterator.next();
                                    w1 = m1.getKey();
                                }
                            }
                        }
                        prediction[i] = w1;
                    }
                }
                b1.setText(prediction[1]);
                b2.setText(prediction[0]);
                b3.setText(prediction[2]);
            }
        }
    }

    private boolean containsAlready(String[] arr, String string) {
        for (String s : arr) {
            if (s.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

}
