 package com.pr.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pr.dto.ApiResponse;

import com.pr.dto.PrData;
import com.pr.dto.PrDetailsResponse;
import com.pr.dto.PrDetailsResponses;
import com.pr.dto.PrLineData;

@RestController
@RequestMapping("/pr")
public class PrController {

    private static final Logger logger = LoggerFactory.getLogger(PrController.class);

    private final com.pr.service.PrService prService;

    public PrController(com.pr.service.PrService prService) {
        this.prService = prService;
    }
    //get
    @GetMapping("/details/{prNumber}")
    public ResponseEntity<ApiResponse<PrDetailsResponse>>
    getPrDetails(@PathVariable String prNumber) {

        ApiResponse<PrDetailsResponse> response =
                prService.getPrDetails(prNumber);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }
    // Get all Purchase Requests
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PrData>>> getAllPurchaseRequests() {
        ApiResponse<List<PrData>> resp = prService.getAllPurchaseRequests();
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    // Get Purchase Request by PR number
    @GetMapping("/{prNumber}")
    public ResponseEntity<ApiResponse<PrData>> getPurchaseRequestByPrNumber(@PathVariable String prNumber) {
        ApiResponse<PrData> resp = prService.getPurchaseRequestByPrNumber(prNumber);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    // Create Purchase Request
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PrData>> createPurchaseRequest(@RequestBody PrData prData) {
        ApiResponse<PrData> resp = prService.createPurchaseRequest(prData);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    // Get PR line details
    @GetMapping("/{prNumber}/lines")
    public ResponseEntity<ApiResponse<List<PrLineData>>> getPrLines(@PathVariable String prNumber) {
        ApiResponse<List<PrLineData>> resp = prService.getPrLines(prNumber);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/{prNumber}/lines")
    public ResponseEntity<ApiResponse<PrLineData>> addPrLine(@PathVariable String prNumber,
                                                             @RequestBody PrLineData lineData) {
        ApiResponse<PrLineData> resp = prService.addPrLine(prNumber, lineData);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    // Update Purchase Request Status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PrData>> updatePrStatus(
            @PathVariable String id,
            @RequestParam String status) {
        ApiResponse<PrData> resp = prService.updatePrStatus(id, status);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    // Approve Purchase Request
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<PrData>> approvePurchaseRequest(@PathVariable String id) {
        ApiResponse<PrData> resp = prService.approvePurchaseRequest(id);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("PR Service is running", "Health check successful"));
    }

    // Decision endpoint: approve / reject / more_info
    @PostMapping("/{prNumber}/decision")
    public ResponseEntity<ApiResponse<PrData>> decidePurchaseRequest(@PathVariable String prNumber,
                                                                      @RequestParam String action,
                                                                      @RequestParam(required = false) String approver) {
        ApiResponse<PrData> resp = prService.decidePurchaseRequest(prNumber, action, approver);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
    

    // Reject Purchase Request
    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<PrData>> rejectPurchaseRequest(@PathVariable String id){
    	ApiResponse<PrData> res = prService.rejectPurchaseRequest(id);
        return  ResponseEntity.status(res.getStatusCode()).body(res);

    }
    
    
 // Get Purchase Request, Purchase Order and Budget Details by PR Number
    @GetMapping("/getPoandBudgetdetails/{prNumber}")
    public ResponseEntity<ApiResponse<PrDetailsResponses>> getPurchaseRequestDetails(
            @PathVariable String prNumber) {

        ApiResponse<PrDetailsResponses> resp =
                prService.getPurchaseRequestDetails(prNumber);

        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
}

