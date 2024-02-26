package thanadon.project.shoppingMall.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtils {
    public enum userRole {
        CUSTOMER,
        OWNER,
        OFFICER,
        ADMIN
    }

    public static boolean validationPhoneNumber(String phoneNumber) {
        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean validationGene(String gene) {
        String[] arrayOfGene = {"M", "F", "O"};
        return Arrays.asList(arrayOfGene).contains(gene);
    }
}
