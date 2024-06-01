package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.PurchaseItemsRecordDto;
import br.lcsoftware.supermarket.models.PurchaseItemsModel;
import br.lcsoftware.supermarket.repositories.PurchaseItemsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PurchaseItemsController {

    @Autowired
    PurchaseItemsRepository purchaseItemsRepository;

    @PostMapping("/purchase-items")
    public ResponseEntity<?> savePurchaseItem(@RequestBody @Valid PurchaseItemsRecordDto purchaseItemsRecordDto) {
        try {
            var purchaseItemsModel = new PurchaseItemsModel();
            BeanUtils.copyProperties(purchaseItemsRecordDto, purchaseItemsModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(purchaseItemsRepository.save(purchaseItemsModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/purchase-items")
    public ResponseEntity<?> getAllPurchaseItems() {
        try {
            List<PurchaseItemsModel> purchaseItemsList = purchaseItemsRepository.findAll();
            if (!purchaseItemsList.isEmpty()) {
                for (PurchaseItemsModel purchaseItem : purchaseItemsList) {
                    UUID id = purchaseItem.getId();
                    purchaseItem.add(linkTo(methodOn(PurchaseItemsController.class).getOnePurchaseItem(id)).withSelfRel());
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(purchaseItemsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/purchase-items/{id}")
    public ResponseEntity<?> getOnePurchaseItem(@PathVariable(value = "id") UUID id) {
        try {
            Optional<PurchaseItemsModel> purchaseItem0 = purchaseItemsRepository.findById(id);
            if (purchaseItem0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase item not found");
            }
            purchaseItem0.get().add(linkTo(methodOn(PurchaseItemsController.class).getAllPurchaseItems()).withSelfRel());
            return ResponseEntity.status(HttpStatus.OK).body(purchaseItem0.get());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/purchase-items/{id}")
    public ResponseEntity<?> updatePurchaseItem(@PathVariable(value = "id") UUID id, @RequestBody @Valid PurchaseItemsRecordDto purchaseItemsRecordDto) {
        try {
            Optional<PurchaseItemsModel> purchaseItem0 = purchaseItemsRepository.findById(id);
            if (purchaseItem0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase item not found");
            }
            var purchaseItemsModel = purchaseItem0.get();
            BeanUtils.copyProperties(purchaseItemsRecordDto, purchaseItemsModel);
            return ResponseEntity.status(HttpStatus.OK).body(purchaseItemsRepository.save(purchaseItemsModel));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/purchase-items/{id}")
    public ResponseEntity<?> deletePurchaseItem(@PathVariable(value = "id") UUID id) {
        try {
            Optional<PurchaseItemsModel> purchaseItem0 = purchaseItemsRepository.findById(id);
            if (purchaseItem0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase item not found");
            }
            purchaseItemsRepository.delete(purchaseItem0.get());
            return ResponseEntity.status(HttpStatus.OK).body("Purchase item deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
