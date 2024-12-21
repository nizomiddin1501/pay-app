package zeroone.developers.payapp

import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*


interface UserService {
    fun getAll(page: Int, size: Int): Page<UserResponse>
    fun getAll(): List<UserResponse>
    fun getOne(id: Long): UserResponse
    fun create(request: UserCreateRequest)
    fun update(id: Long, request: UserUpdateRequest)
    fun delete(id: Long)
}

interface CategoryService {
    fun getAll(page: Int, size: Int): Page<CategoryResponse>
    fun getAll(): List<CategoryResponse>
    fun getOne(id: Long): CategoryResponse
    fun create(request: CategoryCreateRequest)
    fun update(id: Long, request: CategoryUpdateRequest)
    fun delete(id: Long)
}

interface ProductService {
    fun getAll(page: Int, size: Int): Page<ProductResponse>
    fun getAll(): List<ProductResponse>
    fun getOne(id: Long): ProductResponse
    fun create(request: ProductCreateRequest)
    fun update(id: Long, request: ProductUpdateRequest, transactionId: Long)
    fun delete(id: Long)
}

interface TransactionItemService {
    fun getAll(page: Int, size: Int): Page<TransactionItemResponse>
    fun getAll(): List<TransactionItemResponse>
    fun getOne(id: Long): TransactionItemResponse
    fun create(request: TransactionItemCreateRequest)
    fun update(id: Long, request: TransactionItemUpdateRequest)
    fun delete(id: Long)
}

interface TransactionService {
    fun getAll(page: Int, size: Int): Page<TransactionResponse>
    fun getAll(): List<TransactionResponse>
    fun getOne(id: Long): TransactionResponse
    fun create(request: TransactionCreateRequest)
    fun update(id: Long, request: TransactionUpdateRequest)
    fun delete(id: Long)
}

interface UserPaymentTransactionService {
    fun getAll(page: Int, size: Int): Page<UserPaymentTransactionResponse>
    fun getAll(): List<UserPaymentTransactionResponse>
    fun getOne(id: Long): UserPaymentTransactionResponse
    fun create(request: UserPaymentTransactionCreateRequest, transactionId: Long)
    fun update(id: Long, request: UserPaymentTransactionUpdateRequest)
    fun delete(id: Long)
}

