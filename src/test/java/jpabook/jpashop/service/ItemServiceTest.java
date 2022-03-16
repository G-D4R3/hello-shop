package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;

    @Test
    public void 상품_등록() {
        // given
        Item book = new Book();
        book.setName("book1");
        book.setPrice(2000);

        // when
        itemService.saveItem(book);

        // then
        assertEquals(book, itemService.findOne(book.getId()));
    }

    @Test
    @Rollback(false)
    public void 상품_스톡_수정() {
        // given
        Item item = new Book();
        item.setName("book1");
        item.setPrice(2000);
        item.setStockQuantity(0);
        itemService.saveItem(item);

        // when
        Item findItem = itemService.findOne(item.getId());
        findItem.addStock(3);

        // then
        Assertions.assertThat(findItem.getStockQuantity()).isEqualTo(3);

    }

}