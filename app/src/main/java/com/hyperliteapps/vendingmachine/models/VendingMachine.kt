package com.hyperliteapps.vendingmachine.models

/**
 * A class to represent the Vending Machine
 */
class VendingMachine {
    var fundsAvailable: Double = 0.0
    var total: Double = 0.0
    var coinReturn: Double = 0.0
    var displayMessage: String = ""

    // A HashMap to keep track of the quantity of each product
    // for the sake of the exercise the products name is used as the key, howerver,
    // in a real world scenario it would be better to use something that is guaranteed unique like a SKU or UUID
    var vendMap: HashMap<String, VendingItem> = HashMap()

    // Utility function to add a coin to the total
    fun addToTotal(coin : Coin) {
        when(coin) {
            Coin.Nickel -> total += 0.05
            Coin.Dime -> total += 0.10
            Coin.Quarter -> total += 0.25
            else -> coinReturn++
        }
    }

    // Utility function to check if a product is in stock
    fun inStock(product: Product): Boolean {
        val quantity = vendMap[product.name]?.quantity ?: 0
        return  quantity > 0
    }

    // Utility function to vend a product
    fun vendProduct(product: Product) {
        if (inStock(product)) {
            vendMap[product.name]!!.quantity--
            coinReturn = total - product.price
            total = 0.0
            fundsAvailable += (product.price - coinReturn)
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
        for (item in vendMap) {
            val product = item.value.product
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