interface PurchaseService{
    fun processRequest(request: PurchaseRequest)
}

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val entityManager: EntityManager,
    private val categoryMapper: CategoryMapper
) : CategoryService {

    override fun getAll(page: Int, size: Int): Page<CategoryResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val categoriesPage = categoryRepository.findAllNotDeletedForPageable(pageable)
        return categoriesPage.map { categoryMapper.toDto(it) }
    }

    override fun getAll(): List<CategoryResponse> {
        return categoryRepository.findAllNotDeleted().map {
            categoryMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): CategoryResponse {
        categoryRepository.findByIdAndDeletedFalse(id)?.let {
            return categoryMapper.toDto(it)
        } ?: throw CategoryNotFoundException()
    }

    override fun create(request: CategoryCreateRequest) {
        val category = categoryRepository.findByNameAndDeletedFalse(request.name)
        if (category != null) throw CategoryAlreadyExistsException()
        categoryRepository.save(categoryMapper.toEntity(request))
    }

    override fun update(id: Long, request: CategoryUpdateRequest) {
        val category = categoryRepository.findByIdAndDeletedFalse(id) ?: throw CategoryNotFoundException()
        categoryRepository.findByName(id, request.name)?.let { throw CategoryAlreadyExistsException() }
        val updateCategory = categoryMapper.updateEntity(category, request)
        categoryRepository.save(updateCategory)
    }

    @Transactional
    override fun delete(id: Long) {
        categoryRepository.trash(id) ?: throw CategoryNotFoundException()
    }
}

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val entityManager: EntityManager,
    private val productMapper: ProductMapper,
    private val categoryRepository: CategoryRepository,
    private val transactionItemRepository: TransactionItemRepository
) : ProductService {

    override fun getAll(page: Int, size: Int): Page<ProductResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val productsPage = productRepository.findAllNotDeletedForPageable(pageable)
        return productsPage.map { productMapper.toDto(it) }
    }

    override fun getAll(): List<ProductResponse> {
        return productRepository.findAllNotDeleted().map {
            productMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): ProductResponse {
        productRepository.findByIdAndDeletedFalse(id)?.let {
            return productMapper.toDto(it)
        } ?: throw ProductNotFoundException()
    }

    override fun create(request: ProductCreateRequest) {
        val existingProduct = productRepository.findByNameAndDeletedFalse(request.name)
        if (existingProduct != null) throw ProductAlreadyExistsException()
        val existsByCategoryId = categoryRepository.existsByCategoryId(request.categoryId)
        if (!existsByCategoryId) throw CategoryNotFoundException()
        val referenceCategory = entityManager.getReference(
            Category::class.java, request.categoryId
        )
        productRepository.save(productMapper.toEntity(request, referenceCategory))
    }

    //Process 4.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun update(id: Long, request: ProductUpdateRequest, transactionId: Long) {
        val product = productRepository.findByIdAndDeletedFalse(id) ?: throw ProductNotFoundException()
        val totalPurchasedCount = transactionItemRepository.getTotalCountByTransactionId(transactionId)
        if (product.count < totalPurchasedCount) {
            throw ProductNotEnoughException()
        }
        product.count -= totalPurchasedCount.toInt()
        request.name.let {
            val findByName = productRepository.findByName(id, it)
            if (findByName != null) throw ProductAlreadyExistsException()
            product.name = it
        }
        val updateOrder = productMapper.updateEntity(product, request)
        productRepository.save(updateOrder)
    }

    @Transactional
    override fun delete(id: Long) {
        productRepository.trash(id) ?: throw ProductNotFoundException()
    }
}

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val transactionMapper: TransactionMapper,
    private val entityManager: EntityManager,
    private val userRepository: UserRepository
) : TransactionService {

    override fun getAll(page: Int, size: Int): Page<TransactionResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val transactionsPage = transactionRepository.findAllNotDeletedForPageable(pageable)
        return transactionsPage.map { transactionMapper.toDto(it) }
    }

    override fun getAll(): List<TransactionResponse> {
        return transactionRepository.findAllNotDeleted().map {
            transactionMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): TransactionResponse {
        transactionRepository.findByIdAndDeletedFalse(id)?.let {
            return transactionMapper.toDto(it)
        } ?: throw TransactionNotFoundException()
    }

    //Process 1.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun create(request: TransactionCreateRequest) {
        val existsByUserId = userRepository.existsByUserId(request.userId)
        if (!existsByUserId) throw UserNotFoundException()

        val referenceUser = entityManager.getReference(
            User::class.java, request.userId)
        transactionRepository.save(transactionMapper.toEntity(request, referenceUser))
    }

    override fun update(id: Long, request: TransactionUpdateRequest) {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun delete(id: Long) {
        transactionRepository.trash(id) ?: throw TransactionNotFoundException()
    }
}

