package guru.springframework.spring7resttemplate.client;

import java.util.Objects;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;
import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerDTOPageImpl;
import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    // REST constructor injection
    private final RestTemplateBuilder restTemplateBuilder;
    private final ObjectMapper objectMapper;

     // Base URL for the Beer API
    private static final String BASE_URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";   
    
    @Override
    public Page<BeerDTO> listBeers() {

        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<BeerDTOPageImpl> response = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, BeerDTOPageImpl.class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            BeerDTOPageImpl pageImpl = response.getBody();
            return pageImpl;
        }

        return Page.empty();
    }


    // this method saves an old logic, not used anymore.
    private Page<BeerDTO> oldListBeers() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        System.out.println("Using String -> ObjectMapper path");
        String body = restTemplate.getForObject(BASE_URL + GET_BEER_PATH, String.class);

        if (Objects.isNull(body) || body.isBlank()) {
            return Page.empty();
        }

        try {
            JsonNode jsonResponse = objectMapper.readTree(body);
            System.out.println(jsonResponse.findPath("content"));
        } catch (JsonNodeException e) {
            throw new RuntimeException("Failed parsing response to JsonNode", e);
        }

        return Page.empty();
    }

}