package com.bluespoon.searchpostcode.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralErrorResponse {
    String type;
    String title;
    List<GeneralErrorResponseDetail> details;

}
