package me.fadouache.billingservice.services;

import lombok.AllArgsConstructor;
import me.fadouache.billingservice.entities.Bill;

import me.fadouache.billingservice.entities.ProductItem;
import me.fadouache.billingservice.feign.CustomerRestClient;
import me.fadouache.billingservice.feign.ProductItemRestClient;
import me.fadouache.billingservice.models.Customer;
import me.fadouache.billingservice.models.Product;
import me.fadouache.billingservice.repositories.BillRepository;
import me.fadouache.billingservice.repositories.ProductItemRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;


@Service
@AllArgsConstructor
public class BillsEventService {

    private BillRepository br;
    private ProductItemRepository pir;
    private CustomerRestClient crc;
    private ProductItemRestClient prc;
    @Bean
    public Consumer<Bill> BillsEventConsumer()
    {
        return (input) -> {
            Customer customer = crc.getCustomer(1L);
            Bill bill = new Bill(null, new Date(), customer.getId(), null, null);
            br.save(bill);
            PagedModel<Product> productsPage = prc.pageProducts(0, 20);
            productsPage.forEach(p -> {
                ProductItem productItem = new ProductItem();
                productItem.setPrice(p.getPrice());
                productItem.setProductId(p.getId());
                productItem.setQuantity(new Random().nextInt(50) + 1);
                productItem.setBill(bill);
                pir.save(productItem);
            });
            System.out.println("--------------------------");
            System.out.println(input.toString());
            System.out.println("--------------------------");
        };
    }







}
