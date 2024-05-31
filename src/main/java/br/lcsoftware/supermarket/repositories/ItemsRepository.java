package br.lcsoftware.supermarket.repositories;

import br.lcsoftware.supermarket.models.ItemsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemsRepository extends JpaRepository<ItemsModel, UUID> {
}