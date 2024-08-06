package xyz.streetscout.vendor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.streetscout.vendor.dto.VendorDetails;
import xyz.streetscout.vendor.dto.VendorList;
import xyz.streetscout.vendor.dto.VendorRegistration;
import xyz.streetscout.vendor.dto.VendorUpdate;
import xyz.streetscout.vendor.entity.Vendor;
import xyz.streetscout.vendor.repository.VendorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    /**
     * @return VendorList
     */
    @Override
    public VendorList getAllVendors() {
        List<VendorDetails> vendors = vendorRepository.findAll().stream()
                .map(entity -> new VendorDetails(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getPhotos(),
                        entity.getLocation(),
                        entity.getOperatingHours(),
                        entity.getMenu()
                ))
                .collect(Collectors.toList());

        return new VendorList(vendors);
    }

    /**
     * @param vendorId Vendor id
     * @return VendorDetails
     */
    @Override
    public VendorDetails getVendorById(Long vendorId) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorId);

        return vendorOptional.map(entity -> new VendorDetails(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPhotos(),
                entity.getLocation(),
                entity.getOperatingHours(),
                entity.getMenu()
        )).orElse(null);
    }

    /**
     * @param vendorRegistration Initial Vendor information
     * @return VendorDetails
     */
    @Override
    public VendorDetails registerVendor(VendorRegistration vendorRegistration) {
        Vendor vendor = new Vendor();
        vendor.setName(vendorRegistration.name());
        vendor.setDescription(vendorRegistration.description());
        vendor.setLocation(vendorRegistration.location());
        vendor.setOperatingHours(vendorRegistration.operatingHours());
        vendor.setMenu(vendorRegistration.menu());

        Vendor savedVendor = vendorRepository.save(vendor);

        return new VendorDetails(
                savedVendor.getId(),
                savedVendor.getName(),
                savedVendor.getDescription(),
                savedVendor.getPhotos(),
                savedVendor.getLocation(),
                savedVendor.getOperatingHours(),
                savedVendor.getMenu()
        );
    }

    /**
     * @param vendorUpdate Vendor details to be updated
     * @return VendorDetails with updated values
     */
    @Override
    public VendorDetails updateVendor(Long vendorId,VendorUpdate vendorUpdate) throws Exception {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new Exception("User not found"));

        if (vendorUpdate.description() != null && !vendorUpdate.description().isEmpty()) {
            vendor.setDescription(vendorUpdate.description());
        }

        if (vendorUpdate.photos() != null && !vendorUpdate.photos().isEmpty()) {
            if (vendor.getPhotos() == null) {
                vendor.setPhotos(vendorUpdate.photos());
            } else {
                vendor.getPhotos().addAll(vendorUpdate.photos());
            }
        }

        if (vendorUpdate.menu() != null && !vendorUpdate.menu().isEmpty()) {
            if (vendor.getMenu() == null) {
                vendor.setMenu(vendorUpdate.menu());
            } else {
                vendor.getMenu().addAll(vendorUpdate.menu());
            }
        }

        if (vendorUpdate.location() != null) {
            vendor.setLocation(vendorUpdate.location());
        }
        if (vendorUpdate.operatingHours() != null) {
            vendor.setOperatingHours(vendorUpdate.operatingHours());
        }


        Vendor updatedVendor = vendorRepository.save(vendor);

        return new VendorDetails(
                updatedVendor.getId(),
                updatedVendor.getName(),
                updatedVendor.getDescription(),
                updatedVendor.getPhotos(),
                updatedVendor.getLocation(),
                updatedVendor.getOperatingHours(),
                updatedVendor.getMenu()
        );
    }

    /**
     * @param vendorId Vendor id
     */
    @Override
    public void deactivateVendor(Long vendorId) {
        vendorRepository.deleteById(vendorId);
    }
}
