package guru.springframework.spring7resttemplate.client;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerDTOPageImpl;
import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    // REST constructor injection
    private final RestTemplateBuilder restTemplateBuilder;
  
     // Base URL for the Beer API
    private static final String GET_BEER_PATH = "/api/v1/beer";   
    
    @Override
    public Page<BeerDTO> listBeers() {

        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<BeerDTOPageImpl> response = restTemplate.getForEntity(GET_BEER_PATH, BeerDTOPageImpl.class);
        
        return response.getBody();

    }



}