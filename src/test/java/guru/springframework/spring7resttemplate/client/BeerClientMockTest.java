package guru.springframework.spring7resttemplate.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class BeerClientMockTest {

    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private ObjectMapper objectMapper;

    private MockRestServiceServer server;

    private BeerClient beerClient;
    private static final String URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";
    private static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";

    private BeerDTO beerDTO;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplateBuilder().rootUri(URL).build();
        objectMapper = new ObjectMapper();

        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        beerClient = new BeerClientImpl(restTemplateBuilder);

        beerDTO = getBeerDTO();

        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    void testListBeers() throws Exception {
        List<BeerDTO> beerList = List.of(beerDTO);
        Pageable pageable = PageRequest.of(0, 20);
        BeerDTOPageImpl expectedPage = new BeerDTOPageImpl(beerList, pageable, 1L);

        String json = objectMapper.writeValueAsString(expectedPage);

        server.expect(method(HttpMethod.GET))
            .andExpect(requestTo(URL + GET_BEER_PATH))
            .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        Page<BeerDTO> result = beerClient.listBeers();

        assertNotNull(result);
        assertTrue(result.getContent().size() > 0);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Beer mock", result.getContent().get(0).getBeerName());

        server.verify();
    }

    @Test
    void testGetBeerById() throws Exception {
        String json = objectMapper.writeValueAsString(beerDTO);

        server.expect(method(HttpMethod.GET))
            .andExpect(requestToUriTemplate(URL + GET_BEER_BY_ID_PATH, beerDTO.getId()))
            .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        BeerDTO result = beerClient.getBeerById(beerDTO.getId());

        assertNotNull(result);
        assertEquals(beerDTO.getId(), result.getId());
        assertEquals("Test Beer mock", result.getBeerName());
        assertEquals(BeerStyle.IPA, result.getBeerStyle());

        server.verify();
    }

    @Test
    void testCreateBeer() throws Exception {
        URI createdUri = UriComponentsBuilder.fromPath(GET_BEER_BY_ID_PATH).build(beerDTO.getId());

        URI fullCreated = URI.create(URL + GET_BEER_BY_ID_PATH.replace("{beerId}", beerDTO.getId().toString()));
        server.expect(method(HttpMethod.POST))
            .andExpect(requestTo(URL + GET_BEER_PATH))
            .andRespond(withStatus(org.springframework.http.HttpStatus.CREATED).location(fullCreated));

        String json = objectMapper.writeValueAsString(beerDTO);
        server.expect(method(HttpMethod.GET))
            .andExpect(requestTo(URL + GET_BEER_BY_ID_PATH.replace("{beerId}", beerDTO.getId().toString())))
            .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        BeerDTO result = beerClient.createBeer(beerDTO);

        assertNotNull(result);
        assertEquals(beerDTO.getId(), result.getId());

        server.verify();
    }

    @Test
    void testUpdateBeer() throws Exception {
        server.expect(method(HttpMethod.PUT))
            .andExpect(requestToUriTemplate(URL + GET_BEER_BY_ID_PATH, beerDTO.getId()))
            .andRespond(withNoContent());

        String json = objectMapper.writeValueAsString(beerDTO);
        server.expect(method(HttpMethod.GET))
            .andExpect(requestToUriTemplate(URL + GET_BEER_BY_ID_PATH, beerDTO.getId()))
            .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        BeerDTO result = beerClient.updateBeer(beerDTO);

        assertNotNull(result);
        assertEquals(beerDTO.getId(), result.getId());

        server.verify();
    }

    @Test
    void testDeleteBeer()  {
        UUID beerId = beerDTO.getId();

        server.expect(method(HttpMethod.DELETE))
            .andExpect(requestToUriTemplate(URL + GET_BEER_BY_ID_PATH, beerId))
            .andRespond(withNoContent());

        assertDoesNotThrow(() -> beerClient.deleteBeer(beerId));

        server.verify();
    }

    @Test
    void testDeleteNotFound() {
        UUID beerId = beerDTO.getId();

        server.expect(method(HttpMethod.DELETE))
            .andExpect(requestToUriTemplate(URL + GET_BEER_BY_ID_PATH, beerId))
            .andRespond(withResourceNotFound());

        assertThrows(HttpClientErrorException.class, () -> beerClient.deleteBeer(beerId));

        server.verify();
    }

    @Test
    void testListBeersWithQueryParam() throws Exception {

        String json = objectMapper.writeValueAsString(getPage());
        URI uri = UriComponentsBuilder.fromUriString(URL + GET_BEER_PATH)
            .queryParam("beerName", "ALE")
            .build().toUri();

        System.out.println(uri);

        // when assert servet Http
        server.expect(method(HttpMethod.GET))
            .andExpect(requestTo(uri))
            .andExpect(queryParam("beerName", "ALE"))
            .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        // Act
        Page<BeerDTO> responsePage = beerClient.listBeers("ALE");

        assertEquals(responsePage.getContent().size(), 1);
    }

    // ===

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

    private BeerDTOPageImpl getPage(){
        return new BeerDTOPageImpl(Arrays.asList(getBeerDTO()), 1, 25, 1);
    }
}
