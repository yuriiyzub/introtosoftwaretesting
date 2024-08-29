package edu.ncsu.csc326.coffeemaker;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

import static org.junit.Assert.*;

public class InventoryTest {
    private Inventory inventory;

    @Before
    public void setUp() {
        inventory = new Inventory();
    }

    @Test
    public void testInitialInventory() {
        assertEquals(15, inventory.getCoffee());
        assertEquals(15, inventory.getMilk());
        assertEquals(15, inventory.getSugar());
        assertEquals(15, inventory.getChocolate());
    }

    @Test
    public void testSetPositiveCoffee() {
        inventory.setCoffee(10);
        assertEquals(10, inventory.getCoffee());
    }

    @Test
    public void testSetNegativeCoffee() {
        inventory.setCoffee(-1);
        assertEquals(15, inventory.getCoffee()); // Значення не повинно змінитися
    }

    @Test
    public void testAddCoffee() throws InventoryException {
        inventory.addCoffee("5");
        assertEquals(20, inventory.getCoffee());
    }

    @Test(expected = InventoryException.class)
    public void testAddInvalidCoffee() throws InventoryException {
        inventory.addCoffee("invalid");
    }

    @Test(expected = InventoryException.class)
    public void testAddNegativeCoffee() throws InventoryException {
        inventory.addCoffee("-5");
    }

    @Test
    public void testSetPositiveMilk() {
        inventory.setMilk(10);
        assertEquals(10, inventory.getMilk());
    }

    @Test
    public void testSetNegativeMilk() {
        inventory.setMilk(-1);
        assertEquals(15, inventory.getMilk()); // Значення не повинно змінитися
    }

    @Test
    public void testAddMilk() throws InventoryException {
        inventory.addMilk("5");
        assertEquals(20, inventory.getMilk());
    }

    @Test(expected = InventoryException.class)
    public void testAddInvalidMilk() throws InventoryException {
        inventory.addMilk("invalid");
    }

    @Test(expected = InventoryException.class)
    public void testAddNegativeMilk() throws InventoryException {
        inventory.addMilk("-5");
    }

    @Test
    public void testSetPositiveSugar() {
        inventory.setSugar(10);
        assertEquals(10, inventory.getSugar());
    }

    @Test
    public void testSetNegativeSugar() {
        inventory.setSugar(-1);
        assertEquals(15, inventory.getSugar()); // Значення не повинно змінитися
    }

    @Test
    public void testAddSugar() throws InventoryException {
        inventory.addSugar("5");
        assertEquals(20, inventory.getSugar());
    }

    @Test(expected = InventoryException.class)
    public void testAddInvalidSugar() throws InventoryException {
        inventory.addSugar("invalid");
    }

    @Test(expected = InventoryException.class)
    public void testAddNegativeSugar() throws InventoryException {
        inventory.addSugar("-5");
    }

    @Test
    public void testSetPositiveChocolate() {
        inventory.setChocolate(10);
        assertEquals(10, inventory.getChocolate());
    }

    @Test
    public void testSetNegativeChocolate() {
        inventory.setChocolate(-1);
        assertEquals(15, inventory.getChocolate()); // Значення не повинно змінитися
    }

    @Test
    public void testAddChocolate() throws InventoryException {
        inventory.addChocolate("5");
        assertEquals(20, inventory.getChocolate());
    }

    @Test(expected = InventoryException.class)
    public void testAddInvalidChocolate() throws InventoryException {
        inventory.addChocolate("invalid");
    }

    @Test(expected = InventoryException.class)
    public void testAddNegativeChocolate() throws InventoryException {
        inventory.addChocolate("-5");
    }

    @Test
    public void testEnoughIngredientsTrue() throws RecipeException {
        Recipe recipe = new Recipe();
        recipe.setAmtCoffee("10");
        recipe.setAmtMilk("10");
        recipe.setAmtSugar("10");
        recipe.setAmtChocolate("10");
        assertTrue(inventory.enoughIngredients(recipe));
    }

    @Test
    public void testEnoughIngredientsFalse() throws RecipeException {
        Recipe recipe = new Recipe();
        recipe.setAmtCoffee("20");
        recipe.setAmtMilk("10");
        recipe.setAmtSugar("10");
        recipe.setAmtChocolate("10");
        assertFalse(inventory.enoughIngredients(recipe)); // Немає достатньо кави
    }

    @Test
    public void testUseIngredientsTrue() throws RecipeException {
        Recipe recipe = new Recipe();
        recipe.setAmtCoffee("10");
        recipe.setAmtMilk("10");
        recipe.setAmtSugar("10");
        recipe.setAmtChocolate("10");
        assertTrue(inventory.useIngredients(recipe));
        assertEquals(5, inventory.getCoffee());
        assertEquals(5, inventory.getMilk());
        assertEquals(5, inventory.getSugar());
        assertEquals(5, inventory.getChocolate());
    }

    @Test
    public void testUseIngredientsFalse() throws RecipeException {
        Recipe recipe = new Recipe();
        recipe.setAmtCoffee("20");
        recipe.setAmtMilk("10");
        recipe.setAmtSugar("10");
        recipe.setAmtChocolate("10");
        assertFalse(inventory.useIngredients(recipe)); // Недостатньо кави
        assertEquals(15, inventory.getCoffee()); // Значення не повинні змінитися
        assertEquals(15, inventory.getMilk());
        assertEquals(15, inventory.getSugar());
        assertEquals(15, inventory.getChocolate());
    }

    @Test
    public void testToString() {
        String inventoryString = inventory.toString();
        assertTrue(inventoryString.contains("Coffee: 15"));
        assertTrue(inventoryString.contains("Milk: 15"));
        assertTrue(inventoryString.contains("Sugar: 15"));
        assertTrue(inventoryString.contains("Chocolate: 15"));
    }
}

