package mcm.fhooe.at.textprediction;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Musdey on 12.04.2017.
 */

public class Word{

    String word = "";
    int count = 0;
    ArrayList<Word> followingWords = new ArrayList<>();

    public Word(String s){
        followingWords = new ArrayList<>();
        word = s;
    }

    public Word(){
        followingWords = new ArrayList<>();
    }

    public Word(String actual, String following){
        word = actual;
        followingWords = new ArrayList<>();
        followingWords.add(new Word(following));
    }



}
