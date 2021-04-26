package com.hyperliteapps.vendingmachine.models

/**
 * A class to represent the Vending Machine
 */
class VendingMachine {
    var fundsAvailable: Double = 0.0
    var total: Double = 0.0
    var coinReturn: Double = 0.0
    var displayMessage: String = ""
    var products: HashMap<Int, Int> = HashMap()

    // Utility function to add a coin to the total
    fun addToTotal(coin : Coin) {
        when(coin) {
            Coin.Nickel -> total += 5
            Coin.Dime -> total += 10
            Coin.Quarter -> total += 25
            else -> coinReturn++
        }
    }

    // Utility function to check if a product is in stock
    fun inStock(product: Product): Boolean {
        val quantity = products[product.ordinal]
        return  quantity != null && quantity > 0
    }

    // Utility function to vend a product
    fun vendProduct(product: Product) {
        if (inStock(product)) {
            val quantity = products[product.ordinal]!!
            products[product.ordinal] = quantity - 1
            coinReturn = total - product.price
            total = 0.0
            fundsAvailable += product.price
            displayMessage = "THANK YOU"
        }
    }

    // Utility function to reset value of total money inserted by user
    fun resetTotal() {
        total = 0.0
    }

    // Utility function to reset the amount of money owed to the user
    fun resetCoinReturn() {
        coinReturn = 0.0
    }

    // Utility function to determine if the machine should require exact change before interaction with user
    fun isExactChangeOnly(): Boolean {
        for (prod in products) {
            val product = Product.values()[prod.key]
            if (fundsAvailable < product.price) {
                return true
            }
        }

        return false
    }

    // Utility function to determine if the machine should require exact change during the selection of a product
    fun needsExactChangeOnly(product: Product): Boolean {
        return fundsAvailable < product.price
    }
}