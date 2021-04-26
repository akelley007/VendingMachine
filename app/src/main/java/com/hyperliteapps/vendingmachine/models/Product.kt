package com.hyperliteapps.vendingmachine.models

/**
 * An enum class to represent different products
 */
enum class Product(name: String, var price: Double) {
    Cola("Cola", 1.0),
    Chips("Chips", 0.5),
    Candy("Candy", 0.65)
}