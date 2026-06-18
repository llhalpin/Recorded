package com.recordedapp.recordedback.services;

import com.recordedapp.recordedback.dtos.FoodSearchRequestDTO;
import com.recordedapp.recordedback.dtos.FoodSearchResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*FoodSearchService class(4/28/2026) implemented to gets USDA information for the food that user is searching
* for
*/

@Service
public class FoodSearchService {

    //USDA API key which is placed in the application.properties file(note that key was obtained from USDA website)
    @Value("${usda.api.key}")
    private String usdaApiKey;

    //RestClient is Spring's built-in HTTP client for calling external APIs (using this instead of WebClient since
    //this is a small project and not as many users)
    private final RestClient restClient;

    // USDA base search URL
    private static final String USDA_SEARCH_URL = "https://api.nal.usda.gov/fdc/v1/foods/search";

    public FoodSearchService(){

        //set up timeout after 5 seconds for connection request and timeout after 5 seconds for response time for USDA
        RequestConfig timeoutConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(5))
                .setResponseTimeout(Timeout.ofSeconds(5))
                .build();

        var httpClient = HttpClients.custom()
                .setDefaultRequestConfig(timeoutConfig)
                .build();

        var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }
    //searchFood method
    public List<FoodSearchResponseDTO> searchFood(FoodSearchRequestDTO foodSearchRequestDTO){

        List<FoodSearchResponseDTO> foodSearchResponseDTOList = new ArrayList<>();

        //build the search string for USDA with query parameters
        String url = UriComponentsBuilder.fromHttpUrl(USDA_SEARCH_URL)
                .queryParam("query", foodSearchRequestDTO.getFoodName())
                .queryParam("pageSize",5)
                .queryParam("api_key",usdaApiKey)
                .toUriString();

        //call USDA API and get a raw Map response (nested JSON) from USDA and using Map to navigate USDA response
        // and get only the specific information needed to be displayed in the front end.
        Map<String,Object> usdaResponseMap;
        try {
            usdaResponseMap = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(Map.class);
        } catch (Exception e) {
            System.err.println("USDA API call failed: "+e.getMessage());
            throw new RuntimeException("Food search is temporary unavailable. Please try again later");
        }

        List<Map<String,Object>> usdaFoodList = (List<Map<String,Object>>) usdaResponseMap.get("foods");

        //to avoid NullPointerException, do this check and return an empty arraylist
        if(usdaFoodList == null || usdaFoodList.isEmpty()){
            return foodSearchResponseDTOList;
        }

        for (Map<String,Object> food : usdaFoodList){

            //unique id assigned by USDA for this food
            String fdcId = String.valueOf(food.get("fdcId"));

            String foodName = String.valueOf(food.get("description"));
            
        }



    }




}
