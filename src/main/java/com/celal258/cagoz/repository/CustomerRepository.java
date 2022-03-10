package com.celal258.cagoz.repository;

import com.celal258.cagoz.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;



public interface CustomerRepository extends CrudRepository <Customer,Long> {


    @Query(value = "SELECT p.first_name FROM customer p WHERE p.id = :id",nativeQuery = true)
    String[] findFirstNameById(Long id);

    @Query(value = "SELECT id,first_name,number,company,case when musteriBorc.borc is NULL then 0 Else musteriBorc.borc END as debt,customer_description from customer LEFT OUTER join (SELECT customer_id,(sum(price)- sum(received_money)) AS borc FROM public.project GROUP By customer_id) As musteriBorc ON musteriBorc.customer_id=customer.id",nativeQuery = true)
    List<Object[]> findAllCustomer();

    @Query(value = "SELECT * from customer",nativeQuery = true)
    Collection<Customer> findAllC();
}
