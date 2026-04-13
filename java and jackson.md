sdk list java
25.0.2-tem is installed (Java 25)
sdk install java 21.0.10-tem
set 21 as java jdk for project "server" (spring7-rest-mvc) which uses java 21

note that RestTemplate can only deserialize JSON into JsonNode when a MappingJackson2HttpMessageConverter is available and selected. The error happens when either Jackson isn't on the classpath or the HTTP response Content-Type doesn't match any JSON-supporting converter (e.g. server returns text/plain), so conversion to JsonNode fails.

MappingJackson2HttpMessageConverter jacksonConv = new MappingJackson2HttpMessageConverter();
    jacksonConv.setSupportedMediaTypes(Arrays.asList(
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_JSON_UTF8,
        MediaType.TEXT_PLAIN   // accept servers that send JSON with text/plain
    ));


    import java.util.Objects;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;
    private final ObjectMapper objectMapper;

    private static final String BASE_URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";

    @Override
    public Page<BeerDTO> listBeers() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String body = restTemplate.getForObject(BASE_URL + GET_BEER_PATH, String.class);

        if (Objects.isNull(body) || body.isBlank()) {
            return Page.empty();
        }

        try {
            JsonNode jsonResponse = objectMapper.readTree(body);
            System.out.println(jsonResponse.findPath("content"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed parsing response to JsonNode", e);
        }

        return Page.empty();
    }
}