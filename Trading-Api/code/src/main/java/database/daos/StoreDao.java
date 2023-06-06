package database.daos;

import database.dtos.InventoryDto;
import database.dtos.MemberDto;
import database.dtos.NotificationDto;
import database.dtos.StoreDto;

import java.util.List;

public class StoreDao {

    public StoreDto getStoreById(int id){
        return (StoreDto) DaoTemplate.getById(StoreDto.class, id);
    }

    public List<InventoryDto> getStoreProducts(int id){
        List<InventoryDto> products = (List<InventoryDto>)DaoTemplate.getListById(InventoryDto.class, id, "InventoryDto", "storeDto.id");
        for(InventoryDto inventoryDto : products){

        }
        return products;
    }

    public InventoryDto getStoreProduct(int storeId, int productId){
        List<InventoryDto> products = (List<InventoryDto>)DaoTemplate.getListByCompositeKey(InventoryDto.class, storeId, productId, "InventoryDto", "storeDto.id", "productId");
        return products.get(0);
    }
}
