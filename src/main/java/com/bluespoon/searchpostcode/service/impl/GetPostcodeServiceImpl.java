package com.bluespoon.searchpostcode.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bluespoon.searchpostcode.dto.PostTableSearch;
import com.bluespoon.searchpostcode.entity.PostcodeTable;
import com.bluespoon.searchpostcode.model.PostcodeAddress;
import com.bluespoon.searchpostcode.repository.PostcodeTableRepositoryMapper;
import com.bluespoon.searchpostcode.service.GetPostcodeService;

@Service
public class GetPostcodeServiceImpl implements GetPostcodeService{

    private final PostcodeTableRepositoryMapper postcodeTableRepositoryMapper;

    public GetPostcodeServiceImpl(PostcodeTableRepositoryMapper postcodeTableRepositoryMapper){
        this.postcodeTableRepositoryMapper = postcodeTableRepositoryMapper;
    }


    public List<PostcodeAddress> searchPostcode(String postcode){
        List<PostcodeAddress> paList = new ArrayList<>();
        List<PostcodeTable> ptList;

        PostTableSearch pts = new PostTableSearch(postcode);

        ptList = postcodeTableRepositoryMapper.searchByPostcode(pts);
        ptList.forEach(pt -> {
            paList.add(new PostcodeAddress(pt.getKanaPrefecture(), pt.getKanaCity(), pt.getKanaStreet(), pt.getKanjiPrefecture(), pt.getKanjiCity(), pt.getKanjiStreet()));
        });

        return paList;
    }
}
