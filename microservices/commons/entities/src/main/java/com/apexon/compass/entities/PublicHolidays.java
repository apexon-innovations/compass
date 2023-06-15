package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.PUBLIC_HOLIDAYS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = PUBLIC_HOLIDAYS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicHolidays {

    @Id
    private String id;

    private String location;

    private String year;

    private Integer holidayId;

    private String description;

    private Long date;

    private Boolean isRecurring;

    private String length;

    private String day;

}
