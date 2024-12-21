package zeroone.developers.payapp
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDto(user: User): UserResponse {
        return user.run {
            UserResponse(
                id = this.id,
                fullname = this.fullname,
                username = this.username,
                balance = this.balance
            )
        }
    }

    fun toEntity(createRequest: UserCreateRequest): User {
        return createRequest.run {
            User(
                fullname = this.fullname,
                username = this.username,
                balance = this.balance
            )
        }
    }

    fun updateEntity(user: User, updateRequest: UserUpdateRequest): User {
        return updateRequest.run {
            user.apply {
                updateRequest.fullname.let { this.fullname = it }
                updateRequest.username.let { this.username = it }
                updateRequest.balance.let { this.balance = it }
            }
        }
    }
}



@Component
class CategoryMapper {
    fun toDto(category: Category): CategoryResponse {
        return category.run {
            CategoryResponse(
                id = this.id,
                name = this.name,
                orderValue = this.orderValue,
                description = this.description
            )
        }
    }

    fun toEntity(createRequest: CategoryCreateRequest): Category {
        return createRequest.run {
            Category(
                name = this.name,
                orderValue = this.orderValue,
                description = this.description
            )
        }
    }

    fun updateEntity(category: Category, updateRequest: CategoryUpdateRequest): Category {
        return updateRequest.run {
            category.apply {
                updateRequest.name.let { this.name = it }
                updateRequest.orderValue.let { this.orderValue = it }
                updateRequest.description.let { this.description = it }
            }
        }
    }
}


@Component
class ProductMapper {

    fun toDto(product: Product): ProductResponse {
        return product.run {
            ProductResponse(
                id = this.id,
                name = this.name,
                count = this.count,
                categoryName = this.category.name
            )
        }
    }

    fun toEntity(createRequest: ProductCreateRequest, category: Category): Product {
        return createRequest.run {
            Product(
                name = this.name,
                count = this.count,
                category = category
            )
        }
    }

    fun updateEntity(product: Product, updateRequest: ProductUpdateRequest): Product {
        return updateRequest.run {
            product.apply {
                updateRequest.name.let { this.name = it }
                updateRequest.count.let { this.count = it }
            }
        }
    }
}

@Component
class TransactionMapper {

    fun toDto(transaction: Transaction): TransactionResponse {
        return transaction.run {
            TransactionResponse(
                id = this.id,
                userName = this.user.username,
                totalAmount = this.totalAmount,
                date = this.date
            )
        }
    }

    fun toEntity(createRequest: TransactionCreateRequest, user: User): Transaction {
        return createRequest.run {
            Transaction(
                user = user,
                totalAmount = this.totalAmount,
                date = date
            )
        }
    }

    fun updateEntity(transaction: Transaction, updateRequest: TransactionUpdateRequest): Transaction {
        return updateRequest.run {
            transaction.apply {
                updateRequest.totalAmount.let { this.totalAmount = it }
            }
        }
    }
}

@Component
class TransactionItemMapper {

    fun toDto(transactionItem: TransactionItem): TransactionItemResponse {
        return transactionItem.run {
            TransactionItemResponse(
                id = this.id,
                productName = this.product.name,
                count = this.count,
                amount = this.amount,
                totalAmount = this.totalAmount,
                transactionTotalAmount = this.transaction.totalAmount
            )
        }
    }

    fun toEntity(createRequest: TransactionItemCreateRequest,product: Product, transaction: Transaction): TransactionItem {
        return createRequest.run {
            TransactionItem(
                product = product,
                count = this.count,
                amount = this.amount,
                totalAmount = this.totalAmount,
                transaction = transaction
            )
        }
    }

    fun updateEntity(transactionItem: TransactionItem, updateRequest: TransactionItemUpdateRequest): TransactionItem {
        return updateRequest.run {
            transactionItem.apply {
                updateRequest.count.let { this.count = it }
                updateRequest.amount.let { this.amount = it }
                updateRequest.totalAmount.let { this.totalAmount = it }
            }
        }
    }
}



@Component
class UserPaymentTransactionMapper {
    fun toDto(userPaymentTransaction: UserPaymentTransaction): UserPaymentTransactionResponse {
        return userPaymentTransaction.run {
            UserPaymentTransactionResponse(
                id = this.id,
                userName = this.user.username,
                amount = this.amount,
                date = this.date
            )
        }
    }

    fun toEntity(createRequest: UserPaymentTransactionCreateRequest, user: User): UserPaymentTransaction {
        return createRequest.run {
            UserPaymentTransaction(
                user = user,
                amount = this.amount,
                date = this.date
            )
        }
    }

    fun updateEntity(userPaymentTransaction: UserPaymentTransaction, updateRequest: UserPaymentTransactionUpdateRequest): UserPaymentTransaction {
        return updateRequest.run {
            userPaymentTransaction.apply {
                updateRequest.amount.let { this.amount = it }
            }
        }
    }
}








