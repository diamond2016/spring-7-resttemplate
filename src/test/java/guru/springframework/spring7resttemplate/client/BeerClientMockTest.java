package guru.springframework.spring7resttemplate.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.connector.Response;
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
    
    private BeerClient beerClient;
    private static final String URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";
    private static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";

    private BeerDTO beerDTO;

    @BeforeEach
    void setup() {
        // setup mocks
        // When builder.build() is called, return our mocked RestTemplate
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        
        // Create real BeerClient with mocked dependencies
        beerClient = new BeerClientImpl(restTemplateBuilder);

        // create a beeDTO for calls
        beerDTO = getBeerDTO();
    }

    @Test
    void testListBeers() throws Exception {
        // Arrange: Create test data (setup per beerDTO)  

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

    @Test
    void testGetBeerById() throws Exception {
        // Arrange: Create test data    (setup)          

        // Mock RestTemplate: getForObject with path template and vararg (matches implementation)
        when(restTemplate.getForObject(eq(GET_BEER_BY_ID_PATH), eq(BeerDTO.class), eq(beerDTO.getId().toString())))
                .thenReturn(beerDTO);
        
        // Act
        BeerDTO result = beerClient.getBeerById(beerDTO.getId());
        
        // Assert
        assertNotNull(result);
        assertEquals(beerDTO.getId(), result.getId());
        assertEquals("Test Beer mock", result.getBeerName());
        assertEquals(BeerStyle.IPA, result.getBeerStyle());
        
        // Verify
        verify(restTemplate).getForObject(eq(GET_BEER_BY_ID_PATH), eq(BeerDTO.class), eq(beerDTO.getId().toString()));
    }

    @Test
    void testCreateBeer() throws Exception {
        // Arrange: Create test data (setup per beerDTO)         
        URI createdUri = UriComponentsBuilder.fromPath(GET_BEER_BY_ID_PATH).build(beerDTO.getId());
        
        // Mock postForLocation to return the URI where the created resource can be accessed
        when(restTemplate.postForLocation(eq(GET_BEER_PATH), eq(beerDTO)))
                .thenReturn(createdUri);
        
        // Mock RestTemplate: getForObject with the resolved path
        when(restTemplate.getForObject(eq(createdUri.getPath()), eq(BeerDTO.class)))
                .thenReturn(beerDTO);
    
        // Act
        BeerDTO result = beerClient.createBeer(beerDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals(beerDTO.getId(), result.getId());

        // Verify
        verify(restTemplate).postForLocation(eq(GET_BEER_PATH), eq(beerDTO));
        verify(restTemplate).getForObject(eq(createdUri.getPath()), eq(BeerDTO.class));
    }

    @Test
    void testUpdateBeer() throws Exception {
        // Arrange: Create test data (setup per beerDTO)    

        // Mock restTemplate.put returns void, add doNOthing()
        // doNothing().when(restTemplate).put(eq(GET_BEER_BY_ID_PATH), eq(beerDTO), eq(beerDTO.getId().toString()));

        // Mock the getForObject call made by getBeerById() to return the updated beer
        when(restTemplate.getForObject(eq(GET_BEER_BY_ID_PATH), eq(BeerDTO.class), eq(beerDTO.getId().toString())))
            .thenReturn(beerDTO);

        // Act
        BeerDTO result = beerClient.updateBeer(beerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(beerDTO.getId(), result.getId());

        // Verify
        verify(restTemplate).put(eq(GET_BEER_BY_ID_PATH), eq(beerDTO), eq(beerDTO.getId()));
        verify(restTemplate).getForObject(eq(GET_BEER_BY_ID_PATH), eq(BeerDTO.class), eq(beerDTO.getId().toString()));

    }


    private BeerDTO getBeerDTO() {
        BeerDTO beerDTO = BeerDTO.builder()
            .id(UUID.randomUUID())
            .beerName("Test Beer mock")
            .beerStyle(BeerStyle.IPA)
            .upc("123456789012")
            .price(new BigDecimal("10.99"))
            .quantityOnHand(100)
            .build();
        
        return beerDTO;
        }
}