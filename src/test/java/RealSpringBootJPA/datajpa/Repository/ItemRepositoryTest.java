package RealSpringBootJPA.datajpa.Repository;

import RealSpringBootJPA.datajpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemRepositoryTest {
    @Autowired ItemRepository itemRepository;

    @Test
    public void save(){
        Item item = new Item();
        itemRepository.save(item);
    }

}