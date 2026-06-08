package com.pr.service;
import com.pr.dto.ApiResponse;
import com.pr.dto.PoDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PoClientService {
    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "poService", fallbackMethod = "fallbackPo")
    @Retry(name = "poService")
    public PoDTO getPoByPrNumber(String prNumber) {

        String url = "http://localhost:8082/po/by-pr/" + prNumber;
        ResponseEntity<ApiResponse<PoDTO>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ApiResponse<PoDTO>>() {
                        });

            return response.getBody().getData();
    }

    public PoDTO fallbackPo(String prNumber, Exception ex) {

        PoDTO po = new PoDTO();

        po.setPoNumber("N/A");
        po.setPoNumber("Po service in unavailable");
        po.setStatus("UNKNOWN");

        return po;
    }


}

