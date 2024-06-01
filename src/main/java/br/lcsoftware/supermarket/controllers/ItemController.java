package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.ItemRecordDto;
import br.lcsoftware.supermarket.models.ItemModel;
import br.lcsoftware.supermarket.models.ShoppingListsModel;
import br.lcsoftware.supermarket.repositories.ItemRepository;
import br.lcsoftware.supermarket.repositories.ShoppingListsRepository;
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
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ShoppingListsRepository shoppingListsRepository;

    @PostMapping("/items")
    public ResponseEntity<?> saveItem(@RequestBody @Valid ItemRecordDto itemRecordDto) {
        try {
            var itemModel = new ItemModel();
            BeanUtils.copyProperties(itemRecordDto, itemModel);

            Optional<ShoppingListsModel> shoppingListOptional = shoppingListsRepository.findById(itemRecordDto.shoppingListId());
            if (shoppingListOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
            }

            itemModel.setShoppingList(shoppingListOptional.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(itemRepository.save(itemModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/items")
    public ResponseEntity<?> getAllItems() {
        try {
            List<ItemModel> itemsList = itemRepository.findAll();
            if (!itemsList.isEmpty()) {
                for (ItemModel item : itemsList) {
                    UUID id = item.getId();
                    item.add(linkTo(methodOn(ItemController.class).getOneItem(id)).withSelfRel());
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(itemsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<?> getOneItem(@PathVariable(value = "id") UUID id) {
        try {
            Optional<ItemModel> item0 = itemRepository.findById(id);
            if (item0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
            }
            item0.get().add(linkTo(methodOn(ItemController.class).getAllItems()).withSelfRel());
            return ResponseEntity.status(HttpStatus.OK).body(item0.get());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<?> updateItem(@PathVariable(value = "id") UUID id, @RequestBody @Valid ItemRecordDto itemRecordDto) {
        try {
            Optional<ItemModel> item0 = itemRepository.findById(id);
            if (item0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
            }
            var itemModel = item0.get();
            BeanUtils.copyProperties(itemRecordDto, itemModel);

            Optional<ShoppingListsModel> shoppingListOptional = shoppingListsRepository.findById(itemRecordDto.shoppingListId());
            if (shoppingListOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
            }

            itemModel.setShoppingList(shoppingListOptional.get());

            return ResponseEntity.status(HttpStatus.OK).body(itemRepository.save(itemModel));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable(value = "id") UUID id) {
        try {
            Optional<ItemModel> item0 = itemRepository.findById(id);
            if (item0.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
            }
            itemRepository.delete(item0.get());
            return ResponseEntity.status(HttpStatus.OK).body("Item deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
