package mcm.fhooe.at.textprediction;

import android.app.Activity;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Musdey on 12.04.2017.
 */

public class CorpusParser{

    Activity activity = null;
    private static final String ns = null;
    List<String> alltext = new ArrayList<>();
    ArrayList<String> wordList = new ArrayList<String>();
    InputStream is;
    HashMap map = new HashMap<String,HashMap>();

    CorpusParser(Activity ac) {
        activity = ac;
        is = activity.getResources().openRawResource(R.raw.smscorpus);
    }

    public HashMap loadData() {
        try {
            alltext = parse(is);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s : alltext) {
            String[] parts = s.split("[\\W]"); //at the moment everyhting that is not a character will be used as delimiter
            for (int i = 0; i < parts.length; i++) {
                if(i == parts.length -1){// do nothing when last element is reached

                }else{ // alle anderen elemente
                    if(map.get(parts[i])!=null){ //wort is scho drinnen
                        HashMap inner = (HashMap) map.get(parts[i]);
                        if (inner.get(parts[i + 1]) != null){ //nächster is ah scho drinnen
                            inner.put(parts[i+1],((int)inner.get(parts[i+1]))+1);
                        }else{
                            inner.put(parts[i+1],1);
                        }
                       // ((HashMap)map.get(parts[i])).put(parts[i+1],null);
                    }else{
                            HashMap inner = new HashMap<String, Integer>();
                            inner.put(parts[i+1],1);
                            map.put(parts[i],inner);
                    }
                }
            }
        }
        return map;
    }

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<String> sentences = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "smsCorpus");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            //Log.i("PARSER1",name);
            // Starts by looking for the entry tag
            if (name.equals("message")) {
                //  Log.i("PARSER1","name equals message is true");
                sentences.add(readEntry(parser));
            } else {
                skip(parser);
                //  Log.i("PARSER1","skip called");
            }
        }
        return sentences;
    }

    private String readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        String text = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("text")) {
                text = readText(parser);
            } else {
                skip(parser);
            }
        }
        return text;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
