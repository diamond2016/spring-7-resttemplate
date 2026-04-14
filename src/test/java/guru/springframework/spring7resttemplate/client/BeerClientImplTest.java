package guru.springframework.spring7resttemplate.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
