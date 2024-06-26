package br.lcsoftware.supermarket.repositories;

import br.lcsoftware.supermarket.models.ItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, UUID> {
}
