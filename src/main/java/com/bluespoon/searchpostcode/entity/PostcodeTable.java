package com.bluespoon.searchpostcode.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostcodeTable {
    int id;
    String postcode;
    String kanaPrefecture;
    String kanaCity;
    String kanaStreet;
    String kanjiPrefecture;
    String kanjiCity;
    String kanjiStreet;

}
