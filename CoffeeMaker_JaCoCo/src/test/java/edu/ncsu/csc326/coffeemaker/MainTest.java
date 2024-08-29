package edu.ncsu.csc326.coffeemaker;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayInputStream inContent = new ByteArrayInputStream("".getBytes());
    private CoffeeMaker coffeeMaker;

    @Before
    public void setUp() {
        coffeeMaker = new CoffeeMaker();
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
    }

    @Test
    public void testAddRecipe() {
        String simulatedUserInput = "Espresso\n60\n3\n0\n0\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.addRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Espresso successfully added."));
    }

    @Test
    public void testAddRecipeWithInvalidData() {
        String simulatedUserInput = "InvalidRecipe\nnot_a_number\n3\n0\n0\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.addRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Price must be a positive integer."));
    }

    @Test
    public void testDeleteRecipe() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "1\n"; // Select the first recipe
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.deleteRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Latte successfully deleted."));
    }

    @Test
    public void testDeleteNonExistingRecipe() {
        String simulatedUserInput = "1\n"; // Try to delete a non-existing recipe
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.deleteRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Selected recipe doesn't exist and could not be deleted."));
    }

    @Test
    public void testEditRecipe() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "1\n90\n3\n2\n2\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.editRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Latte successfully edited."));
    }

    @Test
    public void testAddInventory() {
        String simulatedUserInput = "5\n5\n5\n5\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.addInventory();

        String output = outContent.toString();
        assertTrue(output.contains("Inventory successfully added"));
    }

    @Test
    public void testAddInvalidInventory() {
        String simulatedUserInput = "5\nnot_a_number\n5\n5\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.addInventory();

        String output = outContent.toString();
        assertTrue(output.contains("Inventory was not added"));
    }

    @Test
    public void testCheckInventory() {
        Main.checkInventory();

        String output = outContent.toString();
        assertTrue(output.contains("Coffee: 15"));
        assertTrue(output.contains("Milk: 15"));
        assertTrue(output.contains("Sugar: 15"));
        assertTrue(output.contains("Chocolate: 15"));
    }

    @Test
    public void testMakeCoffeeWithSufficientFunds() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "1\n100\n"; // Select the recipe and pay 100
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.makeCoffee();

        String output = outContent.toString();
        assertTrue(output.contains("Thank you for purchasing Latte"));
        assertTrue(output.contains("Your change is: 0"));
    }

    @Test
    public void testMakeCoffeeWithInsufficientFunds() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "1\n50\n"; // Select the recipe and pay 50
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.makeCoffee();

        String output = outContent.toString();
        assertTrue(output.contains("Insufficient funds to purchase."));
    }

    @Test
    public void testMainMenuInvalidInput() {
        String simulatedUserInput = "invalid\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.mainMenu();

        String output = outContent.toString();
        assertTrue(output.contains("Please enter a number from 0 - 6"));
    }

    @Test
    public void testMainMenuInvalidNumber() {
        String simulatedUserInput = "7\n0\n"; // Invalid option, should re-prompt
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.mainMenu();

        String output = outContent.toString();
        assertTrue(output.contains("Please enter a number from 0 - 6"));
    }

    @Test
    public void testAddRecipeNegativeAmount() {
        String simulatedUserInput = "NegativeCoffee\n100\n-1\n0\n0\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.addRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Units of coffee must be a positive integer."));
    }

    @Test
    public void testAddRecipeWithMissingInput() {
        String simulatedUserInput = "MissingInputRecipe\n\n\n\n\n\n"; // Missing all input
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.addRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Price must be a positive integer.")); // Assuming first error caught
    }

    @Test
    public void testEditRecipeInvalidSelection() {
        // No recipes added yet
        String simulatedUserInput = "1\n100\n3\n0\n0\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.editRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("could not be edited"));
    }

    @Test
    public void testAddInventoryInvalidChocolate() {
        String simulatedUserInput = "5\n5\n5\ninvalid\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.addInventory();

        String output = outContent.toString();
        assertTrue(output.contains("Inventory was not added"));
    }

    @Test
    public void testMakeCoffeeInvalidPayment() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "1\ninvalid\n"; // Invalid payment amount
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.makeCoffee();

        String output = outContent.toString();
        assertTrue(output.contains("Please enter a positive integer"));
    }
    @Test
    public void testEditRecipeWithNonExistingRecipe() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "3\n100\n3\n2\n2\n0\n"; // Try to edit non-existing recipe at index 3
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.editRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("could not be edited"));
    }

    @Test
    public void testMakeCoffeeWithInvalidSelection() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Mocha", "75", "3", "1", "1", "2"));

        String simulatedUserInput = "4\n100\n"; // Invalid recipe selection (index 4)
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.makeCoffee();

        String output = outContent.toString();
        assertTrue(output.contains("Insufficient funds to purchase."));
    }

    @Test
    public void testDeleteRecipeWithValidSelection() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "1\n"; // Valid selection
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.deleteRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Latte successfully deleted."));
    }

    @Test
    public void testAddRecipeWithDuplicateName() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "Latte\n100\n3\n2\n2\n0\n"; // Attempt to add a recipe with the same name
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.addRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("could not be added"));
    }

    private Recipe createRecipe(String name, String price, String coffee, String milk, String sugar, String chocolate) {
        Recipe recipe = new Recipe();
        try {
            recipe.setName(name);
            recipe.setPrice(price);
            recipe.setAmtCoffee(coffee);
            recipe.setAmtMilk(milk);
            recipe.setAmtSugar(sugar);
            recipe.setAmtChocolate(chocolate);
        } catch (RecipeException e) {
            fail("Recipe setup failed.");
        }
        return recipe;
    }
    @Test
    public void testEditRecipeWithSameName() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "1\n100\n3\n2\n2\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.editRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Latte successfully edited."));
    }

    @Test
    public void testEditRecipeInvalidPrice() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "1\ninvalid\n3\n2\n2\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.editRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Price must be a positive integer."));
    }

    @Test
    public void testMakeCoffeeWithExactChange() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Mocha", "75", "3", "1", "1", "2"));

        String simulatedUserInput = "1\n75\n"; // Exact amount of money
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.makeCoffee();

        String output = outContent.toString();
        assertTrue(output.contains("Thank you for purchasing Mocha"));
        assertTrue(output.contains("Your change is: 0"));
    }

    @Test
    public void testMakeCoffeeWithInsufficientInventory() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Mocha", "75", "20", "1", "1", "2")); // More coffee than available

        String simulatedUserInput = "1\n100\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.makeCoffee();

        String output = outContent.toString();
        assertTrue(output.contains("Insufficient funds to purchase."));
    }

    @Test
    public void testDeleteRecipeWithInvalidSelection() {
        // Add a recipe first
        coffeeMaker.addRecipe(createRecipe("Latte", "100", "3", "2", "2", "0"));

        String simulatedUserInput = "2\n"; // Invalid selection
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.deleteRecipe();

        String output = outContent.toString();
        assertTrue(output.contains("Selected recipe doesn't exist and could not be deleted."));
    }

    @Test
    public void testAddInventoryWithZeroValues() {
        String simulatedUserInput = "0\n0\n0\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Main.addInventory();

        String output = outContent.toString();
        assertTrue(output.contains("Inventory successfully added"));
    }
}

