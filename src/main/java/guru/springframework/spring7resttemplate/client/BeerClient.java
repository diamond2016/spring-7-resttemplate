// client that uses api from spring7-rest-mvc
package guru.springframework.spring7resttemplate.client;
import java.util.UUID;

import org.springframework.data.domain.Page;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerStyle;

public interface BeerClient {

    Page<BeerDTO> listBeers();
    Page<BeerDTO> listBeers(String beerName);
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle);
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber);
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize); 
    // beerName and beerStyle and showInventory are optional query parameters, and so pageNumber and pageSize are optional pagination parameters, so we can pass null or empty string to get all beers

    BeerDTO getBeerById(UUID beerId);

    BeerDTO createBeer(BeerDTO beerDTO);
}

