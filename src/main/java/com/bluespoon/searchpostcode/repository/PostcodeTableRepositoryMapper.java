package com.bluespoon.searchpostcode.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bluespoon.searchpostcode.dto.PostTableSearch;
import com.bluespoon.searchpostcode.entity.PostcodeTable;

@Mapper
public interface PostcodeTableRepositoryMapper {
    List<PostcodeTable> searchByPostcode(PostTableSearch postTableSearch);
}
