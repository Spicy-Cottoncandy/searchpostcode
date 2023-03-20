package com.bluespoon.searchpostcode.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bluespoon.searchpostcode.entity.PostcodeTable;
import com.bluespoon.searchpostcode.model.PostcodeAddress;
import com.bluespoon.searchpostcode.repository.PostcodeTableRepositoryMapper;
import com.bluespoon.searchpostcode.service.impl.GetPostcodeServiceImpl;

public class GetPostcodeServiceImplTest {
    
    @InjectMocks
    private GetPostcodeServiceImpl getPostcodeServiceImpl;

    @Mock
    private PostcodeTableRepositoryMapper postcodeTableRepositoryMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1234567, ｶﾅﾄﾄﾞｳﾌｹﾝ, ｶﾅｼｸﾁｮｳｿﾝ, ｶﾅﾄｵﾘ, 漢字都道府県, 漢字市区町村, 漢字通り",
        "2,,,,,,,"
    })
    public void Test_S0001(int id, String postcode, String kanaPrefecture, String kanaCity, String kanaStreet, String kanjiPrefecture, String kanjiCity, String kanjiStreet){
        List<PostcodeTable> ptList = new ArrayList<>();
        PostcodeTable pt = new PostcodeTable();
        pt.setId(id);
        pt.setPostcode(postcode);
        pt.setKanaPrefecture(kanaPrefecture);
        pt.setKanaCity(kanaCity);
        pt.setKanaStreet(kanaStreet);
        pt.setKanjiPrefecture(kanjiPrefecture);
        pt.setKanjiCity(kanjiCity);
        pt.setKanjiStreet(kanjiStreet);
        ptList.add(pt);

        when(postcodeTableRepositoryMapper.searchByPostcode(any())).thenReturn(ptList);

        List<PostcodeAddress> paList = new ArrayList<>();
        paList = getPostcodeServiceImpl.searchPostcode("1234567");

        assertThat(paList).asList();
        assertThat(1).isEqualTo(paList.size());
        //1
        assertThat(kanaPrefecture).isEqualTo(paList.get(0).getKanaPrefecture());
        assertThat(kanaCity).isEqualTo(paList.get(0).getKanaCity());
        assertThat(kanaStreet).isEqualTo(paList.get(0).getKanaStreet());
        assertThat(kanjiPrefecture).isEqualTo(paList.get(0).getKanjiPrefecture());
        assertThat(kanjiCity).isEqualTo(paList.get(0).getKanjiCity());
        assertThat(kanjiStreet).isEqualTo(paList.get(0).getKanjiStreet());

    }
}
