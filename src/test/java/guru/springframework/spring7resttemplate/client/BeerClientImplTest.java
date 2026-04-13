package guru.springframework.spring7resttemplate.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import guru.springframework.spring7resttemplate.model.BeerDTO;

@SpringBootTest
public class BeerClientImplTest {
    @Autowired
    BeerClient beerClient;
    
    @Test
    void testListBeers() {
        assertNotNull(beerClient.listBeers());
        Page<BeerDTO> beers = beerClient.listBeers();
        assertNotNull(beers);
        assertTrue(beers.isEmpty());
    }
}
