package com.nemislimus.tratometr.case.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DBHelper (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private val DB_NAME = "DBExpenses"
        private val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Создание таб. EXPENSES
        db.execSQL(
            "CREATE TABLE EXPENSES ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "DATE INTEGER NOT NULL,"           // Дата в милисекундах
                    + "AMOUNT INTEGER NOT NULL,"         // Сумма в копейках
                    + "CATEGORY TEXT NOT NULL,"       // Категория
                    + "NOTE TEXT);"                      // Примечание
        )
        // Создание таб. CATEGORIES
        db.execSQL(
            "CREATE TABLE CATEGORIES ("
                    + "CATEGORY TEXT PRIMARY KEY,"      // Название категории как первичный ключ
                    + "ICON_RES_ID INTEGER);"           // id иконки
        )
    }

    // Обновление/миграция БД
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}