package com.bluespoon.searchpostcode.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPostcodeResponse {
    private List<PostcodeAddress> result;
}
