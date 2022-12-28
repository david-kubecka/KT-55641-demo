package com.example.blazedemo

import com.blazebit.persistence.Criteria
import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.integration.view.spring.EnableEntityViews
import com.blazebit.persistence.view.*
import com.blazebit.persistence.view.spi.EntityViewConfiguration
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.beans.factory.annotation.Lookup
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.OffsetDateTime
import javax.persistence.*

@SpringBootApplication
class BlazeDemoApplication

fun main(args: Array<String>) {
    runApplication<BlazeDemoApplication>(*args)
}

@Configuration
@EnableEntityViews
class BlazePersistenceConfig {
    @PersistenceUnit
    private lateinit var entityManagerFactory: EntityManagerFactory

    @Bean
    fun createCriteriaBuilderFactory(): CriteriaBuilderFactory =
        Criteria.getDefault().createCriteriaBuilderFactory(entityManagerFactory)

    @Bean
    fun createEntityViewManager(cbf: CriteriaBuilderFactory, evc: EntityViewConfiguration): EntityViewManager =
        evc.createEntityViewManager(cbf)
}

@CreatableEntityView(excludedEntityAttributes = ["creationTimestamp", "updateTimestamp"])
@EntityView(Animal::class)
@EntityViewInheritance
interface AnimalView {
    @get:IdMapping
    val id: Long?
}

@CreatableEntityView(excludedEntityAttributes = ["creationTimestamp", "updateTimestamp"])
@EntityView(Cat::class)
interface CatView : AnimalView {
    @get:Lookup
    var name: String
}

@CreatableEntityView(excludedEntityAttributes = ["creationTimestamp", "updateTimestamp"])
@EntityView(Person::class)
interface PersonView {
    @get:IdMapping
    val id: Long?
    var name: String
    var animal: AnimalView
}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
class Animal : BaseEntity() {
    @OneToOne(fetch = FetchType.LAZY)
    var owner: Person? = null
}

@Entity
class Cat(
    var name: String,
) : Animal()

@Entity
class Person(
    val name: String,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "owner")
    val animal: Animal? = null,
) : BaseEntity()

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    /*
    CreationTimestamp is by default updatable = true, which might lead to an insert of null update.
    See issue for details -> https://stackoverflow.com/a/60673281
    */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var creationTimestamp: OffsetDateTime

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updateTimestamp: OffsetDateTime

    final override fun toString() =
        this::class.simpleName + "(id = $id)"

    // Note: equals/hashCode are marked as final. Otherwise, child data classes don't recognize them and use the default
    // implementations which are not suitable for ORM use case.
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BaseEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = javaClass.hashCode()
}
