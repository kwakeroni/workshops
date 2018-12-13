import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scratch {

    public static void main(String[] args) throws Exception {

        final int pages = 96;

        List<Integer> list = new ArrayList<>(pages);

        // 96 - 1, 2 -95; 94 - 3; 4 - 93

        for (int i=0; i<pages/2; i+=2){
            list.add(pages-i);
            list.add(i+1);
            list.add(i+2);
            list.add(pages-i-1);
        }

        String arg = list.stream().map(String::valueOf).collect(Collectors.joining(" "));
        System.out.println(" pdftk holebifilmfestival_programmaboekje_2018_EersteVersie.pdf cat " + arg + " output boekje.pdf");

    }
    public static void bigDecimal(String[] args) throws Exception {

        List<BigDecimal> list = toBD("44.85", "8.3", "4.95", "4.55", "35.55", "2", "65", "37.8", "18.9", "7.2", "10.95");

                System.out.println(list.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

            System.out.println(searchFor(new BigDecimal("48.40"), list));


    }

    private static List<BigDecimal> searchFor(BigDecimal result, List<BigDecimal> numbers){

        for (BigDecimal bd : numbers){

            if (result.compareTo(bd) == 0){
                return Collections.singletonList(bd);
            } else if (result.compareTo(bd) > 0) {
                List<BigDecimal> list = searchFor(result.subtract(bd), without(numbers, bd));
                if (list != null) {
                    return with(list, bd);
                }
            } else {
                return null;
            }


        }

        return null;

    }

    public static List<BigDecimal> without(List<BigDecimal> origin, BigDecimal removed){
        List<BigDecimal> list = new ArrayList<>(origin);
        list.remove(removed);
        return list;
    }

    public static List<BigDecimal> with(List<BigDecimal> origin, BigDecimal added){
        List<BigDecimal> list = new ArrayList<>(origin);
        list.add(added);
        return list;
    }


    public static List<BigDecimal> toBD(String... strings){
return          Stream.of(strings)
                .map(BigDecimal::new)
                .collect(Collectors.toList());
    }

    public static void setStringChar(String[] args) throws Exception {

        Field value = String.class.getDeclaredField("value");
        value.setAccessible(true);
        char[] array = (char[]) value.get("Tobania");
        array[0] = 'H';

        System.out.println("Tobania");

    }


}
