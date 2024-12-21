package zeroone.developers.payapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl::class)
class PayAppApplication

fun main(args: Array<String>) {
    runApplication<PayAppApplication>(*args)
}