@Service
class TransactionItemServiceImpl(
    private val transactionItemRepository: TransactionItemRepository,
    private val transactionRepository: TransactionRepository,
    private val transactionItemMapper: TransactionItemMapper,
    private val productRepository: ProductRepository,
    private val entityManager: EntityManager
) : TransactionItemService {

    override fun getAll(page: Int, size: Int): Page<TransactionItemResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val transactionItemsPage = transactionItemRepository.findAllNotDeletedForPageable(pageable)
        return transactionItemsPage.map { transactionItemMapper.toDto(it) }
    }

    override fun getAll(): List<TransactionItemResponse> {
        return transactionItemRepository.findAllNotDeleted().map {
            transactionItemMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): TransactionItemResponse {
        transactionItemRepository.findByIdAndDeletedFalse(id)?.let {
            return transactionItemMapper.toDto(it)
        } ?: throw TransactionItemNotFoundException()
    }


    //Process 2.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun create(request: TransactionItemCreateRequest) {
        val product = productRepository.findByProductId(request.productId) ?: throw ProductNotFoundException()
        if (product.count < request.count) {
            throw ProductNotEnoughException()
        }
        val existsByTransactionId = transactionRepository.existsByTransactionId(request.transactionId)
        if (!existsByTransactionId) throw TransactionNotFoundException()

        val referenceProduct = entityManager.getReference(
            Product::class.java, request.productId)
        val referenceTransaction = entityManager.getReference(
            Transaction::class.java, request.transactionId)

        transactionItemRepository.save(transactionItemMapper.toEntity(request, referenceProduct, referenceTransaction))
    }

    override fun update(id: Long, request: TransactionItemUpdateRequest) {
        val transactionItem =
            transactionItemRepository.findByIdAndDeletedFalse(id) ?: throw TransactionItemNotFoundException()
        request.count.let { transactionItem.count = it }
        request.amount.let { transactionItem.amount = it }
        request.totalAmount.let { transactionItem.totalAmount = it }
        val updateItem = transactionItemMapper.updateEntity(transactionItem, request)
        transactionItemRepository.save(updateItem)
    }

    @Transactional
    override fun delete(id: Long) {
        transactionItemRepository.trash(id) ?: throw TransactionItemNotFoundException()
    }
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserService {

    override fun getAll(page: Int, size: Int): Page<UserResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val usersPage = userRepository.findAllNotDeletedForPageable(pageable)
        return usersPage.map { userMapper.toDto(it) }
    }

    override fun getAll(): List<UserResponse> {
        return userRepository.findAllNotDeleted().map {
            userMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): UserResponse {
        userRepository.findByIdAndDeletedFalse(id)?.let {
            return userMapper.toDto(it)
        } ?: throw UserNotFoundException()
    }

    override fun create(request: UserCreateRequest) {
        val existingUser = userRepository.findByUsernameAndDeletedFalse(request.username)
        if (existingUser != null) throw UserAlreadyExistsException()
        userRepository.save(userMapper.toEntity(request))
    }

    override fun update(id: Long, request: UserUpdateRequest) {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()
        userRepository.findByUsername(id, request.username)?.let { throw UserAlreadyExistsException() }

        val updateUser = userMapper.updateEntity(user, request)
        userRepository.save(updateUser)
    }

    @Transactional
    override fun delete(id: Long) {
        userRepository.trash(id) ?: throw UserNotFoundException()
    }
}

@Service
class UserPaymentTransactionServiceImpl(
    private val userPaymentTransactionRepository: UserPaymentTransactionRepository,
    private val userPaymentTransactionMapper: UserPaymentTransactionMapper,
    private val transactionItemRepository: TransactionItemRepository,
    private val userRepository: UserRepository,
    private val entityManager: EntityManager
) : UserPaymentTransactionService {

    override fun getAll(page: Int, size: Int): Page<UserPaymentTransactionResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val userPaymentsPage = userPaymentTransactionRepository.findAllNotDeletedForPageable(pageable)
        return userPaymentsPage.map { userPaymentTransactionMapper.toDto(it) }
    }

    override fun getAll(): List<UserPaymentTransactionResponse> {
        return userPaymentTransactionRepository.findAllNotDeleted().map {
            userPaymentTransactionMapper.toDto(it)
        }
    }

    override fun getOne(id: Long): UserPaymentTransactionResponse {
        userPaymentTransactionRepository.findByIdAndDeletedFalse(id)?.let {
            return userPaymentTransactionMapper.toDto(it)
        } ?: throw UserPaymentTransactionNotFoundException()
    }

    //Process 3.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun create(request: UserPaymentTransactionCreateRequest, transactionId: Long) {
        val user = userRepository.findByUserId(request.userId) ?: throw UserNotFoundException()

        val totalTransactionAmount = transactionItemRepository.getTotalCountByTransactionId(transactionId)
        if (user.balance < totalTransactionAmount.toBigDecimal()) throw InvalidBalanceException()

        user.balance = user.balance.subtract(totalTransactionAmount.toBigDecimal())
        userRepository.save(user)
        userPaymentTransactionRepository.save(userPaymentTransactionMapper.toEntity(request, user))
    }

    override fun update(id: Long, request: UserPaymentTransactionUpdateRequest) {
        val userPaymentTransaction = userPaymentTransactionRepository.findByIdAndDeletedFalse(id)
            ?: throw UserPaymentTransactionNotFoundException()
        request.amount.let { userPaymentTransaction.amount = it }
        val updateUserPayment = userPaymentTransactionMapper.updateEntity(userPaymentTransaction, request)
        userPaymentTransactionRepository.save(updateUserPayment)
    }

    @Transactional
    override fun delete(id: Long) {
        userPaymentTransactionRepository.trash(id) ?: throw UserPaymentTransactionNotFoundException()
    }
}


