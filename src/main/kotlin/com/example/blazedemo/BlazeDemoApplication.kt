package com.example.blazedemo

import com.blazebit.persistence.Criteria
import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.integration.view.spring.EnableEntityViews
import com.blazebit.persistence.view.*
import com.blazebit.persistence.view.spi.EntityViewConfiguration
import com.example.blazedemo.views.AnimalViewBuilder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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

fun builder() = AnimalViewBuilder.Init(null)
