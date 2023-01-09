package me.lifelessnerd.castlesiege.utils;

public class MyStringUtils {

    public static String itemCamelCase(String string){
        //Input: IRON_SWORD
        String[] split = string.split("_");
        StringBuilder processedSentence = new StringBuilder();
        for (String word : split){
            String processedWord = camelCaseWord(word);
            processedSentence.append(processedWord).append(" ");
        }

        return processedSentence.toString();
        //Output: Iron Sword
    }

    public static String camelCaseWord(String string){
        //Input: SAMPLE
        string = string.toLowerCase();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
        //Output: Sample
    }

    public static String mapStringToEnchantment(String string){

//I {Enchantment:[minecraft:fire_protection, PROTECTION_FIRE]=4, Enchantment:[minecraft:infinity, INFINITY]=1}
        StringBuilder result = new StringBuilder("");

        StringBuilder builder = new StringBuilder(string);
        builder.deleteCharAt(0);
        builder.deleteCharAt(string.length() -2);
        string = builder.toString();
        String[] split = string.split("minecraft:");
        for (int index = 1; index < split.length; index++){
            String word = split[index];

            String[] splitAgain = word.split(",");
            String levelString = splitAgain[1];
            int level = Integer.parseInt(String.valueOf(levelString.charAt(levelString.length() - 1)));
            word = splitAgain[0];
            word = MyStringUtils.itemCamelCase(word);
            result.append(word).append(level).append(" ");
        }


        return result.toString();
        //Output: Fire Protection 4, Infinity 1
    }

    public static String itemMetaToEffects(String string){

//I: POTION_META:meta-type=POTION, potion-type=minecraft:strong_healing)
//OR
//I: POTION_META:meta-type=POTION, display-name="Golem Debuff", potion-type=minecraft:water, custom-effects=[LEVITATION:40t-x7]}

        StringBuilder result = new StringBuilder("");

        if (string.contains("custom-effects")){

            String[] split = string.split("custom-effects=");
            StringBuilder builder = new StringBuilder(split[1]);
            builder.deleteCharAt(0);
            builder.deleteCharAt(split[1].length() - 2 );
            builder.deleteCharAt(split[1].length() - 3 );
            String[] splitAgain = builder.toString().split(":");

            result.append(itemCamelCase(splitAgain[0]));
            String meta = splitAgain[1];
            String[] splitOnceMore = meta.split("-");

            StringBuilder time =  new StringBuilder(splitOnceMore[0]);
            time.deleteCharAt(splitOnceMore[0].length() - 1);
            int timeSec = Integer.parseInt(time.toString()) / 20;


            StringBuilder potency =  new StringBuilder(splitOnceMore[1]);
            potency.deleteCharAt(0);
            int potencyInt = Integer.parseInt(potency.toString()) + 1;

            StringBuilder lvlAndTime = new StringBuilder("");
            if (timeSec > 100000000){
                lvlAndTime.append(potencyInt + " (Infinite)");
            } else {
                lvlAndTime.append(potencyInt + " (" + timeSec + "s)");
            }

            result.append(lvlAndTime);

            return result.toString();
        }

        String[] split = string.split("potion-type=minecraft:");
        StringBuilder effectB = new StringBuilder(split[1]);
        String effect = effectB.deleteCharAt(split[1].length() - 1).toString();

        if (effect.contains("strong")){
            String[] splitAgain = effect.split("strong_");
            result.append(itemCamelCase(splitAgain[1]));
            result.append("II ");
        } else if (effect.contains("long")){
            String[] splitAgain = effect.split("long_");
            result.append(itemCamelCase(splitAgain[1]));
            result.append("(Long) ");
        } else {

            result.append(itemCamelCase(effect));
        }

        return result.toString();
    }

    public static String[] perkLoreDecoder(String string){
        String[] output = string.split("\n");
        return output;
    }

}
