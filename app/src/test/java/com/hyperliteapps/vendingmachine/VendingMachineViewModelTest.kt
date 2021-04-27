package com.hyperliteapps.vendingmachine

import com.hyperliteapps.vendingmachine.models.Coin
import com.hyperliteapps.vendingmachine.models.Product
import com.hyperliteapps.vendingmachine.models.VendingItem
import com.hyperliteapps.vendingmachine.viewModels.VendingMachineViewModel
import org.junit.Assert
import org.junit.Test

class VendingMachineViewModelTest {

    private val viewModel = VendingMachineViewModel()
    private val chipsPrice = 0.50
    private val candyPrice = 0.65

    // Add coin tests
    @Test
    fun add_coin_valid_nickel() {
        viewModel.vendingMachine.resetTotal()
        viewModel.addCoin(Coin.Nickel)
        Assert.assertEquals(0.05, viewModel.vendingMachine.total, 0.001)
    }

    @Test
    fun add_coin_valid_dime() {
        viewModel.vendingMachine.resetTotal()
        viewModel.addCoin(Coin.Dime)
        Assert.assertEquals(0.10, viewModel.vendingMachine.total, 0.001)
    }

    @Test
    fun add_coin_valid_quarter() {
        viewModel.vendingMachine.resetTotal()
        viewModel.addCoin(Coin.Quarter)
        Assert.assertEquals(0.25, viewModel.vendingMachine.total, 0.001)
    }

    @Test
    fun add_coin_invalid_penny() {
        viewModel.vendingMachine.resetTotal()
        viewModel.addCoin(Coin.Penny)
        Assert.assertEquals(0.0, viewModel.vendingMachine.total, 0.001)
        Assert.assertEquals(1.0, viewModel.vendingMachine.coinReturn, 0.001)
    }

    // Return coins tests
    @Test
    fun return_coins() {
        viewModel.vendingMachine.resetCoinReturn()
        val originalAmount = 25.0
        viewModel.vendingMachine.total = originalAmount
        val returnAmount = viewModel.returnCoins()
        Assert.assertEquals(returnAmount, originalAmount, 0.001)
        Assert.assertEquals(0.0, viewModel.vendingMachine.total, 0.001)
    }

    // Select product tests
    @Test
    fun select_product_success() {
        val initialFunds = 3.0
        viewModel.vendingMachine.resetTotal()
        viewModel.vendingMachine.resetCoinReturn()
        viewModel.vendingMachine.vendMap.clear()
        viewModel.vendingMachine.fundsAvailable = initialFunds

        val product = Product("Chips", chipsPrice)
        val item = VendingItem(product, 1)
        viewModel.vendingMachine.vendMap[product.name] = item
        viewModel.vendingMachine.total = candyPrice

        viewModel.selectProduct(product)

        Assert.assertEquals(0.0, viewModel.vendingMachine.total, 0.001)
        Assert.assertEquals((candyPrice - product.price), viewModel.vendingMachine.coinReturn, 0.001)
        Assert.assertEquals(( initialFunds + (product.price - viewModel.vendingMachine.coinReturn)), viewModel.vendingMachine.fundsAvailable, 0.001)
        Assert.assertEquals(false, viewModel.inStock(product))
        Assert.assertEquals("THANK YOU", viewModel.vendingMachine.displayMessage)
    }

    @Test
    fun select_product_success_exact_change() {
        viewModel.vendingMachine.resetTotal()
        viewModel.vendingMachine.resetCoinReturn()
        viewModel.vendingMachine.vendMap.clear()
        viewModel.vendingMachine.fundsAvailable = 0.0

        val product = Product("Chips", chipsPrice)
        val item = VendingItem(product, 1)
        viewModel.vendingMachine.vendMap[product.name] = item
        viewModel.vendingMachine.total = chipsPrice

        viewModel.selectProduct(product)

        Assert.assertEquals(0.0, viewModel.vendingMachine.total, 0.001)
        Assert.assertEquals(0.0, viewModel.vendingMachine.coinReturn, 0.001)
        Assert.assertEquals(chipsPrice, viewModel.vendingMachine.fundsAvailable, 0.001)
        Assert.assertEquals(false, viewModel.inStock(product))
        Assert.assertEquals("THANK YOU", viewModel.vendingMachine.displayMessage)
    }

