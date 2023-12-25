package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.smart.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	//pagination method......
	
	
	@Query("from Contact as c where c.user.id =:userId")
	public List<Contact> findContactByUserId(@Param("userId")int userId);
	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsPerPageByUser(@Param("userId") int userId, Pageable pageable);
}
