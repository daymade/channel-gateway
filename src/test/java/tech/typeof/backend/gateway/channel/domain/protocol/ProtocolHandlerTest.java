package tech.typeof.backend.gateway.channel.domain.protocol;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.Transport;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProtocolHandlerTest {

    @Test
    void sendRequestSuccessfully() throws Exception {
        // Mock preparation
        Transport protocolHandler = Mockito.mock(Transport.class);
        Map<String, Object> mockConfig = new HashMap<>();
        mockConfig.put("key", "value");

        // Define expected result
        String expectedResult = "Request sent successfully";

        // When sendRequest is called, then return expectedResult
        Mockito.when(protocolHandler.sendRequest(mockConfig)).thenReturn(expectedResult);

        // Assertion for the expected outcome
        assertEquals(expectedResult, protocolHandler.sendRequest(mockConfig));
    }

    @Test
    void sendRequestWithException() throws Exception {
        // Mock preparation
        Transport protocolHandler = Mockito.mock(Transport.class);
        Map<String, Object> mockConfig = new HashMap<>();
        mockConfig.put("key", "value");

        // Define the exception
        Exception exception = new Exception("Something went wrong");

        // When sendRequest is called, then throw exception
        Mockito.when(protocolHandler.sendRequest(mockConfig)).thenThrow(exception);

        // Assertion for the expected exception
        Exception actualException = assertThrows(Exception.class, () -> protocolHandler.sendRequest(mockConfig));
        assertEquals("Something went wrong", actualException.getMessage());
    }
}