package edu.miu.cs.cs544.domain.adapter;

import edu.miu.cs.cs544.domain.Item;
import edu.miu.cs.cs544.domain.dto.ItemDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemAdapter {

    public static ItemDTO getItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO(item.getId(),
                item.getOccupants(),
                item.getCheckinDate(),
                item.getCheckoutDate(),
                ProductAdapter.getProductDTO(item.getProduct()),
                AuditDataAdapter.getAuditDataDTO(item.getAuditData())
        );
        return itemDTO;
    }

    public static Item getItem(ItemDTO itemDTO) {
        return new Item(itemDTO.getId(),
                itemDTO.getOccupants(),
                itemDTO.getCheckinDate(),
                itemDTO.getCheckoutDate(),
//                null,
                ProductAdapter.getProduct(itemDTO.getProduct()),
                AuditDataAdapter.getAuditData(itemDTO.getAuditData())
        );
    }

    public static List<Item> getItems(List<ItemDTO> itemDTOList) {
        if (itemDTOList == null)
            return new ArrayList<>();

        return itemDTOList.stream()
                .map(ItemAdapter::getItem)
                .collect(Collectors.toList());
    }

    public static List<ItemDTO> getItemsDTOs(List<Item> itemList) {
        if (itemList == null)
            return new ArrayList<>();

        return itemList.stream()
                .map(ItemAdapter::getItemDTO)
                .collect(Collectors.toList());
    }

}
