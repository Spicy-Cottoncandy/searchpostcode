package com.bluespoon.searchpostcode.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluespoon.searchpostcode.model.GetPostcodeResponse;
import com.bluespoon.searchpostcode.model.PostcodeAddress;
import com.bluespoon.searchpostcode.service.GetPostcodeService;

import jakarta.validation.constraints.Pattern;

@RestController
@Validated
@RequestMapping("/postcode")
public class SearchpostController {

    private final GetPostcodeService getPostcodeService;

    public SearchpostController(GetPostcodeService getPostcodeService){
        this.getPostcodeService = getPostcodeService;
    }

    @GetMapping("/{postcode}")
    public ResponseEntity<GetPostcodeResponse> getPostcode(@PathVariable("postcode") @Pattern(regexp = "[0-9]{7}") String postcode){
        List<PostcodeAddress> paList = getPostcodeService.searchPostcode(postcode);

        GetPostcodeResponse gpr = new GetPostcodeResponse();
        gpr.setResult(paList);
        return new ResponseEntity<GetPostcodeResponse>(gpr, HttpStatus.OK);
    }

    @GetMapping("/echo")
    public ResponseEntity<String> getEcho(){
        return new ResponseEntity<String>("echo", HttpStatus.OK);
    }
}
