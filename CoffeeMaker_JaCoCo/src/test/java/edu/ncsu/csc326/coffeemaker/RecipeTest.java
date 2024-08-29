package edu.ncsu.csc326.coffeemaker;

import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecipeTest {

    private Recipe recipe;

    @Before
    public void setUp() {
        recipe = new Recipe();
    }

    @Test
    public void testSetAndGetAmtChocolate() throws RecipeException {
        recipe.setAmtChocolate("5");
        assertEquals(5, recipe.getAmtChocolate());
    }

    @Test(expected = RecipeException.class)
    public void testSetAmtChocolateNegativeValue() throws RecipeException {
        recipe.setAmtChocolate("-1");
    }

    @Test(expected = RecipeException.class)
    public void testSetAmtChocolateNonNumericValue() throws RecipeException {
        recipe.setAmtChocolate("abc");
    }

    @Test
    public void testSetAndGetAmtCoffee() throws RecipeException {
        recipe.setAmtCoffee("3");
        assertEquals(3, recipe.getAmtCoffee());
    }

    @Test(expected = RecipeException.class)
    public void testSetAmtCoffeeNegativeValue() throws RecipeException {
        recipe.setAmtCoffee("-3");
    }

    @Test(expected = RecipeException.class)
    public void testSetAmtCoffeeNonNumericValue() throws RecipeException {
        recipe.setAmtCoffee("xyz");
    }

    @Test
    public void testSetAndGetAmtMilk() throws RecipeException {
        recipe.setAmtMilk("2");
        assertEquals(2, recipe.getAmtMilk());
    }

    @Test(expected = RecipeException.class)
    public void testSetAmtMilkNegativeValue() throws RecipeException {
        recipe.setAmtMilk("-2");
    }

    @Test(expected = RecipeException.class)
    public void testSetAmtMilkNonNumericValue() throws RecipeException {
        recipe.setAmtMilk("milk");
    }

    @Test
    public void testSetAndGetAmtSugar() throws RecipeException {
        recipe.setAmtSugar("4");
        assertEquals(4, recipe.getAmtSugar());
    }

    @Test(expected = RecipeException.class)
    public void testSetAmtSugarNegativeValue() throws RecipeException {
        recipe.setAmtSugar("-4");
    }

    @Test(expected = RecipeException.class)
    public void testSetAmtSugarNonNumericValue() throws RecipeException {
        recipe.setAmtSugar("sugar");
    }

    @Test
    public void testSetAndGetPrice() throws RecipeException {
        recipe.setPrice("50");
        assertEquals(50, recipe.getPrice());
    }

    @Test(expected = RecipeException.class)
    public void testSetPriceNegativeValue() throws RecipeException {
        recipe.setPrice("-50");
    }

    @Test(expected = RecipeException.class)
    public void testSetPriceNonNumericValue() throws RecipeException {
        recipe.setPrice("price");
    }

    @Test
    public void testSetAndGetName() {
        recipe.setName("Latte");
        assertEquals("Latte", recipe.getName());
    }

    @Test
    public void testSetNameNull() {
        recipe.setName(null);
        assertNull(recipe.getName());
    }

    @Test
    public void testToString() {
        recipe.setName("Espresso");
        assertEquals("Espresso", recipe.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        Recipe recipe1 = new Recipe();
        recipe1.setName("Cappuccino");

        Recipe recipe2 = new Recipe();
        recipe2.setName("Cappuccino");

        assertTrue(recipe1.equals(recipe2));
        assertEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    public void testNotEquals() {
        Recipe recipe1 = new Recipe();
        recipe1.setName("Mocha");

        Recipe recipe2 = new Recipe();
        recipe2.setName("Latte");

        assertFalse(recipe1.equals(recipe2));
        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    public void testEqualsWithNullName() {
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        assertTrue(recipe1.equals(recipe2));

        recipe1.setName(null);
        recipe2.setName("Cappuccino");
        assertFalse(recipe1.equals(recipe2));
    }
}

