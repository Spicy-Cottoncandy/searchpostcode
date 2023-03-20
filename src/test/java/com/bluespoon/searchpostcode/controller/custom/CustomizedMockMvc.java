//コードを書いたが意味がなかったのでコメント。
//https://github.com/Msksgm/kotlin-spring-mockmvc-with-databaserider/blob/main/src/test/kotlin/com/example/kotlinspringmockmvcwithdatabaserider/apiintegration/helper/CustomizedMockMvc.kt
//https://stackoverflow.com/questions/58525387/mockmvc-no-longer-handles-utf-8-characters-with-spring-boot-2-2-0-release
/*
package com.bluespoon.searchpostcode.controller.custom;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
//import java.nio.charset.StandardCharsets;

@Component
public class CustomizedMockMvc implements MockMvcBuilderCustomizer {
    
    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder){
        builder.alwaysDo(result -> result.getResponse().setCharacterEncoding("UTF8"));
    }

}
*/