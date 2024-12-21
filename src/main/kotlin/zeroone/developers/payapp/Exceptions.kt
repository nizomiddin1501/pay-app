package zeroone.developers.payapp

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource

sealed class PayCartExceptionHandler() : RuntimeException() {
    abstract fun errorCode(): ErrorCodes
    open fun getArguments(): Array<Any?>? = null

    fun getErrorMessage(resourceBundleMessageSource: ResourceBundleMessageSource): BaseMessage {
        val message = try {
            resourceBundleMessageSource.getMessage(
                errorCode().name, getArguments(), LocaleContextHolder.getLocale()
            )
        } catch (e: Exception) {
            e.message
        }
        return BaseMessage(errorCode().code, message)
    }
}

class UserAlreadyExistsException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.USER_ALREADY_EXISTS
    }
}

class UserNotFoundException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.USER_NOT_FOUND
    }
}

class UserPaymentTransactionAlreadyExistsException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.USER_PAYMENT_TRANSACTION_ALREADY_EXISTS
    }
}

class UserPaymentTransactionNotFoundException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.USER_PAYMENT_TRANSACTION_NOT_FOUND
    }
}

class InvalidBalanceException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.USER_PAYMENT_TRANSACTION_NOT_FOUND
    }
}

class TransactionAlreadyExistsException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.TRANSACTION_ALREADY_EXISTS
    }
}

class TransactionNotFoundException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.TRANSACTION_NOT_FOUND
    }
}

class TransactionItemAlreadyExistsException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.TRANSACTION_ITEM_ALREADY_EXISTS
    }
}

class TransactionItemNotFoundException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.TRANSACTION_ITEM_NOT_FOUND
    }
}

class ProductAlreadyExistsException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.PRODUCT_ALREADY_EXISTS
    }
}

class ProductNotFoundException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.PRODUCT_NOT_FOUND
    }
}

class ProductNotEnoughException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.PRODUCT_NOT_ENOUGH
    }
}


class CategoryAlreadyExistsException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.CATEGORY_ALREADY_EXISTS
    }
}

class CategoryNotFoundException : PayCartExceptionHandler() {
    override fun errorCode(): ErrorCodes {
        return ErrorCodes.CATEGORY_NOT_FOUND
    }
}





