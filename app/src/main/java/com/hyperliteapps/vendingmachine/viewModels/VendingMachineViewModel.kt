package com.hyperliteapps.vendingmachine.viewModels

import androidx.lifecycle.ViewModel
import com.hyperliteapps.vendingmachine.models.Coin
import com.hyperliteapps.vendingmachine.models.Product
import com.hyperliteapps.vendingmachine.models.VendingMachine

class VendingMachineViewModel : ViewModel() {
    /** What the live data would look like if it was being used in a full App context
     * private val _vendingMachine = MutableLiveData<VendingMachine>()
     * val vendingMachine: LiveData<VendingMachine>
     *   get() = _vendingMachine
     */

    private var vendingMachine: VendingMachine = VendingMachine()

    fun addCoin(coin: Coin) {
        vendingMachine.addToTotal(coin)
        // Update the state of the vending machine to trigger UI updates via the lifecycle observers
        // _vendingMachine.value = vendingMachine
    }

    // Returns the amount entered back to the user and reset the state of the machine
    fun returnCoins(): Double {
        val totalReturn = vendingMachine.total
        vendingMachine.resetTotal()
        checkDisplay()
        return  totalReturn
    }

    // Function to attempt a selection of a product from the user
    fun selectProduct(product: Product) {
        if (vendingMachine.inStock(product)) {
            val curTotal = vendingMachine.total
            val price = product.price
            if (curTotal >= price) {
                if (vendingMachine.needsExactChangeOnly(product)) {
                    if (curTotal == price) {
                        vendingMachine.vendProduct(product)
                    } else {
                        vendingMachine.displayMessage = "EXACT CHANGE ONLY"
                    }
                } else {
                    vendingMachine.vendProduct(product)
                }
            } else {
                vendingMachine.displayMessage = "PRICE"
            }
        } else {
            vendingMachine.displayMessage = "SOLD OUT"
        }

        // Update the state of the vending machine to trigger UI updates via the lifecycle observers
        // _vendingMachine.value = vendingMachine
    }

    // Function to refresh the display to 'clean' state, could be called internally from VM or
    // From the Activity as a result of user input or perhaps a timed UI update
    fun checkDisplay() {
        vendingMachine.resetCoinReturn()
        vendingMachine.displayMessage = if (vendingMachine.total > 0.0) {
            "${vendingMachine.total}"
        } else {
            if (vendingMachine.isExactChangeOnly()) "EXACT CHANGE ONLY" else "INSERT COIN"
        }

        // Update the state of the vending machine to trigger UI updates via the lifecycle observers
        // _vendingMachine.value = vendingMachine
    }
}