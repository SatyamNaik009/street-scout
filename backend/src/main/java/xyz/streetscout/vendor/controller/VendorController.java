package xyz.streetscout.vendor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.streetscout.vendor.dto.VendorList;
import xyz.streetscout.vendor.dto.VendorProfile;
import xyz.streetscout.vendor.dto.VendorUpdate;
import xyz.streetscout.vendor.service.VendorService;

@Tag(
    name = "REST APIs for Vendors",
    description = "REST APIs to CREATE, UPDATE, FETCH and DELETE Vendors")
@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VendorController {

    private final VendorService vendorService;

    @GetMapping("")
    @Operation(
            summary = "Get All Vendors",
            description = "REST API to FETCH all Vendors")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK")
    public ResponseEntity<VendorList> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        VendorList vendors = vendorService.getAllVendors(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(vendors);
    }

    @GetMapping("/{vendorId}")
    @Operation(
            summary = "Get Vendor By Id",
            description = "REST API to FETCH a Vendor by id")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK")
    public ResponseEntity<VendorProfile> getVendorById(@PathVariable(name = "vendorId") Long vendorId){
        VendorProfile vendor = vendorService.getVendorById(vendorId);
        return ResponseEntity.status(HttpStatus.OK).body(vendor);
    }

    @PutMapping("/{vendorId}")
    @Operation(
            summary = "Update Vendor",
            description = "REST API to update Vendor profile")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK")
    public ResponseEntity<VendorProfile> updateVendor(
            @Valid @RequestBody VendorUpdate vendorUpdate,
            @PathVariable Long vendorId
    ) throws Exception {
        VendorProfile vendor = vendorService.updateVendor(vendorId,vendorUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(vendor);
    }

    @DeleteMapping("/{vendorId}")
    @Operation(
            summary = "Deactivate Vendor",
            description = "REST API to deactivate Vendor")
    @ApiResponse(
            responseCode = "204",
            description = "HTTP Status NO CONTENT")
    public ResponseEntity<Void> deactivateVendor(@PathVariable Long vendorId) {
        vendorService.deactivateVendor(vendorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
