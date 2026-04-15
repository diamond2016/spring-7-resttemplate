package guru.springframework.spring7resttemplate.client;

import java.util.UUID;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    // REST constructor injection
    private final RestTemplateBuilder restTemplateBuilder;
  
     // Base URL for the Beer API
    private static final String GET_BEER_PATH = "/api/v1/beer";
    private static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";   
    
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);
        if (beerName != null && !beerName.isEmpty()) {
            uriBuilder.queryParam("beerName", beerName);
        }
        if (beerStyle != null) {
            uriBuilder.queryParam("beerStyle", beerStyle);
        }
        if (showInventory != null) {
            uriBuilder.queryParam("showInventory", showInventory);
        }
        if (pageNumber != null) {
            uriBuilder.queryParam("pageNumber", pageNumber);
        }
        if (pageSize != null) {
            uriBuilder.queryParam("pageSize", pageSize);
        }


        ResponseEntity<BeerDTOPageImpl> response = restTemplate.getForEntity(uriBuilder.toUriString(), BeerDTOPageImpl.class);
        
        return response.getBody();

    }

    @Override
    public Page<BeerDTO> listBeers() {
        return listBeers(null, null, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName) {
        return listBeers(beerName, null, null, null, null);
    }
    
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {
        return listBeers(beerName, beerStyle, null, null, null);
    }
    
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory) {
        return listBeers(beerName, beerStyle, showInventory, null, null);
    }
    
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber) {
        return listBeers(beerName, beerStyle, showInventory, pageNumber, null);
    }

    @Override
    public BeerDTO getBeerById(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<BeerDTO> response = restTemplate.getForEntity(GET_BEER_BY_ID_PATH, BeerDTO.class, beerId.toString());
        return response.getBody();
    }
}