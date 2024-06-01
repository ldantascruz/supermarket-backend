package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.PurchasesRecordDto;
import br.lcsoftware.supermarket.models.PurchasesModel;
import br.lcsoftware.supermarket.repositories.PurchasesRepository;
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
public class PurchasesController {

    @Autowired
    PurchasesRepository purchasesRepository;

    @PostMapping("/purchases")
    public ResponseEntity<?> savePurchase(@RequestBody @Valid PurchasesRecordDto purchasesRecordDto) {
        try {
            var purchasesModel = new PurchasesModel();
            BeanUtils.copyProperties(purchasesRecordDto, purchasesModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(purchasesRepository.save(purchasesModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/purchases")
    public ResponseEntity<?> getAllPurchases() {
        try {
            List<PurchasesModel> purchasesList = purchasesRepository.findAll();
            if (!purchasesList.isEmpty()) {
                for (PurchasesModel purchase : purchasesList) {
                    UUID id = purchase.getId();
                    purchase.add(linkTo(methodOn(PurchasesController.class).getOnePurchase(id)).withSelfRel());
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(purchasesList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/purchases/{id}")
    public ResponseEntity<?> getOnePurchase(@PathVariable(value = "id") UUID id) {
        try {
            Optional<PurchasesModel> purchase0 = purchasesRepository.findById(id);
            if (purchase0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found");
            }
            purchase0.get().add(linkTo(methodOn(PurchasesController.class).getAllPurchases()).withSelfRel());
            return ResponseEntity.status(HttpStatus.OK).body(purchase0.get());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/purchases/{id}")
    public ResponseEntity<?> updatePurchase(@PathVariable(value = "id") UUID id, @RequestBody @Valid PurchasesRecordDto purchasesRecordDto) {
        try {
            Optional<PurchasesModel> purchase0 = purchasesRepository.findById(id);
            if (purchase0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found");
            }
            var purchasesModel = purchase0.get();
            BeanUtils.copyProperties(purchasesRecordDto, purchasesModel);
            return ResponseEntity.status(HttpStatus.OK).body(purchasesRepository.save(purchasesModel));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/purchases/{id}")
    public ResponseEntity<?> deletePurchase(@PathVariable(value = "id") UUID id) {
        try {
            Optional<PurchasesModel> purchase0 = purchasesRepository.findById(id);
            if (purchase0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found");
            }
            purchasesRepository.delete(purchase0.get());
            return ResponseEntity.status(HttpStatus.OK).body("Purchase deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
