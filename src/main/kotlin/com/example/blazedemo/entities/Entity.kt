package com.example.blazedemo.entities

import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import javax.persistence.*

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
