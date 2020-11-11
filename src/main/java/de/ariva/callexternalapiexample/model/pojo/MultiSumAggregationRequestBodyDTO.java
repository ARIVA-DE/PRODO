package de.ariva.callexternalapiexample.model.pojo;

import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultiSumAggregationRequestBodyDTO {

   private String language = "de";

   private Collection<UUID> publisherIds;

   private Collection<UUID> advertiserIds;

   private Collection<UUID> adSlotIds;

   private Collection<UUID> bouquetIds;

   private Collection<Device> devices;

   private Collection<Region> regions;

   private LocalDate startDate;

   private LocalDate endDate;

   private AggregationDimension aggregationDimension;
}
