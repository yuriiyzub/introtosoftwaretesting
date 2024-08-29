package edu.ncsu.csc326.coffeemaker;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;


/**
 * Unit tests for CoffeeMaker class.
 * 
 * @author Sarah Heckman
 */


public class CoffeeMakerTest {
	
	/**
	 * The object under test.
	 */
	private CoffeeMaker coffeeMaker;
	
	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;

	/**
	 * Initializes some recipes to test with and the {@link CoffeeMaker} 
	 * object we wish to test.
	 * 
	 * @throws RecipeException  if there was an error parsing the ingredient 
	 * 		amount when setting up the recipe.
	 */
	@Before
	public void setUp() throws RecipeException {
		coffeeMaker = new CoffeeMaker();
		
		//Set up for r1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");
		
		//Set up for r2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");
		
		//Set up for r4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");
	}
	
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with well-formed quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddInventory() throws InventoryException {
		coffeeMaker.addInventory("4","7","0","9");
	}
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with malformed quantities (i.e., a negative 
	 * quantity and a non-numeric string)
	 * Then we get an inventory exception
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInventoryException() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "asdf", "3");
	}
	
	@Test
	public void testMakeCoffee() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}

	@Test
	public void testAddExcessiveInventory() throws InventoryException {
		coffeeMaker.addInventory("1000", "1000", "1000", "1000");
		String inventory = coffeeMaker.checkInventory();
		assertTrue(inventory.contains("Coffee: 1015"));
		assertTrue(inventory.contains("Milk: 1015"));
		assertTrue(inventory.contains("Sugar: 1015"));
		assertTrue(inventory.contains("Chocolate: 1015"));
	}

	@Test
	public void testMakeCoffeeWithNoRecipes() {
		int change = coffeeMaker.makeCoffee(0, 100);
		assertEquals(100, change); // Should return the full amount as no recipes exist
	}

	@Test
	public void testInventoryAfterRecipeDeletion() {
		try {
			coffeeMaker.addInventory("10", "10", "10", "10");
			coffeeMaker.addRecipe(recipe1);
			coffeeMaker.makeCoffee(0, 50);
			
			String inventoryBeforeDeletion = coffeeMaker.checkInventory();
			coffeeMaker.deleteRecipe(0);
			
			String inventoryAfterDeletion = coffeeMaker.checkInventory();
			
			assertEquals(inventoryBeforeDeletion, inventoryAfterDeletion);
		} catch (InventoryException e) {
			fail("InventoryException was thrown: " + e.getMessage());
		}
	}

	@Test
	public void testEditRecipeWithIdenticalRecipe() {
		coffeeMaker.addRecipe(recipe1);
		String editedRecipeName = coffeeMaker.editRecipe(0, recipe1);
		assertEquals("Coffee", editedRecipeName); // The name should remain the same
		Recipe[] recipes = coffeeMaker.getRecipes();
		assertEquals("Coffee", recipes[0].getName());
	}

	@Test
	public void testDeleteNonExistentRecipe() {
		String result = coffeeMaker.deleteRecipe(10); // Trying to delete a recipe that doesn't exist
		assertNull(result);
	}
	@Test(expected = RecipeException.class)
	public void testAddRecipeWithInvalidIngredients() throws RecipeException {
		Recipe invalidRecipe = new Recipe();
		invalidRecipe.setName("Invalid Coffee");
		invalidRecipe.setAmtChocolate("-2"); // Invalid negative amount
		invalidRecipe.setAmtCoffee("4");
		invalidRecipe.setAmtMilk("1");
		invalidRecipe.setAmtSugar("1");
		invalidRecipe.setPrice("50");
		
		coffeeMaker.addRecipe(invalidRecipe);
	}

	@Test(expected = RecipeException.class)
	public void testAddRecipeWithInvalidPrice() throws RecipeException {
		Recipe invalidRecipe = new Recipe();
		invalidRecipe.setName("Invalid Coffee");
		invalidRecipe.setAmtChocolate("2");
		invalidRecipe.setAmtCoffee("4");
		invalidRecipe.setAmtMilk("1");
		invalidRecipe.setAmtSugar("1");
		invalidRecipe.setPrice("-50"); // Invalid negative price
		
		coffeeMaker.addRecipe(invalidRecipe);
	}
	
	@Test
	public void testAddInventoryBoundaryValues() throws InventoryException {
		// Test with minimum values (zero)
		coffeeMaker.addInventory("0", "0", "0", "0");
		String inventory = coffeeMaker.checkInventory();
		assertTrue(inventory.contains("Coffee: 15"));
		assertTrue(inventory.contains("Milk: 15"));
		assertTrue(inventory.contains("Sugar: 15"));
		assertTrue(inventory.contains("Chocolate: 15"));

		// Test with large values
		coffeeMaker.addInventory("9999", "9999", "9999", "9999");
		inventory = coffeeMaker.checkInventory();
		assertTrue(inventory.contains("Coffee: 10014"));
		assertTrue(inventory.contains("Milk: 10014"));
		assertTrue(inventory.contains("Sugar: 10014"));
		assertTrue(inventory.contains("Chocolate: 10014"));
	}

	@Test(expected = InventoryException.class)
	public void testAddNegativeInventory() throws InventoryException {
		coffeeMaker.addInventory("-1", "10", "10", "10");
	}

	@Test(expected = InventoryException.class)
	public void testAddNegativeInventoryAll() throws InventoryException {
		coffeeMaker.addInventory("-1", "-1", "-1", "-1");
	}

	@Test
	public void testAddRecipeWithZeroIngredients() throws RecipeException {
		Recipe zeroIngredientRecipe = new Recipe();
		zeroIngredientRecipe.setName("ZeroCoffee");
		zeroIngredientRecipe.setAmtChocolate("0");
		zeroIngredientRecipe.setAmtCoffee("0");
		zeroIngredientRecipe.setAmtMilk("0");
		zeroIngredientRecipe.setAmtSugar("0");
		zeroIngredientRecipe.setPrice("10");

		assertTrue(coffeeMaker.addRecipe(zeroIngredientRecipe));
		assertEquals(0, coffeeMaker.makeCoffee(0, 10)); // Should be able to make coffee with no ingredients
	}

	@Test
	public void testDeleteRecipeWithNull() {
		coffeeMaker.addRecipe(recipe1);
		assertNull(coffeeMaker.deleteRecipe(-1)); // Invalid index
	}

	@Test
	public void testGetMultipleRecipes() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);

		Recipe[] recipes = coffeeMaker.getRecipes();
		assertEquals("Coffee", recipes[0].getName());
		assertEquals("Mocha", recipes[1].getName());
		assertEquals("Latte", recipes[2].getName());
	}

	@Test
	public void testAddDuplicateRecipe() {
		coffeeMaker.addRecipe(recipe1);
		assertFalse(coffeeMaker.addRecipe(recipe1)); // Should return false since the recipe already exists
	}
	
	@Test
	public void testMakeCoffeeWithInsufficientFunds() {
		coffeeMaker.addRecipe(recipe1);
		int change = coffeeMaker.makeCoffee(0, 30); // Less than the price of the coffee
		assertEquals(30, change); // Full amount should be returned as no coffee was made
	}

	@Test
	public void testMakeCoffeeAfterRecipeDeletion() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.deleteRecipe(0);
		int change = coffeeMaker.makeCoffee(0, 50);
		assertEquals(50, change); // No coffee should be made, full amount returned
	}
	
	@Test
	public void testAddMultipleRecipesWithSameName() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);

		Recipe duplicateNameRecipe = new Recipe();
		duplicateNameRecipe.setName("Coffee"); // Same name as recipe1
		duplicateNameRecipe.setAmtChocolate("1");
		duplicateNameRecipe.setAmtCoffee("2");
		duplicateNameRecipe.setAmtMilk("2");
		duplicateNameRecipe.setAmtSugar("2");
		duplicateNameRecipe.setPrice("60");

		assertFalse(coffeeMaker.addRecipe(duplicateNameRecipe)); // Should not allow adding due to name duplication
	}

	@Test
	public void testAddRecipeWithExistingName() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);

		Recipe duplicateNameRecipe = new Recipe();
		duplicateNameRecipe.setName("Coffee"); // Same name as recipe1
		duplicateNameRecipe.setAmtChocolate("1");
		duplicateNameRecipe.setAmtCoffee("2");
		duplicateNameRecipe.setAmtMilk("2");
		duplicateNameRecipe.setAmtSugar("2");
		duplicateNameRecipe.setPrice("60");

		assertFalse(coffeeMaker.addRecipe(duplicateNameRecipe)); // Should return false as name already exists
	}

	@Test
	public void testMakeCoffeeWithNonExistentRecipe() {
		int change = coffeeMaker.makeCoffee(5, 100); // Recipe index 5 does not exist
		assertEquals(100, change); // Should return full amount as no recipe exists at this index
	}

	@Test
	public void testRecipeCountAfterDeletion() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);

		assertEquals(3, coffeeMaker.getRecipes().length); // Initially, there should be 3 recipes

		coffeeMaker.deleteRecipe(1); // Delete the second recipe

		Recipe[] recipes = coffeeMaker.getRecipes();
		int nonNullRecipes = 0;
		for (Recipe recipe : recipes) {
			if (recipe != null) {
				nonNullRecipes++;
			}
		}

		assertEquals(2, nonNullRecipes); // After deletion, there should be 2 recipes left
	}

	@Test
	public void testAddNewRecipe() throws RecipeException {
		Recipe newRecipe = new Recipe();
		newRecipe.setName("Cappuccino");
		newRecipe.setAmtChocolate("2");
		newRecipe.setAmtCoffee("3");
		newRecipe.setAmtMilk("1");
		newRecipe.setAmtSugar("1");
		newRecipe.setPrice("50");

		assertTrue(coffeeMaker.addRecipe(newRecipe)); // Should return true as the recipe is added successfully
	}

	@Test
	public void testDeleteRecipe() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);

		String deletedRecipe = coffeeMaker.deleteRecipe(0); // Delete the first recipe
		assertEquals("Coffee", deletedRecipe); // The name of the deleted recipe should be returned

		Recipe[] recipes = coffeeMaker.getRecipes();
		assertNull(recipes[0]); // The first recipe should now be null
		assertNotNull(recipes[1]); // The second recipe should still be present
	}

	@Test
	public void testCheckInventory() throws InventoryException {
		coffeeMaker.addInventory("5", "3", "7", "2");

		String inventory = coffeeMaker.checkInventory();
		assertTrue(inventory.contains("Coffee: 20")); // 15 (initial) + 5
		assertTrue(inventory.contains("Milk: 18")); // 15 (initial) + 3
		assertTrue(inventory.contains("Sugar: 22")); // 15 (initial) + 7
		assertTrue(inventory.contains("Chocolate: 17")); // 15 (initial) + 2
	}
	@Test
	public void testMakeCoffeeReturnChange() {
		coffeeMaker.addRecipe(recipe1); // Assume recipe1 is priced at 50

		int change = coffeeMaker.makeCoffee(0, 100); // Pay 100 for a coffee costing 50
		assertEquals(50, change); // Should return 50 as change
	}
	@Test
	public void testAddMultipleRecipes() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);

		Recipe[] recipes = coffeeMaker.getRecipes();
		assertEquals("Coffee", recipes[0].getName());
		assertEquals("Mocha", recipes[1].getName());
		assertEquals("Latte", recipes[2].getName());
	}

	@Test
	public void testDeleteRecipeWithInvalidIndex() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);

		String deletedRecipe = coffeeMaker.deleteRecipe(10); // Invalid index
		assertNull(deletedRecipe); // Should return null as no recipe exists at this index
	}

	@Test
	public void testMakeCoffeeWithZeroOrNegativeMoney() {
		coffeeMaker.addRecipe(recipe1);

		int change = coffeeMaker.makeCoffee(0, 0); // Pay 0
		assertEquals(0, change); // Should return full amount as coffee can't be made

		change = coffeeMaker.makeCoffee(0, -50); // Pay -50
		assertEquals(-50, change); // Should return full amount as coffee can't be made
	}

	@Test
	public void testAddRecipeWithEqualIngredients() throws RecipeException {
		Recipe equalIngredientsRecipe = new Recipe();
		equalIngredientsRecipe.setName("Equal Mix");
		equalIngredientsRecipe.setAmtChocolate("2");
		equalIngredientsRecipe.setAmtCoffee("2");
		equalIngredientsRecipe.setAmtMilk("2");
		equalIngredientsRecipe.setAmtSugar("2");
		equalIngredientsRecipe.setPrice("50");

		assertTrue(coffeeMaker.addRecipe(equalIngredientsRecipe)); // Should return true
		Recipe[] recipes = coffeeMaker.getRecipes();
		assertEquals("Equal Mix", recipes[0].getName());
		assertEquals(2, recipes[0].getAmtCoffee());
		assertEquals(2, recipes[0].getAmtChocolate());
		assertEquals(2, recipes[0].getAmtMilk());
		assertEquals(2, recipes[0].getAmtSugar());
	}

	@Test
	public void testAddLargeAmountOfInventory() throws InventoryException {
		coffeeMaker.addInventory("10000", "10000", "10000", "10000");

		String inventory = coffeeMaker.checkInventory();
		assertTrue(inventory.contains("Coffee: 10015")); // 15 (initial) + 10000
		assertTrue(inventory.contains("Milk: 10015")); // 15 (initial) + 10000
		assertTrue(inventory.contains("Sugar: 10015")); // 15 (initial) + 10000
		assertTrue(inventory.contains("Chocolate: 10015")); // 15 (initial) + 10000
	}

	@Test
	public void testAddRecipeWithMaxIngredients() throws RecipeException {
		Recipe maxIngredientsRecipe = new Recipe();
		maxIngredientsRecipe.setName("Max Mix");
		maxIngredientsRecipe.setAmtChocolate("1000");
		maxIngredientsRecipe.setAmtCoffee("1000");
		maxIngredientsRecipe.setAmtMilk("1000");
		maxIngredientsRecipe.setAmtSugar("1000");
		maxIngredientsRecipe.setPrice("500");

		assertTrue(coffeeMaker.addRecipe(maxIngredientsRecipe)); // Should return true
		Recipe[] recipes = coffeeMaker.getRecipes();
		assertEquals("Max Mix", recipes[0].getName());
		assertEquals(1000, recipes[0].getAmtCoffee());
		assertEquals(1000, recipes[0].getAmtChocolate());
		assertEquals(1000, recipes[0].getAmtMilk());
		assertEquals(1000, recipes[0].getAmtSugar());
	}

	@Test(expected = RecipeException.class)
	public void testEditRecipeWithInvalidIngredients() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);

		Recipe invalidRecipe = new Recipe();
		invalidRecipe.setName("Invalid Recipe");
		invalidRecipe.setAmtChocolate("-10"); // Invalid amount
		invalidRecipe.setAmtCoffee("5");
		invalidRecipe.setAmtMilk("5");
		invalidRecipe.setAmtSugar("5");
		invalidRecipe.setPrice("100");

		coffeeMaker.editRecipe(0, invalidRecipe); // Should throw RecipeException
	}
}
