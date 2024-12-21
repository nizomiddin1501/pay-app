package zeroone.developers.payapp
import jakarta.annotation.Nonnull
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.*

data class BaseMessage(val code : Int, val message : String?)

@Schema(description = "Data transfer object for User CreateRequest")
data class UserCreateRequest(

    @Schema(description = "User full name", example = "Nizomiddin Mirzanazarov")
    val fullname: String,

    @Schema(description = "Unique username", example = "nizomiddin097")
    @field:Nonnull val username: String,

    @Schema(description = "User balance", example = "1000.00")
    val balance: BigDecimal
)


@Schema(description = "Data transfer object for User Response")
data class UserResponse(
    @Schema(description = "User ID", example = "1")
    val id: Long?,

    @Schema(description = "User full name", example = "Nizomiddin Mirzanazarov")
    val fullname: String?,

    @Schema(description = "Unique username", example = "nizomiddin097")
    val username: String?,

    @Schema(description = "User balance", example = "1000.00")
    val balance: BigDecimal?
)


@Schema(description = "Data transfer object for User UpdateRequest")
data class UserUpdateRequest(

    @Schema(description = "User full name", example = "Nizomiddin Mirzanazarov")
    val fullname: String,

    @Schema(description = "Unique username", example = "nizomiddin097")
    val username: String,

    @Schema(description = "User balance", example = "1000.00")
    val balance: BigDecimal
)


@Schema(description = "Data transfer object for Category CreateRequest")
data class CategoryCreateRequest(

    @Schema(description = "Category name", example = "Electronics")
    @field:Nonnull val name: String,

    @Schema(description = "Category order", example = "1")
    val orderValue: Long,

    @Schema(description = "Category description", example = "All electronic products")
    val description: String
)


@Schema(description = "Data transfer object for Category Response")
data class CategoryResponse(
    @Schema(description = "Category ID", example = "1")
    val id: Long?,

    @Schema(description = "Category name", example = "Electronics")
    @field:Nonnull val name: String?,

    @Schema(description = "Category order", example = "1")
    val orderValue: Long?,

    @Schema(description = "Category description", example = "All electronic products")
    val description: String?
)


@Schema(description = "Data transfer object for Category Response")
data class CategoryUpdateRequest(

    @Schema(description = "Category name", example = "Electronics")
    @field:Nonnull var name: String,

    @Schema(description = "Category order", example = "1")
    val orderValue: Long,

    @Schema(description = "Category description", example = "All electronic products")
    val description: String

)


@Schema(description = "Data transfer object for Product CreateRequest")
data class ProductCreateRequest(

    @Schema(description = "Product name", example = "Laptop")
    val name: String,

    @Schema(description = "Product count", example = "50")
    val count: Long,

    @Schema(description = "Category ID for the product", example = "3")
    @field:Nonnull val categoryId: Long
)


@Schema(description = "Data transfer object for Product Response")
data class ProductResponse(
    @Schema(description = "Product ID", example = "1")
    val id: Long?,

    @Schema(description = "Product name", example = "Laptop")
    val name: String?,

    @Schema(description = "Product count", example = "50")
    val count: Long?,

    @Schema(description = "Category ID for the product", example = "3")
    val categoryName: String?
)


@Schema(description = "Data transfer object for Product UpdateRequest")
data class ProductUpdateRequest(

    @Schema(description = "Product name", example = "Laptop")
    @field:Nonnull val name: String,

    @Schema(description = "Product count", example = "50")
    val count: Long,

)


@Schema(description = "Data transfer object for Transaction CreateRequest")
data class TransactionCreateRequest(

    @Schema(description = "User ID who made the transaction", example = "2")
    val userId: Long,

    @Schema(description = "Total amount of the transaction", example = "150.00")
    val totalAmount: BigDecimal,

    @Schema(description = "Date of the transaction", example = "2023-07-11")
    val date: Date
)


@Schema(description = "Data transfer object for Transaction Response")
data class TransactionResponse(
    @Schema(description = "Transaction ID", example = "1")
    val id: Long?,

    @Schema(description = "User ID who made the transaction", example = "2")
    val userName: String?,

    @Schema(description = "Total amount of the transaction", example = "150.00")
    val totalAmount: BigDecimal?,

    @Schema(description = "Date of the transaction", example = "2023-07-11")
    val date: Date?
)


