package com.conanthelibrarian.librarymanagementsystem.repository;

import com.conanthelibrarian.librarymanagementsystem.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    //todo Query personalizada:
    //- Recupera todos los usuarios que tienen prestados más de X libros.
}
