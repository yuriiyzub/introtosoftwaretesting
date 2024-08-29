package edu.ncsu.csc326.coffeemaker;

import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;
import junit.framework.TestCase;



/**
 * 
 * @author Sarah Heckman
 *
 * Extended by Mike Whalen
 *
 * Unit tests for CoffeeMaker class.
 */

public class CoffeeMakerTest extends TestCase {
	
	private Recipe r1;
	private Recipe r2;
	private Recipe r3;
	private Recipe r4;
	private Recipe r5;
	private CoffeeMaker cm;
	private RecipeBook recipeBookStub;
	private Recipe [] stubRecipies; 
	
	protected void setUp() throws Exception {
		
		cm = new CoffeeMaker();
		
		//Set up for r1
		r1 = new Recipe();
		r1.setName("Coffee");
		r1.setAmtChocolate("0");
		r1.setAmtCoffee("3");
		r1.setAmtMilk("1");
		r1.setAmtSugar("1");
		r1.setPrice("50");
		
		//Set up for r2
		r2 = new Recipe();
		r2.setName("Mocha");
		r2.setAmtChocolate("20");
		r2.setAmtCoffee("3");
		r2.setAmtMilk("1");
		r2.setAmtSugar("1");
		r2.setPrice("75");
		
		//Set up for r3
		r3 = new Recipe();
		r3.setName("Latte");
		r3.setAmtChocolate("0");
		r3.setAmtCoffee("3");
		r3.setAmtMilk("3");
		r3.setAmtSugar("1");
		r3.setPrice("100");
		
		//Set up for r4
		r4 = new Recipe();
		r4.setName("Hot Chocolate");
		r4.setAmtChocolate("4");
		r4.setAmtCoffee("0");
		r4.setAmtMilk("1");
		r4.setAmtSugar("1");
		r4.setPrice("65");
		
		//Set up for r5 (added by MWW)
		r5 = new Recipe();
		r5.setName("Super Hot Chocolate");
		r5.setAmtChocolate("6");
		r5.setAmtCoffee("0");
		r5.setAmtMilk("1");
		r5.setAmtSugar("1");
		r5.setPrice("100");

		stubRecipies = new Recipe [] {r1, r2, r3};
		
		super.setUp();
	}
	
	
	// put your tests here!
	@Test
    public void testMakeCoffee() {
        int change = cm.makeCoffee(0, 75);
        assertEquals(25, change);
    }
	@Test
    public void testAddRecipe() {
        assertTrue(cm.addRecipe(r1));
        assertTrue(cm.addRecipe(r2));
        assertTrue(cm.addRecipe(r3));
    }
	@Test
    public void testAddDuplicateRecipe() {
        cm.addRecipe(r1);
        assertFalse(cm.addRecipe(r1)); // Дублювання рецепту
    }

    /*@Test
    public void testDeleteRecipe() {
        cm.addRecipe(r1);
        assertEquals("Coffee", cm.deleteRecipe(0));
        assertNull(cm.deleteRecipe(0)); // Видалення того, що вже видалено
    }*/

    @Test
    public void testEditRecipe() {
        cm.addRecipe(r1);
        assertEquals("Coffee", cm.editRecipe(0, r2));
        assertEquals("Mocha", cm.getRecipes()[0].getName());
    }

    @Test
    public void testAddInventory() throws InventoryException {
        cm.addInventory("4", "7", "0", "9");
        String inventory = cm.checkInventory();
        assertTrue(inventory.contains("Coffee: 19"));
        assertTrue(inventory.contains("Milk: 22"));
        assertTrue(inventory.contains("Sugar: 20"));
        assertTrue(inventory.contains("Chocolate: 14"));
    }

    @Test(expected = InventoryException.class)
    public void testAddInventoryWithNegativeInput() throws InventoryException {
        cm.addInventory("4", "-7", "3", "2");
    }

    @Test
    public void testMakeCoffeeInsufficientFunds() {
        cm.addRecipe(r1);
        int change = cm.makeCoffee(0, 25);
        assertEquals(25, change);
    }

