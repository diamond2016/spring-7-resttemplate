The test compiles successfully. Here's an explanation of the Mockito-based modifications:

Changes Made
1. Added Mockito Annotations
@ExtendWith(MockitoExtension.class)  // Integrates Mockito with JUnit 5
@Mock private RestTemplate restTemplate;      // Mock HTTP client
@Mock private RestTemplateBuilder builder;  // Mock builder
@Mock private ObjectMapper objectMapper;    // Mock JSON mapper (not used in new version)
`Why: @ExtendWith initializes mocks before each test. @Mock creates test doubles that record interactions and allow stubbing`.

2. Replaced Spring Test Context with Pure Unit Test
Before: Used @Autowired and MockRestServiceServer (Spring integration test) After: Manual instantiation with mocked dependencies

// Setup mocks
when(restTemplateBuilder.build()).thenReturn(restTemplate);
beerClient = new BeerClientImpl(restTemplateBuilder);
`Why: Isolates BeerClientImpl from Spring framework, testing only business logic`.

3. Mocked HTTP Call with when().thenReturn()
when(restTemplate.getForEntity(eq(uriString), eq(BeerDTOPageImpl.class)))
    .thenReturn(ResponseEntity.ok(expectedPage));
when(): Defines condition (method call with specific arguments)
eq(): Argument matcher - matches exact URI and class
thenReturn(): Stubs the return value
`Why: Simulates successful HTTP GET returning a Page<BeerDTO> without network calls`.

4. Verification with verify()
verify(restTemplate).getForEntity(eq(uriString), eq(BeerDTOPageImpl.class));
`Why: Confirms the method was called exactly once with expected arguments. This is behavior verification (vs state verification with assertions)`.

5. Improved Assertions
Added explicit checks:

assertEquals(1, result.getTotalElements());
assertEquals("Test Beer mock", result.getContent().get(0).getBeerName());


**Mockito Concepts Demonstrated**

| Concept | Example | Purpose |
|---------|---------|---------|
| Mock | `@Mock RestTemplate` | Creates a fake object with no real implementation |
| Stubbing | `when(...).thenReturn(...)` | Defines mock behavior for specific inputs |
| Argument Matchers | `eq(uriString)` | Flexible matching of method arguments |
| Verification | `verify(mock).method()` | Ensures interactions occurred as expected |
| Dependency Injection | `new BeerClientImpl(builder)` | Test subject created with mocked dependencies |

Benefits of This Approach
1. Fast: No HTTP server or Spring context startup
1. Isolated: Tests only BeerClientImpl logic, not Spring/RestTemplate
1. Deterministic: No network flakiness or external dependencies
1. Explicit: Clearly shows what's mocked and what's being verified
1. Flexible: Easy to test edge cases (404, errors, malformed responses)

The test now verifies that listBeers() correctly builds the URI and processes the Page<BeerDTO> response from the mocked RestTemplate.
