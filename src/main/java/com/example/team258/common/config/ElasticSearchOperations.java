package com.example.team258.common.config;

import org.springframework.data.elasticsearch.core.DocumentOperations;
import org.springframework.data.elasticsearch.core.SearchOperations;

public interface ElasticSearchOperations extends DocumentOperations, SearchOperations {
    //...
}