    @Test
    public void testMakeCoffeeWithInsufficientInventory() throws RecipeException {
        r1.setAmtCoffee("50");  // Встановлюємо велику кількість кави
        cm.addRecipe(r1);
        int change = cm.makeCoffee(0, 75);
        assertEquals(75, change); // Інвентар недостатній, отже, вся сума повертається
    }

    @Test
    public void testCheckInventory() {
        String inventory = cm.checkInventory();
        assertNotNull(inventory);
        assertTrue(inventory.contains("Coffee"));
        assertTrue(inventory.contains("Milk"));
        assertTrue(inventory.contains("Sugar"));
        assertTrue(inventory.contains("Chocolate"));
    }

    @Test
    public void testGetRecipes() {
        cm.addRecipe(r1);
        cm.addRecipe(r2);
        cm.addRecipe(r3);
        Recipe[] recipes = cm.getRecipes();
        assertEquals(3, recipes.length);
        assertEquals("Coffee", recipes[0].getName());
        assertEquals("Mocha", recipes[1].getName());
        assertEquals("Latte", recipes[2].getName());
    }
	@Test
    public void testMakeCoffeeWithExactAmount() {
        // Тестуємо випадок, коли оплачена сума точно дорівнює ціні
        int change = cm.makeCoffee(0, 50); // "Black Coffee" коштує 50
        assertEquals(0, change);
    }
	@Test
    public void testReplaceExistingRecipe() {
        cm.addRecipe(r1);
        String oldRecipeName = cm.editRecipe(0, r4);
        assertEquals("Coffee", oldRecipeName); // The original recipe name
        assertEquals("Espresso", cm.getRecipes()[0].getName());
    }

    @Test
    public void testAddMoreThanAllowedRecipes() {
        cm.addRecipe(r1);
        cm.addRecipe(r2);
        cm.addRecipe(r3);
        assertFalse(cm.addRecipe(r4)); // Should not allow adding more than the limit
    }

    @Test
    public void testMakeCoffeeWithoutEnoughMoney() {
        cm.addRecipe(r1);
        int change = cm.makeCoffee(0, 30); // Not enough money
        assertEquals(30, change); // Money should be returned
    }

    @Test
    public void testMakeCoffeeWithExactMoney() {
        cm.addRecipe(r1);
        int change = cm.makeCoffee(0, 50); // Exact amount of money
        assertEquals(0, change); // No change should be returned
    }

    @Test
    public void testAddZeroInventory() throws InventoryException {
        cm.addInventory("0", "0", "0", "0");
        String inventory = cm.checkInventory();
        assertTrue(inventory.contains("Coffee: 15"));
        assertTrue(inventory.contains("Milk: 15"));
        assertTrue(inventory.contains("Sugar: 15"));
        assertTrue(inventory.contains("Chocolate: 15"));
    }

    @Test(expected = InventoryException.class)
    public void testAddNonNumericInventory() throws InventoryException {
        cm.addInventory("a", "2", "3", "4"); // Invalid input for coffee
    }

    @Test
    public void testMakeCoffeeWithNoRecipeSelected() {
        cm.addRecipe(r1);
        cm.addRecipe(r2);
        cm.addRecipe(r3);
        int change = cm.makeCoffee(3, 100); // No recipe at index 3
        assertEquals(100, change); // All money should be returned
    }

    @Test
    public void testMakeCoffeeWithMultipleRecipes() {
        cm.addRecipe(r1);
        cm.addRecipe(r2);
        cm.addRecipe(r3);

        int change = cm.makeCoffee(1, 80); // Selecting Mocha
        assertEquals(5, change); // 80 - 75 = 5
        assertEquals(12, cm.checkInventory().contains("Chocolate: 13"));
    }

    @Test
    public void testCheckInventoryAfterUsingIngredients() {
        cm.addRecipe(r1);
        cm.makeCoffee(0, 50); // Use ingredients for Coffee
        String inventory = cm.checkInventory();
        assertTrue(inventory.contains("Coffee: 12")); // 15 - 3 = 12
        assertTrue(inventory.contains("Milk: 14"));   // 15 - 1 = 14
        assertTrue(inventory.contains("Sugar: 14"));  // 15 - 1 = 14
        assertTrue(inventory.contains("Chocolate: 15")); // No chocolate used
    }
	
}
