package com.hyperliteapps.vendingmachine.models

/**
 * A class to represent the Vending Machine 
 */
class VendingMachine {
    private var total: Double = 0.0
        get() = field
        set(value) { field = value}
    private var coinReturn: Double = 0.0
        get() = field
        set(value) { field = value}
    private var exactChange: Boolean = false
        get() = field
        set(value) { field = value}
    private var displayMessage: String = ""
        get() = field
        set(value) { field = value}
    private var products: HashMap<Int, Int> = HashMap()
        get() = field
        set(value) { field = value}

    fun addToTotal(coin : Coin): Boolean {
        total += when(coin) {
            Coin.Nickel -> 5
            Coin.Dime -> 10
            Coin.Quarter -> 25
            else -> return false
        }
        return true
    }

    fun inStock(product: Product): Boolean {
        val quantity = products[product.ordinal]
        return  quantity != null && quantity > 0
    }

    fun vendProduct(product: Product) {
        if (inStock(product)) {
            val quantity = products[product.ordinal]!!
            products[product.ordinal] = quantity - 1
        }
    }
}