package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.COLLECTOR_RUN_HISTORY;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = COLLECTOR_RUN_HISTORY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectorRunHistory extends AbstractDocument {

    private ObjectId iscProjectId;

    private SourceType collector;

    private CollectorStatus status;

    private CollectorAuthor author;

    private String error;

}
