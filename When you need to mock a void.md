When you need to mock a void method in Mockito — such as RestTemplate.put() — you can’t use the usual when(...).thenReturn(...) syntax, because void methods don’t return anything.
Instead, Mockito provides doNothing(), doThrow(), and doAnswer() for this purpose.

Example: Mocking RestTemplate.put()
Javaimport static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

class RestTemplateVoidTest {

    @Test
    void testPutMethod() {
        // Create mock
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        // Mock the void method to do nothing
        doNothing().when(restTemplate)
                   .put(eq("http://example.com/resource"), any());

        // Call the method
        restTemplate.put("http://example.com/resource", new Object());

        // Verify it was called
        verify(restTemplate, times(1))
                .put(eq("http://example.com/resource"), any());
    }

    @Test
    void testPutThrowsException() {
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        // Mock the void method to throw an exception
        doThrow(new RuntimeException("PUT failed"))
                .when(restTemplate)
                .put(eq("http://example.com/resource"), any());

        try {
            restTemplate.put("http://example.com/resource", new Object());
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        verify(restTemplate).put(eq("http://example.com/resource"), any());
    }
}


Key Points:

doNothing() — default behavior, but useful for clarity in tests.
doThrow() — simulate exceptions from void methods.
doAnswer() — custom behavior for void methods.

Example with doAnswer():
JavadoAnswer(invocation -> {
    Object body = invocation.getArgument(1);
    System.out.println("PUT called with body: " + body);
    return null;
}).when(restTemplate).put(anyString(), any());


✅ Rule of thumb:
For void methods in Mockito, use doNothing(), doThrow(), or doAnswer() — never when(...).thenReturn(...).

If you want, I can also show you how to spy on a real RestTemplate so you can partially mock put() while keeping other methods real.
Do you want me to include that?
