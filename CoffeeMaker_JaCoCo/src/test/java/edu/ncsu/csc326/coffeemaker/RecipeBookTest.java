package edu.ncsu.csc326.coffeemaker;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

import static org.junit.Assert.*;

public class RecipeBookTest {

    private RecipeBook recipeBook;
    private Recipe recipe1;
    private Recipe recipe2;

    @Before
    public void setUp() throws Exception {
        recipeBook = new RecipeBook();

        recipe1 = new Recipe();
        recipe1.setName("Coffee");
        recipe1.setPrice("50");
        recipe1.setAmtCoffee("3");
        recipe1.setAmtMilk("1");
        recipe1.setAmtSugar("1");
        recipe1.setAmtChocolate("0");

        recipe2 = new Recipe();
        recipe2.setName("Latte");
        recipe2.setPrice("75");
        recipe2.setAmtCoffee("3");
        recipe2.setAmtMilk("2");
        recipe2.setAmtSugar("2");
        recipe2.setAmtChocolate("0");
    }

    @Test
    public void testAddRecipeSuccess() {
        assertTrue(recipeBook.addRecipe(recipe1));
        assertTrue(recipeBook.addRecipe(recipe2));

        Recipe[] recipes = recipeBook.getRecipes();
        assertEquals(recipe1, recipes[0]);
        assertEquals(recipe2, recipes[1]);
    }

    @Test
    public void testAddDuplicateRecipe() {
        recipeBook.addRecipe(recipe1);
        assertFalse(recipeBook.addRecipe(recipe1)); // Trying to add the same recipe again
    }

    @Test
    public void testAddRecipeWhenFull() throws RecipeException {
        recipeBook.addRecipe(recipe1);
        recipeBook.addRecipe(recipe2);

        Recipe recipe3 = new Recipe();
        recipe3.setName("Mocha");
        recipe3.setPrice("100");
        recipe3.setAmtCoffee("3");
        recipe3.setAmtMilk("2");
        recipe3.setAmtSugar("2");
        recipe3.setAmtChocolate("1");

        Recipe recipe4 = new Recipe();
        recipe4.setName("Espresso");
        recipe4.setPrice("60");
        recipe4.setAmtCoffee("3");
        recipe4.setAmtMilk("1");
        recipe4.setAmtSugar("1");
        recipe4.setAmtChocolate("0");

        Recipe recipe5 = new Recipe();
        recipe5.setName("Cappuccino");
        recipe5.setPrice("90");
        recipe5.setAmtCoffee("3");
        recipe5.setAmtMilk("2");
        recipe5.setAmtSugar("2");
        recipe5.setAmtChocolate("1");

        assertTrue(recipeBook.addRecipe(recipe3));
        assertTrue(recipeBook.addRecipe(recipe4));
        assertFalse(recipeBook.addRecipe(recipe5)); 
    }

    @Test
    public void testDeleteRecipeSuccess() {
        recipeBook.addRecipe(recipe1);
        recipeBook.addRecipe(recipe2);

        String deletedRecipeName = recipeBook.deleteRecipe(0);
        assertEquals("Coffee", deletedRecipeName);

        Recipe[] recipes = recipeBook.getRecipes();
        assertNull(recipes[0]); // First spot should be empty
    }

    @Test
    public void testDeleteRecipeNonExisting() {
        recipeBook.addRecipe(recipe1);

        String deletedRecipeName = recipeBook.deleteRecipe(1); // Trying to delete a non-existing recipe
        assertNull(deletedRecipeName);
    }

    @Test
    public void testEditRecipeSuccess() throws RecipeException {
        recipeBook.addRecipe(recipe1);

        Recipe newRecipe = new Recipe();
        newRecipe.setName("Modified Coffee");
        newRecipe.setPrice("60");
        newRecipe.setAmtCoffee("4");
        newRecipe.setAmtMilk("2");
        newRecipe.setAmtSugar("2");
        newRecipe.setAmtChocolate("1");

        String editedRecipeName = recipeBook.editRecipe(0, newRecipe);
        assertEquals("Coffee", editedRecipeName);

        Recipe[] recipes = recipeBook.getRecipes();
        assertEquals(newRecipe, recipes[0]);
    }

    @Test
    public void testEditRecipeNonExisting() {
        recipeBook.addRecipe(recipe1);

        Recipe newRecipe = new Recipe();
        newRecipe.setName("Modified Coffee");

        String editedRecipeName = recipeBook.editRecipe(1, newRecipe); // Trying to edit a non-existing recipe
        assertNull(editedRecipeName);
    }

    @Test
    public void testGetRecipes() {
        recipeBook.addRecipe(recipe1);
        recipeBook.addRecipe(recipe2);

        Recipe[] recipes = recipeBook.getRecipes();
        assertEquals(2, recipes.length);
        assertEquals(recipe1, recipes[0]);
        assertEquals(recipe2, recipes[1]);
    }
}

