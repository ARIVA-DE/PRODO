package de.ariva.callexternalapiexample.service;

import de.ariva.callexternalapiexample.model.pojo.MultiSumAggregationDTO;
import de.ariva.callexternalapiexample.model.pojo.MultiSumAggregationRequestBodyDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class ExternalApiCallService {

   final RestTemplate restTemplate;
   @Value("${apiService.url}")
   private String apiService;
   @Value("${apiService.apiVersion}")
   private String apiVersion;
   private String apiServiceURL;

   public ExternalApiCallService(RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
   }

   @PostConstruct
   public void init() {
      apiServiceURL = apiService + "/api/" + apiVersion;
   }

   public String getAuthentication(String username, String password) {
      ResponseEntity<String> response = restTemplate.getForEntity(
              apiServiceURL + "/authentication?username={username}&password={password}", String.class, username, password);

      return response.getBody();
   }

   public MultiSumAggregationDTO getMultiSumAggregation(MultiSumAggregationRequestBodyDTO requestBody, String jwtToken) {
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Collections.singletonList(MediaType.ALL));
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("Authorization", "bearer " + jwtToken);

      HttpEntity<MultiSumAggregationRequestBodyDTO> entity = new HttpEntity<>(requestBody, headers);
      ResponseEntity<MultiSumAggregationDTO> response = restTemplate.exchange(
              apiServiceURL + "/multiAggregation/sum", HttpMethod.POST, entity, MultiSumAggregationDTO.class);

      return response.getBody();
   }
}
