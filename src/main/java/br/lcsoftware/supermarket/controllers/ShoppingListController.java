package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.ItemResponseRecordDto;
import br.lcsoftware.supermarket.dtos.ShoppingListRecordDto;
import br.lcsoftware.supermarket.dtos.ShoppingListResponseRecordDto;
import br.lcsoftware.supermarket.dtos.UserResponseRecordDto;
import br.lcsoftware.supermarket.models.ShoppingListModel;
import br.lcsoftware.supermarket.models.UserModel;
import br.lcsoftware.supermarket.repositories.ShoppingListRepository;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shopping-lists")
public class ShoppingListController {

    @Autowired
    ShoppingListRepository shoppingListsRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> saveShoppingList(@RequestBody @Valid ShoppingListRecordDto shoppingListsRecordDto) {
        var shoppingListsModel = new ShoppingListModel();
        BeanUtils.copyProperties(shoppingListsRecordDto, shoppingListsModel);

        Optional<UserModel> userOptional = userRepository.findById(shoppingListsRecordDto.userId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        shoppingListsModel.setUser(userOptional.get());
        shoppingListsRepository.save(shoppingListsModel);

        var responseDto = new ShoppingListResponseRecordDto(
                shoppingListsModel.getId(),
                shoppingListsModel.getName(),
                shoppingListsModel.getItems().stream().map(item ->
                        new ItemResponseRecordDto(
                                item.getId(),
                                item.getShoppingList().getId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getUnitPrice()
                        )
                ).collect(Collectors.toList()),
                new UserResponseRecordDto(
                        shoppingListsModel.getUser().getId(),
                        shoppingListsModel.getUser().getEmail(),
                        shoppingListsModel.getUser().getName()
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllShoppingLists() {

        List<ShoppingListResponseRecordDto> shoppingListsList = shoppingListsRepository.findAll().stream().map(shoppingList ->
                new ShoppingListResponseRecordDto(
                        shoppingList.getId(),
                        shoppingList.getName(),
                        shoppingList.getItems().stream().map(item ->
                                new ItemResponseRecordDto(
                                        item.getId(),
                                        item.getShoppingList().getId(),
                                        item.getName(),
                                        item.getQuantity(),
                                        item.getUnitPrice()
                                )
                        ).collect(Collectors.toList()),
                        new UserResponseRecordDto(
                                shoppingList.getUser().getId(),
                                shoppingList.getUser().getEmail(),
                                shoppingList.getUser().getName()
                        )
                )
        ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(shoppingListsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneShoppingList(@PathVariable(value = "id") UUID id) {
        Optional<ShoppingListModel> shoppingList0 = shoppingListsRepository.findById(id);
        if (shoppingList0.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping list not found");
        }
        ShoppingListModel shoppingList = shoppingList0.get();
        var responseDto = new ShoppingListResponseRecordDto(
                shoppingList.getId(),
                shoppingList.getName(),
                shoppingList.getItems().stream().map(item ->
                        new ItemResponseRecordDto(
                                item.getId(),
                                item.getShoppingList().getId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getUnitPrice()
                        )
                ).collect(Collectors.toList()),
                new UserResponseRecordDto(
                        shoppingList.getUser().getId(),
                        shoppingList.getUser().getEmail(),
                        shoppingList.getUser().getName()
                )
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShoppingList(@PathVariable(value = "id") UUID id, @RequestBody @Valid ShoppingListRecordDto shoppingListsRecordDto) {
        Optional<ShoppingListModel> shoppingList0 = shoppingListsRepository.findById(id);
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
        shoppingListsRepository.save(shoppingListsModel);

        var responseDto = new ShoppingListResponseRecordDto(
                shoppingListsModel.getId(),
                shoppingListsModel.getName(),
                shoppingListsModel.getItems().stream().map(item ->
                        new ItemResponseRecordDto(
                                item.getId(),
                                item.getShoppingList().getId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getUnitPrice()
                        )
                ).collect(Collectors.toList()),
                new UserResponseRecordDto(
                        shoppingListsModel.getUser().getId(),
                        shoppingListsModel.getUser().getEmail(),
                        shoppingListsModel.getUser().getName()
                )
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShoppingList(@PathVariable(value = "id") UUID id) {
        Optional<ShoppingListModel> shoppingList0 = shoppingListsRepository.findById(id);
        if (shoppingList0.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shopping list not found");
        }
        shoppingListsRepository.delete(shoppingList0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Shopping list deleted successfully");
    }
}
