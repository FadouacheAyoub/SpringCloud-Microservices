package me.fadouache.billingservice.web;

import lombok.AllArgsConstructor;
import me.fadouache.billingservice.entities.Bill;
import me.fadouache.billingservice.feign.CustomerRestClient;
import me.fadouache.billingservice.feign.ProductItemRestClient;
import me.fadouache.billingservice.models.Customer;
import me.fadouache.billingservice.models.Product;
import me.fadouache.billingservice.repositories.BillRepository;
import me.fadouache.billingservice.repositories.ProductItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController @AllArgsConstructor
public class BillingRestController {

    private BillRepository br;
    private ProductItemRepository pir;
    private CustomerRestClient crc;
    private ProductItemRestClient prc;
    @GetMapping("/bills/{id}")
    public Bill getBill(@PathVariable Long id)
    {
        Bill bill = br.findById(id).orElseThrow(() -> new RuntimeException(String.format("bill - %s - not found", id)));
        Customer customer = crc.getCustomer(bill.getCustomerId());
        bill.getProductItems().forEach(p -> {
            Product product = prc.getProductById(p.getProductId());
            p.setProduct(product);
        });
        bill.setCustomer(customer);
        return bill;
    }

    @GetMapping("/bills")
    public Collection<Bill> getBills()
    {
        Collection<Bill> bills = br.findAll();
        bills.forEach( b -> {
            Customer customer = crc.getCustomer(b.getCustomerId());
            b.setCustomer(customer);
            b.getProductItems().forEach(p -> {
                Product product = prc.getProductById(p.getProductId());
                p.setProduct(product);
            });
        });

        return bills;
    }
}
