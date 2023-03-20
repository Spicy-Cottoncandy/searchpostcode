package com.bluespoon.searchpostcode.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bluespoon.searchpostcode.aspect.ExceptionControllerAdvice;
import com.bluespoon.searchpostcode.controller.custom.CustomizedDispatcherServletCustomizer;
import com.bluespoon.searchpostcode.model.GeneralErrorResponse;
import com.bluespoon.searchpostcode.model.GetPostcodeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
public class SearchpostControllerTest {

    //なぜかフィールドインジェクションでなければ動作しなかった。
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SearchpostController searchpostController;

    @BeforeEach
    public void setUp() {
       this.mockMvc = MockMvcBuilders.standaloneSetup(searchpostController)
           .setControllerAdvice(new ExceptionControllerAdvice())
           .addDispatcherServletCustomizer(new CustomizedDispatcherServletCustomizer())
           .build();
    }

    /**
     * Test_S0000
     * 単純な応答確認
     * @throws Exception
     */
    @Test
    public void Test_S0000() throws Exception{
        this.mockMvc.perform(get("/postcode/echo"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    /**
     * Test_S0001
     * レスポンス1件
     * @param postcode
     * @throws Exception
     */
    @ParameterizedTest
    @ValueSource(strings = {"1100000"})
    public void Test_S0001(String postcode) throws Exception{
        final ObjectMapper om = new ObjectMapper();
        GetPostcodeResponse gpr;
        String res = this.mockMvc.perform(get("/postcode/" + postcode))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        
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
    @ParameterizedTest
    @ValueSource(strings = {"4980000"})
    public void Test_S0002(String postcode) throws Exception{
        final ObjectMapper om = new ObjectMapper();
        GetPostcodeResponse gpr;
        String res = this.mockMvc.perform(get("/postcode/" + postcode))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        
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
     * Test_E0001
     * 404 存在しないパス
     * DispatcherServletCustomizerを実装して、NoHandlerFoundExceptionを発生させる必要あり。
     * https://zenn.dev/yoshio/articles/f842d537bf0074a28aed
     *
     * @param postcode
     * @throws Exception
     */
    @ParameterizedTest
    @ValueSource(strings = {""})
    public void Test_E0001(String postcode) throws Exception{
        final ObjectMapper om = new ObjectMapper();
        GeneralErrorResponse ger;
        String res = this.mockMvc.perform(get("/postcode/" + postcode))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ger = om.readValue(res, GeneralErrorResponse.class);
        assertThat(ger.getType()).isNull();
        assertThat(ger.getTitle()).isNull();
        assertThat(ger.getDetails()).isNull();
    }

    /**
     * Test_E0002
     * 400 バリデーション
     *
     * @param postcode
     * @throws Exception
     */
    @ParameterizedTest
    @ValueSource(strings = {"1", "12345678", "abcdefg", "123-4567", "%a"})
    public void Test_E0002(String postcode) throws Exception{
        final ObjectMapper om = new ObjectMapper();
        GeneralErrorResponse ger;
        String res = this.mockMvc.perform(get("/postcode/" + postcode))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

        ger = om.readValue(res, GeneralErrorResponse.class);
        assertThat(ger.getType()).isNull();
        assertThat(ger.getTitle()).isNull();
//        assertThat("arg0").isEqualTo(gpr.getDetails().get(0).getField());
        assertThat("must match \"[0-9]{7}\"").isEqualTo(ger.getDetails().get(0).getMessage());

    }
}
