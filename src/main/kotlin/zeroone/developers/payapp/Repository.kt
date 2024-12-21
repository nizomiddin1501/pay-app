package zeroone.developers.payapp

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun findByIdAndDeletedFalse(id: Long): T?
    fun trash(id: Long): T?
    fun trashList(ids: List<Long>): List<T?>
    fun findAllNotDeleted(): List<T>
    fun findAllNotDeleted(pageable: Pageable): List<T>
    fun findAllNotDeletedForPageable(pageable: Pageable): Page<T>
    fun saveAndRefresh(t: T): T
}

class BaseRepositoryImpl<T : BaseEntity>(
    entityInformation: JpaEntityInformation<T, Long>,
    private val entityManager: EntityManager
) : SimpleJpaRepository<T, Long>(entityInformation, entityManager), BaseRepository<T> {

    val isNotDeletedSpecification = Specification<T> { root, _, cb -> cb.equal(root.get<Boolean>("deleted"), false) }

    override fun findByIdAndDeletedFalse(id: Long) = findByIdOrNull(id)?.run { if (deleted) null else this }

    @Transactional
    override fun trash(id: Long): T? = findByIdOrNull(id)?.run {
        deleted = true
        save(this)
    }

    override fun findAllNotDeleted(): List<T> = findAll(isNotDeletedSpecification)
    override fun findAllNotDeleted(pageable: Pageable): List<T> = findAll(isNotDeletedSpecification, pageable).content
    override fun findAllNotDeletedForPageable(pageable: Pageable): Page<T> =
        findAll(isNotDeletedSpecification, pageable)

    override fun trashList(ids: List<Long>): List<T?> = ids.map { trash(it) }

    @Transactional
    override fun saveAndRefresh(t: T): T {
        return save(t).apply { entityManager.refresh(this) }
    }
}


@Repository
interface CategoryRepository : BaseRepository<Category> {

    @Query(value = "select count(*) > 0 from category c where c.name = :name", nativeQuery = true)
    fun existsByName(@Param("name") name: String): Boolean

    fun findByNameAndDeletedFalse(ame: String): Category?

    @Query("""
        select c from category c
        where c.id != :id
        and c.name = :name
        and c.deleted = false 
    """)
    fun findByName(id: Long, name: String): Category?

    // Category ID exists check
    @Query(value = "select count(*) > 0 from category c where c.id = :id", nativeQuery = true)
    fun existsByCategoryId(@Param("id") id: Long?): Boolean

}

@Repository
interface ProductRepository : BaseRepository<Product> {

    @Query(value = "select count(*) > 0 from product p where p.name = :name", nativeQuery = true)
    fun existsByName(@Param("name") name: String): Boolean

    fun findByNameAndDeletedFalse(ame: String): Product?

    @Query("""
        select p from product p
        where p.id != :id
        and p.name = :name
        and p.deleted = false 
    """)
    fun findByName(id: Long, name: String): Product?

    // Product ID exists check
    @Query(value = "select count(*) > 0 from product p where p.id = :id", nativeQuery = true)
    fun existsByProductId(@Param("id") id: Long?): Boolean

    @Query("select * from product p where p.id = :productId and deleted is false", nativeQuery = true)
    fun findByProductId(@Param("productId") productId: Long): Product?


}

@Repository
interface TransactionRepository : BaseRepository<Transaction> {

    // Transaction ID exists check
    @Query(value = "select count(*) > 0 from transaction t where t.id = :id", nativeQuery = true)
    fun existsByTransactionId(@Param("id") id: Long?): Boolean

}

@Repository
interface TransactionItemRepository : BaseRepository<TransactionItem> {

    @Query("select t from transaction t where t.id = :transactionId")
    fun findByTransactionId(@Param("transactionId") transactionId: Long): Optional<Transaction>

    @Query(value = "select sum(count) from transaction_item where transaction_id = :transactionId", nativeQuery = true)
    fun getTotalCountByTransactionId(transactionId: Long): Long
}



@Repository
interface UserPaymentTransactionRepository : BaseRepository<UserPaymentTransaction> {

    @Query("select u from users u where u.id = :userId")
    fun findByUserId(@Param("userId") userId: Long): Optional<User>

}

@Repository
interface UserRepository : BaseRepository<User> {
    fun findByUsernameAndDeletedFalse(username: String): User?

    @Query("""
        select u from users u
        where u.id != :id
        and u.username = :username
        and u.deleted = false 
    """)
    fun findByUsername(id: Long, username: String): User?

    @Query("select * from users u where u.id = :userId and deleted = false", nativeQuery = true)
    fun findByUserId(@Param("userId") userId: Long): User?

    // User ID exists check
    @Query(value = "select count(*) > 0 from users u where u.id = :id", nativeQuery = true)
    fun existsByUserId(@Param("id") id: Long?): Boolean
}



