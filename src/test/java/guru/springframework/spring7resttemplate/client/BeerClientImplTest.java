package guru.springframework.spring7resttemplate.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerStyle;

@SpringBootTest
public class BeerClientImplTest {
    @Autowired
    BeerClient beerClient;
    
    @Test
    void testListBeers() {
        assertNotNull(beerClient.listBeers());
    }
    
    @Test
    void testListBeersWithName() {
        assertNotNull(beerClient.listBeers("ALE"));
    }

    @Test
    void testListBeersWithNameAndStyle() {
        assertNotNull(beerClient.listBeers("ALE", null));
        assertNotNull(beerClient.listBeers("ALE", null));       
    }

    void testListBeersWithNameAndStyleAndShowInventory() {
        assertNotNull(beerClient.listBeers("ALE", null, true));
        assertNotNull(beerClient.listBeers("ALE", null, false));       
    }   

    void testListBeersWithNameAndStyleAndShowInventoryAndPageNumber() {
        assertNotNull(beerClient.listBeers("ALE", null, true, 1));
        assertNotNull(beerClient.listBeers("ALE", null, false, 1));       
    }   

    void testListBeersWithNameAndStyleAndShowInventoryAndPageNumberAndPageSize() {
        assertNotNull(beerClient.listBeers("ALE", null, true, 1, 25));
        assertNotNull(beerClient.listBeers("ALE", null, false, 1, 25));       
    }

    @Test
    void testGetBeerById() {
        Page<BeerDTO> beerDTOs = beerClient.listBeers();
        BeerDTO beer = beerDTOs.getContent().get(0);
        BeerDTO beerById = beerClient.getBeerById(beer.getId());
        assertNotNull(beerById);
    }

    @Test
    void testCreateBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("Test Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("123456789012")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(100)
                .build();
        BeerDTO savedBeer = beerClient.createBeer(beerDTO);
        assertNotNull(savedBeer);
    }

}
