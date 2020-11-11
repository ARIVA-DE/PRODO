package de.ariva.callexternalapiexample.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ariva.callexternalapiexample.model.pojo.AggregationDimension;
import de.ariva.callexternalapiexample.model.pojo.MultiSumAggregationDTO;
import de.ariva.callexternalapiexample.model.pojo.MultiSumAggregationRequestBodyDTO;
import de.ariva.callexternalapiexample.service.ExternalApiCallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@Component
public class ExternalApiCallScheduler {

   private static final Logger LOG = LoggerFactory.getLogger(ExternalApiCallScheduler.class);

   @Autowired
   private ExternalApiCallService externalApiCallService;

   @Value("${username}")
   private String username;
   @Value("${password}")
   private String password;

   private String jwtToken = null;

   @Scheduled(fixedDelayString = "60000", initialDelayString = "30000")
   public void scheduleExternalApiCall() throws JsonProcessingException {
      if (jwtToken == null) {
         jwtToken = externalApiCallService.getAuthentication(username, password);
      }
      MultiSumAggregationRequestBodyDTO requestBodyDTO = new MultiSumAggregationRequestBodyDTO();
      requestBodyDTO.setPublisherIds(Collections.emptyList());
      requestBodyDTO.setAdvertiserIds(Collections.emptyList());
      requestBodyDTO.setAdSlotIds(Collections.emptyList());
      requestBodyDTO.setBouquetIds(Collections.emptyList());
      requestBodyDTO.setAggregationDimension(AggregationDimension.PUBLISHER);
      requestBodyDTO.setDevices(Collections.emptyList());
      requestBodyDTO.setRegions(Collections.emptyList());
      LocalDate today = LocalDate.now();
      requestBodyDTO.setStartDate(today.minusMonths(3));
      requestBodyDTO.setEndDate(today);
      MultiSumAggregationDTO multiSumAggregation = null;
      try {
         multiSumAggregation = externalApiCallService.getMultiSumAggregation(requestBodyDTO, jwtToken);
      } catch (Exception exception) {
         LOG.info("JWT token might have to be refreshed.");
         jwtToken = externalApiCallService.getAuthentication(username, password);
         multiSumAggregation = externalApiCallService.getMultiSumAggregation(requestBodyDTO, jwtToken);
      }
      ObjectMapper mapper = new ObjectMapper();
      LOG.info("Multi sum aggregation of last three months: {}", mapper.writeValueAsString(multiSumAggregation));
   }
}
