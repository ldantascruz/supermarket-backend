package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.ItemRecordDto;
import br.lcsoftware.supermarket.dtos.ItemResponseRecordDto;
import br.lcsoftware.supermarket.models.ItemModel;
import br.lcsoftware.supermarket.models.ShoppingListModel;
import br.lcsoftware.supermarket.repositories.ItemRepository;
import br.lcsoftware.supermarket.repositories.ShoppingListRepository;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ShoppingListRepository shoppingListsRepository;

    @PostMapping
    public ResponseEntity<?> saveItem(@RequestBody @Valid ItemRecordDto itemRecordDto) {
        var itemModel = new ItemModel();
        BeanUtils.copyProperties(itemRecordDto, itemModel);

        Optional<ShoppingListModel> shoppingListOptional = shoppingListsRepository.findById(itemRecordDto.shoppingListId());
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }

        itemModel.setShoppingList(shoppingListOptional.get());
        itemRepository.save(itemModel);

        var responseDto = new ItemResponseRecordDto(
                itemModel.getId(),
                itemModel.getShoppingList().getId(),
                itemModel.getName(),
                itemModel.getQuantity(),
                itemModel.getUnitPrice()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllItems() {
        List<ItemResponseRecordDto> itemsList = itemRepository.findAll().stream().map(item ->
                new ItemResponseRecordDto(
                        item.getId(),
                        item.getShoppingList().getId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getUnitPrice()
                )
        ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(itemsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneItem(@PathVariable(value = "id") UUID id) {
        Optional<ItemModel> item0 = itemRepository.findById(id);
        if (item0.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        ItemModel item = item0.get();
        var responseDto = new ItemResponseRecordDto(
                item.getId(),
                item.getShoppingList().getId(),
                item.getName(),
                item.getQuantity(),
                item.getUnitPrice()
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable(value = "id") UUID id, @RequestBody @Valid ItemRecordDto itemRecordDto) {
        Optional<ItemModel> item0 = itemRepository.findById(id);
        if (item0.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        var itemModel = item0.get();
        BeanUtils.copyProperties(itemRecordDto, itemModel);

        Optional<ShoppingListModel> shoppingListOptional = shoppingListsRepository.findById(itemRecordDto.shoppingListId());
        if (shoppingListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shopping list not found");
        }

        itemModel.setShoppingList(shoppingListOptional.get());
        itemRepository.save(itemModel);

        var responseDto = new ItemResponseRecordDto(
                itemModel.getId(),
                itemModel.getShoppingList().getId(),
                itemModel.getName(),
                itemModel.getQuantity(),
                itemModel.getUnitPrice()
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable(value = "id") UUID id) {
        Optional<ItemModel> item0 = itemRepository.findById(id);
        if (item0.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        itemRepository.delete(item0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Item deleted successfully");
    }
}
