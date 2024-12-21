package zeroone.developers.payapp

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionHandler(private val errorMessageSource: ResourceBundleMessageSource) {

    @ExceptionHandler(PayCartExceptionHandler::class)
    fun handleAccountException(exception: PayCartExceptionHandler): ResponseEntity<BaseMessage> {
        return ResponseEntity.badRequest().body(exception.getErrorMessage(errorMessageSource))
    }
}


@RestController
@RequestMapping("/api/users")
class UserController(val service: UserService) {

    @Operation(summary = "Get all users", description = "Fetches all users from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all users"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all users with pagination", description = "Fetches users with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated users"))
    @GetMapping("/page")
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int) =
        service.getAll(page, size)


    @Operation(summary = "Get user by ID", description = "Fetches a single user based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully fetched the user"),
        ApiResponse(responseCode = "404", description = "User not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new user", description = "Creates a new user record.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "User successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: UserCreateRequest) = service.create(request)


    @Operation(summary = "Update existing user", description = "Updates an existing user based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "User successfully updated"),
        ApiResponse(responseCode = "404", description = "User not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: UserUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete user by ID", description = "Deletes a user based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "User successfully deleted"),
        ApiResponse(responseCode = "404", description = "User not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
class CategoryController(val service: CategoryService) {


    @Operation(summary = "Get all categories", description = "Fetches all categories from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all categories"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all categories with pagination", description = "Fetches categories with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated categories"))
    @GetMapping("/page")
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int) =
        service.getAll(page, size)


    @Operation(summary = "Get category by ID", description = "Fetches a single category based on the provided category ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully fetched the category"),
        ApiResponse(responseCode = "404", description = "Category not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new category", description = "Creates a new category record.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Category successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: CategoryCreateRequest) = service.create(request)


    @Operation(summary = "Update existing category", description = "Updates an existing category based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Category successfully updated"),
        ApiResponse(responseCode = "404", description = "Category not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: CategoryUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete category by ID", description = "Deletes a category based on the provided category ID.")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Category successfully deleted"),
        ApiResponse(responseCode = "404", description = "Category not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
class ProductController(val service: ProductService) {


    @Operation(summary = "Get all products", description = "Fetches all products from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all products"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all products with pagination", description = "Fetches products with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated products"))
    @GetMapping("/page")
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int) =
        service.getAll(page, size)


    @Operation(summary = "Get product by ID", description = "Fetches a single product based on the provided product ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully fetched the product"),
        ApiResponse(responseCode = "404", description = "Product not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new product", description = "Creates a new product record.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Product successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: ProductCreateRequest) = service.create(request)


    @Operation(summary = "Update existing product", description = "Updates an existing product based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Product successfully updated"),
        ApiResponse(responseCode = "404", description = "Product not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long,
               @RequestParam transactionId: Long,
               @RequestBody @Valid request: ProductUpdateRequest) = service.update(id, request, transactionId)


    @Operation(summary = "Delete product by ID", description = "Deletes a product based on the provided product ID.")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Product successfully deleted"),
        ApiResponse(responseCode = "404", description = "Product not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
class TransactionController(val service: TransactionService) {


    @Operation(summary = "Get all transactions", description = "Fetches all transactions from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all transactions"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all transactions with pagination", description = "Fetches transactions with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated transactions"))
    @GetMapping("/page")
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int) =
        service.getAll(page, size)


    @Operation(summary = "Get transaction by ID", description = "Fetches a single transaction based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully fetched the transaction"),
        ApiResponse(responseCode = "404", description = "Transaction not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new transaction", description = "Creates a new transaction record.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Transaction successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: TransactionCreateRequest) = service.create(request)



    @Operation(summary = "Update existing transaction", description = "Updates an existing transaction based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Transaction successfully updated"),
        ApiResponse(responseCode = "404", description = "Transaction not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: TransactionUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete transaction by ID", description = "Deletes a transaction based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Transaction successfully deleted"),
        ApiResponse(responseCode = "404", description = "Transaction not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction-items")
class TransactionItemController(val service: TransactionItemService) {

    @Operation(summary = "Get all transactionItems", description = "Fetches all transactionItems from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all transactionItems"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all transactionItems with pagination", description = "Fetches transactionItems with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated transactionItems"))
    @GetMapping("/page")
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int) =
        service.getAll(page, size)


    @Operation(summary = "Get transactionItem by ID", description = "Fetches a single transactionItem based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully fetched the transactionItem"),
        ApiResponse(responseCode = "404", description = "TransactionItem not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new transactionItem", description = "Creates a new transactionItem record.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "TransactionItem successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: TransactionItemCreateRequest) = service.create(request)


    @Operation(summary = "Update existing transactionItem", description = "Updates an existing transactionItem based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "TransactionItem successfully updated"),
        ApiResponse(responseCode = "404", description = "TransactionItem not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: TransactionItemUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete transactionItem by ID", description = "Deletes a transactionItem based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "TransactionItem successfully deleted"),
        ApiResponse(responseCode = "404", description = "TransactionItem not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-payment-transactions")
class UserPaymentTransactionController(val service: UserPaymentTransactionService) {

    @Operation(summary = "Get all userTransactions", description = "Fetches all userTransactions from the database.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched all userTransactions"))
    @GetMapping
    fun getAll() = service.getAll()


    @Operation(summary = "Get all userTransactions with pagination", description = "Fetches userTransactions with pagination support.")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successfully fetched paginated userTransactions"))
    @GetMapping("/page")
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int) =
        service.getAll(page, size)


    @Operation(summary = "Get userTransaction by ID", description = "Fetches a single userTransaction based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully fetched the userTransaction"),
        ApiResponse(responseCode = "404", description = "UserTransaction not found"))
    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)


    @Operation(summary = "Create new userTransaction", description = "Creates a new userTransaction record.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "UserTransaction successfully created"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PostMapping
    fun create(@RequestBody @Valid request: UserPaymentTransactionCreateRequest,
               @RequestParam transactionId: Long,) = service.create(request,transactionId)


    @Operation(summary = "Update existing userTransaction", description = "Updates an existing userTransaction based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "UserTransaction successfully updated"),
        ApiResponse(responseCode = "404", description = "UserTransaction not found"),
        ApiResponse(responseCode = "400", description = "Invalid request data"))
    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: UserPaymentTransactionUpdateRequest) = service.update(id, request)


    @Operation(summary = "Delete userTransaction by ID", description = "Deletes a userTransaction based on the provided ID.")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "UserTransaction successfully deleted"),
        ApiResponse(responseCode = "404", description = "UserTransaction not found"))
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequestMapping("/api/purchase")
class PurchaseController(private val purchaseService: PurchaseService) {

    @Operation(summary = "Process a new purchase request", description = "Processes a new purchase request including transaction and product updates.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Request processed successfully"),
        ApiResponse(responseCode = "400", description = "Invalid request data"),
        ApiResponse(responseCode = "404", description = "User, Product or Transaction not found"),
        ApiResponse(responseCode = "409", description = "Insufficient balance or product quantity")
    )
    @PostMapping("/process")
    fun processPurchaseRequest(@RequestBody @Valid request: PurchaseRequest) {
        purchaseService.processRequest(request)
    }
}








