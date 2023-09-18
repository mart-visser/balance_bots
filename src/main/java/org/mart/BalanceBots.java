package org.mart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BalanceBots {

    // Define input file
    private final static String INPUT_FILE_NAME = "C:\\tmp\\input.txt";

    // Define for which chip numbers we want the bot responsible for comparing
    private final static Integer TARGET_CHIP_HIGH = 61;
    private final static Integer TARGET_CHIP_LOW = 17;

    private static Map<Integer, Bot> bots = new HashMap<>();
    private static Map<Integer, Integer> outputs = new HashMap<>();
    private static Bot targetBot;

    public static void main(String[] args) {

        // Build instructions into our bots hashmap
        buildInstructions();

        // Run the bots until we find the one comparing our inputted target chips
        findTheBot();
        System.out.println("Bot that processes the target chips is: " + targetBot.getNumber());
        System.out.printf("Output 0, 1 and 3 are the following: %s, %s, %s%n", outputs.get(0), outputs.get(1), outputs.get(2));
        System.out.println("These 3 numbers multiplied equal: " + outputs.get(0) * outputs.get(1) * outputs.get(2));
    }

    private static void findTheBot() {
        boolean botFound = false;

        // Make sure to continue looping through the bots their instructions until both the correct bot and output bins 0, 1, and 2 are found
        while (true) {
            for (Bot bot : bots.values()) {

                // Check if we've found our bot that processes the chip we're looking for
                if(isTargetBot(bot) ) {
                    targetBot = bot;
                    botFound = true;
                }

                // If the bot has been found, and we've filled the correct output bind we can quit stop the method from running
                if(botFound && outputs.containsKey(0) && outputs.containsKey(1) && outputs.containsKey(2)) {
                    return;
                }

                // Only proceed if the bot has two chips
                if(bot.hasTwoChips()) {
                    if (bot.isLowToOutput()) {
                        outputs.put(bot.getLowTarget(), bot.getLowValue());
                    } else {
                        bots.computeIfAbsent(bot.getLowTarget(), Bot::new).addChip(bot.getLowValue());
                    }
                    if (bot.isHighToOutput()) {
                        outputs.put(bot.getHighTarget(), bot.getHighValue());
                    } else {
                        bots.computeIfAbsent(bot.getHighTarget(), Bot::new).addChip(bot.getHighValue());
                    }

                    // Remove the chips from the bot after giving them away
                    bot.clearChips();
                }
            }
        }
    }

    // Compare the chip values of the bot to our target chip values
    private static boolean isTargetBot(Bot bot) {
        if(!bot.hasTwoChips())
            return false;
        return Objects.equals(bot.getLowValue(), TARGET_CHIP_LOW) && Objects.equals(bot.getHighValue(), TARGET_CHIP_HIGH);
    }


    private static void buildInstructions() {
        // Open our input file
        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {

                // Check whether it's a new chip, or a bot giving chips. Saving the bots their chips or instructions of giving chips
                if (line.startsWith("value")) {
                    String[] parts = line.split(" ");
                    int value = Integer.parseInt(parts[1]);
                    int botNumber = Integer.parseInt(parts[5]);
                    bots.computeIfAbsent(botNumber, Bot::new).addChip(value);
                } else {
                    String[] parts = line.split(" ");
                    int botNumber = Integer.parseInt(parts[1]);
                    bots.computeIfAbsent(botNumber, Bot::new).setInstructions(parts);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}