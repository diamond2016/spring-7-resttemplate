package guru.springframework.spring7resttemplate.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

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

    @Test
    void testUpdateBeer() {
        Page<BeerDTO> beerDTOs = beerClient.listBeers();
        BeerDTO beer = beerDTOs.getContent().get(0);
        String newName = "Updated Test Beer";
        beer.setBeerName(newName);
        BeerDTO updatedBeer = beerClient.updateBeer(beer);
        assertEquals(updatedBeer.getBeerName(), newName);
    }

    @Test
    void testDeleteBeer() {
         BeerDTO beerDTO = BeerDTO.builder()
        .beerName("Test Beer to delete")
        .beerStyle(BeerStyle.IPA)
        .upc("1234567890123")
        .price(new BigDecimal("10.90"))
        .quantityOnHand(100)
        .build();
        BeerDTO savedBeer = beerClient.createBeer(beerDTO);
        beerClient.deleteBeer(savedBeer.getId());

        // RestClient throws 400 if problems with request, 404 if not found, so we can check that the beer is not found after deletion
        assertThrows (HttpClientErrorException.class, () -> {
            beerClient.getBeerById(savedBeer.getId());
        });
    }
}   