@Schema(description = "Data transfer object for Transaction UpdateRequest")
data class TransactionUpdateRequest(

    @Schema(description = "Total amount of the transaction", example = "150.00")
    val totalAmount: BigDecimal,

)


@Schema(description = "Data transfer object for Transaction Item CreateRequest")
data class TransactionItemCreateRequest(

    @Schema(description = "Product ID in the transaction", example = "10")
    @field:Nonnull val productId: Long,

    @Schema(description = "Quantity of the product", example = "2")
    val count: Long,

    @Schema(description = "Price of the product", example = "75.00")
    val amount: BigDecimal,

    @Schema(description = "Total amount for this item", example = "150.00")
    val totalAmount: BigDecimal,

    @Schema(description = "Transaction ID for the item", example = "5")
    @field:Nonnull val transactionId: Long
)


@Schema(description = "Data transfer object for Transaction Item Response")
data class TransactionItemResponse(
    @Schema(description = "Transaction item ID", example = "1")
    val id: Long?,

    @Schema(description = "Product ID in the transaction", example = "10")
    val productName: String?,

    @Schema(description = "Quantity of the product", example = "2")
    val count: Long?,

    @Schema(description = "Price of the product", example = "75.00")
    val amount: BigDecimal?,

    @Schema(description = "Total amount for this item", example = "150.00")
    val totalAmount: BigDecimal?,

    @Schema(description = "Transaction ID for the item", example = "5")
    val transactionTotalAmount: BigDecimal?
)


@Schema(description = "Data transfer object for Transaction Item UpdateRequest")
data class TransactionItemUpdateRequest(

    @Schema(description = "Quantity of the product", example = "2")
    val count: Long,

    @Schema(description = "Price of the product", example = "75.00")
    val amount: BigDecimal,

    @Schema(description = "Total amount for this item", example = "150.00")
    val totalAmount: BigDecimal
)


@Schema(description = "Data transfer object for User Payment Transaction CreateRequest")
data class UserPaymentTransactionCreateRequest(

    @Schema(description = "User ID who made the payment", example = "2")
    @field:Nonnull val userId: Long,

    @Schema(description = "Amount of the payment", example = "200.00")
    val amount: BigDecimal,

    @Schema(description = "Date of the payment", example = "2023-07-11")
    val date: Date
)


@Schema(description = "Data transfer object for User Payment Transaction Response")
data class UserPaymentTransactionResponse(
    @Schema(description = "Payment transaction ID", example = "1")
    val id: Long?,

    @Schema(description = "User ID who made the payment", example = "2")
    val userName: String?,

    @Schema(description = "Amount of the payment", example = "200.00")
    val amount: BigDecimal?,

    @Schema(description = "Date of the payment", example = "2023-07-11")
    val date: Date?
)


@Schema(description = "Data transfer object for User Payment Transaction UpdateRequest")
data class UserPaymentTransactionUpdateRequest(

    @Schema(description = "Amount of the payment", example = "200.00")
    val amount: BigDecimal,
)



@Schema(description = "Unified data transfer object for all transaction-related requests")
data class PurchaseRequest(

    @Schema(description = "User ID who made the transaction", example = "2")
    val userId: Long,

    @Schema(description = "Total amount of the transaction", example = "150.00")
    val totalAmount: BigDecimal? = null,

    @Schema(description = "Date of the transaction", example = "2023-07-11")
    val date: Date? = null,

    @Schema(description = "Product ID in the transaction", example = "10")
    val productId: Long? = null,

    @Schema(description = "Quantity of the product", example = "2")
    val count: Long? = null,

    @Schema(description = "Price of the product", example = "75.00")
    val amount: BigDecimal? = null,

    @Schema(description = "Total amount for this item", example = "150.00")
    val totalAmountItem: BigDecimal? = null,

//    @Schema(description = "Transaction ID for the item", example = "5")
//    val transactionId: Long? = null
)




