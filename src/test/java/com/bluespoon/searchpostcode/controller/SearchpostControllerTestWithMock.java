package com.bluespoon.searchpostcode.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bluespoon.searchpostcode.aspect.ExceptionControllerAdvice;
import com.bluespoon.searchpostcode.controller.custom.CustomizedDispatcherServletCustomizer;
import com.bluespoon.searchpostcode.model.GeneralErrorResponse;
import com.bluespoon.searchpostcode.model.GetPostcodeResponse;
import com.bluespoon.searchpostcode.model.PostcodeAddress;
import com.bluespoon.searchpostcode.service.impl.GetPostcodeServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
//@SpringBootTest
//不要。設定すると、MockMvcのインジェクションができず失敗する。
//設定せず、Contrllerを@InjectMocksで指定すれば動作した。
//どうやら、MockitoでMockオブジェクトを作るとき、SpringのDIは使用しないらしい。
//本来は@RunWithでインジェクションするクラスの指定が必要なようだが、openMocks()でそれがされているため不要。
//https://stackoverflow.com/questions/66388363/mockito-injecting-mocks-spring-boot-test
//https://dawaan.com/mockbean-vs-mock/
public class SearchpostControllerTestWithMock {
    //なぜかフィールドインジェクションでなければ動作しなかった。
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private SearchpostController searchpostController;

