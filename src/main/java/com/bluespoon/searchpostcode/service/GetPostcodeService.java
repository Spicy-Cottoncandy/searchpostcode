package com.bluespoon.searchpostcode.service;

import java.util.List;

import com.bluespoon.searchpostcode.model.PostcodeAddress;

public interface GetPostcodeService {
    List<PostcodeAddress> searchPostcode(String postcode);
}