@Service
class PurchaseServiceImpl(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository,
    private val transactionItemRepository: TransactionItemRepository,
    private val userPaymentTransactionRepository: UserPaymentTransactionRepository,
    private val entityManager: EntityManager,
    private val productMapper: ProductMapper
) : PurchaseService {


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun processRequest(request: PurchaseRequest) {
        var generatedTransactionId: Long? = null
        when {
            // Process 1 - Transaction Create
            request.userId != null && request.totalAmount != null && request.date != null -> {
                val existsByUserId = userRepository.existsByUserId(request.userId)
                if (!existsByUserId) throw UserNotFoundException()

                val referenceUser = entityManager.getReference(User::class.java, request.userId)
                val transaction = Transaction(
                    user = referenceUser,
                    totalAmount = request.totalAmount,
                    date = request.date
                )
                val savedTransaction = transactionRepository.save(transaction)
                generatedTransactionId = savedTransaction.id
            }

            // Process 2 - Transaction Item Create
            request.productId != null && request.count != null && request.amount != null -> {
                if (generatedTransactionId == null) throw TransactionNotFoundException()

                val product = productRepository.existsByProductId(request.productId) //?: throw ProductNotFoundException()
                if(!product) throw ProductNotFoundException()

                val referenceProduct = entityManager.getReference(Product::class.java, request.productId)
                val referenceTransaction = entityManager.getReference(Transaction::class.java, generatedTransactionId)
                if (referenceProduct.count < request.count) {
                    throw ProductNotEnoughException()
                }

                val transactionItem = TransactionItem(
                    product = referenceProduct,
                    transaction = referenceTransaction,
                    count = request.count,
                    amount = request.amount,
                    totalAmount = request.totalAmountItem!!
                )
                transactionItemRepository.save(transactionItem)
            }

            // Process 3 - User Payment Transaction Create
            request.userId != null -> {
                if (generatedTransactionId == null) throw TransactionNotFoundException()
                val user = userRepository.findByUserId(request.userId) ?: throw UserNotFoundException()

                val referenceUser = entityManager.getReference(User::class.java, request.userId)

                val totalTransactionAmount = transactionItemRepository.getTotalCountByTransactionId(generatedTransactionId)
                if (user.balance < totalTransactionAmount.toBigDecimal()) throw InvalidBalanceException()

                user.balance = user.balance.subtract(totalTransactionAmount.toBigDecimal())
                userRepository.save(user)

                val userPaymentTransaction = UserPaymentTransaction(
                    user = referenceUser,
                    amount = totalTransactionAmount.toBigDecimal(),
                    date = Date()
                )
                userPaymentTransactionRepository.save(userPaymentTransaction)
            }

            // Process 4 - Product Update
            request.productId != null && request.count != null -> {
                val product = productRepository.findByIdAndDeletedFalse(request.productId) ?: throw ProductNotFoundException()
                val totalPurchasedCount = transactionItemRepository.getTotalCountByTransactionId(generatedTransactionId!!)
                if (product.count < totalPurchasedCount) {
                    throw ProductNotEnoughException()
                }
                product.count -= totalPurchasedCount.toInt()
                val updatedProduct = ProductUpdateRequest(
                    name = product.name,
                    count = product.count - totalPurchasedCount.toInt())
                productRepository.save(productMapper.updateEntity(product,updatedProduct))
            }
            else -> throw IllegalArgumentException("Invalid request data")
        }
    }
}





