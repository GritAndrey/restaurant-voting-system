package ru.andreygri.restaurantvotingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.andreygri.restaurantvotingsystem.model.Vote;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
}