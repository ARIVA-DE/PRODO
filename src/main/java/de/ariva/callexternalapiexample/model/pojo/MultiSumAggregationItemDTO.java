package de.ariva.callexternalapiexample.model.pojo;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultiSumAggregationItemDTO {
   private UUID id;

   private String name;

   private Integer clickCount;

   private Integer viewCount;

   private Double ctr;
}
