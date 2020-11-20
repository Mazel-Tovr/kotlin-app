package com.epam.kotlinapp.crud.dao.nosql


import jetbrains.exodus.database.*
import kotlinx.dnq.*
import kotlinx.dnq.store.container.*
import kotlinx.dnq.util.*
import java.io.*

object CommonStore {

    val entityStore: TransientEntityStore;

    init {
        XdModel.registerNodes(XdUser, XdProductGroup, XdProduct)
        val store = StaticStoreContainer.init(File("db"), "db")
        initMetaData(XdModel.hierarchy, store)
        //Костыль чтоб порядоковый номер внешней сущности был всегда одинаковый\\PS никто 100% не понял про что я\\чтоб все работало короче
        XdUser.all();XdProductGroup.all();XdProduct.all()
        entityStore = store
    }


}
