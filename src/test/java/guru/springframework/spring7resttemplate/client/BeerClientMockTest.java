package guru.springframework.spring7resttemplate.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.boot.restclient.RestTemplateBuilder;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import tools.jackson.databind.ObjectMapper;

// Using Mockito to unit test BeerClient in isolation
@ExtendWith(MockitoExtension.class)
public class BeerClientMockTest {
   
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private RestTemplateBuilder restTemplateBuilder;
    
    @Mock
    private ObjectMapper objectMapper;
    
    private BeerClientImpl beerClient;
    private static final String URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";
        
    @BeforeEach
    void setup() {
        // When builder.build() is called, return our mocked RestTemplate
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        
        // Create real BeerClient with mocked dependencies
        beerClient = new BeerClientImpl(restTemplateBuilder);
    }

    @Test
    void testListBeers() throws Exception {
        // Arrange: Create test data
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("Test Beer mock")
                .beerStyle(BeerStyle.IPA)
                .upc("123456789012")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(100)
                .build();
        List<BeerDTO> beerList = List.of(beerDTO);
        Pageable pageable = PageRequest.of(0, 20);
        BeerDTOPageImpl expectedPage = new BeerDTOPageImpl(beerList, pageable, 1L);
        
        String uriString = UriComponentsBuilder.fromPath(GET_BEER_PATH).toUriString();
        
        // Mock RestTemplate behavior: when getForEntity is called with specific URI, return our page
        when(restTemplate.getForEntity(eq(uriString), eq(BeerDTOPageImpl.class)))
                .thenReturn(ResponseEntity.ok(expectedPage));
        
        // Act: Call the method under test
        Page<BeerDTO> result = beerClient.listBeers();
        
        // Assert: Verify results
        assertNotNull(result);
        assertTrue(result.getContent().size() > 0);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Beer mock", result.getContent().get(0).getBeerName());
        
        // Verify: Ensure RestTemplate was called correctly
        verify(restTemplate).getForEntity(eq(uriString), eq(BeerDTOPageImpl.class));
    }
}