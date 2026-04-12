package guru.springframework.spring7resttemplate.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import guru.springframework.spring7resttemplate.model.BeerDTO;

public class BeerClientImplTest {
    @Test
    void testListBeers() {
        BeerClient beerClient = new BeerClientImpl();
        assertNotNull(beerClient.listBeers());
        Page<BeerDTO> beers = beerClient.listBeers();
        assertNotNull(beers);
        assertTrue(beers.isEmpty());
    }
}
