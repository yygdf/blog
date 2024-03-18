package com.iksling.blog.mapper;

import com.iksling.blog.dto.ArticlesSearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticsearchMapper extends ElasticsearchRepository<ArticlesSearchDTO, Integer> {
}
