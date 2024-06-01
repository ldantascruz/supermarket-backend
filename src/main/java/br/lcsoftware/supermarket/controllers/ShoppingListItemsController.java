package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.ShoppingListItemsRecordDto;
import br.lcsoftware.supermarket.models.ShoppingListItemsModel;
import br.lcsoftware.supermarket.models.ShoppingListsModel;
import br.lcsoftware.supermarket.models.ItemModel;
import br.lcsoftware.supermarket.repositories.ShoppingListItemsRepository;
import br.lcsoftware.supermarket.repositories.ShoppingListsRepository;
import br.lcsoftware.supermarket.repositories.ItemRepository;
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
public class ShoppingListItemsController {

    @Autowired
    ShoppingListItemsRepository shoppingListItemsRepository;

    @Autowired
    ShoppingListsRepository shoppingListsRepository;

    @Autowired
    ItemRepository itemRepository;

    @PostMapping("/shopping-list-items")
    public ResponseEntity<?> saveShoppingListItem(@RequestBody @Valid ShoppingListItemsRecordDto shoppingListItemsRecordDto) {
        try {
            var shoppingListItemsModel = new ShoppingListItemsModel();
            BeanUtils.copyProperties(shoppingListItemsRecordDto, shoppingListItemsModel);

            Optional<ShoppingListsModel> shoppingListOptional = shoppingListsRepository.findById(shoppingListItemsRecordDto.shoppingListId());
            if (shoppingListOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
            }

            Optional<ItemModel> itemOptional = itemRepository.findById(shoppingListItemsRecordDto.itemId());
            if (itemOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item not found");
            }

            shoppingListItemsModel.setShoppingList(shoppingListOptional.get());
            shoppingListItemsModel.setItem(itemOptional.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(shoppingListItemsRepository.save(shoppingListItemsModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/shopping-list-items")
    public ResponseEntity<?> getAllShoppingListItems() {
        try {
            List<ShoppingListItemsModel> shoppingListItemsList = shoppingListItemsRepository.findAll();
            if (!shoppingListItemsList.isEmpty()) {
                for (ShoppingListItemsModel shoppingListItem : shoppingListItemsList) {
                    UUID id = shoppingListItem.getId();
                    shoppingListItem.add(linkTo(methodOn(ShoppingListItemsController.class).getOneShoppingListItem(id)).withSelfRel());
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(shoppingListItemsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/shopping-list-items/{id}")
    public ResponseEntity<?> getOneShoppingListItem(@PathVariable(value = "id") UUID id) {
        try {
            Optional<ShoppingListItemsModel> shoppingListItem0 = shoppingListItemsRepository.findById(id);
            if (shoppingListItem0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping list item not found");
            }
            shoppingListItem0.get().add(linkTo(methodOn(ShoppingListItemsController.class).getAllShoppingListItems()).withSelfRel());
            return ResponseEntity.status(HttpStatus.OK).body(shoppingListItem0.get());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/shopping-list-items/{id}")
    public ResponseEntity<?> updateShoppingListItem(@PathVariable(value = "id") UUID id, @RequestBody @Valid ShoppingListItemsRecordDto shoppingListItemsRecordDto) {
        try {
            Optional<ShoppingListItemsModel> shoppingListItem0 = shoppingListItemsRepository.findById(id);
            if (shoppingListItem0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping list item not found");
            }
            var shoppingListItemsModel = shoppingListItem0.get();
            BeanUtils.copyProperties(shoppingListItemsRecordDto, shoppingListItemsModel);

            Optional<ShoppingListsModel> shoppingListOptional = shoppingListsRepository.findById(shoppingListItemsRecordDto.shoppingListId());
            if (shoppingListOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
            }

            Optional<ItemModel> itemOptional = itemRepository.findById(shoppingListItemsRecordDto.itemId());
            if (itemOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item not found");
            }

            shoppingListItemsModel.setShoppingList(shoppingListOptional.get());
            shoppingListItemsModel.setItem(itemOptional.get());

            return ResponseEntity.status(HttpStatus.OK).body(shoppingListItemsRepository.save(shoppingListItemsModel));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/shopping-list-items/{id}")
    public ResponseEntity<?> deleteShoppingListItem(@PathVariable(value = "id") UUID id) {
        try {
            Optional<ShoppingListItemsModel> shoppingListItem0 = shoppingListItemsRepository.findById(id);
            if (shoppingListItem0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping list item not found");
            }
            shoppingListItemsRepository.delete(shoppingListItem0.get());
            return ResponseEntity.status(HttpStatus.OK).body("Shopping list item deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
