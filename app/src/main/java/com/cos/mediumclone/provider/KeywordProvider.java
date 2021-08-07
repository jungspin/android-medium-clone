package com.cos.mediumclone.provider;

import com.cos.mediumclone.model.Keyword;

import java.util.ArrayList;
import java.util.List;

public class KeywordProvider {


    public List<Keyword> findAll(){
        List<Keyword> keywords = new ArrayList<>();
        for (int i=1; i<21; i++){
            keywords.add(new Keyword("keyword"+i));
        }
        return keywords;
    }
}
