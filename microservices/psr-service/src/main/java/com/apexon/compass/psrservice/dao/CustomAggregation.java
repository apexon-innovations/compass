package com.apexon.compass.psrservice.dao;

import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

@AllArgsConstructor
public class CustomAggregation implements AggregationOperation {

    private String aggregationOperations;

    @Override
    public Document toDocument(AggregationOperationContext aggregationContext) {
        return aggregationContext.getMappedObject(Document.parse(aggregationOperations));
    }

}
