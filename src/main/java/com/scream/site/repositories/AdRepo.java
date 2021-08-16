package com.scream.site.repositories;

import com.scream.site.models.Ad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepo extends CrudRepository<Ad,Long> {
    Ad findById(long id);
    Iterable<Ad> findAllByTitle(String title);
    Iterable<Ad> findAllByTitleAndCategory(String title, String category);
    Iterable<Ad> findAllByCategory(String category);
    Iterable<Ad> findAllByAuthorId(long id);
}

