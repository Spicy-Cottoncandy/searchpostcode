package com.bluespoon.searchpostcode.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostcodeAddress {
    private String kanaPrefecture;
    private String kanaCity;
    private String kanaStreet;
    private String kanjiPrefecture;
    private String kanjiCity;
    private String kanjiStreet;

}
