package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long itemId, Book book) {

        // 영속성 엔티티의 변경감지 사용
        Item findItem = itemRepository.findOne(itemId);

        // findItem.change(price, stockQuantity, name); 등 의미있는 메소드로 작성하는 편이 좋다.

        findItem.setPrice(book.getPrice());
        findItem.setName(book.getName());
        findItem.setStockQuantity(book.getStockQuantity());

        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
}
