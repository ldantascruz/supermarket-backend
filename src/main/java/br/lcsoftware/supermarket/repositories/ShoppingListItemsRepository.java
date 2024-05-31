package br.lcsoftware.supermarket.repositories;

import br.lcsoftware.supermarket.models.ShoppingListItemsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShoppingListItemsRepository extends JpaRepository<ShoppingListItemsModel, UUID> {
}
