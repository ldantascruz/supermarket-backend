package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.ShoppingListsRecordDto;
import br.lcsoftware.supermarket.models.ShoppingListsModel;
import br.lcsoftware.supermarket.models.UserModel;
import br.lcsoftware.supermarket.repositories.ShoppingListsRepository;
import br.lcsoftware.supermarket.repositories.UserRepository;
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
public class ShoppingListsController {

    @Autowired
    ShoppingListsRepository shoppingListsRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/shopping-lists")
    public ResponseEntity<?> saveShoppingList(@RequestBody @Valid ShoppingListsRecordDto shoppingListsRecordDto) {
        try {
            var shoppingListsModel = new ShoppingListsModel();
            BeanUtils.copyProperties(shoppingListsRecordDto, shoppingListsModel);

            Optional<UserModel> userOptional = userRepository.findById(shoppingListsRecordDto.userId());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

            shoppingListsModel.setUser(userOptional.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(shoppingListsRepository.save(shoppingListsModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/shopping-lists")
    public ResponseEntity<?> getAllShoppingLists() {
        try {
            List<ShoppingListsModel> shoppingListsList = shoppingListsRepository.findAll();
            if (!shoppingListsList.isEmpty()) {
                for (ShoppingListsModel shoppingList : shoppingListsList) {
                    UUID id = shoppingList.getId();
                    shoppingList.add(linkTo(methodOn(ShoppingListsController.class).getOneShoppingList(id)).withSelfRel());
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(shoppingListsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/shopping-lists/{id}")
    public ResponseEntity<?> getOneShoppingList(@PathVariable(value = "id") UUID id) {
        try {
            Optional<ShoppingListsModel> shoppingList0 = shoppingListsRepository.findById(id);
            if (shoppingList0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping list not found");
            }
            shoppingList0.get().add(linkTo(methodOn(ShoppingListsController.class).getAllShoppingLists()).withSelfRel());
            return ResponseEntity.status(HttpStatus.OK).body(shoppingList0.get());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/shopping-lists/{id}")
    public ResponseEntity<?> updateShoppingList(@PathVariable(value = "id") UUID id, @RequestBody @Valid ShoppingListsRecordDto shoppingListsRecordDto) {
        try {
            Optional<ShoppingListsModel> shoppingList0 = shoppingListsRepository.findById(id);
            if (shoppingList0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping list not found");
            }
            var shoppingListsModel = shoppingList0.get();
            BeanUtils.copyProperties(shoppingListsRecordDto, shoppingListsModel);

            Optional<UserModel> userOptional = userRepository.findById(shoppingListsRecordDto.userId());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

            shoppingListsModel.setUser(userOptional.get());

            return ResponseEntity.status(HttpStatus.OK).body(shoppingListsRepository.save(shoppingListsModel));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/shopping-lists/{id}")
    public ResponseEntity<?> deleteShoppingList(@PathVariable(value = "id") UUID id) {
        try {
            Optional<ShoppingListsModel> shoppingList0 = shoppingListsRepository.findById(id);
            if (shoppingList0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping list not found");
            }
            shoppingListsRepository.delete(shoppingList0.get());
            return ResponseEntity.status(HttpStatus.OK).body("Shopping list deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
