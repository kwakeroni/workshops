import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HeapPollution {


    public void testSafeVarargs(){
        List<String>[] lists = getLengths(
                Arrays.asList("The", "Quick", "Brown", "Fox"),
                Arrays.asList("The", "Lazy", "Dog")
        );

        for (List<String> list : lists){
            for (String string : list){ // ClassCastException
                System.out.println(string);
            }
        }
    }

//    @SafeVarargs
    private List<String>[] getLengths(List<String>... lists){
        Object[] array = Objects.requireNonNull(lists);

        for (int i=0; i<lists.length; i++){
            List<Integer> lengths = new ArrayList<>();
            List<String> strings = lists[i];

            for (String string : strings){
                lengths.add(string.length());
            }

            array[i] = lengths; // Heap Pollution
        }

        return lists;
    }
}
