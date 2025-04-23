package com.nemislimus.tratometr.expenses.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context):
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Создание таб. EXPENSES
        db.execSQL(
            "CREATE TABLE EXPENSES ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "DATE INTEGER NOT NULL,"          // Дата в милисекундах
                    + "AMOUNT INTEGER NOT NULL,"        // Сумма в копейках
                    + "CATEGORY TEXT NOT NULL,"         // Категория
                    + "NOTE TEXT);"                     // Примечание
        )

        // Создание таб. CATEGORIES
        db.execSQL(
            "CREATE TABLE CATEGORIES ("
                    + "CATEGORY_NAME TEXT PRIMARY KEY," // Название категории - первичный ключ
                    + "ICON_RES_ID TEXT NOT NULL);"           // id иконки
        )
        // Вставка предустановленных записей в таблицу CATEGORIES
        val insertCategories = arrayOf(
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Кафе', 'ic_main_cat_cafe');",
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Транспорт', 'ic_main_cat_transport');",
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Здоровье', 'ic_main_cat_health');",
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Подарки', 'ic_main_cat_gift');",
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Образование', 'ic_main_cat_education');",
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Дом', 'ic_main_cat_home');",
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Продукты', 'ic_main_cat_groceries');",
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Спорт', 'ic_main_cat_sports');",
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Досуг', 'ic_main_cat_hobby');",
            "INSERT INTO CATEGORIES (CATEGORY_NAME, ICON_RES_ID) VALUES ('Семья', 'ic_main_cat_family');"
        )
        for (query in insertCategories) {
            db.execSQL(query)
        }
    }

    // Обновление/миграция БД
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { }

    companion object {
        private const val DB_NAME = "DBExpenses"
        private const val DB_VERSION = 1
    }
}