    @Test
    fun select_product_fail_out_of_stock() {
        viewModel.vendingMachine.resetTotal()
        viewModel.vendingMachine.resetCoinReturn()
        viewModel.vendingMachine.vendMap.clear()
        viewModel.vendingMachine.fundsAvailable = 0.0

        val product = Product("Chips", chipsPrice)
        val item = VendingItem(product, 0)
        viewModel.vendingMachine.vendMap[product.name] = item
        viewModel.vendingMachine.total = chipsPrice

        viewModel.selectProduct(product)

        Assert.assertEquals(chipsPrice, viewModel.vendingMachine.total, 0.001)
        Assert.assertEquals(0.0, viewModel.vendingMachine.coinReturn, 0.001)
        Assert.assertEquals(0.0, viewModel.vendingMachine.fundsAvailable, 0.001)
        Assert.assertEquals(false, viewModel.inStock(product))
        Assert.assertEquals("SOLD OUT", viewModel.vendingMachine.displayMessage)
    }

    @Test
    fun select_product_fail_not_enough_funds() {
        viewModel.vendingMachine.resetTotal()
        viewModel.vendingMachine.resetCoinReturn()
        viewModel.vendingMachine.vendMap.clear()
        viewModel.vendingMachine.fundsAvailable = 0.0

        val product = Product("Chips", chipsPrice)
        val item = VendingItem(product, 1)
        viewModel.vendingMachine.vendMap[product.name] = item
        viewModel.vendingMachine.total = 0.0

        viewModel.selectProduct(product)

        Assert.assertEquals(0.0, viewModel.vendingMachine.total, 0.001)
        Assert.assertEquals(0.0, viewModel.vendingMachine.coinReturn, 0.001)
        Assert.assertEquals(0.0, viewModel.vendingMachine.fundsAvailable, 0.001)
        Assert.assertEquals(true, viewModel.inStock(product))
        Assert.assertEquals("PRICE", viewModel.vendingMachine.displayMessage)
    }

    @Test
    fun select_product_fail_no_exact_change() {
        viewModel.vendingMachine.resetTotal()
        viewModel.vendingMachine.resetCoinReturn()
        viewModel.vendingMachine.vendMap.clear()
        viewModel.vendingMachine.fundsAvailable = 0.0

        val product = Product("Chips", chipsPrice)
        val item = VendingItem(product, 1)
        viewModel.vendingMachine.vendMap[product.name] = item
        viewModel.vendingMachine.total = candyPrice

        viewModel.selectProduct(product)

        Assert.assertEquals(candyPrice, viewModel.vendingMachine.total, 0.001)
        Assert.assertEquals(0.0, viewModel.vendingMachine.coinReturn, 0.001)
        Assert.assertEquals(0.0, viewModel.vendingMachine.fundsAvailable, 0.001)
        Assert.assertEquals(true, viewModel.inStock(product))
        Assert.assertEquals("EXACT CHANGE ONLY", viewModel.vendingMachine.displayMessage)
    }

    // Check display tests
    @Test
    fun check_display_total() {
        viewModel.vendingMachine.total = 25.0
        viewModel.checkDisplay()

        Assert.assertEquals("$25.0", viewModel.vendingMachine.displayMessage)
    }

    @Test
    fun check_display_insert() {
        viewModel.vendingMachine.resetTotal()
        viewModel.vendingMachine.resetCoinReturn()
        viewModel.vendingMachine.vendMap.clear()
        viewModel.vendingMachine.fundsAvailable = 0.0

        viewModel.checkDisplay()

        Assert.assertEquals("INSERT COIN", viewModel.vendingMachine.displayMessage)
    }

    @Test
    fun check_display_exact() {
        viewModel.vendingMachine.resetTotal()
        viewModel.vendingMachine.resetCoinReturn()
        viewModel.vendingMachine.vendMap.clear()
        viewModel.vendingMachine.fundsAvailable = 0.0

        val product = Product("Chips", chipsPrice)
        val item = VendingItem(product, 1)
        viewModel.vendingMachine.vendMap[product.name] = item

        viewModel.checkDisplay()

        Assert.assertEquals("EXACT CHANGE ONLY", viewModel.vendingMachine.displayMessage)
    }

    // In-stock tests
    @Test
    fun in_stock_true() {
        viewModel.vendingMachine.vendMap.clear()
        val product = Product("Chips", chipsPrice)
        val item = VendingItem(product, 1)
        viewModel.vendingMachine.vendMap[product.name] = item

        Assert.assertEquals(true, viewModel.inStock(product))
    }

    @Test
    fun in_stock_false() {
        viewModel.vendingMachine.vendMap.clear()
        val product = Product("Chips", chipsPrice)
        val item = VendingItem(product, 0)
        viewModel.vendingMachine.vendMap[product.name] = item

        Assert.assertEquals(false, viewModel.inStock(product))
    }
}