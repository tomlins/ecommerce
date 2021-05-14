package net.tomlins.udacity.p4.ecommerce.controllers;

import net.tomlins.udacity.p4.ecommerce.model.persistence.Item;
import net.tomlins.udacity.p4.ecommerce.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController = new ItemController();
    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {

        Item item_one = new Item();
        item_one.setId(1L);
        item_one.setName("apples");
        item_one.setDescription("Red rosy apples");
        item_one.setPrice(BigDecimal.valueOf(5.00));

        Item item_two = new Item();
        item_two.setId(1L);
        item_two.setName("coconut");
        item_two.setDescription("tough and milky");
        item_two.setPrice(BigDecimal.valueOf(6.50));

        List<Item> itemList = Arrays.asList(item_one, item_two);

        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item_one));
        when(itemRepository.findByName("coconut")).thenReturn(Arrays.asList(item_two));
        when(itemRepository.findAll()).thenReturn(itemList);

    }

    @Test
    public void getItems_success() {
        ResponseEntity<List<Item>> items = itemController.getItems();
        assertNotNull(items);
        assertEquals(2, items.getBody().size());
        assertEquals("apples", items.getBody().get(0).getName());
        assertEquals("coconut", items.getBody().get(1).getName());
    }

    @Test
    public void getItemById_success() {
        ResponseEntity<Item> item = itemController.getItemById(1L);
        assertNotNull(item);
        assertEquals("apples", item.getBody().getName());
    }

    @Test
    public void getItemById_fail() {
        ResponseEntity<Item> item = itemController.getItemById(999L);
        assertNotNull(item);
        assertEquals(404, item.getStatusCodeValue());
    }

    @Test
    public void getItemsByName_success() {
        ResponseEntity<List<Item>> item = itemController.getItemsByName("coconut");
        assertNotNull(item);
        assertEquals("coconut", item.getBody().get(0).getName());
    }

    @Test
    public void getItemsByName_fail() {
        ResponseEntity<List<Item>> item = itemController.getItemsByName("chocolate");
        assertNotNull(item);
        assertEquals(404, item.getStatusCodeValue());
    }

}