    @Mock
    private GetPostcodeServiceImpl getPostcodeServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(searchpostController)
           .setControllerAdvice(new ExceptionControllerAdvice())
           .addDispatcherServletCustomizer(new CustomizedDispatcherServletCustomizer())
           .build();
    }

    /**
     * Test_S0001
     * レスポンス1件
     * @param postcode
     * @throws Exception
     */
    @Test
    public void Test_S0001() throws Exception{
        final ObjectMapper om = new ObjectMapper();
        List<PostcodeAddress> paList = new ArrayList<>();

        String answer = "[ { \"kanaPrefecture\": \"ﾄｳｷｮｳﾄ\", \"kanaCity\": \"ﾀｲﾄｳｸ\", \"kanaStreet\": \"ｲｶﾆｹｲｻｲｶﾞﾅｲﾊﾞｱｲ\", \"kanjiPrefecture\": \"東京都\", \"kanjiCity\": \"台東区\", \"kanjiStreet\": \"以下に掲載がない場合\" } ]";
        paList = om.readValue(answer, new TypeReference<List<PostcodeAddress>>(){});
       
        when(getPostcodeServiceImpl.searchPostcode("1234567")).thenReturn(paList);

        String res = this.mockMvc.perform(get("/postcode/1234567"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        GetPostcodeResponse gpr;
        gpr = om.readValue(res, GetPostcodeResponse.class);

        assertThat(1).isEqualTo(gpr.getResult().size());
        assertThat("ﾄｳｷｮｳﾄ").isEqualTo(gpr.getResult().get(0).getKanaPrefecture());
        assertThat("ﾀｲﾄｳｸ").isEqualTo(gpr.getResult().get(0).getKanaCity());
        assertThat("ｲｶﾆｹｲｻｲｶﾞﾅｲﾊﾞｱｲ").isEqualTo(gpr.getResult().get(0).getKanaStreet());
        assertThat("東京都").isEqualTo(gpr.getResult().get(0).getKanjiPrefecture());
        assertThat("台東区").isEqualTo(gpr.getResult().get(0).getKanjiCity());
        assertThat("以下に掲載がない場合").isEqualTo(gpr.getResult().get(0).getKanjiStreet());
    }

    /**
     * Test_S0002
     * レスポンス2件
     * @param postcode
     * @throws Exception
     */
    @Test
    public void Test_S0002() throws Exception{
        final ObjectMapper om = new ObjectMapper();
        List<PostcodeAddress> paList = new ArrayList<>();

        String answer = "[ { \"kanaPrefecture\": \"ｱｲﾁｹﾝ\", \"kanaCity\": \"ﾔﾄﾐｼ\", \"kanaStreet\": \"ｲｶﾆｹｲｻｲｶﾞﾅｲﾊﾞｱｲ\", \"kanjiPrefecture\": \"愛知県\", \"kanjiCity\": \"弥富市\", \"kanjiStreet\": \"以下に掲載がない場合\" }, { \"kanaPrefecture\": \"ﾐｴｹﾝ\", \"kanaCity\": \"ｸﾜﾅｸﾞﾝｷｿｻｷﾁｮｳ\", \"kanaStreet\": \"ｲｶﾆｹｲｻｲｶﾞﾅｲﾊﾞｱｲ\", \"kanjiPrefecture\": \"三重県\", \"kanjiCity\": \"桑名郡木曽岬町\", \"kanjiStreet\": \"以下に掲載がない場合\" } ]";
        paList = om.readValue(answer, new TypeReference<List<PostcodeAddress>>(){});
       
        when(getPostcodeServiceImpl.searchPostcode("1234567")).thenReturn(paList);

        String res = this.mockMvc.perform(get("/postcode/1234567"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        GetPostcodeResponse gpr;
        gpr = om.readValue(res, GetPostcodeResponse.class);

        assertThat(2).isEqualTo(gpr.getResult().size());
        //1
        assertThat("ｱｲﾁｹﾝ").isEqualTo(gpr.getResult().get(0).getKanaPrefecture());
        assertThat("ﾔﾄﾐｼ").isEqualTo(gpr.getResult().get(0).getKanaCity());
        assertThat("ｲｶﾆｹｲｻｲｶﾞﾅｲﾊﾞｱｲ").isEqualTo(gpr.getResult().get(0).getKanaStreet());
        assertThat("愛知県").isEqualTo(gpr.getResult().get(0).getKanjiPrefecture());
        assertThat("弥富市").isEqualTo(gpr.getResult().get(0).getKanjiCity());
        assertThat("以下に掲載がない場合").isEqualTo(gpr.getResult().get(0).getKanjiStreet());
        //2
        assertThat("ﾐｴｹﾝ").isEqualTo(gpr.getResult().get(1).getKanaPrefecture());
        assertThat("ｸﾜﾅｸﾞﾝｷｿｻｷﾁｮｳ").isEqualTo(gpr.getResult().get(1).getKanaCity());
        assertThat("ｲｶﾆｹｲｻｲｶﾞﾅｲﾊﾞｱｲ").isEqualTo(gpr.getResult().get(1).getKanaStreet());
        assertThat("三重県").isEqualTo(gpr.getResult().get(1).getKanjiPrefecture());
        assertThat("桑名郡木曽岬町").isEqualTo(gpr.getResult().get(1).getKanjiCity());
        assertThat("以下に掲載がない場合").isEqualTo(gpr.getResult().get(1).getKanjiStreet());
    }

    /**
     * Test_S0003
     * レスポンス0件
     * @param postcode
     * @throws Exception
     */
    @Test
    public void Test_S0003() throws Exception{
        final ObjectMapper om = new ObjectMapper();
        List<PostcodeAddress> paList = new ArrayList<>();

        String answer = "[]";
        paList = om.readValue(answer, new TypeReference<List<PostcodeAddress>>(){});
       
        when(getPostcodeServiceImpl.searchPostcode("1234567")).thenReturn(paList);

        String res = this.mockMvc.perform(get("/postcode/1234567"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        GetPostcodeResponse gpr;
        gpr = om.readValue(res, GetPostcodeResponse.class);

        assertThat(0).isEqualTo(gpr.getResult().size());
    }

    /**
     * Test_E0001
     * getPostcodeServiceImplで例外発生
     * @param postcode
     * @throws Exception
     */
    @Test
    public void Test_E0001() throws Exception{
        final ObjectMapper om = new ObjectMapper();
        doThrow(new NullPointerException()).when(getPostcodeServiceImpl).searchPostcode("1234567");

        String res = this.mockMvc.perform(get("/postcode/1234567"))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        GeneralErrorResponse ger;
        ger = om.readValue(res, GeneralErrorResponse.class);

        assertThat(ger.getType()).isNull();
        assertThat(ger.getTitle()).isNull();
        assertThat(ger.getDetails()).isNull();
    }

}
