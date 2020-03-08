package pl.komjago.ticketapp.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.komjago.ticketapp.entity.Movie

@Repository
interface MovieRepository : JpaRepository<Movie, Long?>