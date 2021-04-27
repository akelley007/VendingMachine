package com.hyperliteapps.vendingmachine.models

/**
 * An class to represent different products with a name and price
 */
data class Product(val name: String, val price: Double)

/**
 * Depending on requirements could also be an Enum with explicit values for name and price but general class is better for
 * supporting any number of 'products' especially if it is expected they should be dynamic or could also be extended if required
enum class Product(name: String, val price: Double) {
    Cola("Cola", 1.0),
    Chips("Chips", 0.5),
    Candy("Candy", 0.65)
}
 */