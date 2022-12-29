package com.example.blazedemo.views

import com.blazebit.persistence.view.CreatableEntityView
import com.blazebit.persistence.view.EntityView
import com.blazebit.persistence.view.EntityViewInheritance
import com.blazebit.persistence.view.IdMapping
import com.example.blazedemo.entities.Animal
import com.example.blazedemo.entities.Cat
import com.example.blazedemo.entities.Person
import org.springframework.beans.factory.annotation.Lookup

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
